package tw.niq.portal.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ClientRequestInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) {
		log.info("ClientRequestInterceptor - apply()");
	}

}
