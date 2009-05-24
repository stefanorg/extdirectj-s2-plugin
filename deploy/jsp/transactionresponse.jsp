
<%@page import="it.extdirectj.bean.ExtDirectJResponse"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%
response.setContentType("application/json");
response.setCharacterEncoding("UTF-8");
 
//serializing api response
 ActionContext ac = ActionContext.getContext();
 ValueStack stack = ac.getValueStack();
 ExtDirectJResponse res = (ExtDirectJResponse) stack.findValue("extdirectjResponse",ExtDirectJResponse.class);
 Gson gs = new Gson();
 out.print(gs.toJson(res));
%>