package smartspace.layout;

import java.util.List;

import smartspace.data.ElementEntity;
import smartspace.data.UserRole;

public class ElementsUserServiceImpl implements ElementsUserService{

	public ElementEntity newElement(ElementEntity element, String userSmartspace, String userEmail,UserRole role) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setElement(String userSmartspace, String userEmail, String elementSmartspace, String elementId,
			ElementBoundary element, UserRole role) {
		// TODO Auto-generated method stub
		
	}

	public ElementEntity getSpecificElement(String userSmartspace, String userEmail, String elementSmartspace,
			String elementId, UserRole role) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ElementEntity> getElementsUsingPagination(String userSmartspace, String userEmail, int size, int page,
			UserRole role) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ElementEntity> getElementsUsingPaginationOfLocation(String userSmartspace, String userEmail,
			int x, int y, int distance, int size, int page, UserRole role) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ElementEntity> getElementsUsingPaginationOfName(String userSmartspace, String userEmail,
			int name, int size, int page, UserRole role) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ElementEntity> getElementsUsingPaginationOfSpecifiedType(String userSmartspace, String userEmail,
			int type, int size, int page, UserRole role) {
		// TODO Auto-generated method stub
		return null;
	}

}
