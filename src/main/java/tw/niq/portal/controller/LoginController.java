package tw.niq.portal.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;
import tw.niq.portal.model.UserModel;

@RequiredArgsConstructor
@Controller
public class LoginController {
	
	private final SessionRegistry sessionRegistry;

	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/loggedIn")
	public String loggedIn(Model model) {
		List<UserModel> users = sessionRegistry.getAllPrincipals().stream().map(principal -> (UserModel) principal).collect(Collectors.toList());
		model.addAttribute("users", users);
		return "loggedIn";
	}
	
	@GetMapping("/manualLogout/{userId}")
	public String manualLogout(@PathVariable("userId") String userId) {
		
		List<SessionInformation> sessionInformations = sessionRegistry.getAllPrincipals().stream()
				.filter(principal -> principal.toString().contains(userId))
				.map(principal -> sessionRegistry.getAllSessions(principal, false))
				.flatMap(List::stream)
				.collect(Collectors.toList());
		
		for (SessionInformation sessionInformation : sessionInformations) {
			sessionInformation.expireNow();
		}
		
		return "redirect:/loggedIn";
	}

	@GetMapping("/changePasswoed")
	public String changePasswoed() {
		return "changePasswoed";
	}
	
}
