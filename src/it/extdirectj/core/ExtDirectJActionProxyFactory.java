package it.extdirectj.core;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import org.apache.struts2.impl.StrutsActionProxyFactory;

import java.util.Map;

/**
 * @Author: Simone Ricciardi
 */
public class ExtDirectJActionProxyFactory extends StrutsActionProxyFactory {

   public ActionProxy createActionProxy(String namespace, String actionName, String methodName, Map<String, Object> extraContext, boolean executeResult, boolean cleanupContext) {

      ActionInvocation inv = new ExtDirectJActionInvocation(extraContext, true);
      container.inject(inv);
      return createActionProxy(inv, namespace, actionName, methodName, executeResult, cleanupContext);
   }
}
