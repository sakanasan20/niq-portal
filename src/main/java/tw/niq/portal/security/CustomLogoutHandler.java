package tw.niq.portal.security;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tw.niq.portal.model.UserModel;

@Slf4j
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {
	
	private final RedisTemplate<String, String> redisTemplate;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

		String userId = ((UserModel) authentication.getPrincipal()).getUserId();
		
		redisTemplate.delete(userId);
		
		log.debug("Token delete: " + userId);
	}

}
