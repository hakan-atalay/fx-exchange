package dto.auth;

import dto.response.UserResponseDTO;

public class LoginResponseDTO {

    private UserResponseDTO user;
    private String sessionToken;

    public LoginResponseDTO() {}

    public LoginResponseDTO(UserResponseDTO user) {
        this.user = user;
    }

    public LoginResponseDTO(UserResponseDTO user, String sessionToken) {
        this.user = user;
        this.sessionToken = sessionToken;
    }

    public UserResponseDTO getUser() {
        return user;
    }

    public void setUser(UserResponseDTO user) {
        this.user = user;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}
