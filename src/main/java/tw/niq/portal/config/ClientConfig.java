package tw.niq.portal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tw.niq.portal.interceptor.ClientRequestInterceptor;

@Configuration
public class ClientConfig {
	
	@Bean
	public ClientRequestInterceptor clientRequestInterceptor() {
		return new ClientRequestInterceptor();
	}
	
}
