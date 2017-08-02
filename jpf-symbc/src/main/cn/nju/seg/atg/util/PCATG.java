package cn.nju.seg.atg.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import cn.nju.seg.atg.BEParse.BinaryExpressionParse;
import cn.nju.seg.atg.model.Coodinate;
import cn.nju.seg.atg.model.Interval;
import cn.nju.seg.atg.model.SearchTask;
import cn.nju.seg.atg.model.SimpleCFGNode;
import cn.nju.seg.atg.model.constraint.BinaryExpression;
import cn.nju.seg.atg.parse.CFGBuilder;
import cn.nju.seg.atg.parse.TestBuilder;

/**
 * path-oriented test data generation 
 * @author zy
 *
 */
public class PCATG extends ATG {
	
	static ExecutorService executor;
    //构建完成服务
    static CompletionService<Boolean> completionService;	
    
	/*
	 * Solver 求解（核心过程）
	 */
	@Override
	protected int generateTestDataForParamForSolver(int paramIndex,  double[] seed, int round, int nextRoundSeedIndex, int max_num_of_predict_param)
	{	
		currentSearchParamIndex = paramIndex;
		//初始化覆盖到的最长路径片段为
		SimpleCFGNode maxCFGNode=new SimpleCFGNode("finalCFGNode@LFF_Solver",ConstantValue.BRANCH_IF,
				BinaryExpressionParse.targetCFGNode.getConstraint(), true,0.0);
		
		//初始化自变参数产生池为空
		TestBuilder.parameterList = new ArrayList<Double>();
		TestBuilder.autoIncreasedParameterList = new ArrayList<Double>();
		
		//初始化参数值
	    initialParameter(seed);
		//添加当前自变参数到"自变参数产生池"
	    TestBuilder.parameterList.add(ATG.parameters[paramIndex]);
		//标记为非线性拟合函数预测的
		TestBuilder.autoIncreasedParameterList.add(ATG.parameters[paramIndex]);
		//判断是否已覆盖目标路径
		
		double[] paramsForNow=new double[NUM_OF_PARAM];
		System.arraycopy(ATG.parameters, 0, paramsForNow, 0, NUM_OF_PARAM);
		SimpleCFGNode targetCFGNode=new SimpleCFGNode("finalCFGNode@LFF_Solver",ConstantValue.BRANCH_IF,
				BinaryExpressionParse.targetCFGNode.getConstraint(), true,0.0);
		boolean isCoveredTargetPath =getConstraintCovered(maxCFGNode,targetCFGNode,paramsForNow,paramIndex);
		
		if (!isCoveredTargetPath)
		{	
			//改变当前自变参数为另一随机值，生成第二个随机输入向量
			setRandom(paramIndex, ATG.parameters[paramIndex], ConstantValue.INTERVAL_LEFT_OPEN);
			//添加当前自变参数到"自变参数产生池"
			int index = parameters[paramIndex] > TestBuilder.parameterList.get(0) ? 1 : 0;
			TestBuilder.parameterList.add(index, ATG.parameters[paramIndex]);			
			//标记为非线性拟合函数预测的
			TestBuilder.autoIncreasedParameterList.add(ATG.parameters[paramIndex]);
			
			double[] paramsForNow1=new double[NUM_OF_PARAM];
			System.arraycopy(ATG.parameters, 0, paramsForNow1, 0, NUM_OF_PARAM);
			//判断是否已覆盖目标路径
			isCoveredTargetPath = getConstraintCovered(maxCFGNode,targetCFGNode,paramsForNow1,paramIndex);
			
			if (!isCoveredTargetPath)
			{
				int parameterNum = 0;
				while(parameterNum < max_num_of_predict_param && !isCoveredTargetPath)
				{			        
					//确定当前搜索区间的左右边界值
					Interval maxInterval = maxInterval();
					TestBuilder.leftSearchBoundary = maxInterval.getLeftBoundary();
					TestBuilder.rightSearchBoundary = maxInterval.getRightBoundary();
                    /************************************************************************/
					//从自变参数衍生池中衍生新的自变参数
					List<Double> newParameterList = maxCFGNode.getNewParameterList(paramIndex);			
					//更新自变参数衍生池
					List<Double> newParameters = updateParameterList(newParameterList);
					/*************************************************************************/
					parameterNum = TestBuilder.parameterList.size();
					if(newParameters.size() < 1) 
						break;
					//判断衍生出的新输入向量中是否存在覆盖目标路径的
					int newParameterNum = newParameters.size();
					/********************************
					 * 所有新衍生出的输入变量均可以并行执行 *
					 ********************************/					
					//创建线程池
					//ExecutorService executor = Executors.newCachedThreadPool();
					//线程池的数量是当前设备的核心数量加1，这是为了提高CPU的使用效率，可以较好地保证CPU始终处于计算状态
					//之所以加1是因为对于当前的计算密集行的任务，核心数量加1是考虑到即使计算密集的线程偶尔由于缺失故障，
					//这个额外的线程也可以保证CPU始终处于运行状态					
					//ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()+1);
				    //构建完成服务
				    //CompletionService<Boolean> completionService = new ExecutorCompletionService<Boolean>(executor);	
				    List<ConcrrrentTask> taskList=new ArrayList<ConcrrrentTask>();
				    List<Double> times = new ArrayList<>();
					for (int i=0; i<newParameterNum; i++)
					{
						//读取一个衍生出的新输入向量，这是单线程的计算过程
						//parameters[paramIndex] = newParameters.get(i);
						//double[] paramsForNow2=new double[NUM_OF_PARAM];
						//System.arraycopy(ATG.parameters, 0, paramsForNow2, 0, NUM_OF_PARAM);
						//isCoveredTargetPath = getConstraintCovered(maxCFGNode,targetCFGNode,paramsForNow2,paramIndex);
						
						TestBuilder.function_frequency++;
						//创建新的变量输入
						double []paramsTemp=new double[NUM_OF_PARAM];
						ATG.parameters[paramIndex] = newParameters.get(i);
						System.arraycopy(ATG.parameters, 0, paramsTemp, 0, NUM_OF_PARAM);											
						
/*						//打印尝试的向量信息
						System.out.print("("+paramsTemp[0]);
						for(int gg=1;gg<paramsTemp.length;gg++)
							System.out.print(","+paramsTemp[gg]);
						System.out.println("),");*/
						
						
						//SimpleCFGNode tmp=new SimpleCFGNode("finalCFGNode@LFF_Solver",ConstantValue.BRANCH_IF,
								//BinaryExpressionParse.targetCFGNode.getConstraint(), true,0.0);
						//添加任务，并行执行
				    	ConcrrrentTask task=new ConcrrrentTask(maxCFGNode,paramsTemp,paramIndex);
					    // 向线程池提交任务
				    	taskList.add(task);
					    completionService.submit(task);
					}	
					
					for (int i=0; i<newParameterNum; i++)
					{				
						try {
							Future<Boolean>fu =completionService.take();
							isCoveredTargetPath=fu.get();
							if (isCoveredTargetPath)
							{	
								executor.shutdownNow();
								break;
							}
							
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							System.err.println("Sothing Wrong Happens,and maybe it is because Milt Threads Lock Problem");
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							System.err.println("Sothing Wrong Happens,and maybe it is because Milt Threads Lock Problem");
							e.printStackTrace();
						}
					}
					
					for(ConcrrrentTask task : taskList)
					{
						times.add(task.getExecTime());
					}
					TestBuilder.function_time +=getAverage(times);				
				
				}
			}
		}
				
		//返回结果
		if (isCoveredTargetPath)
		{	
			System.arraycopy(maxCFGNode.getFinalParams(), 0, TestBuilder.finalParams, 0,NUM_OF_PARAM);
			TestBuilder.finalCovered=maxCFGNode.getMaxCovered();
			
			/*System.out.println("TestBuilder.finalParams");
			for(int i=0;i<NUM_OF_PARAM;i++)
				System.out.print(TestBuilder.finalParams[i]+"  ");
			System.out.println();*/
			//求解成功，直接返回	
			return 1;
		}
		else
		{				
			System.arraycopy(maxCFGNode.getOptimalParams(), 0, TestBuilder.finalParams, 0,TestBuilder.finalParams.length);
			TestBuilder.finalCovered=maxCFGNode.getMaxCovered();
			
			//获取路径覆盖度值最高的输入数据
			double[] optimalInput = new double[NUM_OF_PARAM];
			System.arraycopy(maxCFGNode.getOptimalParams(), 0, optimalInput, 0, NUM_OF_PARAM);
			double priority = maxCFGNode.getMaxCovered();
			optimalInput[paramIndex] = maxCFGNode.getOptimalParams()[paramIndex];
			
			if(round == -1)
			{
				seedArray = optimalInput;
			}
			//第一轮，且是以随机值开始搜索
			else if(seed==null )
			{  
				seeds[nextRoundSeedIndex] = new SearchTask(optimalInput, priority, paramIndex, round);			  
			}
			//第一轮，且是以指定值开始搜索
			else if (round == 0)
			{			
				seeds[nextRoundSeedIndex] = new SearchTask(optimalInput, priority, paramIndex, round);			
			}
			//非第一轮
			else 
			{
				nextSeeds[nextRoundSeedIndex] = new SearchTask(optimalInput, priority, paramIndex, round);
			}					
			return -1;
		}
	}
    

