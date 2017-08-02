package cn.nju.seg.atg.model.constraint;


public class IdExpression extends Expression{

	private String name;

	public IdExpression() {
		
	}
	
	public IdExpression(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
