package smartspace.infra;

import java.util.Map;

import smartspace.data.ActionEntity;
import smartspace.data.UserRole;

public interface ActionsUserService {
	
	public Map<String,Object> invokeAction(ActionEntity action,String smartspace,String email ,UserRole role);
}
