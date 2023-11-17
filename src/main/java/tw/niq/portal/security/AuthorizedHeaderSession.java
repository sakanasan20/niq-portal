package tw.niq.portal.security;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@Scope("session")
public class AuthorizedHeaderSession {
	
	private String userId;
	
	private String authorization;

}
