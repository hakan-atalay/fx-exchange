package service;

import dao.UserDAO;
import dto.request.UserUpdateDTO;
import dto.response.UserResponseDTO;
import entity.User;
import exception.ServiceException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import mapper.UserMapper;
import service.interfaces.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserServiceImpl implements UserService{

    @Inject
    private UserDAO userDAO;

    public UserResponseDTO getUser(Long userId) {
        validateUserId(userId);
        Optional<User> user = userDAO.findById(userId);
        if (user.isEmpty()) throw new ServiceException("User not found");
        return UserMapper.toResponse(user.get());
    }

    public List<UserResponseDTO> getAllUsers() {
        return userDAO.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponseDTO updateUser(Long userId, UserUpdateDTO updateDTO) {
        validateUserId(userId);
        Optional<User> optionalUser = userDAO.findById(userId);
        if (optionalUser.isEmpty()) throw new ServiceException("User not found");

        User user = optionalUser.get();
        if (updateDTO.getFirstName() != null) user.setFirstName(updateDTO.getFirstName());
        if (updateDTO.getLastName() != null) user.setLastName(updateDTO.getLastName());
        if (updateDTO.getRole() != null) user.setRole(updateDTO.getRole());
        if (updateDTO.getStatus() != null) user.setStatus(updateDTO.getStatus());

        userDAO.update(user);
        return UserMapper.toResponse(user);
    }

    public void deleteUser(Long userId) {
        validateUserId(userId);
        userDAO.delete(userId);
    }

    private void validateUserId(Long userId) {
        if (userId == null || userId <= 0)
            throw new ServiceException("Invalid userId");
    }
}
