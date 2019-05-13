package smartspace.infra;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import smartspace.aop.CheckRollOfUser;
import smartspace.dao.EnhancedActionDao;
import smartspace.data.ActionEntity;
import smartspace.data.UserRole;

public class ActionsUserServiceImpl implements ActionsUserService{

	private EnhancedActionDao actionDao;
	
	@Autowired	
	public ActionsUserServiceImpl(EnhancedActionDao actionDao) {
		super();
		this.actionDao = actionDao;
	}
	
	@Override
	@Transactional
	@CheckRollOfUser
	public Map<String,Object> invokeAction(ActionEntity action,String smartspace,String email ,UserRole role) {
		
		if(role == UserRole.ADMIN) {
			throw new RuntimeException("The URl isn't match for manager or player. use another user or URL that match admin user");	
		}
		
		else {
			switch(action.getActionType()) {
			case  "echo": 
				if(role == UserRole.MANAGER){
					return convertToMap(actionDao.create(action));
				}
				
				else {
					action.setCreationTimestamp(new Date());
					return convertToMap(actionDao.create(action));
				}
				default: throw new RuntimeException("Action type does no exist!");	
			}		
			
		}
	}
	
	public Map<String,Object> convertToMap(ActionEntity action){
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
	}

}
