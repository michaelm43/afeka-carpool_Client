package smartspace.dao.rdb;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import smartspace.dao.EnhancedActionDao;
import smartspace.data.ActionEntity;

@Repository
public class RdbActionDao implements EnhancedActionDao {

	private ActionCrud actionCrud;

	public RdbActionDao() {
		super();
	}

	@Autowired
	public RdbActionDao(ActionCrud actionCrud) {
		super();
		this.actionCrud = actionCrud;
	}

	@Override
	@Transactional
	public ActionEntity create(ActionEntity actionEntity) {
		if (actionEntity != null) {
			if (this.actionCrud != null) {
				if (!this.actionCrud.existsById(actionEntity.getKey())) {
					ActionEntity rv = this.actionCrud.save(actionEntity);
					return rv;
				} else
					throw new RuntimeException("action already exists with key: " + actionEntity.getKey());
			} 
			else
				throw new RuntimeException("actionCrud is null");
		} else
			throw new RuntimeException("action is null");
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionEntity> readAll() {
		List<ActionEntity> rv = new ArrayList<>();

		this.actionCrud.findAll().forEach(rv::add);

		return rv;
	}

	@Override
	@Transactional
	public void deleteAll() {
		this.actionCrud.deleteAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionEntity> readAll(int size, int page) {
		return this.actionCrud.findAll(PageRequest.of(page, size)).getContent();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionEntity> readAll(String sortBy, int size, int page) {
		return this.actionCrud.findAll(PageRequest.of(page, size, Direction.ASC, sortBy)).getContent();
	}

	@Override
	@Transactional
	public ActionEntity createImportAction(ActionEntity entity) {
		return this.actionCrud.save(entity);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionEntity> readMessageWithSmartspaceContaining(String smartspace, int size, int page) {
		return this.actionCrud.findAllByActionSmartspaceLike("%" + smartspace + "%", PageRequest.of(page, size));
	}

	@Override
	@Transactional
	public ActionEntity createWithId(ActionEntity actionEntity, Long id) {
		actionEntity.setKey(actionEntity.getActionSmartspace() + "=" + id);
		return this.create(actionEntity);
	}
}