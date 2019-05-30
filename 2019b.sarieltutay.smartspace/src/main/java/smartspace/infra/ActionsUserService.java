package smartspace.infra;

import java.util.Map;

import smartspace.data.ActionEntity;
import smartspace.data.UserRole;

public interface ActionsUserService {
	
	public ActionEntity invokeAction(String smartspace,String email ,UserRole role,ActionEntity action);
}
