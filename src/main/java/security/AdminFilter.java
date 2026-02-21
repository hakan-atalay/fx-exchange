package security;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/pages/admin/*")
public class AdminFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		HttpSession session = req.getSession(false);

		if (session == null) {
			res.sendRedirect(req.getContextPath() + "/login.xhtml");
			return;
		}

		Object roleObj = session.getAttribute("role");

		if (roleObj == null || !"ADMIN".equals(roleObj.toString())) {
			res.sendRedirect(req.getContextPath() + "/pages/home.xhtml");
			return;
		}

		chain.doFilter(request, response);
	}
}