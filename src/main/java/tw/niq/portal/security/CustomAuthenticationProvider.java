package tw.niq.portal.security;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tw.niq.portal.model.AuthorityModel;
import tw.niq.portal.model.RoleModel;
import tw.niq.portal.model.UserLoginModel;
import tw.niq.portal.model.UserModel;
import tw.niq.portal.service.LoginService;
import tw.niq.portal.service.UserService;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
		
	private final LoginService loginService;
	private final UserService userService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		log.debug("CustomAuthenticationProvider - authenticate()");
		
		UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication;
		
		String username = authToken.getName();
		String password = authToken.getCredentials().toString();

		ResponseEntity<Void> responseEntity = loginService.login(UserLoginModel.builder().username(username).password(password).build());
		String authorization = responseEntity.getHeaders().getFirst("Authorization");
		String userId = responseEntity.getHeaders().getFirst("UserId");
		String authenticationException = responseEntity.getHeaders().getFirst("AuthenticationException");
		
		if (!(authorization != null && !authorization.isBlank() && authorization.startsWith("Bearer "))) {
			log.debug("CustomAuthenticationProvider - authenticate() authenticationException:" + authenticationException);
			if (authenticationException != null) {
				if (authenticationException.contains("LockedException")) {
					throw new LockedException("Locked");
				}
				if (authenticationException.contains("DisabledException")) {
					throw new DisabledException("Disabled");
				}
				if (authenticationException.contains("AccountExpiredException")) {
					throw new AccountExpiredException("Account Expired");
				}
				if (authenticationException.contains("CredentialsExpiredException")) {
					throw new CredentialsExpiredException("Credentials Expired");
				}
				if (authenticationException.contains("BadCredentialsException")) {
					throw new BadCredentialsException("Bad Credentials");
				}
			}
			throw new AuthenticationServiceException("Login Failed");
		}

		UserModel userModel = userService.getByUserId(authorization, userId);

		Object principalToReturn = userModel;
		
		return createSuccessAuthentication(principalToReturn, authentication, userModel);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
	protected Authentication createSuccessAuthentication(Object principal, 
			Authentication authentication, UserModel user) {
		
		Set<SimpleGrantedAuthority> roles = user.getRoles().stream()
		.map(role -> String.format("ROLE_%s", role.getName()))
		.map(SimpleGrantedAuthority::new)
		.collect(Collectors.toSet());

		Set<SimpleGrantedAuthority> authorities = user.getRoles().stream()
				.map(RoleModel::getAuthorities)
				.flatMap(Set::stream)
				.map(AuthorityModel::getName)
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toSet());
		
		Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();
		grantedAuthorities.addAll(roles);
		grantedAuthorities.addAll(authorities);

		UsernamePasswordAuthenticationToken result = 
				UsernamePasswordAuthenticationToken.authenticated(principal, authentication.getCredentials().toString(), grantedAuthorities);

		result.setDetails(authentication.getDetails());

		return result;
	}

}
