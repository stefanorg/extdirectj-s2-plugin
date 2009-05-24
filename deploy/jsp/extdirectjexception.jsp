
<%@page import="com.google.gson.GsonBuilder"%>
<%@page import="com.google.gson.reflect.TypeToken"%>
<%@page import="java.lang.reflect.Type"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="it.extdirectj.bean.ExtDirectJException"%>

<%
response.setContentType("application/json");
response.setCharacterEncoding("UTF-8");
 
//serializing api response
 ActionContext ac = ActionContext.getContext();
 ValueStack stack = ac.getValueStack();
 ExtDirectJException e = (ExtDirectJException) stack.findValue("extdirectjResponse",ExtDirectJException.class);
 Gson gs = new GsonBuilder().setPrettyPrinting().create();
 out.print(gs.toJson(e));
%>