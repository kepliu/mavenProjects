package jpatest.ejb.util;

import java.security.Principal;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import jpatest.util.RequestContext;

public class RequestContextInterceptor {

	@Resource
	private EJBContext ctx;

	@AroundInvoke
	public Object initRequestContext(InvocationContext invCtx) throws Exception {
		String userName = "DefaultUser";
		
		Principal p = ctx.getCallerPrincipal();
		if (p != null) {
			userName = p.getName();
		}
		
		RequestContext requestContext = RequestContext.getLocalInstance();
		requestContext.initRequestContext(userName);

		return invCtx.proceed();
	}
}
