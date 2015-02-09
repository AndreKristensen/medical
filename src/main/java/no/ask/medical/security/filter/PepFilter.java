package no.ask.medical.security.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamException;

import no.ask.xacml.util.XACMLCommunication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.xacmlinfo.xacml.pep.agent.PEPAgentException;

//import org.xacmlinfo.xacml.pep.agent.PEPAgentException;

public class PepFilter implements Filter {

	@Autowired
	private XACMLCommunication xacml;
	
	@Autowired
	private Environment env;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String requestUrl = new FilterInvocation(request, response, chain).getRequestUrl();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String environment = env.getProperty("xacml.env");

		List<String> decisonResults;
		try {
			decisonResults = xacml.getDecisonResults(auth.getName(), new ArrayList<String>(), environment, requestUrl);

			if (!decisonResults.isEmpty() && decisonResults.get(0).equals(XACMLCommunication.RESULT_PERMIT)) {
				// chain.doFilter(request, response);
			} else {
				System.out.println(requestUrl);
				System.out.println(decisonResults);
				((HttpServletResponse) response).sendError(401, decisonResults.isEmpty() ? "empty" : decisonResults.get(0) + " For url " + requestUrl);
			}
		} catch (NullPointerException | PEPAgentException | XMLStreamException e) {
			e.printStackTrace();
		}

		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
