package service;

import dao.LoginHistoryDAO;
import dao.UserDAO;
import dto.auth.LoginRequestDTO;
import dto.auth.LoginResponseDTO;
import dto.auth.RegisterRequestDTO;
import dto.response.UserResponseDTO;
import entity.LoginHistory;
import entity.User;
import entity.enums.Role;
import exception.DAOException;
import exception.ServiceException;
import infrastructure.redis.LoginAttemptRateLimiter;
import infrastructure.redis.UserLoginCacheService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import at.favre.lib.crypto.bcrypt.BCrypt;
import mapper.UserMapper;
import service.interfaces.AuthService;

import java.util.Optional;

@ApplicationScoped
public class AuthServiceImpl implements AuthService {

	@Inject
	private UserDAO userDAO;

	@Inject
	private UserLoginCacheService userCacheService;

	@Inject
	private LoginHistoryDAO loginHistoryDAO;

	@Inject
	private LoginAttemptRateLimiter rateLimitService;

	@Override
	public LoginResponseDTO login(LoginRequestDTO request, String ipAddress) {

		if (request == null || request.getEmail() == null || request.getPassword() == null)
			throw new ServiceException("Invalid login request");

		String email = request.getEmail().toLowerCase();

		try {

			try {
				rateLimitService.checkLimit(email);
			} catch (RuntimeException ex) {
				throw new ServiceException(ex.getMessage());
			}

			User user;
			try {
				user = resolveUser(email);
			} catch (ServiceException ex) {
				rateLimitService.recordFailure(email);
				throw ex;
			}

			BCrypt.Result result = BCrypt.verifyer().verify(request.getPassword().toCharArray(),
					user.getPasswordHash());

			if (!result.verified) {
				rateLimitService.recordFailure(email);
				throw new ServiceException("Invalid email or password");
			}

			if (!"ACTIVE".equalsIgnoreCase(user.getStatus()))
				throw new ServiceException("User is inactive");

			rateLimitService.reset(email);

			recordLogin(user.getId(), ipAddress);

			return new LoginResponseDTO(UserMapper.toResponse(user));

		} catch (DAOException e) {
			throw new ServiceException("Login failed", e);
		}
	}

	@Override
	public UserResponseDTO register(RegisterRequestDTO request) {

		if (request == null)
			throw new ServiceException("Invalid registration request");

		try {

			Optional<User> existing = userDAO.findByEmail(request.getEmail());
			if (existing.isPresent())
				throw new ServiceException("Email is already in use");

			User user = new User();
			user.setUserName(request.getUsername());
			user.setEmail(request.getEmail().toLowerCase());
			user.setPasswordHash(BCrypt.withDefaults().hashToString(12, request.getPassword().toCharArray()));
			user.setFirstName(request.getFirstName());
			user.setLastName(request.getLastName());
			user.setRoleEnum(Role.USER);
			user.setStatus("ACTIVE");

			User saved = userDAO.save(user);

			userCacheService.cacheUserId(saved.getEmail(), saved.getId());

			return UserMapper.toResponse(saved);

		} catch (DAOException e) {
			throw new ServiceException("Registration failed", e);
		}
	}

	private User resolveUser(String email) {

		Long cachedUserId = userCacheService.getCachedUserId(email);

		if (cachedUserId != null) {
			Optional<User> user = userDAO.findById(cachedUserId);
			if (user.isPresent()) {
				return user.get();
			}
			userCacheService.evict(email);
		}

		Optional<User> optionalUser = userDAO.findByEmail(email);
		if (optionalUser.isEmpty())
			throw new ServiceException("Invalid email or password");

		User user = optionalUser.get();
		userCacheService.cacheUserId(email, user.getId());

		return user;
	}

	private void recordLogin(Long userId, String ip) {
		LoginHistory history = new LoginHistory();
		history.setUserId(userId);
		history.setIpAddress(ip);
		loginHistoryDAO.save(history);
	}
}