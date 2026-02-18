package mapper;

import dto.response.UserResponseDTO;
import entity.User;

public class UserMapper {

    private UserMapper() {}

    public static UserResponseDTO toResponse(User user) {
        if (user == null) return null;

        return new UserResponseDTO(
        	    user.getId(),
        	    user.getUserName(),
        	    user.getEmail(),
        	    user.getFirstName(),
        	    user.getLastName(),
        	    user.getRole(),
        	    user.getStatus(),
        	    user.getCreatedAt()
        );

    }
}
