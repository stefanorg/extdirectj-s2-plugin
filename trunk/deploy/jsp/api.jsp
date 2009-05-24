
<%@page import="java.text.MessageFormat"%>
<%@page import="it.extdirectj.bean.ExtDirectJAction"%><%@page import="
  com.opensymphony.xwork2.ActionContext,
  com.opensymphony.xwork2.util.ValueStack,
  java.util.*,
  com.google.gson.Gson,
  it.extdirectj.bean.ExtDirectJApiResponse;
"%>
<%
response.setContentType("application/json");
response.setCharacterEncoding("UTF-8");
 
//serializing api response
 ActionContext ac = ActionContext.getContext();
 
 ExtDirectJApiResponse apiResponse = (ExtDirectJApiResponse) ac.get("apiResponse");
 String apiPrefix = (String) ac.get("apiPrefix");
 Gson gs = new Gson();
 
  
 String actions = "";
 ExtDirectJAction[] _actions = apiResponse.getActions();
 for(int i=0; i<_actions.length; i++){
    ExtDirectJAction action = _actions[i];
    actions += gs.toJson(action.getName()) +":"+ gs.toJson(action.getMethods());
    //adding comma if necessary
    if(i<_actions.length-1) actions+=",";
 }
 
 String res = apiPrefix+" = { \"url\": \""+apiResponse.getUrl()+"\", \"type\": \"remoting\", \"actions\": {"+actions+"} };";
 
 out.print(res);
%>