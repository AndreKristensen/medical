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

import no.ask.medical.exception.PEPException;
import no.ask.xacml.util.XACMLCommunication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.xacmlinfo.xacml.pep.agent.PEPAgentException;


public class PepFilter implements Filter {

	private static final Logger log = LoggerFactory.getLogger(PepFilter.class);

	@Autowired
	private XACMLCommunication xacml;
	
	@Value("${xacml.env}")
	private String environment;
	
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		FilterInvocation filterInvocation = new FilterInvocation(request, response, chain);
		String requestUrl = filterInvocation.getRequestUrl();
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<String> decisonResults;
		try {
			ArrayList<String> privileges = new ArrayList<String>();
            privileges.add(filterInvocation.getRequest().getMethod());
            
			decisonResults = xacml.getDecisionResults(auth.getName(), privileges, environment, requestUrl);

			if (!decisonResults.isEmpty() && !decisonResults.get(0).equals(XACMLCommunication.RESULT_PERMIT)) {
				log.info(requestUrl + " " + decisonResults);
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