	private Double getAverage(List<Double> times) {
		// TODO Auto-generated method stub
		Double sum=0.0;
		for(int i=0;i<times.size();i++)
		{
			sum+=times.get(i);
		}
		Double aver=sum/times.size();
		return aver;
	}
	
	/**
	 * 获取路径覆盖情况
	 * @param maxPath
	 * @param paramIndex
	 * @return isCovered
	 */
	public static boolean getConstraintCovered(SimpleCFGNode maxCFGNode,SimpleCFGNode targetCFGNode,double []paraForNow,int paramIndex){

/*		//打印尝试的向量信息
  		System.out.print("("+paraForNow[0]);
		for(int gg=1;gg<paraForNow.length;gg++)
			System.out.print(","+paraForNow[gg]);
		System.out.println("),");*/
		
		double startTime=System.currentTimeMillis();
		boolean isCoveredTargetPath = false;	
		SimpleCFGNode targetCfgNode=targetCFGNode;
		
		if( ATG.SEARCH_STRATEGY == ConstantValue.SEARCH_STRATEGY_ALL)
		{
			List<List<BinaryExpression>> constGroups=targetCfgNode.getConstraint().getAtomicConstraintGroups();
			//保存每一个子约束的时刻运行值的结果	
			Map<String, SubConstraintCover> allRes=new HashMap<>();
			//依次计算
			int outSize=constGroups.size();
			for(int i=0;i<outSize;i++)
			{
				List<BinaryExpression> group=constGroups.get(i);
				int inSize=group.size();
				for(int j=0;j<inSize;j++)
				{	
					String target=group.get(j).toString();
					if(!allRes.keySet().contains(target))
					{
					    SubConstraintCover res=calaConstraint(group.get(j),paraForNow,paramIndex);
						//添加计算得到的点
						Coodinate c=res.getResult();
						//实时计算后添加值对
						targetCfgNode.addValue(target, c);
						allRes.put(target, res);
					}
				}	
				
			}
			TestBuilder.function_frequency++;
			TestBuilder.function_time=TestBuilder.function_time+System.currentTimeMillis()-startTime;		
			isCoveredTargetPath=isCoveredAll(constGroups,allRes,paraForNow);
			double maxCovered=getMaxPathCovered(targetCfgNode,allRes);
			targetCfgNode.setMaxCovered(maxCovered);
			targetCfgNode.setOptimalParams(paraForNow);	
			
			if(isCoveredTargetPath)
			{
				//设置当前执行过后的最大覆盖度以及最优解
				maxCFGNode.setMaxCovered(maxCovered);
				maxCFGNode.setFinalParams(paraForNow);
						
				//找到可行解，直接返回
				return isCoveredTargetPath;
			}else
			{
				isCoveredTargetPath=false;
				//设置当前执行过后的最大覆盖度以及最优解
				maxCFGNode.updateForSolver(targetCfgNode);
				//没有得到可行解
			}
			return isCoveredTargetPath;
		}else  //按照程序短路的思想来实现
		{
			List<List<BinaryExpression>> constGroups=targetCfgNode.getConstraint().getAtomicConstraintGroups();
			//保存每一个子约束的时刻运行值的结果	
			Map<String, SubConstraintCover> allRes=new HashMap<>();
			//依次计算
			int outSize=constGroups.size();
			for(int i=0;i<outSize;i++)
			{
				List<BinaryExpression> group=constGroups.get(i);
				int inSize=group.size();
				int j=0;
				for(j=0;j<inSize;j++)
				{	
					String target=group.get(j).toString();
					SubConstraintCover res=calaConstraint(group.get(j),paraForNow,paramIndex);
					//添加计算得到的点
					Coodinate c=res.getResult();
					//实时计算后添加值对
					targetCfgNode.addValue(target, c);
					allRes.put(target, res);
					//只要遇到false就停止
					if(res.isSubCobstraintCovered()==false)
					{
						break;
					}
				}
				//j>=inSize 表示内部循环不是通过break结束，表示子合区式子全部为true，也即找到了解
				if(j>=inSize)
				{
					isCoveredTargetPath=true;
					break;
				}else //j<inSize 表示内部循环是通过break结束，程序短路停止
				{
					isCoveredTargetPath=false;
					break;
				}
				
				
			}
			TestBuilder.function_frequency++;
			TestBuilder.function_time=TestBuilder.function_time+System.currentTimeMillis()-startTime;		
			
			double maxCovered=getMaxPathCovered(targetCfgNode,allRes);
			targetCfgNode.setMaxCovered(maxCovered);
			targetCfgNode.setOptimalParams(paraForNow);	
			
			if(isCoveredTargetPath)
			{
				//设置当前执行过后的最大覆盖度以及最优解
				maxCFGNode.setMaxCovered(maxCovered);
				maxCFGNode.setFinalParams(paraForNow);
						
				//找到可行解，直接返回
				return isCoveredTargetPath;
			}else
			{
				isCoveredTargetPath=false;
				//设置当前执行过后的最大覆盖度以及最优解
				maxCFGNode.updateForSolver(targetCfgNode);
				//没有得到可行解
			}
			return isCoveredTargetPath;
		}
	}
	

