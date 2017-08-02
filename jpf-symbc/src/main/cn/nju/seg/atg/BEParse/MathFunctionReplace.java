package cn.nju.seg.atg.BEParse;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
/*
 * 主要是替换字符串
 * 添加Math的函数调用，这是因为javascript的调用需要，否者无法执行
 */
public class MathFunctionReplace {
	private String expression;
	
	public MathFunctionReplace() {
		// TODO Auto-generated constructor stub
	}
	
	public MathFunctionReplace(String expression) {
		// TODO Auto-generated constructor stub
		this.expression=expression;
	}
	
	/**
	 *  替换约束使以便在JS下的evale函数执行
	 * @param expression
	 * @param res
	 */
	public String runReplace()
	{
		String expression=this.expression;
		if (this.shouldReplace(expression))
		{
			//下面的需要精细的字符串匹配
			expression=this.dealCos(expression);
			expression=this.dealSin(expression);
			expression=this.dealTan(expression);
			//无需精细的字符串匹配处理
			expression=this.aboutLogFunc(expression);
			expression=this.aboutExpFunc(expression);
			expression=this.aboutPowFunc(expression);
			expression=this.aboutSqrtFunc(expression);
			expression=this.aboutAbsFunc(expression);
			expression=this.aboutCeilFunc(expression);
			expression=this.aboutFloorFunc(expression);
			expression=this.aboutRoundFunc(expression);
		}
		return expression;
	}
	
	/**
	 * 简单的判断是否需要做函数替换
	 * @param expression
	 * @param shouldReplace
	 */
	public boolean shouldReplace(String expression) {
		boolean shouldReplace=false;
		if(expression.contains("cos")||expression.contains("sin")
			||expression.contains("tan")||expression.contains("log")
			||expression.contains("exp")||expression.contains("sqrt")
			||expression.contains("abs")||expression.contains("ceil")
			||expression.contains("floor")||expression.contains("pow")
			||expression.contains("round")
				)
		{
			if(expression.contains("Math."))
			{
				shouldReplace=false;
			}
			else 
			{
				shouldReplace=true;
			}
		}
		 return shouldReplace;
	}
	
	/**
	 * 针对cos、acos特殊处理
	 * 
	 * 要先做acos的替换处理，然后做cos的替换处理，
	 * 这是因为cos是acos的子串
	 * 其中最重要就是找到真的cos函数，而不是acos函数
	 * 
	 * @param expression
	 * @param expression
	 */
	public String dealCos(String expression)
	{
		if(expression.contains("acos"))
		{
			//先处理acos
			expression=this.aboutAcosFunc(expression);
			
			String tar="cos";
			List<Integer> beginIndex=new ArrayList<Integer>();
			
			for(int i=0;i<expression.length();i++)
			{
				if(i+tar.length()-1<expression.length())
				{
					String tmp=expression.substring(i,i+tar.length());
					if (tmp.equals(tar))
					{
						if(i==0 ||( i>=1 && expression.charAt(i-1)!='a' ))
						{
							beginIndex.add(new Integer(i));
						}
					}
				}
			}
			//假如查到了cos依次替换
			if(beginIndex.size()>=1)
			{
				for (int i = beginIndex.size()-1; i >=0; i--)
				{
					String left=expression.substring(0,beginIndex.get(i));
					String mid="Math.cos";
					String right=expression.substring(beginIndex.get(i)+tar.length());
					expression=left+mid+right;
				}
			}
		}else
		{
			expression=this.aboutCosFunc(expression);
		}
		return expression;
	}

