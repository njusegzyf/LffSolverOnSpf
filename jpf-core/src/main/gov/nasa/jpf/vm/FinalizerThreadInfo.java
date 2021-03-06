//
// Copyright (C) 2013 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
// 
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
// directory tree for the complete NOSA document.
// 
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//
package gov.nasa.jpf.vm;

/**
 * @author Nastaran Shafiei <nastaran.shafiei@gmail.com>
 *  
 * This class represents the finalizer thread in VM which is responsible for
 * executing finalize() methods upon garbage collection of finalizable objects.
 * By a finalizable object, we mean an object, the class of which overrides the
 * Object.finalize() method. By default, we do not allow for processing finalizers, 
 * to enforce that one needs to set the property "vm.process_finalizers" to true.
 * 
 * If "vm.process_finalizers" is set to true, during vm initialization we create
 * a FinalizerThreadInfo object per process. ApplicationContext keeps a reference
 * to FinalizerThreadInfo of the process. This thread is alive during the entire
 * process execution. The finalizer thread is "always" waiting on an internal
 * private lock. The JPF Thread object corresponding to the FinalizerThreadInfo 
 * is encapsulated by the model class gov.nasa.jpf.FinalizerThread. The thread 
 * encapsulated by FinalizerThread has a queue of objects called "finalizeQueue"
 * which is kept at the SUT level. This queue is initially empty, and it is 
 * populated during the sweep() phase of the garbage collection. During sweep(), 
 * before removing unmark objects from the heap, any unmark finalizable object is 
 * marked and added to finalizeQueue. 
 * 
 * In VM.forward(), after each garbage collection, VM checks if finalizeQueue of 
 * the current process finalizer thread is not empty. If so, VM schedules the
 * finalizer thread to execute next, i.e. finalizer thread stops waiting and its 
 * state becomes runnable. To accomplish that, VM replaces the next choiceGenerator 
 * with a new choice generator from which only finalizer thread can proceed.
 *  
 * After finalizer thread processes the finalize() methods of all objects in
 * finalizeQueue, the queue becomes empty and the thread waits on its internal lock
 * again. After the process terminates, we still keep the finalizer thread to be 
 * processed after the last garbage collection involving the process. The runnable
 * finalizer thread terminates itself when it has processed its finalizeQueue and 
 * there is no other alive thread in the process.
 */
public class FinalizerThreadInfo extends ThreadInfo {
  
  static final String FINALIZER_NAME = "finalizer";
  
  ChoiceGenerator<?> replacedCG;
  
  protected FinalizerThreadInfo (VM vm, ApplicationContext appCtx) {
    super(vm, -1, appCtx);
    
    ci = appCtx.getSystemClassLoader().getResolvedClassInfo("gov.nasa.jpf.FinalizerThread");
    threadData.name = FINALIZER_NAME;
  }
  
  protected void createFinalizerThreadObject (SystemClassLoaderInfo sysCl){
    Heap heap = getHeap();

    ElementInfo eiThread = heap.newObject( ci, this);
    objRef = eiThread.getObjectRef();
    
    ElementInfo eiName = heap.newString(FINALIZER_NAME, this);
    int nameRef = eiName.getObjectRef();
    eiThread.setReferenceField("name", nameRef);
    
    // Since we create FinalizerThread upon VM initialization, they are assigned to the
    // same group as the main thread
    int grpRef = ThreadInfo.getCurrentThread().getThreadGroupRef();
    eiThread.setReferenceField("group", grpRef);
    
    eiThread.setIntField("priority", Thread.MAX_PRIORITY-2);

    ClassInfo ciPermit = sysCl.getResolvedClassInfo("java.lang.Thread$Permit");
    ElementInfo eiPermit = heap.newObject( ciPermit, this);
    eiPermit.setBooleanField("blockPark", true);
    eiThread.setReferenceField("permit", eiPermit.getObjectRef());

    addToThreadGroup(getElementInfo(grpRef));
    
    // we still haven't set the id. 
    id = computeId(objRef);
    addId( objRef, id);
    
    threadData.name = FINALIZER_NAME;

    // start the thread by pushing Thread.run()
    startFinalizerThread();
    
    eiThread.setBooleanField("done", false);
    ElementInfo finalizeQueue = getHeap().newArray("Ljava/lang/Object;", 0, this);
    eiThread.setReferenceField("finalizeQueue", finalizeQueue.getObjectRef());
    
    // create an internal private lock used for lock-free wait
    ElementInfo lock = getHeap().newObject(appCtx.getSystemClassLoader().objectClassInfo, this);
    eiThread.setReferenceField("semaphore", lock.getObjectRef());
    
    // make it wait on the internal private lock until its finalizeQueue is populated. This way,
    // we avoid scheduling points from including FinalizerThreads
    waitOnInternalLock();

    assert this.isWaiting();
  }
  
