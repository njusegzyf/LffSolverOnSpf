//
// Copyright (C) 2006 United States Government as represented by the
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

import gov.nasa.jpf.Config;
import gov.nasa.jpf.annotation.MJI;

import java.lang.reflect.Modifier;

public class JPF_java_lang_reflect_Field extends NativePeer {

  // the registry is rather braindead, let's hope we don't have many lookups - 
  // using Fields is fine, but creating them is not efficient until we fix this
  
  static final int NREG = 64;
  static FieldInfo[] registered;
  static int nRegistered;
  
  public static boolean init (Config conf){
    registered = new FieldInfo[NREG];
    nRegistered = 0;
    return true;
  }
  
  static int registerFieldInfo (FieldInfo fi) {
    int idx;
    
    for (idx=0; idx < nRegistered; idx++) {
      if (registered[idx] == fi) {
        return idx;
      }
    }
    
    if (idx == registered.length) {
      FieldInfo[] newReg = new FieldInfo[registered.length+NREG];
      System.arraycopy(registered, 0, newReg, 0, registered.length);
      registered = newReg;
    }
    
    registered[idx] = fi;
    nRegistered++;
    return idx;
  }
  
  static FieldInfo getRegisteredFieldInfo (int idx) {
    return registered[idx];
  }
  
  /**
   * >2do> that doesn't take care of class init yet
   */
  @MJI
  public int getType____Ljava_lang_Class_2 (MJIEnv env, int objRef) {
    ThreadInfo ti = env.getThreadInfo();
    FieldInfo fi = getFieldInfo(env, objRef);

    ClassInfo ci = fi.getTypeClassInfo();
    if (!ci.isRegistered()) {
      ci.registerClass(ti);
    }

    return ci.getClassObjectRef();
  }
  
  @MJI
  public int getModifiers____I (MJIEnv env, int objRef){
    FieldInfo fi = getFieldInfo(env, objRef);
    return fi.getModifiers();
  }

  static ElementInfo getCheckedElementInfo (MJIEnv env, FieldInfo fi, int fobjRef, Class<?> fiType, String type, boolean isWrite){
    ElementInfo ei;

    if (!isAvailable(env, fi, fobjRef)){
      return null;
    }

    if (fi.isStatic()){
      ClassInfo fci = fi.getClassInfo();
      ei = isWrite ? fci.getModifiableStaticElementInfo() : fci.getStaticElementInfo();
    } else { // instance field
      ei = isWrite ? env.getModifiableElementInfo(fobjRef) : env.getElementInfo(fobjRef);
    }

    // our guards (still need IllegalAccessException)
    if (ei == null) {
      env.throwException("java.lang.NullPointerException");
      return null;
    }
    if (fiType != null && !fiType.isInstance(fi)) {
      env.throwException("java.lang.IllegalArgumentException", "field type incompatible with " + type);
      return null;
    }

    return ei;
  }
  
  @MJI
  public boolean getBoolean__Ljava_lang_Object_2__Z (MJIEnv env, int objRef, int fobjRef) {
    FieldInfo fi = getFieldInfo(env, objRef);    
    ElementInfo ei = getCheckedElementInfo(env, fi, fobjRef, BooleanFieldInfo.class, "boolean", false);
    if (ei != null){
      return ei.getBooleanField(fi);
    }
    return false;
  }

  @MJI
  public byte getByte__Ljava_lang_Object_2__B (MJIEnv env, int objRef, int fobjRef) {
    FieldInfo fi = getFieldInfo(env, objRef);
    ElementInfo ei = getCheckedElementInfo(env, fi, fobjRef, ByteFieldInfo.class, "byte", false);
    if (ei != null){
      return ei.getByteField(fi);
    }
    return 0;
  }

  @MJI
  public char getChar__Ljava_lang_Object_2__C (MJIEnv env, int objRef, int fobjRef) {
    FieldInfo fi = getFieldInfo(env, objRef);
    ElementInfo ei = getCheckedElementInfo(env, fi, fobjRef, CharFieldInfo.class, "char", false);
    if (ei != null){
      return ei.getCharField(fi);
    }
    return 0;
  }

