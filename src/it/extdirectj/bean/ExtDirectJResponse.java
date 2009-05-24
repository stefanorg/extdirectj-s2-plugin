package it.extdirectj.bean;

public class ExtDirectJResponse{
	private String action = "";
	private String method = "";
	private String result ="";
	private String type = "rpc";
	private int tid;
		
	public ExtDirectJResponse(ExtDirectJTransaction trans,String result){
		this.action = trans.getAction();
		this.method = trans.getMethod();
		this.type = trans.getType();
		this.tid = trans.getTid();		
		this.result = result;
	}
	
	public void setType(String type){
		this.type = type;		
	}	
	public String setResult(String result){
		return this.result = result;		
	}	
	public String getAction() {
		return action;
	}
	public String getMethod() {
		return method;
	}
	public String getResult(){
		return result;
	}
	public String getType() {
		return type;
	}
	public int getTid() {
		return tid;
	}
}
