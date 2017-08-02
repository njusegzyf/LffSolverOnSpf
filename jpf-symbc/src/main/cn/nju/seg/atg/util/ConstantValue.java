package cn.nju.seg.atg.util;

import org.eclipse.cdt.internal.core.dom.parser.ASTNode;

public class ConstantValue {
	/**
	 * 0:if分支节点;		1：else分支节点;    -1:顺序执行节点;
	 */
	public static final int STATEMENT_SEQUENCE = -1;
	public static final int STATEMENT_IF = 0;
	public static final int STATEMENT_ELSE = 1;
	
	/**
	 * if
	 */
	public static final int BRANCH_IF = 3;
	
	/**
	 * for
	 */
	public static final int BRANCH_FOR = 2;
	
	/**
	 * return
	 */
	public static final int STATEMENT_RETURN = 4;
	
	/**
	 * while
	 */
	public static final int BRANCH_WHILE = 5;
	
	/**
	 * do
	 */
	public static final int BRANCH_DO = 6;
	
	/**
	 * break
	 */
	public static final int STATEMENT_BREAK = 7;
	
	/**
	 * continue
	 */
	public static final int STATEMENT_CONTINUE = 8;
	
	/**
	 * function call in sequential statement
	 */
	public static final int STATEMENT_CALL = 9;
	
	/**
	 * function call in compound expression
	 */
	public static final int STATEMENT_CALL_CE = 10;
	
	/**
	 * 分支节点
	 */
	public static final int BRANCH_NODE = 15;
	
	/**
	 * 入口节点
	 */
	public static final int ENTRY_NODE = -2;
	
	/**
	 * 出口节点
	 */
	public static final int EXIT_NODE = -3;
	
	/**
	 * 全开区间
	 */
	public static final int INTERVAL_BOTH_OPEN = 0;
	/**
	 * 左开区间
	 */
	public static final int INTERVAL_LEFT_OPEN = 1;
	/**
	 * 右开区间
	 */
	public static final int INTERVAL_RIGHT_OPEN = 2;
	/**
	 * 全闭区间
	 */
	public static final int INTERVAL_BOTH_CLOSED = 3;
	
	/**
	 * 拟合线段与横轴交点在区间左侧
	 */
	public static final int LEFT = 1;	
	/**
	 * 拟合线段与横轴交点在区间右侧
	 */
	public static final int RIGHT = 2;
	/**
	 * 拟合线段与横轴交点不在区间外
	 */
	public static final int INSIDE = 0;
	
	/**
	 * 变量搜索方式为全变量交叉搜索
	 */
	public static final int SEARCH_STRATEGY_ALL = 0;
	/**
	 * 变量搜索方式为按变量下标顺序搜索
	 */
	public static final int SEARCH_STRATEGY_ONE_BY_ONE = 1;
	

	private ConstantValue() {};
}
