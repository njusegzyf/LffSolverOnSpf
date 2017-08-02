package cn.nju.seg.atg.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nju.seg.atg.model.constraint.BinaryExpression;
import cn.nju.seg.atg.model.constraint.BinaryExpressionUtil;
import cn.nju.seg.atg.model.constraint.Expression;
import cn.nju.seg.atg.model.constraint.IdExpression;
import cn.nju.seg.atg.model.constraint.Operator;

/**
 * 条件约束
 * <p>尚不支持对非操作符"!"的处理
 *
 * @author ChengXin
 * @author zy
 */
public class Constraint {

  /**
   * 约束条件表达式
   */
  private Expression expression;

  /**
   * 原子约束组的集合
   * JZ atomicConstraintGroups存放的是析取范式,List<BinaryExpression>存放的是简单和取式子,
   * JZ List<List<BinaryExpression>>存放的是简单和取式子 经过析取操作后的析取范式
   */
  private List<List<BinaryExpression>> atomicConstraintGroups;

  /**
   * 带参构造函数
   *
   * @param Expression expression
   */
  public Constraint(Expression expression) {
    this.expression = expression;
    this.atomicConstraintGroups = new ArrayList<List<BinaryExpression>>();
    this.getAtomicConstraintGroupsFromExpression((BinaryExpression) expression);

    //下面是计算每一个BinaryExpression的非线性成系数NonLineardegree
    //this.calaNonLinerDrgree();
    //排序
    //this.sortByNonLinerDegree();
  }

  /**
   * 对atomicConstraintGroups做排序，这个很重要，直接影响着等式约束已经符合约束的执行结果
   */
  public void sortByNonLinerDegree() {
    //首先按照析取范式中的每一个合取子约束的size排序
    Collections.sort(this.atomicConstraintGroups, new Comparator<List<BinaryExpression>>() {
                       public int compare(List<BinaryExpression> list1, List<BinaryExpression> list2) {
                         //添加这个等号，对类似benchmark70这种约束的求解效果会好很多
                         int res = list1.size() >= list2.size() ? 1 : -1;
                         return res;
                       }
                     }
                    );

    for (int i = 0; i < this.atomicConstraintGroups.size(); i++) {
      List<BinaryExpression> tempList = this.atomicConstraintGroups.get(i);
      //首先按照析取范式中的每一个合取子约束的size排序
      Collections.sort(tempList, new Comparator<BinaryExpression>() {
                         public int compare(BinaryExpression node1, BinaryExpression node2) {
                           int res = node1.getNonLinearDegree() >= node2.getNonLinearDegree() ? 1 : -1;
                           //double x=Math.random();
                           //double y=Math.random();
                           //int res= x>=y? 1: -1;
                           return res;
                         }
                       }
                      );

    }

  }

  /**
   * 计算每一个简单子约束的非线性程度，便于排序
   */
  public void calaNonLinerDrgree() {
    for (int i = 0; i < this.atomicConstraintGroups.size(); i++) {
      List<BinaryExpression> tempList = this.atomicConstraintGroups.get(i);
      for (int j = 0; j < tempList.size(); j++) {
        BinaryExpression temp = tempList.get(j);
        double NonLinearDegree = calaNonLinDegreeForOne(temp);
        temp.setNonLinearDegree(NonLinearDegree);
      }
    }
  }

  /**
   * 计算每一个简单子约束的非线性程度，便于排序
   */
  private double calaNonLinDegreeForOne(BinaryExpression temp) {
    String leftString = temp.getOperand1().toString();
    String rightString = temp.getOperand2().toString();
    String tarString = leftString + " " + rightString;
    int count[] = new int[1];
    double equalNum = 0;
    if (temp.getOp() == Operator.EQ) {
      equalNum = Double.MAX_VALUE;
    }

    count[0] = 0;
    subStringNumbers(tarString, "pow", count);
    subStringNumbers(tarString, "abs", count);
    subStringNumbers(tarString, "ceil", count);
    subStringNumbers(tarString, "floor", count);
    int polynomialNum = count[0];

    count[0] = 0;
    subStringNumbers(tarString, "sin", count);
    subStringNumbers(tarString, "cos", count);
    subStringNumbers(tarString, "tan", count);
    subStringNumbers(tarString, "log", count);
    subStringNumbers(tarString, "exp", count);
    subStringNumbers(tarString, "sqrt", count);
    int NonLinearNum = count[0];

    double finalRes = 0.5 * polynomialNum + NonLinearNum + equalNum;
    return finalRes;
  }

