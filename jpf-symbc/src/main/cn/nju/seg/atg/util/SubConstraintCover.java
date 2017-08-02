package cn.nju.seg.atg.util;

import cn.nju.seg.atg.model.Coodinate;

/*
 * 保存每一个子约束运行过的结果
 */
public class SubConstraintCover {
	private Coodinate result;
	private boolean isSubCobstraintCovered;
	
	private String calaString;
	
	public SubConstraintCover() 
	{
		// TODO Auto-generated constructor stub
		isSubCobstraintCovered=false;
	}
	public SubConstraintCover(Coodinate res,boolean isCovered,String cala) 
	{
		// TODO Auto-generated constructor stub
		this.result=res;
		this.isSubCobstraintCovered=isCovered;
		this.calaString=cala;
	}
	public Coodinate getResult()
	{
		return result;
	}
	public void setResult(Coodinate result) 
	{
		this.result = result;
	}
	public boolean isSubCobstraintCovered() 
	{
		return isSubCobstraintCovered;
	}
	public void setSubCobstraintCovered(boolean isSubCobstraintCovered) 
	{
		this.isSubCobstraintCovered = isSubCobstraintCovered;
	}
	
	public String getCalaString() {
		return calaString;
	}
	public void setCalaString(String calaString) {
		this.calaString = calaString;
	}
	
	

}
