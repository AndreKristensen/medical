package no.ask.medical.config;

import javax.sql.DataSource;

import no.ask.medical.security.filter.PEPFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.ExceptionTranslationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
		.dataSource(dataSource)
			.usersByUsernameQuery("select username, password, enabled from users where username = ?")
			.authoritiesByUsernameQuery("select username, role from user_roles where username = ?");
		
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
		.authorizeRequests()
			.anyRequest()
			.authenticated()
			.and()
				.httpBasic()
				.and()
					.addFilterBefore(pepFilter(), ExceptionTranslationFilter.class)
				.logout().deleteCookies("jsessionid");

	}
	
	@Bean
	public PEPFilter pepFilter (){
		return new PEPFilter();
	}

}
