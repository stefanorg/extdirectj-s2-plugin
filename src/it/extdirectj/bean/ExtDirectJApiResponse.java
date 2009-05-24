package it.extdirectj.bean;

import java.util.ArrayList;

public class ExtDirectJApiResponse{

	private String url;
	private String nameSpace;
	private String type = "remoting";
	private ArrayList<ExtDirectJAction> actions = new ArrayList<ExtDirectJAction>();
	
	public ExtDirectJApiResponse(String url, String namespace) {
		this.url = url;
		this.nameSpace = namespace;
	}
	
	public void addAction(ExtDirectJAction action) {
		this.actions.add(action);
	}

	public ExtDirectJAction[] getActions() {
		return this.actions.toArray(new ExtDirectJAction[this.actions.size()]);
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public ExtDirectJAction getActionByName(String actionName) throws Exception{
		for (ExtDirectJAction action : this.actions) {
			if(action.getName().equals(actionName))
				return action;
		}
		throw new Exception("No Action "+actionName+" Found.");
	}
}
