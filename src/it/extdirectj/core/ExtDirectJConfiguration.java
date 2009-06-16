package it.extdirectj.core;

import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.entities.PackageConfig;
import com.opensymphony.xwork2.inject.Inject;
import com.google.gson.GsonBuilder;
import it.extdirectj.annotation.ExtDirectMethod;
import it.extdirectj.bean.ExtDirectJAction;
import it.extdirectj.bean.ExtDirectJApiResponse;
import it.extdirectj.bean.ExtDirectJMethod;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.text.DateFormat;

/**
 * @Author: Simone Ricciardi
 */
public class ExtDirectJConfiguration {

   private Configuration configuration;
   private String apiPrefix;
   private String apiUrl;
   private String apiNamespace;
   private ExtDirectJApiResponse apiResponse;

   public boolean isConfigured() {
      return this.apiResponse.hasActions();
   }

   @Inject
   public void setConfiguration(Configuration configuration) throws ClassNotFoundException {
      this.configuration = configuration;
      this.apiResponse = new ExtDirectJApiResponse();
      populateAnnotationDB();
   }

   public String getApiPrefix() {
      return apiPrefix;
   }

   @Inject("extdirectj.apiPrefix")
   public void setApiPrefix(String apiPrefix) {
      this.apiPrefix = apiPrefix;
   }

   public String getApiUrl() {
      return apiUrl;
   }

   @Inject("extdirectj.apiUrl")
   public void setApiUrl(String apiUrl) {
      this.apiUrl = apiUrl;
      this.apiResponse.setUrl(apiUrl);
   }

   public String getApiNamespace() {
      return apiNamespace;
   }

   @Inject("extdirectj.apiNamespace") 
   public void setApiNamespace(String apiNamespace) {
      this.apiNamespace = apiNamespace;
      this.apiResponse.setNameSpace(apiNamespace);
   }

   public ExtDirectJApiResponse getApiResponse() {
      return apiResponse;
   }

   private void populateAnnotationDB() throws ClassNotFoundException {

      //searching for annotated classes
      Collection<PackageConfig> packages = this.configuration.getPackageConfigs().values();
      for (PackageConfig packageConfig : packages) {
         Collection<ActionConfig> actions = packageConfig.getAllActionConfigs().values();
         for (ActionConfig actionConfig : actions) {
            Method[] methods = Class.forName(actionConfig.getClassName()).getMethods();
            ExtDirectJAction apiAction = new ExtDirectJAction(actionConfig.getName());
            for (Method method : methods) {
               if (!method.isAnnotationPresent(ExtDirectMethod.class)) continue;
               ExtDirectMethod extmethod = method.getAnnotation(ExtDirectMethod.class);

               //method with params
               String mName = StringUtils.isNotBlank(extmethod.name()) ? extmethod.name() : method.getName();
               int mLen = method.getParameterTypes().length;
               ExtDirectJMethod apimethod = new ExtDirectJMethod(mName, method.getName(), mLen, extmethod.formHandler());
               apimethod.setParams(method.getParameterTypes());

               apiAction.addMethod(apimethod);
            }
            if(apiAction.hasMethods())
               this.apiResponse.addAction(apiAction);
         }
      }
   }

}
