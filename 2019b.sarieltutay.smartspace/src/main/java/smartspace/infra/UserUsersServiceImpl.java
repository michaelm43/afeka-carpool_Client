package smartspace.infra;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import smartspace.aop.CheckRoleOfUser;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;

public class UserUsersServiceImpl implements UserUsersService{

	private EnhancedUserDao<String> userDao;
	
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
		return this.userDao.readUserUsingSmartspaceAndEmail(smartspace, email);
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

}
