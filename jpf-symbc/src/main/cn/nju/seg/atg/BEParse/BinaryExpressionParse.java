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
import cn.nju.seg.atg.parse.CFGBuilder;
import cn.nju.seg.atg.solver.runByLFF;
import cn.nju.seg.atg.util.ATG;
import cn.nju.seg.atg.util.ConstantValue;

/**
 * @author Seg, Zhang Yifan
 * @version 0.1
 */
public class BinaryExpressionParse {

  public static SimpleCFGNode targetCFGNode = null;

  public static BinaryExpression targetBinaryExpression = null;

  public static Constraint targetConstraint = null;

  /**
   * 带求解约束的输入
   */
  public static void setBinaryExpression(String binaryExpression, String nodeName) {
    String AbsolutePath = new File("").getAbsolutePath();
    String filePath = AbsolutePath + "\\binaryExpression.c";

    //建立待处理约束的AST树
    try {
      File beFile = new File(filePath);
      if (!beFile.exists()) {
        beFile.createNewFile();
      }

      try (FileOutputStream outFile = new FileOutputStream(beFile);
           BufferedWriter writeFile = new BufferedWriter(new OutputStreamWriter(outFile))) {
        StringBuilder code = new StringBuilder();
        code.append("int main(){if(" + binaryExpression + "){}}");
        writeFile.write(code.toString());
      }

    } catch (FileNotFoundException e) {
      System.err.println("Input Source Code has problems!");
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    IAstBinaryExpressionVisitor expressionVisitor = new IAstBinaryExpressionVisitor();
    CDTAstUtil ast = new CDTAstUtil(filePath);
    IASTTranslationUnit astUnit = ast.getIASTTranslationUnit();

    //System.out.println("AST Parse Bgein： ");
    astUnit.accept(expressionVisitor);
    //System.out.println("AST Parse End!!\n");

    IASTExpression iabe = expressionVisitor.getIabe();
    BinaryExpressionParse.targetBinaryExpression = BinaryExpressionUtil.translateExpr2(iabe);

    //System.out.println("The Input Expression is :"+binaryExpression);
    //System.out.println("The Final Binary Expression Without '!' is: "+BinaryExpressionParse.targetBinaryExpression.toString()+"\n\n");

    BinaryExpressionParse.targetConstraint = new Constraint(targetBinaryExpression);
    BinaryExpressionParse.targetCFGNode = new SimpleCFGNode(nodeName,                            //"finalCFGNode@LFF_Solver"
                                                            ConstantValue.BRANCH_IF, BinaryExpressionParse.targetConstraint, true, 0.0);

  }

  /**
   * 待处理约束的类型
   */
  public static void setParameterTypes(String typeString) {
    CFGBuilder.parameterTypes = typeString.replace(" ", "").split(",");
    ATG.NUM_OF_PARAM = CFGBuilder.parameterTypes.length;
    BinaryExpressionParse.setParameters();
  }

  /**
   * 待处理约束的变量名
   */
  public static void setParameterNames(String nameString) {
    CFGBuilder.parameterNames = nameString.replace(" ", "").split(",");
    ATG.NUM_OF_PARAM = CFGBuilder.parameterNames.length;
  }

  /**
   * 获取输入变量的类型（仅区分int与double)
   */
  public static void setParameters() {
    for (int i = 0; i < ATG.NUM_OF_PARAM; i++) {
      switch (CFGBuilder.parameterTypes[i]) {
      case "int":
        CFGBuilder.parameterTypes[i] = "int";
        break;
      case "const int":
        CFGBuilder.parameterTypes[i] = "int";
        break;
      case "int&":
        CFGBuilder.parameterTypes[i] = "int";
        break;
      case "long":
        CFGBuilder.parameterTypes[i] = "int";
        break;
      case "double":
        CFGBuilder.parameterTypes[i] = "double";
        break;
      case "float":
        CFGBuilder.parameterTypes[i] = "double";
        break;
      case "short":
        CFGBuilder.parameterTypes[i] = "int";
        break;
      case "unsigned short":
        CFGBuilder.parameterTypes[i] = "int";
        break;
      case "unsigned long":
        CFGBuilder.parameterTypes[i] = "int";
        break;
      default:
        CFGBuilder.parameterTypes[i] = "double";
        break;
      }
    }
  }

  /**
   * 第三方使用者初始化入口
   */
  @Deprecated
  public static void initAll(String binaryExpression, String nodeName, String typeString, String nameString) {
    BinaryExpressionParse.setBinaryExpression(binaryExpression, nodeName);
    BinaryExpressionParse.setParameterTypes(typeString);
    BinaryExpressionParse.setParameterNames(nameString);
  }

  @Deprecated
  public static void beginSolver() {
    new runByLFF();
  }

  /**
   * Disables constructor, since only static methods in this class should be used.
   *
   * @since 0.1
   */
  @Deprecated
  private BinaryExpressionParse() { throw new UnsupportedOperationException(); }
}
