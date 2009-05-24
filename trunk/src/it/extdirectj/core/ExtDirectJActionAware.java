package it.extdirectj.core;

import it.extdirectj.bean.ExtDirectJAction;
import it.extdirectj.bean.ExtDirectJTransaction;

public interface ExtDirectJActionAware {

	public void setAction(ExtDirectJAction action);
	public void setTransaction(ExtDirectJTransaction transaction);
		
}
