package it.extdirectj.bean;

import java.util.ArrayList;

public class ExtDirectJAction{

	private String name;
	private ArrayList<ExtDirectJMethod> methods = new ArrayList<ExtDirectJMethod>();
	
	public ExtDirectJAction(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addMethod(ExtDirectJMethod method) {
		this.methods.add(method);
	}
	
	public ExtDirectJMethod[] getMethods(){
		return this.methods.toArray(new ExtDirectJMethod[this.methods.size()]);
	}

   public boolean hasMethods() {
      return this.methods.size() > 0;   
   }
	
	public ExtDirectJMethod getMethodByName(String methodName) throws NoSuchMethodException{
		for (ExtDirectJMethod method : this.methods) {
			if(method.getName().equals(methodName))
				return method;
		}
		throw new NoSuchMethodException("No method found in action "+this.name+" with name "+methodName);
	}
}
