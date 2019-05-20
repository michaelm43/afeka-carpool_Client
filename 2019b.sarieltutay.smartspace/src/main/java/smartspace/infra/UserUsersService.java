package smartspace.infra;

import smartspace.data.UserEntity;
import smartspace.data.UserRole;

public interface UserUsersService {
	
	public UserEntity newUser(UserEntity user);
	
	public UserEntity getUser(String userSmartspace, String userEmail);
	
	public void updateUser(String userSmartspace, String userEmail,UserRole role,UserEntity entity);
}
