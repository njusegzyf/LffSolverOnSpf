package cn.nju.seg.atg.BEParse;

import java.awt.print.Printable;

import javax.xml.soap.Node;

import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.IASTBinaryExpression;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTUnaryExpression;

public class IAstBinaryExpressionVisitor extends ASTVisitor{
	
	private IASTExpression iabe;
	
	public IAstBinaryExpressionVisitor() {
		// TODO Auto-generated constructor stub
		this.shouldVisitExpressions=true;	
	}
	
	public int visit(IASTExpression node) {
		
		if(node instanceof IASTBinaryExpression)
		{
			//System.out.println(node.getRawSignature()+" is IASTBinaryExpression.");	
			this.iabe=node;	
		}else if(node instanceof IASTUnaryExpression)
		{
			//System.out.println(node.getRawSignature()+" is UnaryExpression: ");
			this.iabe=node;
		}else
		{
			//System.err.println("The Expression:"+node.getRawSignature() +" has some problems,because it is not UnaryExpression or BinaryExpression");
		}
		return PROCESS_ABORT;
	}
	
	

	String getOp(IASTBinaryExpression node)
	{
		String res=null;
		if(node.getOperator()==IASTBinaryExpression.op_assign)
			res="=";
		else if(node.getOperator()==IASTBinaryExpression.op_divide)
			res="/";
		else if(node.getOperator()==IASTBinaryExpression.op_equals)
			res="==";
		else if(node.getOperator()==IASTBinaryExpression.op_greaterEqual)
			res=">=";
		else if(node.getOperator()==IASTBinaryExpression.op_greaterThan)
			res=">";
		else if(node.getOperator()==IASTBinaryExpression.op_lessEqual)
			res="<=";
		else if(node.getOperator()==IASTBinaryExpression.op_lessThan)
			res="<";
		else if(node.getOperator()==IASTBinaryExpression.op_logicalAnd)
			res="&&";
		else if(node.getOperator()==IASTBinaryExpression.op_logicalOr)
			res="||";
		else if(node.getOperator()==IASTBinaryExpression.op_notequals)
			res="!";
		return res;
	}

	public IASTExpression getIabe() {
		return iabe;
	}

	public void setIabe(IASTExpression iabe) {
		this.iabe = iabe;
	}


}
