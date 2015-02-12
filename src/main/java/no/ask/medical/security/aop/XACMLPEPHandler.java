package no.ask.medical.security.aop;

import java.util.ArrayList;
import java.util.List;

import no.ask.medical.exception.PEPException;
import no.ask.medical.security.annotations.PEP;
import no.ask.xacml.util.XACMLCommunication;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Aspect
public class XACMLPEPHandler {

	private static final Logger log = LoggerFactory.getLogger(XACMLPEPHandler.class);

	@Value("${xacml.env}")
	private String environment;

	@Autowired
	private XACMLCommunication xacml;

	@Before("execution(* *(..)) && @annotation(pep)")
	public void pep(JoinPoint jp, PEP pep) throws Throwable {
		Object[] args = jp.getArgs();
		CodeSignature signature = (CodeSignature) jp.getSignature();
		String[] parameterNames = signature.getParameterNames();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		ArrayList<String> actions = new ArrayList<String>();
		List<String> decisonResults = new ArrayList<String>();

		actions.add(pep.action());
		String resource = (parameterNames.length > 0 && parameterNames[0].contains("id")) ? args[0] + "" : jp.getSignature().toShortString();
		decisonResults = xacml.getDecisionResults(auth.getName(), actions, environment, resource);

		log.info(auth.getName() + " " + actions + " " + environment + " " + resource + " " +decisonResults.toString());
		decsion(decisonResults);

	}

	private void decsion(List<String> decisonResults) {
		if (!decisonResults.isEmpty() && !decisonResults.get(0).equals(XACMLCommunication.RESULT_PERMIT)) {
			throw new PEPException("Xacml respons is " + decisonResults.toString());
		}
	}
}
