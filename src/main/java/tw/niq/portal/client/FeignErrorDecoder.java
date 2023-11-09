package tw.niq.portal.client;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

	@Override
	public Exception decode(String methodKey, Response response) {
		
		log.debug("FeignErrorDecoder - decode");
		log.debug("methodKey: " + methodKey);
		log.debug("response.status(): " + response.status());
		String authenticationException = "";
		Collection<String> authenticationExceptions = response.headers().get("AuthenticationException");
		if (!authenticationExceptions.isEmpty()) {
			authenticationExceptions.forEach(e -> log.debug(e));
			authenticationException = authenticationExceptions.stream().collect(Collectors.toList()).get(0);
		}
		
		switch (response.status()) {
		
			case 401:
				if (methodKey.contains("login")) {
					if (authenticationException.contains("LockedException") 
							|| authenticationException.contains("DisabledException")
							|| authenticationException.contains("AccountExpiredException")
							|| authenticationException.contains("CredentialsExpiredException")
							|| authenticationException.contains("BadCredentialsException")) {
						return new ResponseStatusException(HttpStatus.valueOf(response.status()), authenticationException);
					} else {
						return new ResponseStatusException(HttpStatus.valueOf(response.status()), "Login Failed");
					}
				}
				break;
		
			case 403:
				if (methodKey.contains("login")) {
					return new ResponseStatusException(HttpStatus.valueOf(response.status()), "Login failed");
				}
				break;
				
			case 404:
				if (methodKey.contains("login")) {
					return new ResponseStatusException(HttpStatus.valueOf(response.status()), "Users account not found");
				}
				break;
				
			default:
				return new Exception(response.reason());
		}
		
		return new Exception(response.reason());
	}

}
