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
/**
 * 
 * @author Andre
 *
 */
@Aspect
public class CopyOfXACMLPEPHandler {

	private static final Logger log = LoggerFactory.getLogger(CopyOfXACMLPEPHandler.class);

	@Value("${xacml.env}")
	private String environment;

	@Autowired
	private XACMLCommunication xacml;

	/**
	 * Method that intercepts all the methods with  the @PEP annotation before executing. 
	 * The method checks if the methods arguments contains an resources id and uses it in the
	 * XACML request. It checks if the user have the right to execute the method. A XACML 
	 * request is sent to the PDP and if the response is denied the 
	 * method throws an {@link PEPException}, the method terminates and the user is not allowed 
	 * to get the resources. If the XACML request gets permit the method is continued.
	 * @param jp
	 * @param pep
	 * @throws Throwable
	 */
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
		if (!decisonResults.isEmpty() && !decisonResults.get(0).equals(XACMLCommunication.RESULT_PERMIT)) {
			throw new PEPException("XACML respons is " + decisonResults.toString());
		}
	}
}
