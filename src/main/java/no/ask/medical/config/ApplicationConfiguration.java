package no.ask.medical.config;

import no.ask.medical.service.PatientService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
//import no.ask.xacml.util.XACMLCommunication;

@Configuration
@ComponentScan(basePackages = { "no.ask.medical.serivce" })
public class ApplicationConfiguration {

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
		return ppc;
	}

	@Bean
	public PatientService medicalService() {
		return new PatientService();
	}

}