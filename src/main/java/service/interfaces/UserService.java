package service.interfaces;

import dto.response.UserResponseDTO;

import java.util.List;

public interface UserService {
	
	UserResponseDTO getUser(Long userId);

	List<UserResponseDTO> getAllUsers();

	UserResponseDTO updateUser(Long userId, String firstName, String lastName, String role, String status);

	void deleteUser(Long userId);
}