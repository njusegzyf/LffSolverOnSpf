package cn.nju.seg.atg.BEParse;

import org.eclipse.cdt.core.dom.ast.IASTBinaryExpression;
/*
 * @author jc
 * 用于包装分解 UnaryExpression约束到BinaryExpression约束
 * */
public class UnaryExpression {
	
	private IASTBinaryExpression iastBinaryExpression;
	private int NotCount;
	
	public UnaryExpression() {
		// TODO Auto-generated constructor stub
	}
	
	public UnaryExpression(IASTBinaryExpression iast,int NotCount) {
		// TODO Auto-generated constructor stub
		this.iastBinaryExpression=iast;
		this.NotCount=NotCount%2;
	}
	

	
	public IASTBinaryExpression getIastBinaryExpression() {
		return iastBinaryExpression;
	}

	public void setIastBinaryExpression(IASTBinaryExpression iastBinaryExpression) {
		this.iastBinaryExpression = iastBinaryExpression;
	}

	public int getNotCount() {
		return NotCount;
	}
	
	public void setNotCount(int notCount) {
		NotCount = notCount%2;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.iastBinaryExpression.getRawSignature();
	}
	
}