  /**
   * 递归统计一个字符串包含子串的数量
   */
  public int subStringNumbers(String targetString, String tar, int count[]) {
    if (targetString.indexOf(tar) == -1) {
      return 0;
    } else if (targetString.indexOf(tar) != -1) {
      count[0]++;
      String temp = targetString.substring(targetString.indexOf(tar) + tar.length());
      subStringNumbers(temp, tar, count);
      return count[0];
    }
    return 0;
  }

  /**
   * 获取自定义BinaryExpression类型expression的等价原子约束组的集合
   */
  private void getAtomicConstraintGroupsFromExpression(BinaryExpression expression) {
    if (BinaryExpressionUtil.isAtomicConstraint(expression)) {
      List<BinaryExpression> temp = new ArrayList<BinaryExpression>();
      temp.add(expression);
      this.atomicConstraintGroups.add(temp);
    } else {
      List<BinaryExpression> temp = new ArrayList<BinaryExpression>();
      this.atomicConstraintGroups.add(temp);

      int[] beginIndex = new int[1];
      int[] endIndex = new int[1];
      beginIndex[0] = 0;
      endIndex[0] = 1;

      Operator operator = expression.getOp();
      if (operator == Operator.AND) {
        generateAtomicConstraintGroupsForAND(beginIndex[0], endIndex, expression);
      } else if (operator == Operator.OR) {
        this.partCopy(beginIndex[0], endIndex[0]);
        generateAtomicConstraintGroupsForOR(beginIndex, endIndex, expression);
      }
    }
  }

  /**
   * 递归函数
   * 用于为&&复合条件生成原子约束组的集合
   */
  private void generateAtomicConstraintGroupsForAND(int beginIndex, int[] endIndex, BinaryExpression be) {
    //处理左节点
    BinaryExpression beLeft = (BinaryExpression) be.getOperand1();
    if (BinaryExpressionUtil.isAtomicConstraint(beLeft)) {
      for (int i = beginIndex; i < endIndex[0]; i++) {
        this.atomicConstraintGroups.get(i).add(beLeft);
      }
    } else {
      int[] beginIndexTemp = new int[1];
      beginIndexTemp[0] = beginIndex;

      Operator operatorLeft = beLeft.getOp();
      if (operatorLeft == Operator.AND) {
        generateAtomicConstraintGroupsForAND(beginIndexTemp[0], endIndex, beLeft);
      } else if (operatorLeft == Operator.OR) {
        this.partCopy(beginIndexTemp[0], endIndex[0]);
        generateAtomicConstraintGroupsForOR(beginIndexTemp, endIndex, beLeft);
      }
    }
    //处理右节点
    BinaryExpression beRight = (BinaryExpression) be.getOperand2();
    if (BinaryExpressionUtil.isAtomicConstraint(beRight)) {
      for (int i = beginIndex; i < endIndex[0]; i++) {
        this.atomicConstraintGroups.get(i).add(beRight);
      }
    } else {
      int[] beginIndexTemp = new int[1];
      beginIndexTemp[0] = beginIndex;

      Operator operatorRight = beRight.getOp();
      if (operatorRight == Operator.AND) {
        generateAtomicConstraintGroupsForAND(beginIndexTemp[0], endIndex, beRight);
      } else if (operatorRight == Operator.OR) {
        this.partCopy(beginIndexTemp[0], endIndex[0]);
        generateAtomicConstraintGroupsForOR(beginIndexTemp, endIndex, beRight);
      }
    }
  }