	/**
	 * 针对sin、asin特殊处理
	 *  
	 * 要先做asin的替换处理，然后做sin的替换处理，
	 * 这是因为sin是asin的子串
	 * 其中最重要就是找到真的sin函数，而不是asin函数
	 * 
	 * @param expression
	 * @param expression
	 */
	public String dealSin(String expression)
	{
		if(expression.contains("asin"))
		{
			//先处理asin
			expression=this.aboutAsinFunc(expression);
			
			String tar="sin";
			List<Integer> beginIndex=new ArrayList<Integer>();
			
			for(int i=0;i<expression.length();i++)
			{
				if(i+tar.length()-1<expression.length())
				{
					String tmp=expression.substring(i,i+tar.length());
					if (tmp.equals(tar))
					{
						if(i==0 ||( i>=1 && expression.charAt(i-1)!='a' ))
						{
							beginIndex.add(new Integer(i));
						}
					}
				}
			}
			//假如查到了sin依次替换
			if(beginIndex.size()>=1)
			{
				for (int i = beginIndex.size()-1; i >=0; i--)
				{
					String left=expression.substring(0,beginIndex.get(i));
					String mid="Math.sin";
					String right=expression.substring(beginIndex.get(i)+tar.length());
					expression=left+mid+right;
				}
			}
		}else
		{
			expression=this.aboutSinFunc(expression);
		}
		return expression;
	}

	/**
	 * 针对tan、atan、atan2特殊处理
	 * 
	 * 这个相对于sin和cos函数的替换，复杂多了
	 * 首先要做atan2的替换，然后做atan的替换，
	 * 这是因为atan是atan2的子串，tan是atan的子串
	 * 其中最重要的就是找到真正的atan函数，而不是atan2函数
	 * 最后做tan的替换，最重要的还是找到真正的tan函数，
	 * 而不是atan和atan2函数
	 * 
	 * @param expression
	 * @param expression
	 */
	public String dealTan(String expression)
	{
		if(expression.contains("atan2"))
		{
			//先处理atan2
			expression=this.aboutAtan2Func(expression);
			//接下来处理atan
			String tar="atan";
			List<Integer> beginIndex=new ArrayList<Integer>();
			
			for(int i=0;i<expression.length();i++)
			{
				if(i+tar.length()-1<expression.length())
				{
					String tmp=expression.substring(i,i+tar.length());
					if (tmp.equals(tar))
					{
						if( expression.charAt(i+tar.length())!='2')
						{
							beginIndex.add(new Integer(i));
						}
					}
				}
			}
			//假如查到了atan依次替换
			if(beginIndex.size()>=1)
			{
				for (int i = beginIndex.size()-1; i >=0; i--)
				{
					String left=expression.substring(0,beginIndex.get(i));
					String mid="Math.atan";
					String right=expression.substring(beginIndex.get(i)+tar.length());
					expression=left+mid+right;
				}
			}
			//处理tan运算
			tar="tan";
			List<Integer> beginIndex2=new ArrayList<Integer>();
			
			for(int i=0;i<expression.length();i++)
			{
				if(i+tar.length()-1<expression.length())
				{
					String tmp=expression.substring(i,i+tar.length());
					if (tmp.equals(tar))
					{
						if( (i==0&&(i+tar.length())<expression.length()&&expression.charAt(i+tar.length())!='2') 
						  ||(i>=1&&expression.charAt(i-1)!='a'&&(i+tar.length())<expression.length()&&expression.charAt(i+tar.length())!='2')
						  )
						{
							beginIndex2.add(new Integer(i));
						}
					}
				}
			}
			//假如查到了tan依次替换
			if(beginIndex2.size()>=1)
			{
				for (int i = beginIndex2.size()-1; i >=0; i--)
				{
					String left=expression.substring(0,beginIndex2.get(i));
					String mid="Math.tan";
					String right=expression.substring(beginIndex2.get(i)+tar.length());
					expression=left+mid+right;
				}
			}
			
		}else if(expression.contains("atan"))
		{
			expression=this.aboutAtanFunc(expression);
			
			String tar="tan";
			List<Integer> beginIndex=new ArrayList<Integer>();
			
			for(int i=0;i<expression.length();i++)
			{
				if(i+tar.length()-1<expression.length())
				{
					String tmp=expression.substring(i,i+tar.length());
					if (tmp.equals(tar))
					{
						if(i==0 ||( i>=1 && expression.charAt(i-1)!='a' ))
						{
							beginIndex.add(new Integer(i));
						}
					}
				}
			}
			//假如查到了tan依次替换
			if(beginIndex.size()>=1)
			{
				for (int i = beginIndex.size()-1; i >=0; i--)
				{
					String left=expression.substring(0,beginIndex.get(i));
					String mid="Math.tan";
					String right=expression.substring(beginIndex.get(i)+tar.length());
					expression=left+mid+right;
				}
			}
			
		}else
		{
			expression=this.aboutTanFunc(expression);
		}
		return expression;
	}

	
	/**
	 * 处理cos函数的替换
	 * @param expression
	 * @param expression
	 */
	public String aboutCosFunc(String expression) {
		if(expression.contains("cos"))
		{
			if (expression.contains("math.cos"))
			{
				expression=expression.replace("math.cos","Math.cos");
			}else
			{
				expression=expression.replace("cos","Math.cos");
			}
		}
		return expression;
	}
	
