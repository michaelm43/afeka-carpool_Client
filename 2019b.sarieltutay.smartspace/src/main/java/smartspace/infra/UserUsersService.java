package smartspace.infra;

import smartspace.data.UserEntity;

public interface UserUsersService {
	
	public UserEntity newUser(UserEntity convertToUserEntity);
	
	public UserEntity getUser(String userSmartspace, String userEmail);
	
	public void updateUser(UserEntity entity, String userSmartspace, String userEmail);
}
