package tw.niq.portal.listener;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthenticationFailureListener {
	
	@EventListener
	public void listen(AuthenticationFailureBadCredentialsEvent event) {
		
		if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getSource();
			log.info("AuthenticationFailureListener - listen()");
			log.info("token: " + token);
		}
	}


}
