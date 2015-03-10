package no.ask.medical.config;

import java.io.File;
import java.io.IOException;
import java.util.List;

import no.ask.medical.security.aop.XACMLPEPHandler;
import no.ask.medical.service.PatientService;
import no.ask.pip.medical.MedicalJDBCAttributeFinder;
//import no.ask.xacml.util.XACMLCommunication;
import no.ask.xacml.util.XACMLCommunication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.wso2.balana.Balana;
import org.wso2.balana.PDP;
import org.wso2.balana.PDPConfig;
import org.wso2.balana.finder.AttributeFinder;
import org.wso2.balana.finder.AttributeFinderModule;
import org.wso2.balana.finder.impl.FileBasedPolicyFinderModule;

@Configuration
@ComponentScan(basePackages = { "no.ask.medical.secuirty.aop", "no.ask.medical.serivce" })
@EnableAspectJAutoProxy
@PropertySource(value = "classpath:/application.properties")
public class ApplicationConfiguration {

	@Value("${xacml.hostName}")
	private String hostName;

	@Value("${xacml.port}")
	private String port;

	@Value("${xacml.username}")
	private String username;

	@Value("${xacml.password}")
	private String password;

	@Value("${xacml.trustStoreFileURL}")
	private String trustStoreFileURL;

	@Value("${xacml.trustStorPassowd}")
	private String trustStorPassowd;

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
		return ppc;
	}

	@Bean
	public PatientService medicalService() {
		return new PatientService();
	}

	@Bean
	public XACMLCommunication xacml() {
		return new XACMLCommunication(hostName, port, username, password, trustStoreFileURL, trustStorPassowd);
	}

	@Bean
	public XACMLPEPHandler handler() {
		return new XACMLPEPHandler();
	}

	private Balana balana() {

		try {
			// using file based policy repository. so set the policy location as
			// system property
			String policyLocation = (new File(".")).getCanonicalPath() + File.separator + "resources";
			System.setProperty(FileBasedPolicyFinderModule.POLICY_DIR_PROPERTY, policyLocation);
		} catch (IOException e) {
			System.err.println("Can not locate policy repository");
		}
		// create default instance of Balana
		return Balana.getInstance();
	}

	/**
	 * Returns a new PDP instance with new XACML policies
	 *
	 * @return a PDP instance
	 */
	private PDP getPDPNewInstance() {

		PDPConfig pdpConfig = balana().getPdpConfig();

		// registering new attribute finder. so default PDPConfig is needed to
		// change
		AttributeFinder attributeFinder = pdpConfig.getAttributeFinder();

		List<AttributeFinderModule> finderModules = attributeFinder.getModules();
//		attributeFinder.add(new MedicalJDBCAttributeFinder());
//		finderModules.add(new SampleAttributeFinderModule());
//		
		attributeFinder.setModules(finderModules);

		return new PDP(new PDPConfig(attributeFinder, pdpConfig.getPolicyFinder(), null, true));
	}

}