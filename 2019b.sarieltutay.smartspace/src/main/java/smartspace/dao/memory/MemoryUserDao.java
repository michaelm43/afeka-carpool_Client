package smartspace.dao.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;


import smartspace.dao.UserDao;
import smartspace.data.UserEntity;


public class MemoryUserDao implements UserDao<String> {
	private List<UserEntity> users;
	private AtomicLong nextId;
	
	public MemoryUserDao() {
		this.users = Collections.synchronizedList(new ArrayList<>());
		this.nextId = new AtomicLong(1);
	}
	
	protected List<UserEntity> getUsers (){
		return this.users;
	}

	@Override
	public UserEntity create(UserEntity userEntity) {
		if(userEntity!=null) {
			userEntity.setKey(userEntity.getUserSmartspace()+"="+nextId.getAndIncrement());
			this.users.add(userEntity);
		}
		return userEntity;
	}

	@Override
	public Optional<UserEntity> readById(String userKey) {
		UserEntity target = null;
		for (UserEntity current : this.users) {
			if (current.getKey().equals(userKey)) {
				target = current;
			}
		}
		if (target != null) {
			return Optional.of(target);
		}else {
			return Optional.empty();
		}
	}

	@Override
	public List<UserEntity> readAll() {
		return this.users;
	}

	@Override
	public void update(UserEntity user) {
		synchronized (this.users) {
			UserEntity existing = readById(user.getKey()).orElseThrow(() -> 
				new RuntimeException("user couldn't be found"));
			
			existing.setPoints(user.getPoints());

			if(user.getAvatar() != null) {
				existing.setAvatar(user.getAvatar());
			}
			
			if(user.getRole() != null) {
				existing.setRole(user.getRole());
			}
			
			if(user.getUserEmail() != null) {
				existing.setUserEmail(user.getUserEmail());
			}
			
			if(user.getUsername() != null) {
				existing.setUsername(user.getUsername());
			}
		}
		
	}

	@Override
	public void deleteAll() {
		this.users.clear();	
	}
}