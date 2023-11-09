package tw.niq.portal.security;

import java.io.IOException;

import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	public CustomAuthenticationFailureHandler(String defaultFailureUrl) {
		super(defaultFailureUrl);
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		log.debug("CustomAuthenticationFailureHandler - onAuthenticationFailure()");
		log.debug("CustomAuthenticationFailureHandler - onAuthenticationFailure() exception instanceof CredentialsExpiredException:" + (exception instanceof CredentialsExpiredException));
		if (exception instanceof CredentialsExpiredException) {
			response.sendRedirect("/changePassword");
			return;
		} else {
			super.onAuthenticationFailure(request, response, exception);
		}
	}

}
