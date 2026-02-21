package entity;

import java.time.LocalDateTime;
import java.util.Objects;
import entity.enums.Role;

public class User {

	private Long id;
	private String userName;
	private String email;
	private String passwordHash;
	private String firstName;
	private String lastName;

	private String role = Role.USER.name();

	private String status = "ACTIVE";
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public User() {
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		if (role == null || role.isBlank()) {
			this.role = Role.USER.name();
			return;
		}

		try {
			Role.valueOf(role.toUpperCase());
			this.role = role.toUpperCase();
		} catch (IllegalArgumentException e) {
			this.role = Role.USER.name();
		}
	}

	public Role getRoleEnum() {
		try {
			return Role.valueOf(this.role);
		} catch (Exception e) {
			return Role.USER;
		}
	}

	public void setRoleEnum(Role role) {
		if (role == null) {
			this.role = Role.USER.name();
		} else {
			this.role = role.name();
		}
	}

	public boolean isAdmin() {
		return Role.ADMIN.name().equals(this.role);
	}

	public boolean isUser() {
		return Role.USER.name().equals(this.role);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(email, other.email);
	}
}