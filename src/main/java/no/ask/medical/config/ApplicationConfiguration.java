package no.ask.medical.config;

import no.ask.medical.security.aop.XACMLPEPHandler;
import no.ask.medical.service.MedicalService;
//import no.ask.xacml.util.XACMLCommunication;
import no.ask.xacml.util.XACMLCommunication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

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
	public MedicalService medicalService() {
		return new MedicalService();
	}

	@Bean
	public XACMLCommunication xacml() {
		return new XACMLCommunication(hostName, port, username, password, trustStoreFileURL, trustStorPassowd);
	}

	@Bean
	public XACMLPEPHandler handler() {
		return new XACMLPEPHandler();
	}

}