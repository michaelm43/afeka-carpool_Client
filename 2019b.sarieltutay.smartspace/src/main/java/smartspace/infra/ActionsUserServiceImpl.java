package smartspace.infra;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import smartspace.aop.CheckRoleOfUser;
import smartspace.dao.EnhancedActionDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ActionEntity;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
@Service
public class ActionsUserServiceImpl implements ActionsUserService{

	private EnhancedActionDao actionDao;
	ObjectMapper jackson;
	private EnhancedUserDao<String> userDao;
	
	@Autowired	
	public ActionsUserServiceImpl(EnhancedActionDao actionDao, EnhancedUserDao<String> userDao) {
		super();
		this.actionDao = actionDao;
		this.userDao = userDao;
	}
	
	@Override
	@Transactional
	@CheckRoleOfUser
	public ActionEntity invokeAction(String smartspace,String email ,UserRole role,ActionEntity action) {
		if(role == UserRole.PLAYER) {
		Optional<UserEntity> user = this.userDao.readById(smartspace + "=" + email);
		if(user.isPresent()) {
			user.get().setPoints(user.get().getPoints()+10);
			userDao.update(user.get());
		}
		else
			throw new RuntimeException("The user isn't excist");
		String type = action.getActionType();
			switch(type) {
			case "echo": 
				action.setCreationTimestamp(new Date());
				try {
					return this.actionDao.create(action);
				} catch (Exception e) {
					new RuntimeException(e);
				}
			
			default: 
				throw new RuntimeException("Action type does not exist!");	
			}
		}
		else {
			throw new RuntimeException("Only players can invoke actions");	
		}
	}
	
	
/*	public Map<String,Object> convertToMap(ActionEntity action){
		Map<String,Object> actionMap = new HashMap<String, Object>();
		Map<String,String> keyMap = new HashMap<String, String>();
		Map<String,String> elementMap = new HashMap<String, String>();
		Map<String,String> playerMap = new HashMap<String, String>();
		
		
		keyMap.put("id",action.getActionId());
		keyMap.put("smartspace",action.getActionSmartspace());

		elementMap.put("id", action.getElementId());
		elementMap.put("smartspace", action.getElementSmartspace());
		
		playerMap.put("smartspace", action.getPlayerSmartspace());
		playerMap.put("email", action.getPlayerEmail());
		
		actionMap.put("actionKey", keyMap);
		actionMap.put("type",action.getActionType());
		actionMap.put("created",action.getCreationTimestamp());
		actionMap.put("element",elementMap);
		actionMap.put("player",playerMap);
		actionMap.put("properties",action.getMoreAttributes());
		
		return actionMap;
	}*/
}
