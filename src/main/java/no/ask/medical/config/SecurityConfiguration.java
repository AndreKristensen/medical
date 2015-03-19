package no.ask.medical.config;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.sql.DataSource;

import no.ask.medical.security.aop.XACMLPEPHandler;
import no.ask.medical.security.aop.XACMLPEPHandlerWSO2IS;
import no.ask.medical.security.filter.PEPFilter;
import no.ask.medical.security.filter.PEPFilterWSO2;
import no.ask.medical.security.pip.SampleAttributeFinderModule;
import no.ask.xacml.util.XACMLCommunication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.wso2.balana.Balana;
import org.wso2.balana.PDP;
import org.wso2.balana.PDPConfig;
import org.wso2.balana.finder.AttributeFinder;
import org.wso2.balana.finder.AttributeFinderModule;
import org.wso2.balana.finder.impl.FileBasedPolicyFinderModule;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = { "no.ask.medical.secuirty.aop"})
@EnableAspectJAutoProxy
@PropertySource(value = "classpath:/application.properties")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

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
	
	@Autowired
	private DataSource dataSource;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
		.jdbcAuthentication()
			.dataSource(dataSource)
				.usersByUsernameQuery("select username, password, enabled from users where username = ?")
				.authoritiesByUsernameQuery("select username, role from user_roles where username = ?");

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
			.authorizeRequests()
				.anyRequest().authenticated()
				.and()
					.httpBasic()
				.and()
					.addFilterBefore(pepFilter(), ExceptionTranslationFilter.class)
					.logout().deleteCookies("jsessionid");

	}

	@Bean
	public Filter pepFilter() {
		return new PEPFilterWSO2();
	}
	
	@Bean
	public XACMLCommunication xacml() {
		return new XACMLCommunication(hostName, port, username, password, trustStoreFileURL, trustStorPassowd);
	}

//	@Bean
//	public XACMLPEPHandler handler() {
//		return new XACMLPEPHandler();
//	}
	
	@Bean
	public XACMLPEPHandlerWSO2IS handlerWSO2IS() {
		return new XACMLPEPHandlerWSO2IS();
	}
	
	private Balana balana() {

		try {
			// using file based policy repository. so set the policy location as
			// system property
			String policyLocation = (new File(".")).getCanonicalPath() + File.separator + "src"+File.separator+"main"+File.separator+"resources"+ File.separator +"policy";
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
	@Bean
	public PDP pdp() {

		PDPConfig pdpConfig = balana().getPdpConfig();

		// registering new attribute finder. so default PDPConfig is needed to
		// change
		AttributeFinder attributeFinder = pdpConfig.getAttributeFinder();

		List<AttributeFinderModule> finderModules = attributeFinder.getModules();
		finderModules.add(new SampleAttributeFinderModule());
		//
		attributeFinder.setModules(finderModules);

		return new PDP(new PDPConfig(attributeFinder, pdpConfig.getPolicyFinder(), null, true));
	}

}
