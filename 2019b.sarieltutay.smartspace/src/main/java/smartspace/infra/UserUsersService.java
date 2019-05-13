package smartspace.infra;

import smartspace.data.UserEntity;
import smartspace.data.UserRole;

public interface UserUsersService {
	
	public UserEntity newUser(UserEntity user);
	
	public UserEntity getUser(String userSmartspace, String userEmail);
	
	public void updateUser(UserEntity entity, String userSmartspace, String userEmail,UserRole role);
}