	/**
	 * 处理sin函数的替换
	 * @param expression
	 * @param expression
	 */
	public String aboutSinFunc(String expression) {
		if(expression.contains("sin"))
		{
			if (expression.contains("math.sin"))
			{
				expression=expression.replace("math.sin","Math.sin");
			}else
			{
				expression=expression.replace("sin","Math.sin");
			}
		}
		return expression;
	}
	
	/**
	 * 处理acos函数的替换
	 * @param expression
	 * @param expression
	 */
	public String aboutAcosFunc(String expression) {
		if(expression.contains("acos"))
		{
			if (expression.contains("math.acos"))
			{
				expression=expression.replace("math.acos","Math.acos");
			}else
			{
				expression=expression.replace("acos","Math.acos");
			}
		}
		 return expression;
	}
	
	/**
	 * 处理asin函数的替换
	 * @param expression
	 * @param expression
	 */
	public String aboutAsinFunc(String expression) {
		if(expression.contains("asin"))
		{
			if (expression.contains("math.asin"))
			{
				expression=expression.replace("math.asin","Math.asin");
			}else
			{
				expression=expression.replace("asin","Math.asin");
			}
		}
		 return expression;
	}
	
	/**
	 * 处理atan函数的替换
	 * @param expression
	 * @param expression
	 */
	public String aboutAtanFunc(String expression) {
		if(expression.contains("atan"))
		{
			if (expression.contains("math.atan"))
			{
				expression=expression.replace("math.atan","Math.atan");
			}else
			{
				expression=expression.replace("atan","Math.atan");
			}
		}
		 return expression;
	}
	
	
	/**
	 * 处理tan函数的替换
	 * @param expression
	 * @param expression
	 */
	public String aboutTanFunc(String expression) {
		if(expression.contains("tan"))
		{
			if (expression.contains("math.tan"))
			{
				expression=expression.replace("math.tan","Math.tan");
			}else
			{
				expression=expression.replace("tan","Math.tan");
			}
		}
		 return expression;
	}
	
	/**
	 * 处理atan2函数的替换
	 * @param expression
	 * @param expression
	 */
	public String aboutAtan2Func(String expression) {
		if(expression.contains("atan2"))
		{
			if (expression.contains("math.atan2"))
			{
				expression=expression.replace("math.atan2","Math.atan2");
			}else
			{
				expression=expression.replace("atan2","Math.atan2");
			}
		}
		 return expression;
	}
	
	/**
	 * 处理exp函数的替换
	 * @param expression
	 * @param expression
	 */
	public String aboutExpFunc(String expression) {
		if(expression.contains("exp"))
		{
			if (expression.contains("math.exp"))
			{
				expression=expression.replace("math.exp","Math.exp");
			}else
			{
				expression=expression.replace("exp","Math.exp");
			}
		}
		 return expression;
	}
	
