package cn.nju.seg.atg.BEParse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;

import cn.nju.seg.atg.model.Constraint;
import cn.nju.seg.atg.model.SimpleCFGNode;
import cn.nju.seg.atg.model.constraint.BinaryExpression;
import cn.nju.seg.atg.model.constraint.BinaryExpressionUtil;
import cn.nju.seg.atg.model.constraint.Expression;
import cn.nju.seg.atg.parse.CFGBuilder;
import cn.nju.seg.atg.solver.runByLFF;
import cn.nju.seg.atg.util.ATG;
import cn.nju.seg.atg.util.ConstantValue;

public class BinaryExpressionParse {

	public static SimpleCFGNode targetCFGNode=null;
	public static BinaryExpression targetBinaryExpression=null;
	public static Constraint targetConstraint=null;
	
	public BinaryExpressionParse() {
		// TODO Auto-generated constructor stub
	}
    /**
	 * 带求解约束的输入
	 * @param BinaryExpression
	 */
	public static void setBinartExpression(String binaryExpression,String nodeName) 
	{
		String AbsolutePath=new File("").getAbsolutePath().toString();
		String filePath=AbsolutePath+"\\binaryExpression.c";
		
		//建立待处理约束的AST树
		try {
			File beFile=new File(filePath);
			if(!beFile.exists())
			{
				beFile.createNewFile();
			}
			FileOutputStream outFile=new FileOutputStream(beFile);
			BufferedWriter writeFile=new BufferedWriter(new OutputStreamWriter(outFile));
			
			StringBuilder code=new StringBuilder();
			code.append("int main(){if(" + binaryExpression + "){}}");
			writeFile.write(code.toString());
			writeFile.flush();
			writeFile.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.err.println("Input Source Code has problems!");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		IASTBinaryExprssionVisitor expressionVisitor=new IASTBinaryExprssionVisitor();
		CDTAstUtil ast=new CDTAstUtil(filePath);
		IASTTranslationUnit astUnit=ast.getIASTTranslationUnit();
		
		//System.out.println("AST Parse Bgein： ");
		astUnit.accept(expressionVisitor);
		//System.out.println("AST Parse End!!\n");
		
		IASTExpression iabe=expressionVisitor.getIabe();
		BinaryExpressionParse.targetBinaryExpression=BinaryExpressionUtil.translateExpr2(iabe);
		
		//System.out.println("The Input Expression is :"+binaryExpression);
		//System.out.println("The Final Binary Expression Without '!' is: "+BinaryExpressionParse.targetBinaryExpression.toString()+"\n\n");	
		
		BinaryExpressionParse.targetConstraint=new Constraint((Expression)targetBinaryExpression);
		BinaryExpressionParse.targetCFGNode=new SimpleCFGNode(nodeName,                            //"finalCFGNode@LFF_Solver"
				ConstantValue.BRANCH_IF,BinaryExpressionParse.targetConstraint, true,0.0);
		
	}
	
    /**
	 * 待处理约束的类型
	 * @param typeString
	 */
	public static void setParameterTypes(String typeString)
	{
		CFGBuilder.parameterTypes=typeString.replace(" ","").split(",");
		ATG.NUM_OF_PARAM=CFGBuilder.parameterTypes.length;
		BinaryExpressionParse.setParameters();
	}
	
    /**
	 * 待处理约束的变量名
	 * @param nameString
	 */
	public static void setParameterNames(String nameString)
	{
		CFGBuilder.parameterNames=nameString.replace(" ","").split(",");
		ATG.NUM_OF_PARAM=CFGBuilder.parameterNames.length;
		
	}
	/**
	 * 获取输入变量的类型（仅区分int与double)
	 */
	public static void setParameters()
	{
		for (int i=0; i<ATG.NUM_OF_PARAM; i++)
		{
			if (CFGBuilder.parameterTypes[i].equals("int"))
			{
				CFGBuilder.parameterTypes[i] = "int";
			}
			else if (CFGBuilder.parameterTypes[i].equals("const int"))
			{
				CFGBuilder.parameterTypes[i] = "int";
			}
			else if (CFGBuilder.parameterTypes[i].equals("int&"))
			{
				CFGBuilder.parameterTypes[i] = "int";
			}
			else if (CFGBuilder.parameterTypes[i].equals("long"))
			{
				CFGBuilder.parameterTypes[i] = "int";
			}
			else if (CFGBuilder.parameterTypes[i].equals("double"))
			{
				CFGBuilder.parameterTypes[i] = "double";
			}
			else if (CFGBuilder.parameterTypes[i].equals("float"))
			{
				CFGBuilder.parameterTypes[i] = "double";
			}
			else if (CFGBuilder.parameterTypes[i].equals("short"))
			{
				CFGBuilder.parameterTypes[i] = "int";
			}
			else if (CFGBuilder.parameterTypes[i].equals("unsigned short"))
			{
				CFGBuilder.parameterTypes[i] = "int";
			}
			else if (CFGBuilder.parameterTypes[i].equals("unsigned long"))
			{
				CFGBuilder.parameterTypes[i] = "int";
			}
			else {
				CFGBuilder.parameterTypes[i] = "double";
			}
		}
	}
	
    /**
	 * 第三方使用者初始化入口
	 * @param nameString
	 */
	public static void initAll(String binaryExpression,String nodeName,String typeString,String nameString) 
	{
		BinaryExpressionParse.setBinartExpression(binaryExpression,nodeName);
		BinaryExpressionParse.setParameterTypes(typeString);
		BinaryExpressionParse.setParameterNames(nameString);
		
	}
	
	public static void beginSolver()
	{
		new runByLFF();
	}

}
