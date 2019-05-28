package smartspace.data;


import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection="USERS")
public class UserEntity implements SmartspaceEntity<String> {
	private String userSmartspace;
	private String userEmail;
	private String username;
	private String avatar;
	private UserRole role;
	private long points;
	private String key;
	
	
	public UserEntity(String userSmartspace, String userEmail, String username, String avatar, UserRole role,
			long points) {
		super();
		this.userSmartspace = userSmartspace;
		this.userEmail = userEmail;
		this.username = username;
		this.avatar = avatar;
		this.role = role;
		this.points = points;
	}
	
	public UserEntity() {
		super();
	}
	
	public UserEntity(String userEmail) {
		this.userEmail = userEmail;
	}

//	@Transient
	@JsonIgnore
	public String getUserSmartspace() {
		return userSmartspace;
	}



	public void setUserSmartspace(String userSmartspace) {
		this.userSmartspace = userSmartspace;
	}



//	@Transient
	@JsonIgnore
	public String getUserEmail() {
		return userEmail;
	}



	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
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


	@Enumerated(EnumType.STRING)
	public UserRole getRole() {
		return role;
	}



	public void setRole(UserRole role) {
		this.role = role;
	}



	public long getPoints() {
		return points;
	}



	public void setPoints(long points) {
		this.points = points;
	}



//	@Override
//	@Id
//	public String getKey() {
//		return this.userSmartspace + "=" + this.userEmail;
//	}

	@Override
	@Id
	public String getKey() {
		return this.userSmartspace + "=" + this.userEmail;
	}
	
	@Override
	public void setKey(String key) {		
		this.key = key;
		String[] split = key.split("=");
		if(split!=null & split.length==2) {
			this.userSmartspace = split[0];
			this.userEmail= split[1];	
		}
	}
	
//	@Override
//	public void setKey(String key) {		
//		String[] split = key.split("=");
//		if(split!=null & split.length==2) {
//			this.userSmartspace = split[0];
//			this.userEmail= split[1];	
//		}
//	}

	@Override
	public String toString() {
		return "UserEntity [userSmartspace=" + userSmartspace + ", userEmail=" + userEmail + ", username=" + username
				+ ", avatar=" + avatar + ", role=" + role + ", points=" + points + "]";
	}
}
