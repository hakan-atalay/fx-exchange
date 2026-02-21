package bean;

import dto.auth.LoginRequestDTO;
import dto.auth.LoginResponseDTO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpSession;
import service.interfaces.AuthService;

@Named
@RequestScoped
public class LoginBean {

	private String email;
	private String password;

	@Inject
	private AuthService authService;

	public String login() {

		try {
			LoginRequestDTO request = new LoginRequestDTO(email, password);
			LoginResponseDTO response = authService.login(request, getClientIp());

			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();

			HttpSession oldSession = (HttpSession) ec.getSession(false);
			if (oldSession != null) {
				oldSession.invalidate();
			}

			HttpSession session = (HttpSession) ec.getSession(true);

			session.setAttribute("user", response.getUser());
			session.setAttribute("role", response.getUser().getRole());

			return "/pages/home.xhtml?faces-redirect=true";

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			return null;
		}
	}

	public String logout() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.invalidateSession();
		return "/login.xhtml?faces-redirect=true";
	}

	private String getClientIp() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		return ec.getRequestHeaderMap().getOrDefault("X-FORWARDED-FOR", "unknown");
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}