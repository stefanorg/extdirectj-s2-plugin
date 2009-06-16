package it.extdirectj.core;

import com.opensymphony.xwork2.DefaultActionInvocation;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.config.entities.ActionConfig;

import java.util.Map;
import java.lang.reflect.Method;

import it.extdirectj.bean.*;

/**
 * @Author: Simone Ricciardi
 */
public class ExtDirectJActionInvocation extends DefaultActionInvocation {

   private ExtDirectJsonParser extDirectJsonParser;
   private ExtDirectJTransaction extTransaction;
   private ExtDirectJAction extAction;
   private ExtDirectJResponse extResponse;

   @Inject
   public void setExtDirectJsonParser(ExtDirectJsonParser extDirectJsonParser) {
      this.extDirectJsonParser = extDirectJsonParser;
   }
   
   public void setExtTransaction(ExtDirectJTransaction extTransaction) {
      this.extTransaction = extTransaction;
   }

   public void setExtAction(ExtDirectJAction extAction) {
      this.extAction = extAction;
   }

   public ExtDirectJResponse getExtResponse() {
      return extResponse;
   }

   public ExtDirectJActionInvocation(Map<String, Object> extraContext, boolean pushAction) {
      super(extraContext, pushAction);
   }

   @Override
   protected String invokeAction(Object action, ActionConfig actionConfig) throws Exception {

      if (this.extTransaction != null) {
         String result =  this.invokeExtDirectJMethod(action);
         invocationContext.put("extdirectjResponse", this.extResponse);
         return result;
      } else {
         return super.invokeAction(action, actionConfig);
      }
   }

   private String invokeExtDirectJMethod(Object act) {

      Method queryMethod;
      Object queryResult;

      //Get the Method corresponding to requested class/methode
      ExtDirectJMethod method;
      try {
         method = this.extAction.getMethodByName(this.extTransaction.getMethod());
         queryMethod = act.getClass().getDeclaredMethod(method.getRealName(), method.getParams());
      } catch (Exception e) {
         this.extResponse = new ExtDirectJException(this.extTransaction, " invalid method ", e.getMessage());
         return Action.ERROR;
      }

      //Prepare Method arguments with the right type
      Class<?>[] methodParams = method.getParams();
      Object[] dataParams = new Object[methodParams.length];

      for (int i = 0; i < methodParams.length; i++) {
         try {
            dataParams[i] = extTransaction.getData().get(i).parseValue(methodParams[i]);
         } catch (Exception e) {
            this.extResponse = new ExtDirectJException(this.extTransaction, " bad parameter type ", e.getMessage());
            return Action.ERROR;
         }
      }

      //Invoke the Method with parameters and processing result
      try {
         queryResult = queryMethod.invoke(act, dataParams);
      } catch (Exception e) {
         this.extResponse = new ExtDirectJException(this.extTransaction, " error while invoking method ", e.getMessage());
         return Action.ERROR;
      }

      this.extResponse = new ExtDirectJResponse(extTransaction, this.extDirectJsonParser.serialize(queryResult));

      return "transresponse";
   }
}
