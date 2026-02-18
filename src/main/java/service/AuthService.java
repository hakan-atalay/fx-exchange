package service;

import dao.UserDAO;
import dto.auth.LoginRequestDTO;
import dto.auth.LoginResponseDTO;
import dto.auth.RegisterRequestDTO;
import dto.response.UserResponseDTO;
import entity.User;
import exception.DAOException;
import exception.ServiceException;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import at.favre.lib.crypto.bcrypt.BCrypt;
import redis.clients.jedis.Jedis;
import mapper.UserMapper;

import java.util.Optional;

@ApplicationScoped
public class AuthService {

    @Inject
    private UserDAO userDAO;

    private Jedis redisClient;

    private static final String REDIS_LOGIN_KEY_PREFIX = "user:login:";

    @PostConstruct
    public void init() {
        String redisHost = System.getenv("REDIS_HOST");
        String redisPortStr = System.getenv("REDIS_PORT");
        int redisPort = 6379;
        if (redisPortStr != null) {
            redisPort = Integer.parseInt(redisPortStr);
        }
        redisClient = new Jedis(redisHost != null ? redisHost : "localhost", redisPort);
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        try {
            String redisKey = REDIS_LOGIN_KEY_PREFIX + request.getEmail();
            String cachedUserId = redisClient.get(redisKey);
            User user;

            if (cachedUserId != null) {
                Optional<User> optionalUser = userDAO.findById(Long.parseLong(cachedUserId));
                if (optionalUser.isEmpty()) {
                    redisClient.del(redisKey);
                    throw new ServiceException("Cached user bulunamadı");
                }
                user = optionalUser.get();
            } else {
                Optional<User> optionalUser = userDAO.findByEmail(request.getEmail());
                if (optionalUser.isEmpty()) {
                    throw new ServiceException("Kullanıcı bulunamadı");
                }
                user = optionalUser.get();
                redisClient.setex(redisKey, 3600, String.valueOf(user.getId()));
            }

            BCrypt.Result result = BCrypt.verifyer().verify(request.getPassword().toCharArray(), user.getPasswordHash());
            if (!result.verified) {
                throw new ServiceException("Geçersiz şifre");
            }

            if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
                throw new ServiceException("Kullanıcı aktif değil");
            }

            return new LoginResponseDTO(UserMapper.toResponse(user));
        } catch (DAOException e) {
            throw new ServiceException("Login sırasında veri tabanı hatası oluştu", e);
        }
    }

    public UserResponseDTO register(RegisterRequestDTO request) {
        try {
            Optional<User> existingUser = userDAO.findByEmail(request.getEmail());
            if (existingUser.isPresent()) {
                throw new ServiceException("Email zaten kullanımda");
            }

            User user = new User();
            user.setUserName(request.getUsername());
            user.setEmail(request.getEmail());
            String hashed = BCrypt.withDefaults().hashToString(12, request.getPassword().toCharArray());
            user.setPasswordHash(hashed);
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setRole("USER");
            user.setStatus("ACTIVE");

            User saved = userDAO.save(user);

            String redisKey = REDIS_LOGIN_KEY_PREFIX + saved.getEmail();
            redisClient.setex(redisKey, 3600, String.valueOf(saved.getId()));

            return UserMapper.toResponse(saved);
        } catch (DAOException e) {
            throw new ServiceException("Register sırasında veri tabanı hatası oluştu", e);
        }
    }
}
