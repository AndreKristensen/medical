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

import no.ask.medical.security.common.XACMLHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.wso2.balana.PDP;
import org.wso2.balana.ParsingException;
import org.wso2.balana.ctx.AbstractResult;
import org.wso2.balana.ctx.ResponseCtx;

public class PEPFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(PEPFilter.class);

    @Autowired
    private PDP pdp;

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
        // try {
        ArrayList<String> actions = new ArrayList<String>();
        actions.add(filterInvocation.getRequest().getMethod());

        System.out.println(XACMLHelper.createXACMLRequest(auth.getName(), actions.get(0), environment, requestUrl));
        String evaluate = pdp.evaluate(XACMLHelper.createXACMLRequest(auth.getName(), actions.get(0), environment, requestUrl));

        ResponseCtx responseCtx;
        try {
            responseCtx = ResponseCtx.getInstance(XACMLHelper.getXacmlResponse(evaluate));
            AbstractResult result = responseCtx.getResults().iterator().next();
            if (AbstractResult.DECISION_PERMIT != result.getDecision()) {
                if ("anonymousUser".equals((auth.getName()))) {
                    chain.doFilter(request, response);
                } else {
                    log.info(requestUrl + " " + evaluate);
                    ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, result.getDecision() + " For url " + requestUrl);

                }
            } else {
                chain.doFilter(request, response);
            }
        } catch (ParsingException e) {
            e.printStackTrace();
        }
        // if (!evaluate.equals(XACMLCommunication.RESULT_PERMIT)) {
        // log.info(requestUrl + " " + evaluate);
        // ((HttpServletResponse)
        // response).sendError(HttpServletResponse.SC_UNAUTHORIZED, evaluate +
        // " For url " + requestUrl);
        // }

        // chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }

}