  @MJI
  public short getShort__Ljava_lang_Object_2__S (MJIEnv env, int objRef, int fobjRef) {
    FieldInfo fi = getFieldInfo(env, objRef);
    ElementInfo ei = getCheckedElementInfo(env, fi, fobjRef, ShortFieldInfo.class, "short", false);
    if (ei != null){
      return ei.getShortField(fi);
    }
    return 0;
  }

  @MJI
  public int getInt__Ljava_lang_Object_2__I (MJIEnv env, int objRef, int fobjRef) {
    FieldInfo fi = getFieldInfo(env, objRef);
    ElementInfo ei = getCheckedElementInfo(env, fi, fobjRef, IntegerFieldInfo.class, "int", false);
    if (ei != null){
      return ei.getIntField(fi);
    }
    return 0;
  }

  @MJI
  public long getLong__Ljava_lang_Object_2__J (MJIEnv env, int objRef, int fobjRef) {
    FieldInfo fi = getFieldInfo(env, objRef);
    ElementInfo ei = getCheckedElementInfo(env, fi, fobjRef, LongFieldInfo.class, "long", false);
    if (ei != null){
      return ei.getLongField(fi);
    }
    return 0;
  }

  @MJI
  public float getFloat__Ljava_lang_Object_2__F (MJIEnv env, int objRef, int fobjRef) {
    FieldInfo fi = getFieldInfo(env, objRef);
    ElementInfo ei = getCheckedElementInfo(env, fi, fobjRef, FloatFieldInfo.class, "float", false);
    if (ei != null){
      return ei.getFloatField(fi);
    }
    return 0;
  }

  @MJI
  public double getDouble__Ljava_lang_Object_2__D (MJIEnv env, int objRef, int fobjRef) {
    FieldInfo fi = getFieldInfo(env, objRef);
    ElementInfo ei = getCheckedElementInfo(env, fi, fobjRef, DoubleFieldInfo.class, "double", false);
    if (ei != null){
      return ei.getDoubleField(fi);
    }
    return 0;
  }

  @MJI
  public int getAnnotation__Ljava_lang_Class_2__Ljava_lang_annotation_Annotation_2 (MJIEnv env, int objRef, int annotationClsRef) {
    FieldInfo fi = getFieldInfo(env,objRef);
    ClassInfo aci = env.getReferredClassInfo(annotationClsRef);
    
    AnnotationInfo ai = fi.getAnnotation(aci.getName());
    if (ai != null){
      ClassInfo aciProxy = aci.getAnnotationProxy();
      try {
        return env.newAnnotationProxy(aciProxy, ai);
      } catch (ClinitRequired x){
        env.handleClinitRequest(x.getRequiredClassInfo());
        return MJIEnv.NULL;
      }
    }
    
    return MJIEnv.NULL;
  }

  @MJI
  public int getAnnotations_____3Ljava_lang_annotation_Annotation_2 (MJIEnv env, int objRef){
    FieldInfo fi = getFieldInfo(env,objRef);
    AnnotationInfo[] ai = fi.getAnnotations();
    
    try {
      return env.newAnnotationProxies(ai);
    } catch (ClinitRequired x){
      env.handleClinitRequest(x.getRequiredClassInfo());
      return MJIEnv.NULL;
    }
  }

  @MJI
  public void setBoolean__Ljava_lang_Object_2Z__V (MJIEnv env, int objRef, int fobjRef,
                                                          boolean val) {
    FieldInfo fi = getFieldInfo(env, objRef);
    if (!isAvailable(env, fi, fobjRef)){
      return;
    }
    
    ElementInfo ei = getCheckedElementInfo(env, fi, fobjRef, BooleanFieldInfo.class, "boolean", true);
    if (ei != null){
      ei.setBooleanField(fi,val);
    }
  }

  @MJI
  public void setByte__Ljava_lang_Object_2B__V (MJIEnv env, int objRef, int fobjRef, byte val) {
    FieldInfo fi = getFieldInfo(env, objRef);
    if (!isAvailable(env, fi, fobjRef)){
      return;
    }
    
    ElementInfo ei = getCheckedElementInfo(env, fi, fobjRef, ByteFieldInfo.class, "byte", true);
    if (ei != null){
      ei.setByteField(fi,val);
    }
  }

