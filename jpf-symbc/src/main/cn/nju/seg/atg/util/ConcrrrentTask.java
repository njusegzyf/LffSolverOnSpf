package cn.nju.seg.atg.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import cn.nju.seg.atg.BEParse.BinaryExpressionParse;
import cn.nju.seg.atg.model.Coodinate;
import cn.nju.seg.atg.model.SimpleCFGNode;
import cn.nju.seg.atg.model.constraint.BinaryExpression;
import cn.nju.seg.atg.parse.CFGBuilder;

public class ConcrrrentTask implements Callable<Boolean>
{
	private SimpleCFGNode maxCfgNode;
	private double [] parameters;
	private int paramIndex;
	private double execTime;	
	public ConcrrrentTask() 
	{
		// TODO Auto-generated constructor stub
	}
	
	public ConcrrrentTask(SimpleCFGNode maxCFGNode,double para[],int paramIndex) 
	{
		// TODO Auto-generated constructor stub
		this.maxCfgNode=maxCFGNode;
		this.parameters=new double[para.length];
		System.arraycopy(para, 0, this.parameters,0, para.length);
		this.paramIndex=paramIndex;
		this.execTime=0.0;
	}
	
	@Override
	public Boolean call() 
	{	
		boolean isCovered=false;
		try {
			double startTime=System.currentTimeMillis();
			isCovered=getConstraintCovered(this.maxCfgNode,this.parameters,this.paramIndex);
			this.execTime=System.currentTimeMillis()-startTime;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
		return isCovered;
	}
	
	
	/**
	 * 获取路径覆盖情况
	 * @param maxPath
	 * @param paramIndex
	 * @return isCovered
	 */
	public boolean getConstraintCovered(SimpleCFGNode maxCFGNode,double []paraForNow,int paramIndex)
	{	
		boolean isCoveredTargetPath = false;	
		SimpleCFGNode targetCfgNode=new SimpleCFGNode("finalCFGNode@LFF_Solver",ConstantValue.BRANCH_IF,
				BinaryExpressionParse.targetCFGNode.getConstraint(),true,0.0);
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
			
			isCoveredTargetPath=isCoveredAll(constGroups,allRes,paraForNow);	
			double maxCovered=getMaxPathCovered(targetCfgNode,allRes);
			allRes.clear();
			
			targetCfgNode.setMaxCovered(maxCovered);
			targetCfgNode.setOptimalParams(paraForNow);	
			
			if(isCoveredTargetPath)
			{
				//设置当前执行过后的最大覆盖度以及最优解
				maxCFGNode.setMaxCovered(maxCovered);
				maxCFGNode.setFinalParams(paraForNow);
				//找到可行解，直接返回
			}else
			{
				//设置当前执行过后的最大覆盖度以及最优解
				maxCFGNode.updateForSolver(targetCfgNode);
				//没有得到可行解
			}
			return isCoveredTargetPath;
		}else  //单变量搜索模式
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
			
			double maxCovered=getMaxPathCovered(targetCfgNode,allRes);
			allRes.clear();
			targetCfgNode.setMaxCovered(maxCovered);
			targetCfgNode.setOptimalParams(paraForNow);	
			
			if(isCoveredTargetPath)
			{
				//设置当前执行过后的最大覆盖度以及最优解
				maxCFGNode.setMaxCovered(maxCovered);
				maxCFGNode.setFinalParams(paraForNow);
				//找到可行解，直接返回
			}else
			{
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
	public boolean isCoveredAll(List<List<BinaryExpression>> constGroups,Map<String, SubConstraintCover> allRes,
			double [] paraForNow)
	{
		boolean isCoveredTargetPath=false;
	    int outSize=constGroups.size();
		for(int i=0;i<outSize;i++)
		{
			List<BinaryExpression> group=constGroups.get(i);
			int inSize=group.size();
			StringBuilder target=new StringBuilder("(");
			target.append(group.get(0).toString());
			target.append(")");
			for(int j=1;j<inSize;j++)
			{	
				target.append("&&");
				target.append("(");
				target.append(group.get(j).toString());
				target.append(")");
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
	public SubConstraintCover calaConstraint(BinaryExpression node,double []paraForNow,int paramIndex)
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
        sub.setCalaString(calaBuilder.toString());
 	  
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
	
	
	
	
	public SimpleCFGNode getMaxCfgNode() {
		return maxCfgNode;
	}

	public void setMaxCfgNode(SimpleCFGNode maxCfgNode) {
		this.maxCfgNode = maxCfgNode;
	}

	public double[] getParameters() {
		return parameters;
	}

	public void setParameters(double[] parameters) 
	{
		this.parameters = parameters;
	}

	public int getParamIndex() {
		return paramIndex;
	}

	public void setParamIndex(int paramIndex) {
		this.paramIndex = paramIndex;
	}

	public double getExecTime() {
		return execTime;
	}

	public void setExecTime(double execTime) {
		this.execTime = execTime;
	}

}
