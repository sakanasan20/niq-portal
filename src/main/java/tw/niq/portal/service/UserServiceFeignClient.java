package tw.niq.portal.service;

import java.util.Set;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tw.niq.portal.client.UserClient;
import tw.niq.portal.model.UserModel;

@Primary
@RequiredArgsConstructor
@Service
public class UserServiceFeignClient implements UserService {
	
	private final UserClient userClient;

	@Override
	public UserModel getByUserId(String authorization, String userId) {
		return userClient.getByUserId(authorization, userId);
	}

	@Override
	public Set<UserModel> getAll(String authorization, int page, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

}