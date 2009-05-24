package it.extdirectj.bean;

public class ExtDirectJException extends ExtDirectJResponse  {

	private String message;
	private String where;
	
	public ExtDirectJException(ExtDirectJTransaction trans, String message, String where) {
		super(trans,"");
		this.message = message;
		this.where = where;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getWhere() {
		return where;
	}
	
	public void setWhere(String where) {
		this.where = where;
	}
}
