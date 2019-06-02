package smartspace.infra;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import smartspace.aop.CheckRoleOfUser;
import smartspace.dao.EnhancedActionDao;
import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.dao.SequenceDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;

@Service
public class ActionsUserServiceImpl implements ActionsUserService {

	private EnhancedActionDao actionDao;
	ObjectMapper jackson;
	private EnhancedUserDao<String> userDao;
	private EnhancedElementDao<String> elementDao;
	private SequenceDao sequenceDao;

	@Autowired
	public ActionsUserServiceImpl(EnhancedActionDao actionDao, EnhancedUserDao<String> userDao,
			EnhancedElementDao<String> elementDao, SequenceDao sequenceDao) {
		super();
		this.actionDao = actionDao;
		this.userDao = userDao;
		this.elementDao = elementDao;
		this.sequenceDao = sequenceDao;
	}

	@Override
	@Transactional
	@CheckRoleOfUser
	public ActionEntity invokeAction(String smartspace, String email, UserRole role, ActionEntity action) {
		if (role == UserRole.PLAYER) {
			Optional<UserEntity> user = this.userDao.readById(smartspace + "=" + email);
			if (user.isPresent()) {
				user.get().setPoints(user.get().getPoints() + 10);
				userDao.update(user.get());
			} else
				throw new RuntimeException("The user doesn't exist");

			System.out.println(action.getActionType());
			System.out.println("*****");
			String type = action.getActionType();
			switch(type) {
			case "echo":
				action.setCreationTimestamp(new Date());
				try {
					return actionDao.createWithId(action, sequenceDao.newEntity(ActionEntity.SEQUENCE_NAME));
				} catch (Exception e) {
					new RuntimeException(e);
				}
				break;
			case "to-afeka":
				try {
					return actionDao.createWithId(action, sequenceDao.newEntity(ActionEntity.SEQUENCE_NAME));
				} catch (Exception e) {
					new RuntimeException(e);
				}
				break;

			case "from-afeka":
				try {
					return actionDao.createWithId(action, sequenceDao.newEntity(ActionEntity.SEQUENCE_NAME));
				} catch (Exception e) {
					new RuntimeException(e);
				}
				break;

			case "check-in":
				try {
					Optional<ElementEntity> element = this.elementDao
							.readById(action.getElementSmartspace() + "=" + action.getElementId());
					if (element.isPresent()) {
						Map<String, Object> drivers = (Map<String, Object>) element.get().getMoreAttributes()
								.get("drivers");
						if (drivers == null)
							drivers = new HashMap<>();
						drivers.put(element.get().getCreatorEmail(), "In station");
						element.get().getMoreAttributes().put("drivers", drivers);
						this.elementDao.update(element.get());
						//return element.get();
					}
					return null;
				} catch (Exception e) {
					new RuntimeException(e);
				}
				break;

			case "check-out":
				try {
					Optional<ElementEntity> element = this.elementDao
							.readById(action.getElementSmartspace() + "=" + action.getElementId());
					if (element.isPresent()) {
						Map<String, Object> drivers = (Map<String, Object>) element.get().getMoreAttributes()
								.get("drivers");
						if (drivers != null) {
							drivers.remove(element.get().getCreatorEmail());
							element.get().getMoreAttributes().put("drivers", drivers);
							this.elementDao.update(element.get());
						}
						//return element.get();
					}
					return null;
				} catch (Exception e) {
					new RuntimeException(e);
				}
				break;

			case "max":
				try {
					List<ElementEntity> elements = this.elementDao.readAll();
					ElementEntity maxElement = null;
					int max = -1;
					for (ElementEntity element : elements) {
						int counter = 0;
						Map<String, Object> drivers = (Map<String, Object>) element.getMoreAttributes().get("drivers");
						if (drivers != null) {
							for (String driver : drivers.keySet())
								counter++;
						}
						if (counter > max) {
							max = counter;
							maxElement = element;
						}
					}
					if (maxElement != null)
						//return maxElement;
						return null;
					else
						return null;
				} catch (Exception e) {
					new RuntimeException(e);
				}
				break;

			default:
				throw new RuntimeException("Action type does not exist!");
			}
		} else {
			throw new RuntimeException("Only players can invoke actions");
		}
		return null;
	}

	public Map<String, Object> convertToMap(ActionEntity action) {
		Map<String, Object> actionMap = new HashMap<String, Object>();
		Map<String, String> keyMap = new HashMap<String, String>();
		Map<String, String> elementMap = new HashMap<String, String>();
		Map<String, String> playerMap = new HashMap<String, String>();

		keyMap.put("id", action.getActionId());
		keyMap.put("smartspace", action.getActionSmartspace());

		elementMap.put("id", action.getElementId());
		elementMap.put("smartspace", action.getElementSmartspace());

		playerMap.put("smartspace", action.getPlayerSmartspace());
		playerMap.put("email", action.getPlayerEmail());

		actionMap.put("actionKey", keyMap);
		actionMap.put("type", action.getActionType());
		actionMap.put("created", action.getCreationTimestamp());
		actionMap.put("element", elementMap);
		actionMap.put("player", playerMap);
		actionMap.put("properties", action.getMoreAttributes());

		return actionMap;
	}

	public Map<String, Object> convertToMap(ElementEntity element) {
		Map<String, Object> elementMap = new HashMap<String, Object>();
		Map<String, String> keyMap = new HashMap<String, String>();
		Map<String, String> playerMap = new HashMap<String, String>();
		Map<String, String> locationMap = new HashMap<String, String>();

		keyMap.put("id", element.getElementId());
		keyMap.put("smartspace", element.getElementSmartspace());

		playerMap.put("smartspace", element.getCreatorSmartspace());
		playerMap.put("email", element.getCreatorEmail());

		locationMap.put("lat", element.getLocation().getX() + "");
		locationMap.put("lng", element.getLocation().getY() + "");

		elementMap.put("key", keyMap);
		elementMap.put("elementType", element.getType());
		elementMap.put("name", element.getName());
		elementMap.put("expired", element.isExpired());
		elementMap.put("created", element.getCreationTimestamp());
		elementMap.put("creator", playerMap);
		elementMap.put("latlng", locationMap);
		elementMap.put("elementProperties", element.getMoreAttributes());

		return elementMap;
	}
}
