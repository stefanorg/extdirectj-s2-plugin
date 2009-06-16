package it.extdirectj.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.util.ValueStack;
import it.extdirectj.core.ExtDirectJActionInvocation;
import it.extdirectj.core.ExtDirectJRouter;

public class ExtDirectJInterceptor extends AbstractInterceptor {

   public String intercept(ActionInvocation invocation) throws Exception {

      ValueStack stack = invocation.getStack();
      Object root = stack.getRoot().get(1);

      String result;
      if(root != null && root instanceof ExtDirectJRouter && invocation instanceof ExtDirectJActionInvocation) {

         ExtDirectJRouter extRouter = ((ExtDirectJRouter) root);
         ExtDirectJActionInvocation extInvocation = ((ExtDirectJActionInvocation) invocation);

         extInvocation.setExtTransaction(extRouter.getTransaction());
         extInvocation.setExtAction(extRouter.getAction());
         
         result = extInvocation.invoke();
      } else {

         result = invocation.invoke();
      }

      return result;
   }
}
