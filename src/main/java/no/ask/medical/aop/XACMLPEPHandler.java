package no.ask.medical.aop;

import java.util.ArrayList;
import java.util.List;

import no.ask.medical.annotations.PEP;
import no.ask.xacml.util.XACMLCommunication;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Aspect
public class XACMLPEPHandler {

	// private static final Logger log =
	// LoggerFactory.getLogger(XACMLPEPHandler.class);

	@Autowired
	private Environment env;

	@Autowired
	private XACMLCommunication xacml;

	@Before("execution(* *(..)) && @annotation(pep)")
	public void pep(JoinPoint jp, PEP pep) throws Throwable {
		Object[] args = jp.getArgs();

		if (args.length >= 1) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			ArrayList<String> actions = new ArrayList<String>();
			for (String action : pep.actions()) {
				actions.add(action);
			}

			String environment = env.getProperty("xacml.env");

			List<String> decisonResults = xacml.getDecisonResults(auth.getName(), actions, environment, "" + args[0]);

			 if(!decisonResults.isEmpty() &&
			 !decisonResults.get(0).equals(XACMLCommunication.RESULT_PERMIT)){
			// throw new PEPException("Xacml respons is " +
			// decisonResults.toString());
			 }

			System.out.println(auth.getName() + " " + actions + " " + environment + " " + args[0]);
		}

	}
}
