package smartspace.dao;

import java.util.List;

import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;

public interface EnhancedElementDao<Key> extends ElementDao<Key> {
	public List<ElementEntity> readAll(int size, int page);
	public List<ElementEntity> readAll(String sortBy, int size, int page);
	public List<ElementEntity> readMessageWithSmartspaceContaining (String smartspace, int size, int page);
	public ElementEntity createImportAction(ElementEntity entity);
}
