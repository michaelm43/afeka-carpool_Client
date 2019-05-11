package smartspace.infra;

import java.util.Collection;
import java.util.List;

import smartspace.data.ElementEntity;
import smartspace.data.UserRole;

public interface ElementsUserService {
	
	public ElementEntity newElement(ElementEntity element, String userSmartspace, String userEmail, UserRole role);
	
	public void setElement(String userSmartspace, String userEmail, String elementSmartspace, String elementId,
			ElementEntity element, UserRole role);
	
	public ElementEntity getSpecificElement(String userSmartspace, String userEmail, String elementSmartspace,
			String elementId, UserRole role);
	
	public List<ElementEntity> getElementsUsingPagination(String userSmartspace, String userEmail, int size, int page,
			UserRole role);
	
	public List<ElementEntity> getElementsUsingPaginationOfLocation(String userSmartspace, String userEmail,
			int x, int y, int distance, int size, int page, UserRole role);
	
	public Collection<ElementEntity> getElementsUsingPaginationOfName(String userSmartspace, String userEmail,
			String name, int size, int page, UserRole role);

	public List<ElementEntity> getElementsUsingPaginationOfSpecifiedType(String userSmartspace, String userEmail,
			String type, int size, int page, UserRole role);
}
