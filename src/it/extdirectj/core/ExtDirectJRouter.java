package it.extdirectj.core;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.inject.Inject;
import it.extdirectj.bean.*;
import org.apache.struts2.interceptor.ServletRequestAware;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

public class ExtDirectJRouter implements Action, ServletRequestAware {

   private HttpServletRequest request;
   private ExtDirectJTransaction transaction = new ExtDirectJTransaction();
   private ExtDirectJAction action;
   private ExtDirectJResponse extdirectjResponse;
   private ExtDirectJConfiguration extDirectJConfiguration;
   private ExtDirectJsonParser extDirectJsonParser;

   @Inject
   public void setExtDirectJConfiguration(ExtDirectJConfiguration extDirectJConfiguration) {
      this.extDirectJConfiguration = extDirectJConfiguration;
   }

   @Inject
   public void setExtDirectJsonParser(ExtDirectJsonParser extDirectJsonParser) {
      this.extDirectJsonParser = extDirectJsonParser;
   }

   public void setServletRequest(HttpServletRequest request) {
      this.request = request;
   }

   public ExtDirectJTransaction getTransaction() {
      return transaction;
   }

   public ExtDirectJAction getAction() {
      return action;
   }

   public ExtDirectJResponse getExtdirectjResponse() {
      return extdirectjResponse;
   }

   public String execute() throws Exception {

      if (!this.extDirectJConfiguration.isConfigured()) {
         this.extdirectjResponse = new ExtDirectJException(transaction, " api init error ", "ExtDirectJInterceptor");
         return Action.ERROR;
      }

      ExtDirectJApiResponse apiResponse = this.extDirectJConfiguration.getApiResponse();

      //in case of GET request we want to sent back the apiresponse
      if ("GET".equals(request.getMethod())) {
         System.out.println("GET request for the router");
         ActionContext.getContext().put("apiResponse", apiResponse);
         ActionContext.getContext().put("apiPrefix", this.extDirectJConfiguration.getApiPrefix());

         return "apiresponse";
      }

      //in case of POST we dispatch the request to the router action
      if ("POST".equals(request.getMethod())) {

         String line;
         String transactionString = "";

         //parse request
         BufferedReader br = request.getReader();
         while ((line = br.readLine()) != null) {
            transactionString += line;
         }

         //encoding
         try {

            this.transaction = extDirectJsonParser.deserialize(transactionString, ExtDirectJTransaction.class);
         } catch (Exception e) {
            this.extdirectjResponse = new ExtDirectJException(transaction, " invalid transaction request", "ExtDirectJInterceptor");
            return Action.ERROR;
         }

         //find action
         try {

            this.action = apiResponse.getActionByName(transaction.getAction());
         } catch (Exception e) {
            this.extdirectjResponse = new ExtDirectJException(transaction, " invalid action/class", "ExtDirectJInterceptor");
            return Action.ERROR;
         }
      }

      return Action.SUCCESS;
   }
}