	/**
	 * 由于ECMAScript5不支持log10(),log2()函数，所以这个需要特殊处理
	 * 处理log函数的替换
	 * 对于log2和log10的嵌套使用不支持，比如log10(log2(x))
	 * 
	 * 最先处理的是log10和log2的替换，因为二者没有并不是子串的关系
	 * 所以对log10和log2的替换的替换是平行的
	 * 然后最log的替换，这是最重要的就是找到真正的log，而不是
	 * log2和log10
	 * 
	 * @param expression
	 * @param expression
	 */
	public String dealLogForECMAScript5(String expression) {
		if(!expression.contains("log10")&&!expression.contains("log2"))
		{
			expression=this.aboutLogFunc(expression);
		}else
		{
			//先处理log10
			expression=this.aboutLog10FuncForECMAScript5(expression);
			//先处理log2
			expression=this.aboutLog2FuncForECMAScript5(expression);
			//最后添加对log10和log2的支持
			expression=this.addConstForLog102(expression);
			
			//最后添加对log的处理
			String tar="log";
			List<Integer> beginIndex=new ArrayList<Integer>();
			
			for(int i=0;i<expression.length();i++)
			{
				if(i+tar.length()-1<expression.length())
				{
					String tmp=expression.substring(i,i+tar.length());
					if (tmp.equals(tar))
					{
						if(expression.charAt(i+tar.length())!='/')
						{
							beginIndex.add(new Integer(i));
						}
					}
				}
			}
			//假如查到了log依次替换
			if(beginIndex.size()>=1)
			{
				for (int i = beginIndex.size()-1; i >=0; i--)
				{
					String left=expression.substring(0,beginIndex.get(i));
					String mid="Math.log";
					String right=expression.substring(beginIndex.get(i)+tar.length());
					expression=left+mid+right;
				}
			}
			
			//去掉之前添加的注释，这主要是为了美观
			expression=expression.replace("/*1*/","");
			expression=expression.replace("/*2*/","");		
		}
		return expression;
	}
	
	
	private String addConstForLog102(String expression) {
		// TODO Auto-generated method stub
		
		String tar="log/*";
		List<Integer> beginIndex=new ArrayList<Integer>();
		
		for(int i=0;i<expression.length();i++)
		{
			if(i+tar.length()-1<expression.length())
			{
				String tmp=expression.substring(i,i+tar.length());
				if (tmp.equals(tar))
				{
					if(expression.charAt(i+tar.length())=='1'|| expression.charAt(i+tar.length())=='2')
					{
						if(expression.charAt(i+tar.length()+3)=='(')
						{
							beginIndex.add(new Integer(i+tar.length()+3));
						}
						else
						{
							System.err.println("Error:log function left bracket Wrong");
						}					
					}
				}
			}
		}
		
		List<Integer> rightBracketIndex=new ArrayList<Integer>();
		//假如查到了sin依次替换
		if(beginIndex.size()>=1)
		{
			for (int i=0; i< beginIndex.size(); i++)
			{
				Integer rightBracket=this.bracketMatch(expression,beginIndex.get(i));
				rightBracketIndex.add(rightBracket);
			}
			
			for (int i = beginIndex.size()-1; i >=0; i--)
			{
				String left=expression.substring(0,rightBracketIndex.get(i));
				String mid="***** Replace Wrong *****";
				if(expression.charAt(beginIndex.get(i)-3)=='1')
				{
					mid="/Math.LN10)";
				}else if(expression.charAt(beginIndex.get(i)-3)=='2')
				{
					mid="/Math.LN2)";
				}else
				{
					System.err.println("Error: Something Wrong that should not happen!!!");
				}
				String right=expression.substring(rightBracketIndex.get(i));				
				expression=left+mid+right;
			}
		}
		
		return expression;
	}

