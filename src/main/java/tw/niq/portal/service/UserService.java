package tw.niq.portal.service;

import java.util.Set;

import tw.niq.portal.model.UserModel;

public interface UserService {

	Set<UserModel> getAll(String authorization, int page, int limit);
	
	UserModel getByUserId(String authorization, String userId);
	
}
