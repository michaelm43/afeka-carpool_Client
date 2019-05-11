package smartspace.layout;

import org.springframework.beans.factory.annotation.Value;

import smartspace.data.UserEntity;
import smartspace.data.UserRole;

public class UserForm {
	private String email;
	private UserRole role;
	private String username;
	private String avatar;
	private String smartspace;
	
	@Value("${smartspace.name}")
	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}
	
	public UserForm() {
	}
	
	public UserForm(String email,String role,String username,String avatar) {
		this.email = email;
		this.username = username;
		this.avatar = avatar;
		this.role = UserRole.valueOf(role);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = UserRole.valueOf(role);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	public UserEntity convertToUserEntity(){
		return new UserEntity(smartspace,email,username,avatar,role,0);
	}
}

