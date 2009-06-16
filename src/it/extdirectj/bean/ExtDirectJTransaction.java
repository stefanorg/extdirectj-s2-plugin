package it.extdirectj.bean;

import java.util.ArrayList;
import java.util.List;


public class ExtDirectJTransaction {

	private String action = "";
	private String method = "";
	private List<ExtDirectJType> data = new ArrayList<ExtDirectJType>();
	private String type = "";
	private int tid;
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
   public List<ExtDirectJType> getData() {
      return data;
   }
   public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
}
