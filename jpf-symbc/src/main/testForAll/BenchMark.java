package testForAll;

import java.util.ArrayList;
import java.util.List;

public class BenchMark {

  public static List<String> functionName = new ArrayList<String>();

  public static List<String> binaryExp = new ArrayList<String>();

  public static List<String> paraName = new ArrayList<String>();

  public static List<String> paraType = new ArrayList<String>();

  public static void initBenchmark() {
    functionName.add("benchmark05");
    binaryExp.add("sin(x)+sin(y)+sin(z)==4*cos(x)*cos(y)*cos(z)");
    paraName.add("x,y,z");
    paraType.add("double,double,double");
    functionName.add("benchmark06");
    binaryExp.add("cos(x)+cos(y)+cos(z)>4*sin(x/2)*sin(y/2)*sin(z/2)");
    paraName.add("x,y,z");
    paraType.add("double,double,double");

    functionName.add("benchmark07");
    binaryExp.add("x*x==-1");
    paraName.add("x,y,z,w");
    paraType.add("double,double,double,double");

		
/*		functionName.add("helicalValley");
		binaryExp.add("x3 == 0.0 && x1==0 && 10.0*(x3-10.0*0.0) == 0 &&(10.0*(sqrt(x1*x1+x2*x2)-1))==0.0  || "
				+ "x3==0.0 && x1>0.0&&10.0*(x3-10.0*(atan(x2/x1)/(2*3.14159265358979323846)) )==0 &&(10.0*(sqrt(x1*x1+x2*x2)-1))==0.0 ||"
				+ "x3==0.0 && x1<0.0&&10.0*(x3-10.0*((atan(x2/x1)/(2*3.14159265358979323846)+0.5) ))==0  &&(10.0*(sqrt(x1*x1+x2*x2)-1))==0.0 ");
		paraName.add("x1,x2,x3");
		paraType.add("double,double,double");*/
			
/*		
  		//对应coral 的benchmark82:理论上无解,求不出来
		functionName.add("powell");
		binaryExp.add("(pow(10, 4) * x1 * x2 - 1.0) == 0.0 && (pow(2.7182818284590452354, -x1) + pow(2.7182818284590452354, -x2) - 1.0001) == 0.0");
		paraName.add("x1,x2");
		paraType.add("double,double");
		
		//对应coral的benchmark83,无法求出
		functionName.add("rosenbrock");
		binaryExp.add("pow((1.0 - x1), 2) + 100.0 * (pow((x2 - x1 * x1), 2)) == 0.0");
		paraName.add("x1,x2");
		paraType.add("double,double");
		
		//对应coral 的benchmark84: 在调整约束次序和搜索策略之后,可以求出
		functionName.add("wood");
		binaryExp.add(" 1.0 - x1 == 0.0 && 1.0 - x3 == 0.0 && 10.0 * (x2 - x1 * x1) == 0.0 && (sqrt(90) * (x4 - x3 * x3)) == 0.0  && (sqrt(10) * (x2 + x4 - 2.0))
		== 0.0 && (pow(10, -0.5) * (x2 - x4)) == 0.0");
		paraName.add("x1,x2,x3,x4");
		paraType.add("double,double,double,double");
		*/
		
		
	/*	//本身无解，图像无交点
		functionName.add("benchmark82");
		binaryExp.add("(pow(10,4)*a*b-1)==0 && (exp(-a)+exp(-b)-1.0001)==0");
		paraName.add("a,b");
		paraType.add("double,double");
		
		functionName.add("benchmark78");
		binaryExp.add("(0.0==(pow(((a*sin(((b*0.017453292519943295-c*0.017453292519943295)+(((((((pow(d,2.0)/(sin(e*0.017453292519943295)/cos
		(e*0.017453292519943295)))/68443.0)*0.0)/d)*-1.0)*a)/((pow(a,2.0)/(sin((e*0.017453292519943295))/cos((e*0.017453292519943295))))/68443.0)))))-(d*0.0)),
		2.0)+pow(((a*cos((((b*0.017453292519943295)-(c*0.017453292519943295))+(((((((pow(d,2.0)/(sin((e*0.017453292519943295))/cos((e*0.017453292519943295))))
		/68443.0)*0.0)/d)*-1.0)*a)/((pow(a,2.0)/(sin((e*0.017453292519943295))/cos((e*0.017453292519943295))))/68443.0)))))-d*1.0),2.0)))&&f!=0&&h!=0");
		paraName.add("a,b,c,d,e,f,g,h");
		paraType.add("double,double,double,double,double,int,int,int");
		
		
		functionName.add("Equal Testing:Back!!!");
		binaryExp.add("x+y >=-1 &&pow(x,2)==x+y");
		paraName.add("x,y");
		paraType.add("double,double");
		functionName.add("Equal Testing:Front!!!");
		binaryExp.add("pow(x,2)==x+y && x+y >=-1");
		paraName.add("x,y");
		paraType.add("double,double");

		
		functionName.add("benchmark67_Test");
		binaryExp.add("x-y+tan(v)>(z+t)/(w+t)&&sqrt(x-t)>z/y&&log(x*y)>log(t+w+z)&&z*2+w*3+x*7<pow(y,t)*cos(v)&&z+w>x+y&&w<x/y&&x>(w+y-z)&&log10(t*x)<sqrt(w*y*z)
		&&x*cos(t+y)>log(w*z*3)&&cos(t)*sin(v)>cos(y)");
		paraName.add("x,y,z,w,t,v");
		paraType.add("double,double,double,double,double,double");
		functionName.add("benchmark69_Test");
		binaryExp.add("x-y+tan(v)>(z+t)/(w+t)&&sqrt(x-t)>z/y&&log(x*y)>log(t+w+z)&&z*2+w*3+x*7<pow(y,t)*cos(v)&&z+w>x+y&&w<x/y&&x>(w+y-z)&&log10(t*x)<sqrt(w*y*z)
		&&x*cos(t+y)>log(w*z*3)&&cos(t)*sin(v)>cos(y)&&sin(x*y)+sin(z*w)+sin(t*v)<cos(x*y)+cos(z*w)+cos(t*v)");
		paraName.add("x,y,z,w,t,v");
		paraType.add("double,double,double,double,double,double");
		
		functionName.add("benchmark55_Test");
		binaryExp.add("pow(x,tan(y))+z<x*atan(z)&&sin(y)+cos(y)+tan(y)>=x-z&&atan(x)+atan(y)>y&&log(pow(x,tan(y)))<log(z)&&sqrt(y+z)>sqrt(pow(x,(x-y)))");
		paraName.add("x,y,z");
		paraType.add("double,double,double");
		
		

		binaryExp.add("sin(cos(x*y))<cos(sin(x*z)) &&sin(x*y*z*w)>0&&cos(x*y*z*w)<0 && (sin(2*w-y)/(cos(2*y+v)+1)==cos(2*z+x)/(sin(2*w+v)-1))");
		paraName.add("x,y,z,w,v");
		paraType.add("double,double,double,double,double");
		functionName.add("benchmark27_back");
		binaryExp.add("sin(cos(x*y))<cos(sin(x*z))&&(sin(2*w-y)/(cos(2*y+v)+1)==cos(2*z+x)/(sin(2*w+v)-1))&&sin(x*y*z*w)>0&&cos(x*y*z*w)<0");
		paraName.add("x,y,z,w,v");
		paraType.add("double,double,double,double,double");
		functionName.add("benchmark27_middle");
		binaryExp.add("(sin(2*w-y)/(cos(2*y+v)+1)==cos(2*z+x)/(sin(2*w+v)-1))&&sin(cos(x*y))<cos(sin(x*z))&&sin(x*y*z*w)>0&&cos(x*y*z*w)<0");
		paraName.add("x,y,z,w,v");
		paraType.add("double,double,double,double,double");
		functionName.add("benchmark27_front");
		*/		
		/*
		functionName.add("benchmark67");
		binaryExp.add("x-y+tan(v)>(z+t)/(w+t)&&sqrt(x-t)>z/y&&log(x*y)>log(t+w+z)&&z*2+w*3+x*7<pow(y,t)*cos(v)&&z+w>x+y&&w<x/y&&x>(w+y-z)&&log10(t*x)<sqrt(w*y*z)
		&&x*cos(t+y)>log(w*z*3)&&cos(t)*sin(v)>cos(y)");
		paraName.add("x,y,z,w,t,v");
		paraType.add("double,double,double,double,double,double");
		functionName.add("benchmark69");
		binaryExp.add("x-y+tan(v)>(z+t)/(w+t)&&sqrt(x-t)>z/y&&log(x*y)>log(t+w+z)&&z*2+w*3+x*7<pow(y,t)*cos(v)&&z+w>x+y&&w<x/y&&x>(w+y-z)&&log10(t*x)<sqrt(w*y*z)
		&&x*cos(t+y)>log(w*z*3)&&cos(t)*sin(v)>cos(y)&&sin(x*y)+sin(z*w)+sin(t*v)<cos(x*y)+cos(z*w)+cos(t*v)");
		paraName.add("x,y,z,w,t,v");
		paraType.add("double,double,double,double,double,double");
		
		functionName.add("benchmark79");
		binaryExp.add("(0.0==(pow(((a*sin(((0.017453292519943295*b)-(0.017453292519943295*c))))-(0.0*d)),2.0)+pow((a*cos((((0.017453292519943295*b)-
		(0.017453292519943295*c))+0.0))),2.0)))&&e!=0");
		paraName.add("a,b,c,d,e");
		paraType.add("double,double,double,double,int");
		
		
		functionName.add("benchmark01");
		binaryExp.add("sin(x)+cos(y)>1");
		paraName.add("x,y");
		paraType.add("double,double");
		functionName.add("benchmark02");
		binaryExp.add("sin(x)-cos(y)<0.0000000001");
		paraName.add("x,y");
		paraType.add("double,double");
		functionName.add("benchmark03");
		binaryExp.add("sin(x)-cos(y)==0");
		paraName.add("x,y");
		paraType.add("double,double");
		functionName.add("benchmark04");
		binaryExp.add("exp(x)>0");
		paraName.add("x");
		paraType.add("double");
		functionName.add("benchmark05");
		binaryExp.add("sin(x)+sin(y)+sin(z)==4*cos(x)*cos(y)*cos(z)");
		paraName.add("x,y,z");
		paraType.add("double,double,double");
		functionName.add("benchmark06");
		binaryExp.add("cos(x)+cos(y)+cos(z)>4*sin(x/2)*sin(y/2)*sin(z/2)");
		paraName.add("x,y,z");
		paraType.add("double,double,double");
		functionName.add("benchmark07");
		binaryExp.add("sin(2*x-y)/(cos(2*y+y)+1)==cos(2*z+x)/(sin(2*w+y)-1)");
		paraName.add("x,y,z,w");
		paraType.add("double,double,double,double");
		functionName.add("benchmark08");
		binaryExp.add("cos(3*x+2*y-z)*sin(z+x+y)==cos(z*x*y)");
		paraName.add("x,y,z");
		paraType.add("double,double,double");
		functionName.add("benchmark09");
		binaryExp.add("sin(cos(x*y))<cos(sin(x*y))");
		paraName.add("x,y");
		paraType.add("double,double");
		
		
		functionName.add("benchmark10");
		binaryExp.add("sin(x*cos(y*sin(z)))>cos(x*sin(y*cos(z)))");
		paraName.add("x,y,z");
		paraType.add("double,double,double");
		functionName.add("benchmark11");
		binaryExp.add("asin(x)<cos(y)*cos(z)-atan(w)");
		paraName.add("x,y,z,w");
		paraType.add("double,double,double,double");
		
		*/

		/*
		functionName.add("benchmark12");
		binaryExp.add("(asin(x)*asin(y))-1<atan(z)*atan(w)");
		paraName.add("x,y,z,w");
		paraType.add("double,double,double,double");
		functionName.add("benchmark13");
		binaryExp.add("sin(y)*asin(x)<cos(y)*cos(z)-atan(w)");
		paraName.add("x,y,z,w");
		paraType.add("double,double,double,double");
		functionName.add("benchmark14");
		binaryExp.add("sin(y)*asin(x)-300<cos(y)*cos(z)-atan(w)");
		paraName.add("x,y,z,w");
		paraType.add("double,double,double,double");
		functionName.add("benchmark15");
		binaryExp.add("((asin(1)*asin(cos(9*57)))-1)<(atan(0)*atan(0))");
		paraName.add("x,y,z,w");
		paraType.add("double,double,double,double");
		functionName.add("benchmark16");
		binaryExp.add("tan(w-x)*cos(sin(w/v))-atan(y+20+z)+asin(y-15)<sin(w*w)*cos(x*w*v)-tan(cos(x*w*x))+sin(w)");
		paraName.add("x,y,z,w,v");
		paraType.add("double,double,double,double,double");
		functionName.add("benchmark17");
		binaryExp.add("asin(x)*acos(x)<atan(x)");
		paraName.add("x");
		paraType.add("double");
		functionName.add("benchmark18");
		binaryExp.add("(1+acos(x))<asin(x)");
		paraName.add("x");
		paraType.add("double");
		functionName.add("benchmark19");
		binaryExp.add("3*acos(x)<atan(y)+asin(z)");
		paraName.add("x,y,z");
		paraType.add("double,double,double");
		functionName.add("benchmark20");
		binaryExp.add("sin(sin(x*y))<0&&cos(2*x)>0.25");
		paraName.add("x,y");
		paraType.add("double,double");
		functionName.add("benchmark21");
		binaryExp.add("cos(x*y)<0&&sin(2*x)>0.25");
		paraName.add("x,y");
		paraType.add("double,double");
		functionName.add("benchmark22");
		binaryExp.add("(sin(cos(x*y))<cos(sin(y*z)))&&sin(w*2.0-y)/(cos(t*2.0+q)+1.0)==(cos(z*2.0+x)/(sin(w*2.0+v)+1.0))");
		paraName.add("x,y,z,w,v,t,q");
		paraType.add("double,double,double,double,double,double,double");
		functionName.add("benchmark23");
		binaryExp.add("sin(x*y*z*w)>0&&cos(x*y*z*w)<0&&sin(2*x-y)/(cos(2*y+x)+1)==cos(2*z+x)/(sin(2*w+y)-1)");
		paraName.add("x,y,z,w");
		paraType.add("double,double,double,double");	
		functionName.add("benchmark25");
		binaryExp.add("sin(cos(x*y))<cos(sin(x*z))&&(sin(2*w-y)/(cos(2*y+v)+1)==cos(2*z+x)/(sin(2*w+v)-1))");
		paraName.add("x,y,z,w,v");
		paraType.add("double,double,double,double,double");	
		functionName.add("benchmark26");
		binaryExp.add("sin(cos(x*y))<cos(sin(x*z))&&(sin(2*w-y)/(cos(2*y+v)+1)==cos(2*z+x)/(sin(2*w+v)-1))&&sin(x*y*z*w)>0&&cos(x*y*z*w)<0");
		paraName.add("x,y,z,w,v");
		paraType.add("double,double,double,double,double");
		functionName.add("benchmark27");
		binaryExp.add("sin(x*cos(y*sin(z)))>cos(x*sin(y*cos(z)))&&sin(cos(x*y))<cos(sin(x*y))");
		paraName.add("x,y,z");
		paraType.add("double,double,double");
		functionName.add("benchmark28");
		binaryExp.add("log(x)==2");
		paraName.add("x");
		paraType.add("double");
		functionName.add("benchmark29");
		binaryExp.add("exp(x)>5");
		paraName.add("x");
		paraType.add("double");
		functionName.add("benchmark30");
		binaryExp.add("log10(x)==2");
		paraName.add("x");
		paraType.add("double");
		functionName.add("benchmark31"); 
		binaryExp.add("round(x)>5");
		paraName.add("x");
		paraType.add("double");
		functionName.add("benchmark32");
		binaryExp.add("sqrt(x)>5");
		paraName.add("x");
		paraType.add("double");
		functionName.add("benchmark33");
		binaryExp.add("sqrt(sin(x))>sqrt(cos(x))");
		paraName.add("x");
		paraType.add("double");
		functionName.add("benchmark34");
		binaryExp.add("sqrt(sin(x))<sqrt(cos(x))");
		paraName.add("x");
		paraType.add("double");
		functionName.add("benchmark35");
		binaryExp.add("1.0/sqrt(sin(x))>sqrt(cos(exp(y)))");
		paraName.add("x,y");
		paraType.add("double,double");
		functionName.add("benchmark36");
		binaryExp.add("log10(z)*(1.0/sqrt(sin(x)))==sqrt(cos(exp(y)))");
		paraName.add("x,y,z");
		paraType.add("double,double,double");
		functionName.add("benchmark37");
		binaryExp.add("log10(tan(z))*(1.0/sqrt(sin(x)))==sqrt(cos(exp(y)))");
		paraName.add("x,y,z");
		paraType.add("double,double,double");
		functionName.add("benchmark38");
		binaryExp.add("atan2(x,y)==1.0");
		paraName.add("x,y");
		paraType.add("double,double");
		functionName.add("benchmark39");
		binaryExp.add("pow(x,y)==1.0");
		paraName.add("x,y");
		paraType.add("double,double");
		functionName.add("benchmark40");
		binaryExp.add("pow(x,2)==x+y");
		paraName.add("x,y");
		paraType.add("double,double");
		functionName.add("benchmark41");
		binaryExp.add("x >=-1&&y<=2&&pow(x,2)==x+y");
		paraName.add("x,y");
		paraType.add("double,double");	
		functionName.add("benchmark42");
		binaryExp.add("pow(x,y)>pow(y,x)&&x>1&&y<=10");
		paraName.add("x,y");
		paraType.add("double,double");
		functionName.add("benchmark43");
		binaryExp.add("pow(x,y)>pow(y,x)&&exp(y)>exp(x)&&y<pow(x,2)");
		paraName.add("x,y");
		paraType.add("double,double");
		functionName.add("benchmark44");
		binaryExp.add("pow(x,y)>pow(y,x)&&exp(y)<exp(x)");
		paraName.add("x,y");
		paraType.add("double,double");
		functionName.add("benchmark45");
		binaryExp.add("sqrt(exp(x+y))<pow(z,x)&&x>0&&y>1&&z>1&&y<=x+2");
		paraName.add("x,y,z");
		paraType.add("double,double,double");
		functionName.add("benchmark46");//本身无解的约束
		binaryExp.add("sqrt(exp(x+z))<pow(z,x)&&x>0&&y>1&&z>1&&y<1&&y<x+2&&w==x+2");
		paraName.add("x,y,z,w");
		paraType.add("double,double,double,double");	
		functionName.add("benchmark47");
		binaryExp.add("exp(x+y)==exp(z)");
		paraName.add("x,y,z");
		paraType.add("double,double,double");
		functionName.add("benchmark48");
		binaryExp.add("x+y!=z");
		paraName.add("x,y,z");
		paraType.add("double,double,double");
		functionName.add("benchmark49");
		binaryExp.add("pow(x,2)+3*sqrt(y)<x*y&&x<pow(y,2)&&x+y<50");
		paraName.add("x,y");
		paraType.add("double,double");
		functionName.add("benchmark50");
		binaryExp.add("x+y<50&& x<pow(y,2) && pow(x,2)+3*sqrt(y)<x*y &&   x==-13+y");
		paraName.add("x,y");
		paraType.add("double,double");	
		functionName.add("benchmark51");
		binaryExp.add("pow(x,2)+3*sqrt(y)<x*y&&x<pow(y,2)&&x+y<50&&pow(x,x)<log10(y)");
		paraName.add("x,y");
		paraType.add("double,double");	
		
		functionName.add("benchmark52");
		binaryExp.add("pow(x,tan(y))+z<x*atan(z)&&sin(y)+cos(y)+tan(y)>=x-z");
		paraName.add("x,y,z");
		paraType.add("double,double,double");
		functionName.add("benchmark53");
		binaryExp.add("pow(x,tan(y))+z<x*atan(z)&&sin(y)+cos(y)+tan(y)>=x-z&&atan(x)+atan(y)>y");
		paraName.add("x,y,z");
		paraType.add("double,double,double");
		functionName.add("benchmark54");
		binaryExp.add("pow(x,tan(y))+z<x*atan(z)&&sin(y)+cos(y)+tan(y)>=x-z&&atan(x)+atan(y)>y&&log(pow(x,tan(y)))<log(z)");
		paraName.add("x,y,z");
		paraType.add("double,double,double");
		//这个很难搞benchmark55
		functionName.add("benchmark55");
		binaryExp.add("pow(x,tan(y))+z<x*atan(z)&&sin(y)+cos(y)+tan(y)>=x-z&&atan(x)+atan(y)>y&&log(pow(x,tan(y)))<log(z)&&sqrt(y+z)>sqrt(pow(x,(x-y)))");
		paraName.add("x,y,z");
		paraType.add("double,double,double");
		functionName.add("benchmark56");
		binaryExp.add("x*y+atan(z)*sin(w*t)>x/y+z+tan(w+t)");
		paraName.add("x,y,z,w,t");
		paraType.add("double,double,double,double,double");
		functionName.add("benchmark57");
		binaryExp.add("x*y+atan(z)*sin(w*t)>x/y+z+tan(w+t)&&pow(log10(x),log10(y))<=pow(log10(z+w+t),tan(w*t))");
		paraName.add("x,y,z,w,t");
		paraType.add("double,double,double,double,double");
		functionName.add("benchmark58");
		binaryExp.add("x*y+atan(z)*sin(w*t)>x/y+z+tan(w+t)&&pow(log10(x),log10(y))<=pow(log10(z+w+t),tan(w*t))&&tan(w*(x+y))+sin(t*(y+z))>asin(x+y+z)+acos(x+y+z)
		+atan(x+y+z)");
		paraName.add("x,y,z,w,t");
		paraType.add("double,double,double,double,double");	
		//只是覆盖了6次
		functionName.add("benchmark59");
		binaryExp.add("x*y+atan(z)*sin(w*t)>x/y+z+tan(w+t)&&pow(log10(x),log10(y))<=pow(log10(z+w+t),tan(w*t))&&tan(w*(x+y))+sin(t*(y+z))>asin(x+y+z)+acos(x+y+z)
		+atan(x+y+z)&&w==t*3/4");
		paraName.add("x,y,z,w,t");
		paraType.add("double,double,double,double,double");
		functionName.add("benchmark60");
		binaryExp.add("x*y+atan(z)*sin(w*t)>x/y+z+tan(w+t)&&pow(log10(x),log10(y))<=pow(log10(z+w+t),tan(w*t))&&tan(w*(x+y))+sin(t*(y+z))>asin(x+y+z)+acos(x+y+z)
		+atan(x+y+z)&&x<2*y-3*z&&w==t*3/4");
		paraName.add("x,y,z,w,t");
		paraType.add("double,double,double,double,double");
		functionName.add("benchmark61");
		binaryExp.add("x+y>z/w&&sqrt(x)>z/y&&z*2+w*3+x*7<pow(y,6)&&z+w>x+y&&w<x/y");
		paraName.add("x,y,z,w");
		paraType.add("double,double,double,double");
		functionName.add("benchmark62");
		binaryExp.add("x+y>z/w&&sqrt(x)>z/y&&z*2+w*3+x*7<pow(y,6)&&z+w>x+y&&w<x/y&&x>(w+y-z)");
		paraName.add("x,y,z,w");
		paraType.add("double,double,double,double");
		functionName.add("benchmark64");
		binaryExp.add("x+y>z/(w+t)&&sqrt(x)>z/y&&log(x*y)>log(t+w+z)&&z*2+w*3+x*7<pow(y,t)&&z+w>x+y&&w<x/y&&x>(w+y-z)&&log10(t*x)<sqrt(w*y*z)&&x*(t+y)>log
		(w*z*3)");
		paraName.add("x,y,z,w,t");
		paraType.add("double,double,double,double,double");
		functionName.add("benchmark66");
		binaryExp.add("x+y>(z+t)/(w+t)&&sqrt(x)>z/y&&log(x*y)>log(t+w+z)&&z*2+w*3+x*7<pow(y,t)&&z+w>x+y&&w<x/y&&x>(w+y-z)&&log10(t*x)<sqrt(w*y*z)&&x*cos(t+y)>log
		(w*z*3)&&cos(t)>cos(y)");
		paraName.add("x,y,z,w,t");
		paraType.add("double,double,double,double,double");
		functionName.add("benchmark67");
		binaryExp.add("x-y+tan(v)>(z+t)/(w+t)&&sqrt(x-t)>z/y&&log(x*y)>log(t+w+z)&&z*2+w*3+x*7<pow(y,t)*cos(v)&&z+w>x+y&&w<x/y&&x>(w+y-z)&&log10(t*x)<sqrt(w*y*z)
		&&x*cos(t+y)>log(w*z*3)&&cos(t)*sin(v)>cos(y)");
		paraName.add("x,y,z,w,t,v");
		paraType.add("double,double,double,double,double,double");
		functionName.add("benchmark69");
		binaryExp.add("x-y+tan(v)>(z+t)/(w+t)&&sqrt(x-t)>z/y&&log(x*y)>log(t+w+z)&&z*2+w*3+x*7<pow(y,t)*cos(v)&&z+w>x+y&&w<x/y&&x>(w+y-z)&&log10(t*x)<sqrt(w*y*z)
		&&x*cos(t+y)>log(w*z*3)&&cos(t)*sin(v)>cos(y)&&sin(x*y)+sin(z*w)+sin(t*v)<cos(x*y)+cos(z*w)+cos(t*v)");
		paraName.add("x,y,z,w,t,v");
		paraType.add("double,double,double,double,double,double");
		functionName.add("benchmark63");
		binaryExp.add("x+y>z/w&&sqrt(x)>z/y&&log(x*y)>log(t+w+z)&&z*2+w*3+x*7<pow(y,6)&&z+w>x+y&&w<x/y&&x>(w+y-z)&&log10(t*x)<sqrt(w*y*z)");
		paraName.add("x,y,z,w,t");
		paraType.add("double,double,double,double,double");
		functionName.add("benchmark65");
		binaryExp.add("x+y>z/(w+t)&&sqrt(x)>z/y&&log(x*y)>log(t+w+z)&&z*2+w*3+x*7<pow(y,t)&&z+w>x+y&&w<x/y&&x>(w+y-z)&&log10(t*x)<sqrt(w*y*z)&&x*cos(t+y)>log
		(w*z*3)");
		paraName.add("x,y,z,w,t");
		paraType.add("double,double,double,double,double");
		
		//这个很难搞benchmark55
		functionName.add("benchmark55");
		binaryExp.add("pow(x,tan(y))+z<x*atan(z)&&sin(y)+cos(y)+tan(y)>=x-z&&atan(x)+atan(y)>y&&log(pow(x,tan(y)))<log(z)&&sqrt(y+z)>sqrt(pow(x,(x-y)))");
		paraName.add("x,y,z");
		paraType.add("double,double,double");
		functionName.add("benchmark67");
		binaryExp.add("x-y+tan(v)>(z+t)/(w+t)&&sqrt(x-t)>z/y&&log(x*y)>log(t+w+z)&&z*2+w*3+x*7<pow(y,t)*cos(v)&&z+w>x+y&&w<x/y&&x>(w+y-z)&&log10(t*x)<sqrt(w*y*z)
		&&x*cos(t+y)>log(w*z*3)&&cos(t)*sin(v)>cos(y)");
		paraName.add("x,y,z,w,t,v");
		paraType.add("double,double,double,double,double,double");
		functionName.add("benchmark69");
		binaryExp.add("x-y+tan(v)>(z+t)/(w+t)&&sqrt(x-t)>z/y&&log(x*y)>log(t+w+z)&&z*2+w*3+x*7<pow(y,t)*cos(v)&&z+w>x+y&&w<x/y&&x>(w+y-z)&&log10(t*x)<sqrt(w*y*z)
		&&x*cos(t+y)>log(w*z*3)&&cos(t)*sin(v)>cos(y)&&sin(x*y)+sin(z*w)+sin(t*v)<cos(x*y)+cos(z*w)+cos(t*v)");
		paraName.add("x,y,z,w,t,v");
		paraType.add("double,double,double,double,double,double");
		
		functionName.add("benchmark70");
		binaryExp.add("sin(a)>sin(b)&&sin(b)>sin(c)&&sin(c)>sin(d)&&sin(d)>sin(e)&&sin(e)>sin(f)&&sin(f)>sin(g)&&sin(g)>sin(h)&&sin(h)>sin(i)&&sin(i)>sin(j)&&sin
		(j)>sin(k)&&sin(k)>sin(l)&&sin(l)>sin(m)&&sin(m)>sin(n)&&sin(n)>sin(o)&&sin(o)>sin(p)&&sin(p)>sin(q)&&sin(q)>sin(r)&&sin(r)>sin(s)&&sin(s)>sin(t)&&sin(t)
		>sin(u)&&sin(u)>sin(v)&&sin(v)>sin(x)&&sin(x)>sin(z)");
		paraName.add("a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,x,z");
		paraType.add("double,double,double,double,double,double,double,double,double,double,double,double,double,double,double,double,double,double,double,double,
		double,double,double,double");

	    functionName.add("benchmark71");
		binaryExp.add("sin(a)>sin(b)&&sin(b)>sin(c)&&sin(c)>sin(d)&&sin(d)>sin(e)&&sin(e)>sin(f)&&sin(f)>sin(g)&&sin(g)>sin(h)&&sin(h)>sin(i)&&sin(i)>sin(j)&&sin
		(j)>sin(k)&&sin(k)>sin(l)");
		paraName.add("a,b,c,d,e,f,g,h,i,j,k,l");
		paraType.add("double,double,double,double,double,double,double,double,double,double,double,double");
		functionName.add("benchmark72");
		binaryExp.add("sin(a)>sin(b)&&sin(b)>sin(c)&&sin(c)>sin(d)&&sin(d)>sin(e)&&sin(e)>sin(f)&&sin(f)>sin(g)");
		paraName.add("a,b,c,d,e,f,g");
		paraType.add("double,double,double,double,double,double,double");
		functionName.add("benchmark73");
		binaryExp.add("sin(a)>sin(b)&&sin(b)>sin(c)&&sin(c)>sin(d)&&sin(d)>sin(e)&&sin(e)>sin(f)&&sin(f)>sin(g)&&sin(g)>sin(h)&&sin(h)>sin(i)&&sin(i)>sin(j)");
		paraName.add("a,b,c,d,e,f,g,h,i,j");
		paraType.add("double,double,double,double,double,double,double,double,double,double");
		functionName.add("benchmark74");
		binaryExp.add("sin(a)>sin(b)&&sin(b)>sin(c)&&sin(c)>sin(d)&&sin(d)>sin(e)&&sin(e)>sin(f)&&sin(f)>sin(g)&&sin(g)>sin(h)");
		paraName.add("a,b,c,d,e,f,g,h");
		paraType.add("double,double,double,double,double,double,double,double");
		functionName.add("benchmark75");
		binaryExp.add("sin(a)>sin(b)&&sin(b)>sin(c)&&sin(c)>sin(d)&&sin(d)>sin(e)&&sin(e)>sin(f)&&sin(f)>sin(g)&&sin(g)>sin(h)&&sin(h)>sin(i)");
		paraName.add("a,b,c,d,e,f,g,h,i");
		paraType.add("double,double,double,double,double,double,double,double,double");
		functionName.add("benchmark76");
		binaryExp.add("a>b&&b>c&&c>d&&d>e&&e>f&&f>g&&g>h&&h>i");
		paraName.add("a,b,c,d,e,f,g,h,i");
		paraType.add("double,double,double,double,double,double,double,double,double");
		functionName.add("benchmark77");
		binaryExp.add("a>b&&b>c&&c>d&&d>e&&e>f&&f>g&&g>h&&h>i&&i>j");
		paraName.add("a,b,c,d,e,f,g,h,i,j");
		paraType.add("double,double,double,double,double,double,double,double,double,double");
		
		//本身无解，图像无交点
		functionName.add("benchmark82");
		binaryExp.add("(pow(10,4)*a*b-1)==0 && (exp(-a)+exp(-b)-1.0001)==0");
		paraName.add("a,b");
		paraType.add("double,double");	
		functionName.add("benchmark80");
		binaryExp.add("(1.5-a*(1-b))==0");
		paraName.add("a,b");
		paraType.add("double,double");
		functionName.add("benchmark81");
		binaryExp.add("(-13+a+((5-b)*b-2)*b)+(-29+a+((b+1)*b-14)*b)==0");
		paraName.add("a,b");
		paraType.add("double,double");
		functionName.add("benchmark91");
		binaryExp.add("sin(x)>0&&sin(x)==-sin(y)");
		paraName.add("x,y");
		paraType.add("double,double");
		functionName.add("benchmark83");
		binaryExp.add("pow((1-a),2)+100*(pow((b-a*a),2))==0");
		paraName.add("a,b");
		paraType.add("double,double");
		functionName.add("benchmark84");
		//binaryExp.add("1-c==0&&sqrt(10)*(b+d-2)==0&&pow(10,-0.5)*(b-d)==0&&1-a==0&&10*(b-a*a)==0&&sqrt(90)*(d-c*c)==0");
		binaryExp.add("1-c==0&&1-a==0&&10*(b-a*a)==0&&sqrt(90)*(d-c*c)==0&&sqrt(10)*(b+d-2)==0&&pow(10,-0.5)*(b-d)==0");
		paraName.add("a,b,c,d");
		paraType.add("double,double,double,double");
			
		functionName.add("benchmark79");
		binaryExp.add("(0.0==(pow(((a*sin(((0.017453292519943295*b)-(0.017453292519943295*c))))-(0.0*d)),2.0)+pow((a*cos((((0.017453292519943295*b)-
		(0.017453292519943295*c))+0.0))),2.0)))&&e!=0");
		paraName.add("a,b,c,d,e");
		paraType.add("double,double,double,double,int");
		
		functionName.add("benchmark78");
		binaryExp.add("(0.0==(pow(((a*sin(((b*0.017453292519943295-c*0.017453292519943295)+(((((((pow(d,2.0)/(sin(e*0.017453292519943295)/cos
		(e*0.017453292519943295)))/68443.0)*0.0)/d)*-1.0)*a)/((pow(a,2.0)/(sin((e*0.017453292519943295))/cos((e*0.017453292519943295))))/68443.0)))))-(d*0.0)),
		2.0)+pow(((a*cos((((b*0.017453292519943295)-(c*0.017453292519943295))+(((((((pow(d,2.0)/(sin((e*0.017453292519943295))/cos((e*0.017453292519943295))))
		/68443.0)*0.0)/d)*-1.0)*a)/((pow(a,2.0)/(sin((e*0.017453292519943295))/cos((e*0.017453292519943295))))/68443.0)))))-d*1.0),2.0)))&&f!=0&&h!=0");
		paraName.add("a,b,c,d,e,f,g,h");
		paraType.add("double,double,double,double,double,int,int,int");
		*/

    //本身无解，图像无交点
/*		functionName.add("benchmark82");
		binaryExp.add("(pow(10,4)*a*b-1)==0 && (exp(-a)+exp(-b)-1.0001)==0");
		paraName.add("a,b");
		paraType.add("double,double");
		functionName.add("benchmark83");
		binaryExp.add("pow((1-a),2)+100*(pow((b-a*a),2))==0");
		paraName.add("a,b");
		paraType.add("double,double");
		
		functionName.add("benchmark79");
		binaryExp.add("(0.0==(pow(((a*sin(((0.017453292519943295*b)-(0.017453292519943295*c))))-(0.0*d)),2.0)+pow((a*cos((((0.017453292519943295*b)-
		(0.017453292519943295*c))+0.0))),2.0)))&&e!=0");
		paraName.add("a,b,c,d,e");
		paraType.add("double,double,double,double,int");
		functionName.add("benchmark78");
		binaryExp.add("(0.0==(pow(((a*sin(((b*0.017453292519943295-c*0.017453292519943295)+(((((((pow(d,2.0)/(sin(e*0.017453292519943295)/cos
		(e*0.017453292519943295)))/68443.0)*0.0)/d)*-1.0)*a)/((pow(a,2.0)/(sin((e*0.017453292519943295))/cos((e*0.017453292519943295))))/68443.0)))))-(d*0.0)),
		2.0)+pow(((a*cos((((b*0.017453292519943295)-(c*0.017453292519943295))+(((((((pow(d,2.0)/(sin((e*0.017453292519943295))/cos((e*0.017453292519943295))))
		/68443.0)*0.0)/d)*-1.0)*a)/((pow(a,2.0)/(sin((e*0.017453292519943295))/cos((e*0.017453292519943295))))/68443.0)))))-d*1.0),2.0)))&&f!=0&&h!=0");
		paraName.add("a,b,c,d,e,f,g,h");
		paraType.add("double,double,double,double,double,int,int,int");
		*/

  }

  public static void printAllBenchmark() {
    for (int i = 0; i < functionName.size(); i++) {
      System.out.println(functionName.get(i) + "  "
                         + paraName.get(i) + "   "
                         + paraType.get(i) + "   "
                         + binaryExp.get(i) + "   "
                        );
    }
  }

}
