package dto.auth;

import dto.response.UserResponseDTO;

public class LoginResponseDTO {

    private UserResponseDTO user;

    public LoginResponseDTO() {}

    public LoginResponseDTO(UserResponseDTO user) {
        this.user = user;
    }

    public UserResponseDTO getUser() { return user; }
    public void setUser(UserResponseDTO user) { this.user = user; }
}