	/**
	 * 括号的简单匹配
	 * @param expression
	 * @param expression
	 */
	private Integer bracketMatch(String expression, Integer begin) {
		// TODO Auto-generated method stub
		Stack<Integer> bracket=new Stack<Integer>();
		bracket.push(new Integer(begin));
		int i=begin+1;
		int fin=-1;
		while(!bracket.empty() && i<expression.length())
		{
			if(expression.charAt(i)=='(')
			{
				bracket.push(new Integer(i));
				i++;
			}else if(expression.charAt(i)==')')
			{
				fin=bracket.pop();
				i++;
			}else
			{
				i++;
			}
		}
		return i;
	}

	/**
	 * 这个仅仅是用于ECMAScript6 及以上
	 * 处理log函数的替换
	 * 
	 * 最先处理的是log10和log2的替换，因为二者没有并不是子串的关系
	 * 所以对log10和log2的替换的替换是平行的
	 * 然后最log的替换，这是最重要的就是找到真正的log，而不是
	 * log2和log10
	 * 
	 * @param expression
	 * @param expression
	 */
	public String dealLog(String expression) {
		if(!expression.contains("log10")&&!expression.contains("log2"))
		{
			expression=this.aboutLogFunc(expression);
		}else
		{
			//先处理log10
			expression=this.aboutLog10Func(expression);
			//先处理log2
			expression=this.aboutLog2Func(expression);
			
			String tar="log";
			List<Integer> beginIndex=new ArrayList<Integer>();
			
			for(int i=0;i<expression.length();i++)
			{
				if(i+tar.length()-1<expression.length())
				{
					String tmp=expression.substring(i,i+tar.length());
					if (tmp.equals(tar))
					{
						if(expression.charAt(i+tar.length())!='1'&&expression.charAt(i+tar.length())!='2')
						{
							beginIndex.add(new Integer(i));
						}
					}
				}
			}
			//假如查到了sin依次替换
			if(beginIndex.size()>=1)
			{
				for (int i = beginIndex.size()-1; i >=0; i--)
				{
					String left=expression.substring(0,beginIndex.get(i));
					String mid="Math.log";
					String right=expression.substring(beginIndex.get(i)+tar.length());
					expression=left+mid+right;
				}
			}
		}
		return expression;
	}
	
	/**
	 * 处理log10函数的替换，主要是添加注释，这是为了便于区别
	 * 替换后的log后面的属于注释
	 * @param expression
	 * @param expression
	 */
	public String aboutLog10FuncForECMAScript5(String expression) {
		if (expression.contains("log10"))
		{
			if(expression.contains("math.log10"))
			{
				expression=expression.replace("math.log10","(Math.log/*1*/");
			}else
			{
				expression=expression.replace("log10","(Math.log/*1*/");
			}
		}
		return expression;
	}
	
	/**
	 * 处理log2函数的替换，主要是添加注释，这是为了便于区别
	 * 替换后的log后面的属于注释
	 * @param expression
	 * @param expression
	 */
	public String aboutLog2FuncForECMAScript5(String expression) {
		if (expression.contains("log2"))
		{
			if(expression.contains("math.log2"))
			{
				expression=expression.replace("math.log2","(Math.log/*2*/");
			}else
			{
				expression=expression.replace("log2","(Math.log/*2*/");
			}
		}
		return expression;
	}
	
	/**
	 * 处理log10函数的替换
	 * @param expression
	 * @param expression
	 */
	public String aboutLog10Func(String expression) {
		if (expression.contains("log10"))
		{
			if(expression.contains("math.log10"))
			{
				expression=expression.replace("math.log10","Math.log10");
			}else
			{
				expression=expression.replace("log10","Math.log10");
			}
		}
		return expression;
	}
	
	/**
	 * 处理log2函数的替换
	 * @param expression
	 * @param expression
	 */
	public String aboutLog2Func(String expression) {
		if (expression.contains("log2"))
		{
			if(expression.contains("math.log2"))
			{
				expression=expression.replace("math.log2","Math.log2");
			}else
			{
				expression=expression.replace("log2","Math.log2");
			}
		}
		return expression;
	}
	
