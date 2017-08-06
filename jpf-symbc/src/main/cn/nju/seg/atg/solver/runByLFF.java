package cn.nju.seg.atg.solver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nju.seg.atg.BEParse.BinaryExpressionParse;
import cn.nju.seg.atg.model.SimpleCFGNode;
import cn.nju.seg.atg.model.constraint.BinaryExpression;
import cn.nju.seg.atg.parse.TestBuilder;
import cn.nju.seg.atg.util.ATG;
import cn.nju.seg.atg.util.ComputeEngine;
import cn.nju.seg.atg.util.MathFunc;
import cn.nju.seg.atg.util.PCATG;
import cn.nju.seg.atg.util.SubConstraintCover;

public class runByLFF {

  public runByLFF() {
    // TODO Auto-generated constructor stub
    this.run();
  }

  public void run() {
    TestBuilder.finalParams = new double[ATG.NUM_OF_PARAM];

    StringBuilder resultStr = new StringBuilder();
    for (int indexOfRun = 1; indexOfRun <= TestBuilder.countOfRepeation; indexOfRun++) {
      // 获取当前微秒时间，为计算插件运行时间做准备
      long start_time = System.currentTimeMillis();
      // 初始化目标程序执行时间为0
      TestBuilder.function_time = 0;
      // 初始化目标程序执行次数为0
      TestBuilder.function_frequency = 0;

      // 判断当前路径是否被覆盖：-1:未覆盖；0~+:被覆盖；
      int isCovered = -1;
      // 执行ATG过程
      //System.out.println("Value Size:  "+BinaryExpressionParse.targetCFGNode.getValues().size());
      isCovered = new PCATG().generateTestDataForSolver();

      double execute_time = System.currentTimeMillis() - start_time - TestBuilder.function_time;
      TestBuilder.totalTime[indexOfRun - 1] = (execute_time + TestBuilder.function_time) / 1000;
      TestBuilder.algorithmTime[indexOfRun - 1] = execute_time / 1000;
      TestBuilder.totalFrequency[indexOfRun - 1] = TestBuilder.function_frequency;
      TestBuilder.findResult[indexOfRun - 1] = isCovered > -1 ? "Y" : "N";

      // 输出结果
/*			StringBuilder result = new StringBuilder();
      result.append("----------------------------run"+indexOfRun+"----------------------------\n");
			
			System.out.println("TestBuilder.totalTime["+(indexOfRun-1)+"]="+TestBuilder.totalTime[indexOfRun-1]);
			System.out.println("TestBuilder.algorithmTime["+(indexOfRun-1)+"]="+TestBuilder.algorithmTime[indexOfRun-1]);
			System.out.println("TestBuilder.FunctionTime: "+(TestBuilder.function_time/1000));
			System.out.println("TestBuilder.totalFrequency["+(indexOfRun-1)+"]="+TestBuilder.totalFrequency[indexOfRun-1]);
			System.out.println("TestBuilder.findResult["+(indexOfRun-1)+"]="+TestBuilder.findResult[indexOfRun-1]);*/
					
/*			if(isCovered > -1)
			{
			    result.append("coverage result:\nfind covered input: ");			
			    result.append("(");
				for (int k=0; k<TestBuilder.finalParams.length-1; k++){
					result.append(TestBuilder.finalParams[k]+ ", ");
				}
				result.append(TestBuilder.finalParams[TestBuilder.finalParams.length-1]+")");
				result.append("\n");
				result.append("Best Covered is: "+(TestBuilder.finalCovered*100)+"%");

				//输出到控制台
				System.out.println("***** Find it :");
				double []a=TestBuilder.finalParams;
				for(int i=0;i<a.length;i++)
					System.out.print(a[i]+"  ");
				System.out.println();
				System.out.println("Best Covered is: "+(TestBuilder.finalCovered*100)+"%");
	
				//String coverString=printAllConstraint();
				//result.append(coverString);
				
			}
			else
			{
			    result.append("coverage result:\ncannot find covered input! the best input is:");			
			    result.append("(");
				for (int k=0; k<TestBuilder.finalParams.length-1; k++){
					result.append(TestBuilder.finalParams[k]+ ", ");
				}
				result.append(TestBuilder.finalParams[TestBuilder.finalParams.length-1]+")");
				result.append("\n");
				result.append("Best Covered is: "+(TestBuilder.finalCovered*100)+"%\n");
			
				
				//输出到控制台
				System.out.print("***** NO NO No :");
				double []a=TestBuilder.finalParams;
				for(int i=0;i<a.length;i++)
					System.out.print(a[i]+"  ");
				System.out.println();
				System.out.println("Best Covered is: "+(TestBuilder.finalCovered*100)+"%");
	
				//String coverString=printAllConstraint();
				//result.append(coverString);
			}*/
			
/*			result.append("\ntotal time: " + (execute_time+TestBuilder.function_time)/1000 + " sec\n");
			result.append("function time: " + TestBuilder.function_time/1000 + " sec\n");
			result.append("function execution frequency: "+ TestBuilder.function_frequency + " times\n");
			result.append("algorithm time: " + execute_time/1000 + " sec\n");*/
		    
/*			String folderPath = new File("").getAbsolutePath()+"/LFF_Solver_Result" + "/";
			File folder = new File(folderPath);
			if (!folder.exists()) 
				folder.mkdirs();
			String resultPath = folderPath + "/" 
			+ BinaryExpressionParse.targetCFGNode.getName()
			+ ".result(" + indexOfRun + ")";
			try {
	            FileOutputStream out=new FileOutputStream(resultPath);   
	            BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(out));
	            bufferedwriter.write(result.toString());
	            bufferedwriter.flush();
	            bufferedwriter.close();
	        }catch (Exception e){   
	            e.printStackTrace();   
	        }*/

      //System.gc();
      //resultStr.append(result);
    }
    //printTotalResult(resultStr);
  }

