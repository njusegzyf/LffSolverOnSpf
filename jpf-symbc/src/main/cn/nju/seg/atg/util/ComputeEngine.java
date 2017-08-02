package cn.nju.seg.atg.util;

import javax.xml.crypto.Data;

import cn.nju.seg.atg.parse.CFGBuilder;
import cn.nju.seg.atg.parse.TestBuilder;

import com.greenpineyu.fel.FelEngine;
import com.greenpineyu.fel.FelEngineImpl;
import com.greenpineyu.fel.context.FelContext;
import com.greenpineyu.fel.exception.EvalException;
import com.greenpineyu.fel.exception.ParseException;

public class ComputeEngine
{
	/**
	 * 简单子约束的运行时刻的时是值 Fel计算引擎
	 */
	public static FelEngine myFelEngine=new FelEngineImpl();
    public static FelContext myCtx=myFelEngine.getContext();  
	
    public static synchronized void initForFel() 
    {
    	BasicMathFunc basicMathFunc=new BasicMathFunc();
    	myCtx.set("Math", basicMathFunc);
	}
    
   public static synchronized double getDouForFel(String calaStr,double []paraForNow)
    {
	    for(int i=0;i<CFGBuilder.parameterNames.length;i++)
	    {
	    	myCtx.set(CFGBuilder.parameterNames[i],paraForNow[i]);
	    }
	    
    	double finalRes=0.0;
    	try
    	{    		
    		Object result=myFelEngine.eval(calaStr);
    		finalRes=Double.parseDouble(result.toString());	
	    } catch (EvalException e) 
	    {
			// TODO: handle exception
			e.printStackTrace();
			System.err.println(calaStr);
			System.err.println("EvalException !!!!!");
		}catch (ClassCastException e)
    	{
			// TODO: handle exception
			e.printStackTrace();
			System.err.println(calaStr);
			System.err.println("ClassCastException !!!!!");
		}catch (IllegalArgumentException e) {
			// TODO: handle exception
			e.printStackTrace();
			System.err.println(calaStr);
			System.err.println("IllegalArgumentException: argument type mismatch !!!!!");
		}catch (ParseException e) {
			// TODO: handle exception
			e.printStackTrace();
			System.err.println(calaStr);
			System.err.println("ParseException !!!!!");
		}
    	return finalRes;
    }
    
    
   public static synchronized boolean getBoolForFel(String calaStr,double []paraForNow)
   {
	   for(int i=0;i<CFGBuilder.parameterNames.length;i++)
	   {
		   myCtx.set(CFGBuilder.parameterNames[i],paraForNow[i]);
	   }
	   
    	boolean finalRes=false;
    	try 
    	{	   		
    		Object result=myFelEngine.eval(calaStr);
    		finalRes=Boolean.parseBoolean(result.toString());
	    }  catch (EvalException e) 
	    {
			// TODO: handle exception
			e.printStackTrace();
			System.err.println(calaStr);
			System.err.println("EvalException !!!!!");
		}catch (ClassCastException e)
    	{
			// TODO: handle exception
			e.printStackTrace();
			System.err.println(calaStr);
			System.err.println("ClassCastException !!!!!");
		}catch (IllegalArgumentException e) {
			// TODO: handle exception
			e.printStackTrace();
			System.err.println(calaStr);
			System.err.println("IllegalArgumentException: argument type mismatch !!!!!");
		}catch (ParseException e) {
			// TODO: handle exception
			e.printStackTrace();
			System.err.println(calaStr);
			System.err.println("ParseException !!!!!");
		}
    	return finalRes;
    }
	
	public static synchronized void printAllParam()
	{
		System.out.println("********** All Param is as Following **********");
		for(int i=0;i<CFGBuilder.parameterNames.length;i++)
		{
			   System.out.print(CFGBuilder.parameterNames[i]+" "+myCtx.get(CFGBuilder.parameterNames[i])+" ");
	    }
		System.out.println();
	}

}
