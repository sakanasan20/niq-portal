package tw.niq.portal.service;

import org.springframework.http.ResponseEntity;

import tw.niq.portal.model.UserLoginModel;

public interface LoginService {
	
	ResponseEntity<Void> login(UserLoginModel userModel);

}
