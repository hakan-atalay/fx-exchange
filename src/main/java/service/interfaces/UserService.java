package service.interfaces;

import dto.request.UserUpdateDTO;
import dto.response.UserResponseDTO;

import java.util.List;

public interface UserService {
	
	UserResponseDTO getUser(Long userId);

	List<UserResponseDTO> getAllUsers();

	UserResponseDTO updateUser(Long userId, UserUpdateDTO updateDTO);

	void deleteUser(Long userId);
}