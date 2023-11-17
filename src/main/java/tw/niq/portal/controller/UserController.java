package tw.niq.portal.controller;

import java.util.Collection;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tw.niq.portal.model.UserModel;
import tw.niq.portal.service.UserService;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(UserController.PATH)
public class UserController {

	public static final String PATH = "/users";
	public static final String TEMPLATES_ROOT = "users/";
	
	private final UserService userService;
	private final RedisTemplate<String, String> redisTemplate;
	
	@GetMapping
	public String getUsers(
			@AuthenticationPrincipal UserModel authUser, 
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "25") int limit, 
			Model model) {
		String authorization = redisTemplate.opsForValue().get(authUser.getUserId());
		Collection<UserModel> users = userService.getAll(authorization, page, limit);
		model.addAttribute("users", users);
		
		return TEMPLATES_ROOT + "list";
	}
	
	@GetMapping("/{userId}")
	public String getUser(
			@AuthenticationPrincipal UserModel authUser, 
			@PathVariable("userId") String userId, 
			Model model) {
		UserModel userModel;
		if (userId.equals("create")) {
			// Create
			userModel = UserModel.builder()
					.accountNonExpired(true)
					.accountNonLocked(true)
					.credentialsNonExpired(true)
					.enabled(true)
					.build();
		} else {
			// Update
			String authorization = redisTemplate.opsForValue().get(authUser.getUserId());
			userModel = userService.getByUserId(authorization, userId);
		}
		model.addAttribute("user", userModel);
		return TEMPLATES_ROOT + "details";
	}
	
//	@PostMapping
//	public String createOrUpdateUser(
//			@Valid @ModelAttribute("user") UserModel userModel, 
//			BindingResult bindingResult, 
//			Model model) {
//		
//		if(bindingResult.hasErrors()) {
//			model.addAttribute("user", userModel);
//			return TEMPLATES_ROOT + "details";
//		}
//		
//		UserDto userDto = userMapper.toUserDto(userModel, context);
//		userService.createOrUpdate(userDto);
//		return "redirect:" + PATH;
//	}
//
//	@GetMapping("/delete/{userId}")
//	public String deleteUser(@PathVariable("userId") String userId) {
//		userService.delete(userId);
//		return "redirect:" + PATH;
//	}
	
}