  @MJI
  public void setChar__Ljava_lang_Object_2C__V (MJIEnv env, int objRef, int fobjRef, char val) {
    FieldInfo fi = getFieldInfo(env, objRef);
    if (!isAvailable(env, fi, fobjRef)){
      return;
    }
    
    ElementInfo ei = getCheckedElementInfo(env, fi, fobjRef, CharFieldInfo.class, "char", true);
    if (ei != null){
      ei.setCharField(fi,val);
    }
  }

  @MJI
  public void setShort__Ljava_lang_Object_2S__V (MJIEnv env, int objRef, int fobjRef,  short val) {
    FieldInfo fi = getFieldInfo(env, objRef);
    if (!isAvailable(env, fi, fobjRef)){
      return;
    }
    
    ElementInfo ei = getCheckedElementInfo(env, fi, fobjRef, ShortFieldInfo.class, "short", true);
    if (ei != null){
      ei.setShortField(fi,val);
    }
  }  

  @MJI
  public void setInt__Ljava_lang_Object_2I__V (MJIEnv env, int objRef, int fobjRef, int val) {
    FieldInfo fi = getFieldInfo(env, objRef);
    if (!isAvailable(env, fi, fobjRef)){
      return;
    }
    
    ElementInfo ei = getCheckedElementInfo(env, fi, fobjRef, IntegerFieldInfo.class, "int", true);
    if (ei != null){
      ei.setIntField(fi,val);
    }
  }

  @MJI
  public void setLong__Ljava_lang_Object_2J__V (MJIEnv env, int objRef, int fobjRef, long val) {
    FieldInfo fi = getFieldInfo(env, objRef);
    if (!isAvailable(env, fi, fobjRef)){
      return;
    }
    
    ElementInfo ei = getCheckedElementInfo(env, fi, fobjRef, LongFieldInfo.class, "long", true);
    if (ei != null){
      ei.setLongField(fi,val);
    }
  }

  @MJI
  public void setFloat__Ljava_lang_Object_2F__V (MJIEnv env, int objRef, int fobjRef, float val) {
    FieldInfo fi = getFieldInfo(env, objRef);
    if (!isAvailable(env, fi, fobjRef)){
      return;
    }
    
    ElementInfo ei = getCheckedElementInfo(env, fi, fobjRef, FloatFieldInfo.class, "float", true);
    if (ei != null){
      ei.setFloatField(fi,val);
    }
  }

  @MJI
  public void setDouble__Ljava_lang_Object_2D__V (MJIEnv env, int objRef, int fobjRef, double val) {
    FieldInfo fi = getFieldInfo(env, objRef);
    if (!isAvailable(env, fi, fobjRef)){
      return;
    }
    
    ElementInfo ei = getCheckedElementInfo(env, fi, fobjRef, DoubleFieldInfo.class, "double", true);
    if (ei != null){
      ei.setDoubleField(fi,val);
    }
  }

  @MJI
  public int get__Ljava_lang_Object_2__Ljava_lang_Object_2 (MJIEnv env, int objRef, int fobjRef) {
    FieldInfo fi = getFieldInfo(env, objRef);
    ElementInfo ei = getCheckedElementInfo( env, fi, fobjRef, null, null, false); // no type check here
    if (ei == null){
      return 0;
    }
        
    if (!(fi instanceof ReferenceFieldInfo)) { // primitive type, we need to box it
      if (fi instanceof DoubleFieldInfo){
        double d = ei.getDoubleField(fi);
        return env.newDouble(d);
      } else if (fi instanceof FloatFieldInfo){
        float f = ei.getFloatField(fi);
        return env.newFloat(f);
      } else if (fi instanceof LongFieldInfo){
        long l = ei.getLongField(fi);
        return env.newLong(l);
      } else if (fi instanceof IntegerFieldInfo){
        // this might actually represent a plethora of types
        int i = ei.getIntField(fi);
        return env.newInteger(i);
      } else if (fi instanceof BooleanFieldInfo){
        boolean b = ei.getBooleanField(fi);
        return env.newBoolean(b);
      } else if (fi instanceof ByteFieldInfo){
        byte z = ei.getByteField(fi);
        return env.newByte(z);
      } else if (fi instanceof CharFieldInfo){
        char c = ei.getCharField(fi);
        return env.newCharacter(c);
      } else if (fi instanceof ShortFieldInfo){
        short s = ei.getShortField(fi);
        return env.newShort(s);
      }
      
    } else { // it's a reference
      int ref = ei.getReferenceField(fi); // we internally store it as int
      return ref;
    }
    
    env.throwException("java.lang.IllegalArgumentException", "unknown field type");
    return MJIEnv.NULL;
  }

