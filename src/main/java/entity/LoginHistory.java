package entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class LoginHistory {
    private Long id;
    private Long userId;
    private String ipAddress;
    private LocalDateTime loginTime;

    public LoginHistory() {}

    public LoginHistory(Long id, Long userId, String ipAddress, LocalDateTime loginTime) {
        this.id = id;
        this.userId = userId;
        this.ipAddress = ipAddress;
        this.loginTime = loginTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, loginTime, ipAddress);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        LoginHistory other = (LoginHistory) obj;
        return Objects.equals(userId, other.userId) &&
               Objects.equals(ipAddress, other.ipAddress) &&
               Objects.equals(loginTime, other.loginTime);
    }

    @Override
    public String toString() {
        return "LoginHistory [id=" + id + ", userId=" + userId + ", ipAddress=" + ipAddress +
               ", loginTime=" + loginTime + "]";
    }
}
