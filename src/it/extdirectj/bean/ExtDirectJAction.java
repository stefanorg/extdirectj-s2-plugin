package it.extdirectj.bean;

import java.util.ArrayList;

public class ExtDirectJAction{

	private String name;
	private transient String className;
	private ArrayList<ExtDirectJMethod> methods = new ArrayList<ExtDirectJMethod>();
	
	public ExtDirectJAction(String name,String className) {
		this.name = name;
		this.className = className;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
	public void addMethod(ExtDirectJMethod method) {
		this.methods.add(method);
	}
	
	public ExtDirectJMethod[] getMethods(){
		return this.methods.toArray(new ExtDirectJMethod[this.methods.size()]);
	}
	
	public ExtDirectJMethod getMethodByName(String methodName) throws NoSuchMethodException{
		for (ExtDirectJMethod method : this.methods) {
			if(method.getName().equals(methodName))
				return method;
		}
		throw new NoSuchMethodException("No method found in action "+this.name+" with name "+methodName);
	}
}