	/**
	 * 获取路径覆盖情况
	 * @param constGroups
	 * @param allRes
	 */
	private static boolean isCoveredAll(List<List<BinaryExpression>> constGroups,Map<String, SubConstraintCover> allRes,
			double [] paraForNow) 
	{
		boolean isCoveredTargetPath=false;
	    int outSize=constGroups.size();
		for(int i=0;i<outSize;i++)
		{
			List<BinaryExpression> group=constGroups.get(i);
			int inSize=group.size();
			StringBuilder target=new StringBuilder(group.get(0).toString());
			for(int j=1;j<inSize;j++)
			{	
				target.append("&&");
				target.append(group.get(j));
			}
			isCoveredTargetPath= ComputeEngine.getBoolForFel(target.toString(), paraForNow);
			if(isCoveredTargetPath)
			{
				break;
			}
		}
		
		return isCoveredTargetPath;
	}

	/**
	 * 获取分歧节点中具有最大路径覆盖度评估值
	 * <p> F = max{1 / Ncon(i) * (CC + 1 / (1 + abs(EP))}
	 * @return
	 */
	private static double getMaxPathCovered(SimpleCFGNode targetCfgNode,Map<String, SubConstraintCover> allRes){
		
		List<List<BinaryExpression>> constGroups=targetCfgNode.getConstraint().getAtomicConstraintGroups();
		boolean isSubCovered=false;
		String target="";
		
		double maxPathCovered=-1;
		int outSize=constGroups.size();
		//double tempValue =Double.MAX_VALUE;
		double tempInput=0;
		
		for(int i=0;i<outSize;i++)
		{
			List<BinaryExpression> group=constGroups.get(i);
			double subConstMaxCovered=0.0;
			int inSize=group.size();
			for(int j=0;j<inSize;j++)
			{	
				
				target=group.get(j).toString();
				if(allRes.containsKey(target))
				{
					isSubCovered = allRes.get(target).isSubCobstraintCovered();
					if (isSubCovered) 
					{
						subConstMaxCovered = subConstMaxCovered + 1.0;
					} else 
					{
						Double tempValue = Math.abs(allRes.get(target).getResult().getY());
						String tString = tempValue + "";
						if (!tString.equals("NaN"))
						{
							subConstMaxCovered = subConstMaxCovered + 1.0/ (1 + tempValue);
						}

					}
				}
			   
			}
			
			Double tmp=(double)subConstMaxCovered/inSize;
			
			if(tmp>=maxPathCovered)
			{
				maxPathCovered=tmp;
				//tempInput=allRes.get(target).getResult().getX();
			}
		}
		
		//暂时写在这里，感觉没用
		targetCfgNode.setOptimal(tempInput);
		targetCfgNode.setMaxCovered(maxPathCovered);
		
		return maxPathCovered;
	}
	

