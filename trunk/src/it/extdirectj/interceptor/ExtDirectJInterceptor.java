package it.extdirectj.interceptor;

import it.extdirectj.annotation.ExtDirectAction;
import it.extdirectj.annotation.ExtDirectMethod;
import it.extdirectj.bean.ExtDirectJAction;
import it.extdirectj.bean.ExtDirectJApiResponse;
import it.extdirectj.bean.ExtDirectJException;
import it.extdirectj.bean.ExtDirectJMethod;
import it.extdirectj.bean.ExtDirectJResponse;
import it.extdirectj.bean.ExtDirectJTransaction;
import it.extdirectj.core.ExtDirectJRouter;

import java.io.BufferedReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.scannotation.AnnotationDB;
import org.scannotation.WarUrlFinder;

import com.google.gson.Gson;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

@SuppressWarnings("serial")
public class ExtDirectJInterceptor implements Interceptor{
	
	private String defApiUrl = "extdirectj\\/ExtDirectJRouter.action";
	private String apiPrefix = "Ext.app.REMOTING_API";
	private String apiUrl;
	private String apiNamespace;
	private ExtDirectJApiResponse apiResponse;
	private ExtDirectJResponse extdirectjResponse;
	
	public void init(){
		try{
			this.apiUrl = StringUtils.isNotEmpty(this.apiUrl) && StringUtils.isNotBlank(this.apiUrl) ? this.apiUrl : this.defApiUrl;
			this.apiNamespace = StringUtils.isNotEmpty(this.apiNamespace) && StringUtils.isNotBlank(this.apiNamespace) ? this.apiNamespace : "";
			
			System.out.printf("Initializzo ExtDirectJInterceptor ... url %s namespace %s",this.apiUrl,this.apiNamespace);
			
		}catch (Exception e) {
			System.out.println("Error initizializing ExtDirectJInterceptor "+e.getMessage());
			e.printStackTrace();
		}
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	public String intercept(ActionInvocation invocation) throws Exception {
		
		Object action = invocation.getAction();
		
		if(action instanceof ExtDirectJRouter){
			
			ExtDirectJTransaction transaction = new ExtDirectJTransaction();
			
			//initializing if not yet 
			if(this.apiResponse == null){
				this.apiResponse = new ExtDirectJApiResponse(this.apiUrl,this.apiNamespace);
				
				try{
					this.populateAnnotationDB(ServletActionContext.getServletContext());
				}catch (Exception e) {
					this.extdirectjResponse = new ExtDirectJException(transaction," api init error ", "ExtDirectJInterceptor");
					return Action.ERROR;
				}
			}
			
			try{
				
				HttpServletRequest request = ServletActionContext.getRequest();
				//HttpServletResponse response = ServletActionContext.getResponse();
				//in case of GET request we want to sent back the apiresponse
				if("GET".equals(request.getMethod())){
					System.out.println("GET request for the router");
					
					invocation.getInvocationContext().put("apiResponse", this.apiResponse);
					invocation.getInvocationContext().put("apiPrefix", this.apiPrefix);
					
					return "apiresponse";
				}
				
				//in case of POST we dispatch the request to the router action
				//setting the apiresponse property
				if("POST".equals(request.getMethod())){
					System.out.println("POST request for the router");
					String line;
					String transactionString ="";
					//parse request
					BufferedReader br = request.getReader();
					while((line = br.readLine())!=null){
						transactionString += line;
					}
					//encoding
					try{
						Gson jobj = new Gson();
						transaction = jobj.fromJson(transactionString, ExtDirectJTransaction.class);

					}catch (Exception e) {
						this.extdirectjResponse = new ExtDirectJException(transaction," invalid transaction request", "ExtDirectJInterceptor");
						return Action.ERROR;
					}
					
					//find action
					try{
						ExtDirectJAction extaction = this.apiResponse.getActionByName(transaction.getAction());
						
						//setting the actions on the router
						((ExtDirectJRouter) action).setAction(extaction);
						((ExtDirectJRouter) action).setTransaction(transaction);
						//continue chaining
						
						return invocation.invoke();
						
					}catch (Exception e) {
						this.extdirectjResponse = new ExtDirectJException(transaction," invalid action/class", "ExtDirectJInterceptor");
						return Action.ERROR;
					}
				}
			}catch (Exception e) {
				this.extdirectjResponse = new ExtDirectJException(transaction,e.getMessage(),ExtDirectJInterceptor.class.getSimpleName());
				return Action.ERROR;
			}
			
		}
		
		//request non for router
		return invocation.invoke();
		
	}

	private void populateAnnotationDB(ServletContext context) throws Exception{
		//searching for annotated classes
		URL url = WarUrlFinder.findWebInfClassesPath(context);
		URL[] libs = WarUrlFinder.findWebInfLibClasspaths(context);
		AnnotationDB db = new AnnotationDB();
		db.setScanFieldAnnotations(false);
		db.setScanParameterAnnotations(false);
		db.scanArchives(url);
		db.scanArchives(libs);
		Map<String, Set<String>> maps = db.getAnnotationIndex();
		//classes
		Set<String> classes = maps.get(ExtDirectMethod.class.getCanonicalName());
		if(classes!=null){
			for (String clazz : classes) {
				Class<?> c = Class.forName(clazz);

				ExtDirectAction extaction = c.isAnnotationPresent(ExtDirectAction.class) ? (ExtDirectAction) c.getAnnotation(ExtDirectAction.class) : null;
				//create action
				String name = (extaction!=null && StringUtils.isNotBlank(extaction.name()) ) 
				? extaction.name() 
						: c.getSimpleName();

				ExtDirectJAction apiAction = new ExtDirectJAction(name,c.getCanonicalName());


				Method[] methods = c.getMethods();
				for (Method method : methods) {
					if(!method.isAnnotationPresent(ExtDirectMethod.class)) continue;			
					ExtDirectMethod extmethod = (ExtDirectMethod) method.getAnnotation(ExtDirectMethod.class);

					//method with params
					String mName = StringUtils.isNotBlank(extmethod.name()) ? extmethod.name() : method.getName();
					int mLen = method.getParameterTypes().length;
					ExtDirectJMethod apimethod = new ExtDirectJMethod(mName,method.getName(),mLen,extmethod.formHandler());
					apimethod.setParams(method.getParameterTypes());

					apiAction.addMethod(apimethod);
				}

				this.apiResponse.addAction(apiAction);
			}
		}
	}
		

	public String getApiNamespace() {
		return apiNamespace;
	}

	public void setApiNamespace(String apiNamespace) {
		this.apiNamespace = apiNamespace;
	}

	public ExtDirectJApiResponse getApiResponse() {
		return apiResponse;
	}

	public void setApiResponse(ExtDirectJApiResponse apiResponse) {
		this.apiResponse = apiResponse;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public ExtDirectJResponse getExtdirectjResponse() {
		return extdirectjResponse;
	}

	public void setExtdirectjResponse(ExtDirectJResponse extdirectjResponse) {
		this.extdirectjResponse = extdirectjResponse;
	}

}
