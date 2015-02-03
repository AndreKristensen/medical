package no.ask.medical.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@ImportResource(value = { "application.properties" })
public class ApplicationConfiguration {

	
	@Value("mysql.driver")
	private String driver;
	@Value("mysql.url")
	private String url;
	@Value("mysql.pw")
	private String pw;
	@Value("mysql.username")
	private String username;
	
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
	    driverManagerDataSource.setUsername(username);
	    driverManagerDataSource.setPassword(pw);
	    return driverManagerDataSource;
	}
 
}