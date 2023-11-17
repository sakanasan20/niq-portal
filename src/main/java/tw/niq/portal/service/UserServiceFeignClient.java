package tw.niq.portal.service;

import java.util.Set;

import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tw.niq.portal.client.UserClient;
import tw.niq.portal.model.UserModel;

@Primary
@RequiredArgsConstructor
@Service
public class UserServiceFeignClient implements UserService {
	
	private final UserClient userClient;
	private final RedisTemplate<String, String> redisTemplate;
	
	@Override
	public UserModel getByUserId(String userId) {
		String authorization = redisTemplate.opsForValue().get(userId);
		return userClient.getByUserId(authorization, userId);
	}

	@Override
	public UserModel getByUserId(String authorization, String userId) {
		return userClient.getByUserId(authorization, userId);
	}

	@Override
	public Set<UserModel> getAll(String authorization, int page, int limit) {
		return userClient.getAll(authorization, page, limit);
	}

}
