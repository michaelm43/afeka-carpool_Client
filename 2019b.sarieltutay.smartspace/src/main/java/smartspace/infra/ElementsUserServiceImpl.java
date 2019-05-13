package smartspace.infra;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import smartspace.aop.CheckRoleOfUser;
import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ElementEntity;
import smartspace.data.UserRole;

public class ElementsUserServiceImpl implements ElementsUserService {

	private EnhancedElementDao<String> elementDao;

	@Autowired
	public ElementsUserServiceImpl(EnhancedElementDao<String> elementDao, EnhancedUserDao<String> userDao) {
		super();
		this.elementDao = elementDao;
	}

	@Value("${smartspace.name}")

	@Override
	@Transactional
	@CheckRoleOfUser
	public ElementEntity newElement(String userSmartspace, String userEmail,UserRole role, ElementEntity element) {
		if (role == UserRole.MANAGER || role == UserRole.PLAYER) {
			if (valiadate(element)) {
				element.setCreationTimestamp(new Date());
				this.elementDao.createImportAction(element);
			} else
				throw new RuntimeException("invalid element");
		}

		else {
			throw new RuntimeException(
					"The URl isn't match for manager or player. use another user or URL that match admin user");
		}

		return element;
	}

	@Override
	@Transactional
	@CheckRoleOfUser
	public void setElement(String userSmartspace, String userEmail, UserRole role, String elementSmartspace, String elementId,
			ElementEntity element) // TODO what to do with the smartspace?
			 {

		if (role == UserRole.MANAGER || role == UserRole.PLAYER) {
			if (valiadate(element)) { // TODO should we check the validate?!
				element.setCreationTimestamp(new Date()); // TODO should we change the Timestamp?
				this.elementDao.update(element);
			} else
				throw new RuntimeException("invalid element");
		}

		else {
			throw new RuntimeException(
					"The URl isn't match for manager or player. use another user or URL that match admin user");
		}
	}

	@Override
	@CheckRoleOfUser
	public ElementEntity getSpecificElement(String userSmartspace, String userEmail, UserRole role, String elementSmartspace,
			String elementId) {

		if (role == UserRole.MANAGER) {
			return this.elementDao.readById(elementId)
					.orElseThrow(() -> new RuntimeException("There is no element with the given key"));
		} else if (role == UserRole.PLAYER) {
			this.elementDao.readById(elementId).filter(ElementEntity::isExpired)
					.orElseThrow(() -> new RuntimeException("There is no element with the given key"));
		} else {
			throw new RuntimeException(
					"The URl isn't match for manager or player. use another user or URL that match admin user");
		}

		throw new RuntimeException("The element is expired");
	}

	@Override
	@CheckRoleOfUser
	public List<ElementEntity> getElementsUsingPagination(String userSmartspace, String userEmail,
			UserRole role, int size, int page) {

		if (role == UserRole.MANAGER) {
			return this.elementDao.readAll(size, page);
		} else if (role == UserRole.PLAYER) {
			return this.elementDao.readAllNotExpierd(size, page);
		} else {
			throw new RuntimeException(
					"The URl isn't match for manager or player. use another user or URL that match admin user");
		}
	}

	@Override
	@CheckRoleOfUser
	public List<ElementEntity> getElementsUsingPaginationOfLocation(String userSmartspace, String userEmail, UserRole role,
			int x, int y, int distance, int size, int page) {

		if (role == UserRole.MANAGER) {
			return this.elementDao.readAllUsingLocation(x, y, distance, size, page);
		} else if (role == UserRole.PLAYER) {
			return this.elementDao.readAllUsingLocationNotExpired(x, y, distance, size, page);
		} else {
			throw new RuntimeException(
					"The URl isn't match for manager or player. use another user or URL that match admin user");
		}
	}

	@Override
	@CheckRoleOfUser
	public Collection<ElementEntity> getElementsUsingPaginationOfName(String userSmartspace, String userEmail, UserRole role,
			String name, int size, int page) {

		if (role == UserRole.MANAGER) {
			return this.elementDao.readAllUsingName(name, size, page);
		} else if (role == UserRole.PLAYER) {
			return this.elementDao.readAllUsingNameNotExpired(name, size, page);
		} else {
			throw new RuntimeException(
					"The URl isn't match for manager or player. use another user or URL that match admin user");
		}
	}

	@Override
	@CheckRoleOfUser
	public List<ElementEntity> getElementsUsingPaginationOfSpecifiedType(String userSmartspace, String userEmail, UserRole role,
			String type, int size, int page) {

		if (role == UserRole.MANAGER) {
			return this.elementDao.readAllUsingType(type, size, page);
		} else if (role == UserRole.PLAYER) {
			return this.elementDao.readAllUsingTypeNotExpired(type, size, page);
		} else {
			throw new RuntimeException(
					"The URl isn't match for manager or player. use another user or URL that match admin user");
		}
	}

	private boolean valiadate(ElementEntity entity) {
		return entity != null && entity.getCreatorSmartspace() != null
				&& !entity.getCreatorSmartspace().trim().isEmpty() && entity.getCreatorEmail() != null
				&& !entity.getCreatorEmail().trim().isEmpty() && entity.getName() != null
				&& !entity.getName().trim().isEmpty() && entity.getType() != null && !entity.getType().trim().isEmpty()
				&& entity.getElementId() != null && !entity.getElementId().trim().isEmpty()
				&& entity.getElementSmartspace() != null && !entity.getElementSmartspace().trim().isEmpty()
				&& entity.getLocation() != null && entity.getMoreAttributes() != null;
	}

}
