package no.ask.medical.aop;

import java.util.ArrayList;
import java.util.List;

import no.ask.xacml.util.XACMLCommunication;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class XACMLPEPHandler {

	private static final Logger log = LoggerFactory.getLogger(XACMLPEPHandler.class);
	
	@Autowired
	private XACMLCommunication xacml;

	@Before("execution(public * *(..)) && @annotation(pep)")
	public void saveAPIEvent(JoinPoint pjp, PEP pep) throws Throwable {
		Object[] args = pjp.getArgs();
	
		if(args.length >= 1){
			 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			 ArrayList<String> actions = new ArrayList<String>();
			 actions.add(pep.action());
			 
			String environment ="medical";
			
			List<String> decisonResults = xacml.getDecisonResults(auth.getName(),actions, environment ,"" + args[0]);
			
//			if(!decisonResults.isEmpty() && !decisonResults.get(0).equals(XACMLCommunication.RESULT_PERMIT)){
//				throw new PEPException("Xacml respons is " + decisonResults.toString());
//			}
			
			log.info(auth.getName(),actions, environment , args[0]);
		}
		
	}
}