  @MJI
  public int getDeclaringClass____Ljava_lang_Class_2 (MJIEnv env, int objref){
    FieldInfo fi = getFieldInfo(env, objref);
    ClassInfo ci = fi.getClassInfo();
    return ci.getClassObjectRef();
  }

  @MJI
  public boolean isSynthetic____Z (MJIEnv env, int objref){
    FieldInfo fi = getFieldInfo(env, objref);
    String fn = fi.getName();
    return (fn.startsWith("this$") || fn.startsWith("val$"));
  }

  @MJI
  public int getName____Ljava_lang_String_2 (MJIEnv env, int objRef) {
    FieldInfo fi = getFieldInfo(env, objRef);
    
    int nameRef = env.getReferenceField( objRef, "name");
    if (nameRef == MJIEnv.NULL) {
      nameRef = env.newString(fi.getName());
      env.setReferenceField(objRef, "name", nameRef);
    }
   
    return nameRef;
  }

  static FieldInfo getFieldInfo (MJIEnv env, int objRef) {
    int fidx = env.getIntField( objRef, "regIdx");
    assert ((fidx >= 0) || (fidx < nRegistered)) : "illegal FieldInfo request: " + fidx + ", " + nRegistered;
    
    return registered[fidx];
  }
  
  static boolean isAvailable (MJIEnv env, FieldInfo fi, int fobjRef){
    if (fi.isStatic()){
      ClassInfo fci = fi.getClassInfo();
      if (fci.pushRequiredClinits(env.getThreadInfo())){
        env.repeatInvocation();
        return false;
      }
      
    } else {
      if (fobjRef == MJIEnv.NULL){
        env.throwException("java.lang.NullPointerException");
        return false;        
      }
      // class had obviously been initialized, otherwise we won't have an instance of it
    }

    return true;
  }
  
  /**
  * Peer method for the <code>java.lang.reflect.Field.set</code> method.
  * 
  * @author Mirko Stojmenovic (mirko.stojmenovic@gmail.com)
  * @author Igor Andjelkovic (igor.andjelkovic@gmail.com)
  * @author Milos Gligoric (milos.gligoric@gmail.com)
  *  
  */
  @MJI
  public void set__Ljava_lang_Object_2Ljava_lang_Object_2__V (MJIEnv env, int objRef, int fobjRef, int val) {
    FieldInfo fi = getFieldInfo(env, objRef);
    int modifiers = fi.getModifiers();

    if (!isAvailable(env, fi, fobjRef)){
      return;
    }
        
    if (Modifier.isFinal(modifiers)) {
      env.throwException("java.lang.IllegalAccessException", "field " + fi.getName() + " is final");
      return;
    }
    ClassInfo ci = fi.getClassInfo();
    ClassInfo cio = env.getClassInfo(fobjRef);

    if (!fi.isStatic() && !cio.isInstanceOf(ci)) {
      env.throwException("java.lang.IllegalArgumentException", 
                         fi.getType() + "field " + fi.getName() + " does not belong to this object");
      return;
    }
    
    Object[] attrs = env.getArgAttributes();
    Object attr = (attrs==null)? null: attrs[2];
    
    if (!setValue(env, fi, fobjRef, val, attr)) {
      env.throwException("java.lang.IllegalArgumentException",  
                         "Can not set " + fi.getType() + " field " + fi.getFullName() + " to " + ((MJIEnv.NULL != val) ? env.getClassInfo(val).getName() + " object " : "null"));
    }
  }