  /**
   * 递归函数
   * 用于为||复合条件生成原子约束条件组的集合
   */
  private void generateAtomicConstraintGroupsForOR(int[] beginIndex, int[] endIndex, BinaryExpression be) {
    //计算当前被复制的原子约束条件分组的个数
    int temp = endIndex[0] - beginIndex[0];

    //处理左节点
    BinaryExpression beLeft = (BinaryExpression) be.getOperand1();
    if (BinaryExpressionUtil.isAtomicConstraint(beLeft)) {
      for (int i = beginIndex[0]; i < endIndex[0]; i++) {
        this.atomicConstraintGroups.get(i).add(beLeft);
      }
    } else {
      int[] beginIndexTemp = new int[1];
      beginIndexTemp[0] = beginIndex[0];

      Operator operatorLeft = beLeft.getOp();
      if (operatorLeft == Operator.AND) {
        generateAtomicConstraintGroupsForAND(beginIndexTemp[0], endIndex, beLeft);
      } else if (operatorLeft == Operator.OR) {
        this.partCopy(beginIndexTemp[0], endIndex[0]);
        generateAtomicConstraintGroupsForOR(beginIndexTemp, endIndex, beLeft);
      }
    }
    //处理游标
    beginIndex[0] = endIndex[0];
    endIndex[0] = endIndex[0] + temp;
    //处理右节点
    BinaryExpression beRight = (BinaryExpression) be.getOperand2();
    if (BinaryExpressionUtil.isAtomicConstraint(beRight)) {
      for (int i = beginIndex[0]; i < endIndex[0]; i++) {
        this.atomicConstraintGroups.get(i).add(beRight);
      }
    } else {
      int[] beginIndexTemp = new int[1];
      beginIndexTemp[0] = beginIndex[0];

      Operator operatorRight = beRight.getOp();
      if (operatorRight == Operator.AND) {
        generateAtomicConstraintGroupsForAND(beginIndexTemp[0], endIndex, beRight);
      } else if (operatorRight == Operator.OR) {
        this.partCopy(beginIndexTemp[0], endIndex[0]);
        generateAtomicConstraintGroupsForOR(beginIndexTemp, endIndex, beRight);
      }
    }
  }

  /**
   * 拷贝atomicConstraintGroups中的[beginIndex, endIndex-1]部分
   * 并将其插入到(endIndex-1)与endIndex之间
   */
  private void partCopy(int beginIndex, int endIndex) {
    int atomicConstraintGroupsSize = this.atomicConstraintGroups.size();
    List<List<BinaryExpression>> headListTemp = new ArrayList<List<BinaryExpression>>();
    List<List<BinaryExpression>> tailListTemp = new ArrayList<List<BinaryExpression>>();

    for (int i = 0; i < endIndex; i++) {
      List<BinaryExpression> temp = new ArrayList<BinaryExpression>();
      int sizeTemp = this.atomicConstraintGroups.get(i).size();
      for (int j = 0; j < sizeTemp; j++) {
        temp.add(this.atomicConstraintGroups.get(i).get(j));
      }
      headListTemp.add(temp);
    }
    for (int i = endIndex; i < atomicConstraintGroupsSize; i++) {
      List<BinaryExpression> temp = new ArrayList<BinaryExpression>();
      int sizeTemp = this.atomicConstraintGroups.get(i).size();
      for (int j = 0; j < sizeTemp; j++) {
        temp.add(this.atomicConstraintGroups.get(i).get(j));
      }
      tailListTemp.add(temp);
    }

    for (int i = beginIndex; i < endIndex; i++) {
      List<BinaryExpression> temp = new ArrayList<BinaryExpression>();
      int sizeTemp = this.atomicConstraintGroups.get(i).size();
      for (int j = 0; j < sizeTemp; j++) {
        temp.add(this.atomicConstraintGroups.get(i).get(j));
      }
      headListTemp.add(temp);
    }
    int tailListTempSize = tailListTemp.size();
    for (int i = 0; i < tailListTempSize; i++) {
      List<BinaryExpression> temp = new ArrayList<BinaryExpression>();
      int sizeTemp = tailListTemp.get(i).size();
      for (int j = 0; j < sizeTemp; j++) {
        temp.add(tailListTemp.get(i).get(j));
      }
      headListTemp.add(temp);
    }
    this.atomicConstraintGroups = headListTemp;
  }

  /**
   * 去掉字符串中的空格、回车、换行符、制表符
   *
   * @return 去掉后的字符串
   */
  public static String removeGap(String str) {
    Pattern p = Pattern.compile("\\s*|\t|\r|\n");
    Matcher m = p.matcher(str);
    return m.replaceAll("");
  }

