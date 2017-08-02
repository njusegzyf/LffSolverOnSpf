package cn.nju.seg.atg.parse;

import java.util.ArrayList;
import java.util.List;

public class TestBuilder {
	/**
	 *最后得到的解（是可行解或者最优解）
	 */
	public static double[] finalParams;
	/**
	 *最后覆盖度
	 */
	public static double finalCovered;
	
	/**
	 * 用于存储测试结果的文件夹
	 */
	public static String targetNode;
	/**
	 *  ATG实验过程重复执行次数
	 */
    public static int countOfRepeation = 1;
	/**
	 * 目标程序执行的总时间
	 */
	public static double function_time;
	/**
	 * 目标程序执行的总次数
	 */
	public static int function_frequency;
	/**
	 * 单次运行总时间
	 */
	public static double totalTime[] = new double[countOfRepeation];
	/**
	 * 单次运行总次数
	 */
	public static int totalFrequency[] = new int[countOfRepeation];
	/**
	 * 单次运行中函数执行时间
	 */
	public static double algorithmTime[] = new double[countOfRepeation];
	/**
	 * 单次覆盖率（覆盖路径的条数）
	 */
	public static int coveredRatio[] = new int[countOfRepeation];
	public static String findResult[] = new String[TestBuilder.countOfRepeation];

    /////////////////////////// value space ///////////////////////////
	/**
	 * 自变参数产生池
	 */
	public static ArrayList<Double> parameterList;
	/**
	 * 当前搜索区域的左边界
	 */
	public static double leftSearchBoundary;
	/**
	 * 当前搜索区域的右边界
	 */
	public static double rightSearchBoundary;
	/**
	 * 拟合线段延长线与坐标轴的交点集合（2014-7-9)	
	 */
	public static List<Double> paramsInExtensionCord;
	/**
	 * 向两边扩展后得到的新自变参数
	 */
	public static List<Double> autoIncreasedParameterList;
}