  /**
   * 输出统计结果
   */
  protected void printTotalResult(StringBuilder resultStr) {
    resultStr.append("----------------------------statistical result----------------------------\n");
    resultStr.append("average time:\t" + MathFunc.getAverage(TestBuilder.totalTime) + "\n");
    resultStr.append("best time:\t" + MathFunc.getMin(TestBuilder.totalTime) + "\n");
    resultStr.append("worst time:\t" + MathFunc.getMax(TestBuilder.totalTime) + "\n");

    resultStr.append("average frequency:\t" + MathFunc.getAverage(TestBuilder.totalFrequency) + "\n");
    resultStr.append("coverage result:\t" + Arrays.toString(TestBuilder.findResult) + "\n");
    resultStr.append("parameter setting: " + "MAX_NUM_OF_PREDICT_PARAM=" + ATG.MAX_NUM_OF_PREDICT_PARAM
                     + ", MAX_NUM_OF_GENERATE_CYCLE=" + ATG.MAX_NUM_OF_GENERATE_CYCLE + ", PREDICT_BOUNDARY=" + ATG.PREDICT_BOUNDARY + "\n");
    resultStr.append("search strategy: " + (ATG.SEARCH_STRATEGY == 0 ? "SEARCH_STRATEGY_ALL" : "SEARCH_STRATEGY_ONE_BY_ONE") + "\n");

    String folderPath = "/home/zy/Desktop/LFF_Solver_FinalResult"
                        + "/";
    File folder = new File(folderPath);
    if (!folder.exists()) { folder.mkdirs(); }
    String resultPath = folderPath + "/"
                        + BinaryExpressionParse.targetCFGNode.getName()
                        + ".result";
    try {
      FileOutputStream out = new FileOutputStream(resultPath);
      BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(out));
      bufferedwriter.write(resultStr.toString());
      bufferedwriter.flush();
      bufferedwriter.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  void clear() {
    BinaryExpressionParse.targetCFGNode.getValues().clear();
  }

  String printAllConstraint() {
    String coverString = "";
    //最后根据得到的最优解最后计算一次，以确定到底哪些子约束被满足
    SimpleCFGNode targetCfgNode = BinaryExpressionParse.targetCFGNode;
    List<List<BinaryExpression>> constGroups = targetCfgNode.getConstraint().getAtomicConstraintGroups();
    //保存每一个子约束的时刻运行值的结果
    Map<String, SubConstraintCover> allRes = new HashMap<>();
    //依次计算
    int outSize = constGroups.size();
    for (int i = 0; i < outSize; i++) {
      List<BinaryExpression> group = constGroups.get(i);
      int inSize = group.size();

      coverString = coverString + "\n\n析取范式的合取子式" + (i + 1) + ": \n";
      System.out.println("析取范式的合取子式" + (i + 1) + ": ");
      List<Boolean> subCovered = new ArrayList<Boolean>();
      for (int j = 0; j < inSize; j++) {
        BinaryExpression node = group.get(j);
        String op = node.getOp().toString();
        StringBuilder calaBuilder = new StringBuilder();
        calaBuilder.append("(");
        calaBuilder.append(node.getOperand1().toString());
        calaBuilder.append(") - (");
        calaBuilder.append(node.getOperand2().toString());
        calaBuilder.append(")");

        double instruRes = ComputeEngine.getDouForFel(calaBuilder.toString(), TestBuilder.finalParams);
        boolean isCovered = ComputeEngine.getBoolForFel(node.toString(), TestBuilder.finalParams);

        boolean isCoveredByJZ = PCATG.calaSubCovered(instruRes, node.getOp().toString());
        System.out.println(node.toString() + " " + instruRes + " " + isCovered + " " + isCoveredByJZ);
        coverString = coverString + node.toString() + " " + instruRes + " " + isCovered + " " + isCoveredByJZ;
        subCovered.add(isCovered);
      }
      System.out.println();
      for (int k = 0; k < subCovered.size(); k++) {
        System.out.print(subCovered.get(k) + "  ");
      }
      System.out.println("");
      coverString = coverString + "\n";

    }
    coverString = coverString + "约束最后分析结束！\n";
    return coverString;
  }

}
