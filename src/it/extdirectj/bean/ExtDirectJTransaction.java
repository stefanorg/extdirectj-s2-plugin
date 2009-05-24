package it.extdirectj.bean;

import java.util.ArrayList;
import java.util.List;


public class ExtDirectJTransaction {

	private String action = "";
	private String method = "";
	private List<String> data = new ArrayList<String>();
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
	public String[] getData() {
		return data.toArray(new String[data.size()]);
	}
	public void setData(String[] data) {
		this.data = new ArrayList<String>();
		for (String d : data) {
			this.data.add(d);
		}
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
