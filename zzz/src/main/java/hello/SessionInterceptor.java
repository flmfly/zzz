package hello;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

public class SessionInterceptor implements HandlerInterceptor {

	private StringRedisTemplate stringRedisTemplate;

	public SessionInterceptor(StringRedisTemplate template) {
		this.stringRedisTemplate = template;
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		return this.stringRedisTemplate.hasKey(request.getHeader("SessionKey"));
	}

}
