package it.extdirectj.bean;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class ExtDirectJMethod{

	private String name;
	private int len;
	private boolean formHandler;
	
	private transient String realName;
	private transient ArrayList<Class> params = new ArrayList<Class>(); //local only
	
	public ExtDirectJMethod(String name,String realName, int nbOfArguments, boolean formHandling) {
		this.name = name;
		this.len = nbOfArguments;
		this.formHandler = formHandling;
		this.realName = realName;
	}
	
	public void addParam(Class param) {
		this.params.add(param);
	}
	
	public void setParams(Class<?>[] parametersType){
		this.params = new ArrayList<Class>();		
		for (Class<?> paramType : parametersType) {
			this.params.add(paramType);
		}
	}
	
	public Class[] getParams(){
		return this.params.toArray(new Class[this.params.size()]);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLen() {
		return len;
	}
	public void setLen(int len) {
		this.len = len;
	}
	public boolean isFormHandler() {
		return formHandler;
	}
	public void setFormHandler(boolean formHandler) {
		this.formHandler = formHandler;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
}
