package smartspace.infra;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import smartspace.aop.CheckRoleOfUser;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.layout.UserForm;

@Service
public class UserUsersServiceImpl implements UserUsersService{

	private EnhancedUserDao<String> userDao;
	private String smartspace;
	
	@Value("${smartspace.name}")
	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}
	
	@Autowired	
	public UserUsersServiceImpl(EnhancedUserDao<String> userDao) {
		super();
		this.userDao = userDao;
	}
	
	@Override
	@Transactional
	public UserEntity newUser(UserEntity user) {
		if(user.getRole() == UserRole.PLAYER)
			user.setPoints(0);
		return this.userDao.create(user);
	}
	
	@Override
	public UserEntity getUser(String smartspace, String email) {
		return this.userDao.readById(smartspace + "=" + email).get();
	}

	@Override
	@CheckRoleOfUser
	@Transactional
	public void updateUser(UserEntity user, String userSmartspace, String userEmail,UserRole role) {
		if(user.getRole() == UserRole.PLAYER && role != UserRole.PLAYER) {
			user.setPoints(0);
		}
		this.userDao.update(user);
	}

	public UserEntity convertToUserEntity(UserForm newUser) {
		return new UserEntity(
				this.smartspace,
				newUser.getEmail(),
				newUser.getUsername(),
				newUser.getAvatar(),
				newUser.getRole(),
				0);
	}
}
