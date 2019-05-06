package smartspace.infra;

import java.util.List;

import smartspace.data.UserEntity;


public interface UsersService {

	public List<UserEntity> newUsers(List<UserEntity> users, String adminSmartspace, String adminEmail);
	
	public List<UserEntity> getUsersUsingPagination (String adminSmartspace, String adminEmail, int size, int page);
}
