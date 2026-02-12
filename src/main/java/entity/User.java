package entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {
	private Long id;
	private String userName;
	private String email;
	private String passwordHash;
	private String firstName;
	private String lastName;
	private String role="USER";
	private String status="ACTIVE";
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	public User() {}

	public User(Long id, String userName, String email, String passwordHash, String firstName, String lastName,
			String role, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.id = id;
		this.userName = userName;
		this.email = email;
		this.passwordHash = passwordHash;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
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
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(email, other.email);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", email=" + email + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", role=" + role + ", status=" + status + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + "]";
	}
		
}