package tw.niq.portal.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import tw.niq.portal.model.UserLoginModel;

@FeignClient(name = "loginClient", url = "https://api.niq.tw")
public interface LoginClient {
	
	org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LoginClient.class);
	
	@PostMapping("/login")
	@Retry(name = "loginClient")
	@CircuitBreaker(name = "loginClient", fallbackMethod = "loginFallback")
	public ResponseEntity<Void> login(UserLoginModel userModel);

	default ResponseEntity<Void> loginFallback(UserLoginModel userModel, Throwable exception) {
		log.debug("LoginClient - loginFallback()");
		log.debug("LoginClient - loginFallback():" + userModel.toString());
		log.debug("LoginClient - loginFallback():" + exception.toString());
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("AuthenticationException", exception.toString());
		return new ResponseEntity<Void>(httpHeaders, HttpStatus.UNAUTHORIZED);
	}
	
}