  /**
   * Pushes a frame corresponding to Thread.run() into the finalizer thread stack to
   * start the thread.
   */
  protected void startFinalizerThread() {
    MethodInfo mi = ci.getMethod("run()V", false);
    DirectCallStackFrame frame = mi.createDirectCallStackFrame(this, 0);
    frame.setReferenceArgument(0, objRef, frame);
    pushFrame(frame);
  }
  
  public boolean hasQueuedFinalizers() {
    ElementInfo queue = getFinalizeQueue();
    return (queue!=null && queue.asReferenceArray().length>0);
  }
  
  public ElementInfo getFinalizeQueue() {
    ElementInfo ei = vm.getModifiableElementInfo(objRef);
    ElementInfo queue = null;
    
    if(ei!=null) {
      int queueRef = ei.getReferenceField("finalizeQueue");
      queue = vm.getElementInfo(queueRef);
      return queue;
    }
    
    return queue;
  }
  
  /** 
   * This method is invoked by the sweep() phase of the garbage collection process (GenericHeap.sweep()).
   * It adds a given finalizable object to the finalizeQueue array of gov.nasa.jpf.FinalizerThread.
   */
  public void addToFinalizeQueue(ElementInfo ei) {
    ElementInfo oldQueue = getFinalizeQueue();
    int[] oldValues;
    
    if(oldQueue == null) {
      oldValues = new int[0];
    } else {
      oldValues = oldQueue.asReferenceArray();
    }
    
    int len = oldValues.length;
    ElementInfo newQueue = getHeap().newArray("Ljava/lang/Object;", len+1, this);
    int[] newValues = newQueue.asReferenceArray();
    
    System.arraycopy(oldValues, 0, newValues, 0, len);
    newValues[len] = ei.getObjectRef();
    vm.getModifiableElementInfo(objRef).setReferenceField("finalizeQueue", newQueue.getObjectRef());
    
    // Note that we are in sweep()/phase 1, and we need to mark the new queue to
    // avoid it from being GCed in sweep()/phase 2
    vm.getHeap().queueMark(newQueue.getObjectRef());
    newQueue.markRecursive(vm.getHeap());
    
    assert hasQueuedFinalizers();
    
    // make sure we process this object finalizer only once
    ei.setFinalized();
  }
  
  public boolean scheduleFinalizer() {
    if(hasQueuedFinalizers() && !isRunnable()) {
      replacedCG = vm.getNextChoiceGenerator();
      vm.getSystemState().removeNextChoiceGenerator();
      
      // NOTE - before we get here we have already made sure that nextCg is not Cascaded. 
      // we have to set nextCg to null before setting the nextCG, otherwise, the new CG is 
      // mistakenly considered as "Cascaded"
      vm.getSystemState().nextCg = null;
      ChoiceGenerator<ThreadInfo> cg = vm.getSystemState().getSchedulerFactory().createPreFinalizeCG(this);
      vm.getSystemState().setMandatoryNextChoiceGenerator(cg, "Need to start FinalizerThread to process objects finalizers");
      
      // stop waiting and process finalizers
      notifyOnInternalLock();
      assert this.isRunnable();
      
      return true;
    } 
    
    return false;
  }
  
  protected void waitOnInternalLock() {
    int lockRef = vm.getElementInfo(objRef).getReferenceField("semaphore");
    ElementInfo lock = vm.getModifiableElementInfo(lockRef);
    
    lock.wait(this, 0, false);
  }
  
  protected void notifyOnInternalLock() {
    int lockRef = vm.getElementInfo(objRef).getReferenceField("semaphore");
    ElementInfo lock = vm.getModifiableElementInfo(lockRef);
    
    lock.notifies(vm.getSystemState(), this, false);
  }
  
  @Override
  public boolean isSystemThread() {
    return true;
  }
}