	/**
	 * 处理log函数的替换
	 * @param expression
	 * @param expression
	 */
	public String aboutLogFunc(String expression) {
		if (expression.contains("log"))
		{
			if(expression.contains("math.log"))
			{
				expression=expression.replace("math.log","Math.log");
			}else
			{
				expression=expression.replace("log","Math.log");
			}
		}
		return expression;
	}
	
	/**
	 * 处理pow函数的替换
	 * @param expression
	 * @param expression
	 */
	public String aboutPowFunc(String expression) {
		if(expression.contains("pow"))
		{
			if (expression.contains("math.pow"))
			{
				expression=expression.replace("math.pow","Math.pow");
			}else
			{
				expression=expression.replace("pow","Math.pow");
			}
		}
		 return expression;
	}
	
	/**
	 * 处理sqrt函数的替换
	 * @param expression
	 * @param expression
	 */
	public String aboutSqrtFunc(String expression) {
		if(expression.contains("sqrt"))
		{
			if (expression.contains("math.sqrt"))
			{
				expression=expression.replace("math.sqrt","Math.sqrt");
			}else
			{
				expression=expression.replace("sqrt","Math.sqrt");
			}
		}
		 return expression;
	}
	/**
	 * 处理abs函数的替换
	 * @param expression
	 * @param expression
	 */
	public String aboutAbsFunc(String expression) {
		if(expression.contains("abs"))
		{
			if (expression.contains("math.abs"))
			{
				expression=expression.replace("math.abs","Math.abs");
			}else
			{
				expression=expression.replace("abs","Math.abs");
			}
		}
		 return expression;
	}
	
	/**
	 * 处理ceil函数的替换
	 * @param expression
	 * @param expression
	 */
	public String aboutCeilFunc(String expression) {
		if(expression.contains("ceil"))
		{
			if (expression.contains("math.ceil"))
			{
				expression=expression.replace("math.ceil","Math.ceil");
			}else
			{
				expression=expression.replace("ceil","Math.ceil");
			}
		}
		 return expression;
	}
	
	/**
	 * 处理floor函数的替换
	 * @param expression
	 * @param expression
	 */
	public String aboutFloorFunc(String expression) {
		if(expression.contains("floor"))
		{
			if (expression.contains("math.floor"))
			{
				expression=expression.replace("math.floor","Math.floor");
			}else
			{
				expression=expression.replace("floor","Math.floor");
			}
		}
		 return expression;
	}
	
	/**
	 * 处理floor函数的替换
	 * @param expression
	 * @param expression
	 */
	public String aboutRoundFunc(String expression) {
		if(expression.contains("round"))
		{
			if (expression.contains("math.round"))
			{
				expression=expression.replace("math.round","Math.round");
			}else
			{
				expression=expression.replace("round","Math.round");
			}
		}
		 return expression;
	}
	
	
	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	
	public static void main(String[] args) {
		String tt="cos(cos(x))+acos(e+cos(5))";
		String tmp=new MathFunctionReplace("hh").dealCos(tt);
		//System.out.println(tt+"\n"+tmp);
		
		tt="asin(sin(x))+asin(e+sin(5))";
		tmp=new MathFunctionReplace("hh").dealSin(tt);
		//System.out.println(tt+"\n"+tmp);

		
		tt="atan(tan(x))+atan(e+tan(5))+atan2(tan(x),atan2(atan(x),tan(x)))";
		tmp=new MathFunctionReplace("hh").dealTan(tt);
		//System.out.println(tt+"\n"+tmp);
		
		//tt="log10(log2( x*log(x) ) ) +log2(e+log(5))+log2(log(x)-log2(log10(x)*log10(x)))";
		tt="log10(a)*log2(x)+log10(log(2))+log2(log(2))+log(log10(2))+log(log2(2))".replace(" ","");
		tt=tt.replace(" ","");
		tmp=new MathFunctionReplace(tt).dealLogForECMAScript5(tt);
		System.out.println(tt+"\n\n"+tmp);

	}
}
