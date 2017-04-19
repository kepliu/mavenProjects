package jpatest.web.util;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import jpatest.util.RequestContext;

/**
 * This filer initializes the Request Context with the calling user name at the
 * beginning of a request and clears the request context and user name at the
 * end of the request.
 */
public class RequestContextFilter implements Filter {

	public void init(FilterConfig config) throws ServletException {
	}

	/**
	 * This method gets the RequestContext associated with the current thread,
	 * initializes RequestContext with the user name prior to servicing the
	 * request, and then clears the RequestContext after servicing request.
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		RequestContext requestContext = RequestContext.getLocalInstance();
		
		try {	
			Principal p = ((HttpServletRequest) request).getUserPrincipal();
			String userName = "DefaultUser";
			if (p != null) {
				userName = p.getName();
			} 

			requestContext.initRequestContext(userName);
			chain.doFilter(request, response);
		} finally {
			// clear context at the end of the request
			requestContext.clearRequestContext();
		}
	}

	public void destroy() {
	}

}
