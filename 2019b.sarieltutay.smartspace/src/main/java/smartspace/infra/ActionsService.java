package smartspace.infra;

import java.util.List;

import smartspace.data.ActionEntity;

public interface ActionsService {
	
	public List<ActionEntity> newActions(List<ActionEntity> actions, String adminSmartspace, String adminEmail);
	
	public List<ActionEntity> getActionsUsingPagination (String adminSmartspace, String adminEmail, int size, int page);
	
}
