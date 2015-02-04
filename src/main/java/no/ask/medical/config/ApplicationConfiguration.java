package no.ask.medical.config;

import no.ask.medical.aop.XACMLPEPHandler;
import no.ask.medical.service.MedicalService;
import no.ask.xacml.util.XACMLCommunication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = {"no.ask.medical.aop","no.ask.medical.serivce"})
@EnableAspectJAutoProxy
public class ApplicationConfiguration {

	@Value("${mysql.driver}")
	private String driver;
	@Value("${mysql.url}")
	private String url;
	@Value("${mysql.pw}")
	private String pw;
	
	@Value("${mysql.username}")
	private String mysqlUsername;
	
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

	@Bean(name = "dataSource")
	public DriverManagerDataSource dataSource() {
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		driverManagerDataSource.setDriverClassName(driver);
		driverManagerDataSource.setUrl(url);
		driverManagerDataSource.setUsername(mysqlUsername);
		driverManagerDataSource.setPassword(pw);
		return driverManagerDataSource;
	}

	@Bean
	public MedicalService medicalService(){
		return new MedicalService();
	}
	
	
	@Bean
	public XACMLCommunication xacml (){
		return new XACMLCommunication(hostName, port, username, password, trustStoreFileURL, trustStorPassowd);
	}
	
	@Bean
	public XACMLPEPHandler  handler(){
		return new XACMLPEPHandler();
	}
	
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource());
	}
}