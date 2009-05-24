package it.extdirectj.core;

import it.extdirectj.bean.ExtDirectJAction;
import it.extdirectj.bean.ExtDirectJException;
import it.extdirectj.bean.ExtDirectJMethod;
import it.extdirectj.bean.ExtDirectJResponse;
import it.extdirectj.bean.ExtDirectJTransaction;

import java.lang.reflect.Method;

import com.opensymphony.xwork2.Action;

public class ExtDirectJRouter implements ExtDirectJActionAware{

	private ExtDirectJAction action;
	private ExtDirectJTransaction transaction;
	private ExtDirectJResponse extdirectjResponse;
			
	public String execute(){
		
		Class<?> queryClass = null;
		Object queryClassInstance = null;
		Method queryMethod = null;
		Object queryResult = null;
		//Get the Class corresponding to requested action
		String queryClassName = this.action.getClassName();		
    	try {
    		queryClass = Class.forName(queryClassName);
    		queryClassInstance = queryClass.getConstructor().newInstance();
		} catch (Exception e) {
			this.extdirectjResponse = new ExtDirectJException(this.transaction," invalid action/class ", e.getMessage());
			return Action.ERROR;
		}
		
		//Get the Methode corresponding to requested class/methode
		ExtDirectJMethod method = null;
		try {
			method = this.action.getMethodByName(this.transaction.getMethod());
			queryMethod = queryClass.getDeclaredMethod(method.getRealName(),method.getParams());
		} catch (Exception e) {
			this.extdirectjResponse = new ExtDirectJException(this.transaction," invalid method ", e.getMessage());
			return Action.ERROR;
		}

		//Prepare Method arguments with the right type
		Class<?>[] methodParams = method.getParams();
 		Object[] dataParams = new Object[methodParams.length];
		for (int i = 0 ; i<methodParams.length; i++){
			try {
				if (methodParams[i].getName().equals("int")) {			
					dataParams[i] =  Integer.parseInt(transaction.getData()[i]);
				} else if (methodParams[i].getName().equals("java.lang.String")){
					dataParams[i] =  transaction.getData()[i];
				} else if (methodParams[i].getName().equals("boolean")) {
					dataParams[i] =  Boolean.parseBoolean(transaction.getData()[i]);
				}		
			} catch (Exception e) {
				this.extdirectjResponse = new ExtDirectJException(this.transaction," bad parameter type ", e.getMessage());
				return Action.ERROR;
			}
		}
		
		//Invoke the Method with parameters and processing result		
		try {
			queryResult = queryMethod.invoke(queryClassInstance,dataParams);
		} catch (Exception e) {
			this.extdirectjResponse = new ExtDirectJException(this.transaction," error while invoking method ", e.getMessage());
			return Action.ERROR;
		}
		
		this.extdirectjResponse = new ExtDirectJResponse(transaction,queryResult.toString());
			
		return "transresponse";
	}

	public void setTransaction(ExtDirectJTransaction transaction) {
		this.transaction = transaction;
	}

	public ExtDirectJResponse getResponse() {
		return extdirectjResponse;
	}

	public void setResponse(ExtDirectJResponse respose) {
		this.extdirectjResponse = respose;
	}

	public void setAction(ExtDirectJAction action) {
		this.action = action;
		
	}

	public ExtDirectJResponse getExtdirectjResponse() {
		return extdirectjResponse;
	}

	public void setExtdirectjResponse(ExtDirectJResponse extdirectjResponse) {
		this.extdirectjResponse = extdirectjResponse;
	}	
}
