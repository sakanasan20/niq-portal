package tw.niq.portal.listener;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tw.niq.portal.model.UserModel;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthenticationSuccessListener {

	@EventListener
	public void listen(AuthenticationSuccessEvent event) {
		
		if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getSource();
			log.debug(token.toString());
			
			if (token.getPrincipal() instanceof UserModel) {
				UserModel user = (UserModel) token.getPrincipal();
				log.debug("User logged in: " + user.getUsername());
			}
			
			if (token.getDetails() instanceof WebAuthenticationDetails) {
				WebAuthenticationDetails details = (WebAuthenticationDetails) token.getDetails();
				log.debug("Remote Address: " + details.getRemoteAddress());
			}
		}
	}

}
