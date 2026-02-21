package security;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class SecurityHeadersFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletResponse res = (HttpServletResponse) response;

		res.setHeader("X-Content-Type-Options", "nosniff");
		res.setHeader("X-Frame-Options", "DENY");
		res.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
		res.setHeader("X-XSS-Protection", "1; mode=block");

		res.setHeader("Content-Security-Policy", "default-src 'self'; " +

				"script-src 'self' 'unsafe-inline' 'unsafe-eval' "
				+ "https://cdn.jsdelivr.net https://cdnjs.cloudflare.com https://code.jquery.com; " +

				"style-src 'self' 'unsafe-inline' "
				+ "https://cdn.jsdelivr.net https://cdnjs.cloudflare.com https://fonts.googleapis.com; " +

				"font-src 'self' data: "
				+ "https://cdn.jsdelivr.net https://cdnjs.cloudflare.com https://fonts.gstatic.com; " +

				"img-src 'self' data: blob:; " +

				"connect-src 'self' ws: wss:;");

		chain.doFilter(request, response);
	}
}