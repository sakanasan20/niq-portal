package tw.niq.portal.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
public class SecurityBean {
	
	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
	    return new HttpSessionEventPublisher();
	}
	
	@Bean
	public SessionRegistry sessionRegistry() {
	    return new SessionRegistryImpl();
	}
	
	@Bean
	public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
	    RedisTemplate<String, String> template = new RedisTemplate<>();
	    template.setConnectionFactory(connectionFactory);
	    // Add some specific configuration here. Key serializers, etc.
	    return template;
	}
	
	@Bean
	public CustomLogoutHandler customLogoutHandler(RedisConnectionFactory connectionFactory) {
		return new CustomLogoutHandler(redisTemplate(connectionFactory));
	}
	
}