  public static void main(String[] args) {
    //		Scanner sc = new Scanner(System.in);
    //		String input = sc.nextLine();;
    //		while(input!="#"){
    //		    System.out.println(removeGap(input));
    //		    input = sc.nextLine();
    //		}
    //		sc.close();

    //enabled==1 && ((tcas_equipped==1 && intent_not_known==1) || tcas_equipped==0)
    BinaryExpression e1 = new BinaryExpression(Operator.EQ, new IdExpression("enabled"), new IdExpression("1"));
    BinaryExpression e2 = new BinaryExpression(Operator.EQ, new IdExpression("tcas_equipped"), new IdExpression("1"));
    BinaryExpression e3 = new BinaryExpression(Operator.EQ, new IdExpression("intent_not_known"), new IdExpression("1"));
    BinaryExpression e4 = new BinaryExpression(Operator.EQ, new IdExpression("tcas_equipped"), new IdExpression("0"));
    BinaryExpression e5 = new BinaryExpression(Operator.AND, e2, e3);
    BinaryExpression e6 = new BinaryExpression(Operator.OR, e5, e4);
    BinaryExpression e7 = new BinaryExpression(Operator.AND, e1, e6);
    System.out.println(new Constraint(e7).getAtomicConstraintGroups());

    BinaryExpression e8 = new BinaryExpression(Operator.LE, new IdExpression("Haha"), new IdExpression("sin(0)"));
    BinaryExpression e9 = new BinaryExpression(Operator.OR, e7, e8);

    Constraint my = new Constraint(e9);
    List<List<BinaryExpression>> atoConsGroupList = my.getAtomicConstraintGroups();
    System.out.println("List<List<BinaryExpression>> atoConsGroupList SIZE= " + atoConsGroupList.size());
    for (int i = 0; i < atoConsGroupList.size(); i++) {
      List<BinaryExpression> tmp = atoConsGroupList.get(i);
      System.out.println("List<BinaryExpression> tmp size=" + tmp.size());
      for (int j = 0; j < tmp.size(); j++) {
        System.out.println("List<BinaryExpression> tmp[" + j + "]= " + tmp.get(j));
      }
      System.out.println();
    }

  }

  public Expression getExpression() {
    return expression;
  }

  public void setExpression(Expression expression) {
    this.expression = expression;
  }

  public List<List<BinaryExpression>> getAtomicConstraintGroups() {
    return atomicConstraintGroups;
  }

  public void setAtomicConstraintGroups(List<List<BinaryExpression>> atomicConstraintGroups) {
    this.atomicConstraintGroups = atomicConstraintGroups;
  }

  //	/**
  //	 * AstNode被frozen了...
  //	 * @param iae
  //	 * @return 不包含逻辑连接符非的约束条件
  //	 */
  //	public static IASTExpression removeLogicalNOT(IASTExpression iae){
  //	    removeLogicalNOT(iae, false);
  //	    return iae;
  //	}
  //
  //	private static void removeLogicalNOT(IASTExpression iae, boolean reverseOp){
  //		while(iae instanceof IASTUnaryExpression){
  //			reverseOp = reverseOp ^ (((IASTUnaryExpression)iae).getOperator() == IASTUnaryExpression.op_not);
  //			iae = (IASTExpression) iae.getChildren()[0];
  //		}
  //		IASTBinaryExpression iabe = (IASTBinaryExpression)iae;
  //	    if(reverseOp){
  //		    iabe.setOperator(getReverseOp(iabe.getOperator()));
  //	    }
  //		if(!isAtomicConstraint(iabe)){
  //			removeLogicalNOT(iabe.getOperand1(), reverseOp);
  //			removeLogicalNOT(iabe.getOperand2(), reverseOp);
  //		}
  //	}

  /**
   * JZ
   * 打印析取范式
   */
  public void printConstraint() {
    List<List<BinaryExpression>> conList = this.getAtomicConstraintGroups();
    int groupsSize = conList.size();
    System.out.println("析取范式打印：");
    for (int i = 0; i < groupsSize; i++) {
      List<BinaryExpression> con = conList.get(i);
      int groupSize = con.size();
      for (int j = 0; j < groupSize; j++) {
        System.out.print(con.get(j) + "  ");
      }
      System.out.println();
    }
    System.out.println('\n');

  }

  //JZ20160728
  public static boolean isAtomicConstraint(BinaryExpression iabe) {
    String operator = iabe.getOp().toString();
    if (operator.equals("&&") || operator.equals("||")) {
      return false;
    } else {
      return true;
    }
  }

}
