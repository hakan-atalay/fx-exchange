package service.interfaces;

import dto.auth.LoginRequestDTO;
import dto.auth.LoginResponseDTO;
import dto.auth.RegisterRequestDTO;
import dto.response.UserResponseDTO;

public interface AuthService {
	
    LoginResponseDTO login(LoginRequestDTO request, String ipAddress);
    
    UserResponseDTO register(RegisterRequestDTO request);
}
