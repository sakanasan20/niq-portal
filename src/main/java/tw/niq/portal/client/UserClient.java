package tw.niq.portal.client;

import java.util.HashSet;
import java.util.Set;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import feign.Headers;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import tw.niq.portal.model.UserModel;

@Headers("x-requester-id: {requester}")
@FeignClient(name = "userClient", url = "https://api.niq.tw")
public interface UserClient {
	
	@GetMapping("/api/v1/users")
	@Retry(name = "userClient")
	@CircuitBreaker(name = "userClient", fallbackMethod = "getAllFallback")
	public Set<UserModel> getAll(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader, 
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "25") int limit);
	
	@GetMapping("/api/v1/users/{userId}")
	@Retry(name = "userClient")
	@CircuitBreaker(name = "userClient", fallbackMethod = "getByUserId")
	public UserModel getByUserId(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader, 
			@PathVariable("userId") String userId);
	
	default Set<UserModel> getAllFallback(String authorizationHeader, int page, int limit, Throwable exception) {
		System.out.println("page: " + page);
		System.out.println("limit: " + limit);
		System.out.println(exception.getMessage());
		return new HashSet<>();
	}

	default UserModel getByUserId(String authorizationHeader, String userId, Throwable exception) {
		System.out.println("authorizationHeader: " + authorizationHeader);
		System.out.println("userId: " + userId);
		System.out.println(exception.getMessage());
		return UserModel.builder().build();
	}
	
}
