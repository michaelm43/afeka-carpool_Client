package smartspace.infra;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import smartspace.dao.EnhancedActionDao;
import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ActionEntity;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;

@Service
public class ActionsServiceImpl implements ActionsService {
	private EnhancedActionDao actionDao;
	private EnhancedUserDao<String> userDao;
	private EnhancedElementDao<String> elementDao;
	private String smartspace;
	
	@Autowired	
	public ActionsServiceImpl(EnhancedActionDao actionDao, EnhancedUserDao<String> userDao, EnhancedElementDao<String> elementDao) {
		super();
		this.actionDao = actionDao;
		this.userDao = userDao;
		this.elementDao = elementDao;
	}
	
	@Value("${smartspace.name}")
	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}

	@Override
	@Transactional
	public List<ActionEntity> newActions(List<ActionEntity> actions, String adminSmartspace,
			String adminEmail) {
		List<ActionEntity> actions_entities = new ArrayList<ActionEntity>();
		//check local admin
		if (!(this.smartspace.equals(adminSmartspace)) || !(valiadete_admin(adminSmartspace, adminEmail))) {
			throw new RuntimeException("user are not allowed to create actions");
		}
		
		
		for(ActionEntity action : actions) {
			if(!valiadate(action))
				throw new RuntimeException("invalid action");
			else {
				if(this.smartspace.equals(action.getActionSmartspace()))
					throw new RuntimeException("action smartspace must be different then the local project");
				else {
					if(!validateElement(action))
						throw new RuntimeException("action element must be imported in advance");	
				}
			}
			action.setCreationTimestamp(new Date());
			this.actionDao.createImportAction(action);
			actions_entities.add(action);
		}
		
		return actions_entities;
	}

	private boolean validateElement(ActionEntity action) {
		return this.elementDao.readById(action.getElementSmartspace() + "=" + action.getElementId()).isPresent();		
	}

	private boolean valiadete_admin(String adminSmartspace, String adminEmail) {
		Optional<UserEntity> user = this.userDao.readById(adminSmartspace + "=" + adminEmail);
		if(user.isPresent() && user.get().getRole().equals(UserRole.ADMIN))
			return true;
		return false;
	}
	
		private boolean valiadate(ActionEntity entity) {
			return entity != null &&
					entity.getActionId()!= null &&
					!entity.getActionId().trim().isEmpty() &&
					entity.getActionSmartspace() != null &&
					!entity.getActionSmartspace().trim().isEmpty() &&
					entity.getActionType() != null &&
					!entity.getActionType().trim().isEmpty() &&
					entity.getPlayerSmartspace() != null &&
					!entity.getPlayerSmartspace().trim().isEmpty() &&
					entity.getPlayerEmail() != null &&
					!entity.getPlayerEmail().trim().isEmpty() &&
					entity.getElementId() != null &&
					!entity.getElementId().trim().isEmpty() &&
					entity.getElementSmartspace() != null &&
					!entity.getElementSmartspace().trim().isEmpty() &&
					entity.getMoreAttributes() != null;
		}

	
	
	@Override
	public List<ActionEntity> getActionsUsingPagination(String adminSmartspace, String adminEmail, int size,
			int page) {
		if(!(this.smartspace.equals(adminSmartspace)) || !(valiadete_admin(adminSmartspace, adminEmail)))
			throw new RuntimeException("user are not allowed to get actions");
		else
			return this.actionDao
					.readAll("creationTimestamp", size, page);
	}


	
}