  private static boolean setValue(MJIEnv env, FieldInfo fi, int obj, int value, Object attr) {
    ClassInfo fieldClassInfo = fi.getClassInfo();
    String className = fieldClassInfo.getName();
    String fieldType = fi.getType();
    ClassInfo tci = fi.getTypeClassInfo();
    
    ElementInfo ei = null;
    if (fi.isStatic()) {
      ei = fi.getClassInfo().getModifiableStaticElementInfo();
    } else {
      ei = env.getModifiableElementInfo(obj);
    }

    if (tci.isPrimitive()) {
      if (value == MJIEnv.NULL) {
        return false;
      }

      // checks whether unboxing can be done by accessing the field "value"
      final String fieldName = "value";
      FieldInfo finfo = env.getElementInfo(value).getFieldInfo(fieldName);
      if (finfo == null) {
        return false;
      }
      
      ei.setFieldAttr(fi, attr);

      if ("boolean".equals(fieldType)){
        boolean val = env.getBooleanField(value, fieldName);
        ei.setBooleanField(fi, val);
        return true;
      } else if ("byte".equals(fieldType)){
        byte val = env.getByteField(value, fieldName);
        ei.setByteField(fi, val);
        return true;
      } else if ("char".equals(fieldType)){
        char val = env.getCharField(value, fieldName);
        ei.setCharField(fi, val);
        return true;
      } else if ("short".equals(fieldType)){
        short val = env.getShortField(value, fieldName);
        ei.setShortField(fi, val);
        return true;
      } else if ("int".equals(fieldType)){
        int val = env.getIntField(value, fieldName);
        ei.setIntField(fi, val);
        return true;
      } else if ("long".equals(fieldType)){
        long val = env.getLongField(value, fieldName);
        ei.setLongField(fi, val);
        return true;
      } else if ("float".equals(fieldType)){
        float val = env.getFloatField(value, fieldName);
        ei.setFloatField(fi, val);
        return true;
      } else if ("double".equals(fieldType)){
        double val = env.getDoubleField(value, fieldName);
        ei.setDoubleField(fi, val);
        return true;
      } else {
        return false;
      }

    } else { // it's a reference
      if (value != MJIEnv.NULL) {
        ClassInfo ciValue = env.getClassInfo(value);
        if (!ciValue.isInstanceOf(tci)) {
          return false;
        }
      }

      ei.setFieldAttr(fi, attr);

      if (fi.isStatic()) {
        env.setStaticReferenceField(className, fi.getName(), value);
      } else {
        env.setReferenceField(obj, fi.getName(), value);
      }
      return true;
    }
  }

  @MJI
  public boolean equals__Ljava_lang_Object_2__Z (MJIEnv env, int objRef, int fobjRef){
    int fidx = env.getIntField(fobjRef, "regIdx");
    if (fidx >= 0 && fidx < nRegistered){
      FieldInfo fi1 = getFieldInfo(env, objRef);
      FieldInfo fi2 = getFieldInfo(env, fobjRef);
      return ((fi1.getClassInfo() == fi2.getClassInfo()) && fi1.getName().equals(fi2.getName()) && fi1.getType().equals(fi2.getType()));
    }
    return false;
  }

  @MJI
  public int toString____Ljava_lang_String_2 (MJIEnv env, int objRef){
    StringBuilder sb = new StringBuilder();
    FieldInfo fi = getFieldInfo(env, objRef);
    sb.append(Modifier.toString(fi.getModifiers()));
    sb.append(' ');
    sb.append(fi.getType());
    sb.append(' ');
    sb.append(fi.getFullName());
    int sref = env.newString(sb.toString());
    return sref;
  }

  @MJI
  public int hashCode____I (MJIEnv env, int objRef){
    FieldInfo fi = getFieldInfo(env, objRef);
    return fi.getClassInfo().getName().hashCode() ^ fi.getName().hashCode();
  }

  @MJI
  public int getDeclaredAnnotations_____3Ljava_lang_annotation_Annotation_2 (MJIEnv env, int objRef){
    return getAnnotations_____3Ljava_lang_annotation_Annotation_2(env, objRef);
  }
}