	/**
	 * 计算某一个约束的运行时刻值
	 * @param paramIndex
	 * @param node
	 * @return res
	 */
	public static SubConstraintCover calaConstraint(BinaryExpression node,double []paraForNow,int paramIndex)
	{
	    StringBuilder calaBuilder=new StringBuilder();
	    calaBuilder.append("(");
	    calaBuilder.append(node.getOperand1().toString());
	    calaBuilder.append(") - (");
	    calaBuilder.append(node.getOperand2().toString());
	    calaBuilder.append(")");
	    
	    double instruRes=ComputeEngine.getDouForFel(calaBuilder.toString(), paraForNow);
	    
	    SubConstraintCover sub=new SubConstraintCover();
		String op=node.getOp().toString();
		
        Coodinate c=new Coodinate(paraForNow[paramIndex],instruRes);
        boolean isCovered=calaSubCovered(instruRes, op);
        sub.setResult(c);
        sub.setSubCobstraintCovered(isCovered);
 	  
		return sub;
	}
	
	
	/**
	 * 判断子约束的是否满足
	 * @param maxPath
	 */
	public static boolean calaSubCovered(double res,String op)
	{
		boolean isCovered=false;
		//计算的实时运行的值是无效的
		String string=res+"";
		if(string.equals("NaN"))
		{
			isCovered=false;
			return false;
		}else
		{
			switch (op) 
			{
			case ">":
				isCovered=(res>0)?true:false;
				break;
			case ">=":
				isCovered=(res>=0)?true:false;
				break;
			case "<":
				isCovered=(res<0)?true:false;
				break;
			case "<=":
				isCovered=(res<=0)?true:false;
				break;
			case "!=":
				isCovered=(res!=0.0)?true:false;
				break;
			case "==":
				isCovered=(Math.abs(res)<=Double.MIN_VALUE) ? true:false;
				break;
			default:
				isCovered=false;
				System.err.println("计算子约束是否Covered出错！！！");
				break;
			}
			return isCovered;
		}
	}
	
	
}

