package tw.niq.portal.service;

import java.util.Set;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import tw.niq.portal.model.UserModel;

@RequiredArgsConstructor
@Service
public class UserServiceRestTemplate implements UserService {

	private final RestTemplate restTemplate;

	@Override
	public Set<UserModel> getAll(String authorization, int page, int limit) {
		
		Set<UserModel> users;
		
		String url = String.format("http://localhost:8080/api/v1/users?page=%s&limit=%s", page, limit);
		ResponseEntity<Set<UserModel>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Set<UserModel>>(){});
		users = response.getBody();
		
		return users;
	}

	@Override
	public UserModel getByUserId(String authorization, String userId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public UserModel getByUserId(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

}
