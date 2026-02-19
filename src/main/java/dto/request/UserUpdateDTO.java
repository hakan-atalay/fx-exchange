package dto.request;

public class UserUpdateDTO {

	private String firstName;
	private String lastName;
	private String role;
	private String status;

	public UserUpdateDTO() {
	}

	public UserUpdateDTO(String firstName, String lastName, String role, String status) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.status = status;
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
}
