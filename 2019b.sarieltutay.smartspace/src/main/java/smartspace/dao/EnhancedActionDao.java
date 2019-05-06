package smartspace.dao;

import java.util.List;

import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;

public interface EnhancedActionDao extends ActionDao{
	public List<ActionEntity> readAll(int size, int page);
	public List<ActionEntity> readAll(String sortBy, int size, int page);
	public List<ActionEntity> readMessageWithSmartspaceContaining (String smartspace, int size, int page);
	public ActionEntity createImportAction(ActionEntity entity);
}


