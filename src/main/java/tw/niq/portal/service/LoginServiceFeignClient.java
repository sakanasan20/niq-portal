package tw.niq.portal.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tw.niq.portal.client.LoginClient;
import tw.niq.portal.model.UserLoginModel;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoginServiceFeignClient implements LoginService {
	
	private final LoginClient loginClient;

	@Override
	public ResponseEntity<Void> login(UserLoginModel userModel) {
		log.debug("LoginServiceFeignClient - login()");
		return loginClient.login(userModel);
	}

}
