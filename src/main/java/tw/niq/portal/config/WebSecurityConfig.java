package tw.niq.portal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import lombok.RequiredArgsConstructor;
import tw.niq.portal.security.CustomAuthenticationProvider;
import tw.niq.portal.service.LoginService;
import tw.niq.portal.service.UserService;

@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

	private final UserService userService;
	private final LoginService loginService;
	private final SessionRegistry sessionRegistry;
	
	@Bean
	protected SecurityFilterChain configure(HttpSecurity http, 
			HandlerMappingIntrospector introspector) throws Exception {
		
		MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
		
		AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.eraseCredentials(false);
		authenticationManagerBuilder.authenticationProvider(new CustomAuthenticationProvider(loginService, userService));
		AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
		
		http
				.authenticationManager(authenticationManager)
				.sessionManagement(sessionManagement -> sessionManagement
						.maximumSessions(-1)
					    .sessionRegistry(sessionRegistry)
					    .expiredUrl("/login?error")) 
				.headers(headersConfigurer -> headersConfigurer
						.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
				.authorizeHttpRequests((authorize) -> authorize
						.requestMatchers(
								mvcMatcherBuilder.pattern("/webjars/**"), 
								mvcMatcherBuilder.pattern("/resources/**"), 
								mvcMatcherBuilder.pattern("/css/**")
								).permitAll()
						.anyRequest().authenticated())
				.formLogin(formLogin -> formLogin
						.loginProcessingUrl("/login")
						.loginPage("/login").permitAll()
						.successForwardUrl("/")
						.defaultSuccessUrl("/")
						.failureUrl("/login?error"))
				.logout(logout -> logout
						.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
						.logoutSuccessUrl("/login?logout").permitAll())
				.httpBasic(Customizer.withDefaults());

		return http.build();
	}
	
}
