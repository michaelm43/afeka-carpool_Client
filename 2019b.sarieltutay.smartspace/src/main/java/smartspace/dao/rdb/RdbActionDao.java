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

	private GenericIdGeneratorCrud genericIdGeneratorCrud;
	private ActionCrud actionCrud;

	public RdbActionDao() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Autowired
	public RdbActionDao(ActionCrud actionCrud, GenericIdGeneratorCrud genericIdGeneratorCrud) {
		super();
		this.actionCrud = actionCrud;
		this.genericIdGeneratorCrud = genericIdGeneratorCrud;
	}

	@Override
	@Transactional
	public ActionEntity create(ActionEntity actionEntity) {
		if (actionEntity != null) {
			if (this.actionCrud != null) {
				if (!this.actionCrud.existsById(actionEntity.getKey())) {
					GenericIdGenerator nextId = this.genericIdGeneratorCrud.save(new GenericIdGenerator());
					actionEntity.setKey(actionEntity.getActionSmartspace() + "=" + nextId.getId());
					this.genericIdGeneratorCrud.delete(nextId);
					if (actionCrud != null) {
						if (!this.actionCrud.existsById(actionEntity.getKey())) {
							ActionEntity rv = this.actionCrud.save(actionEntity);
							return rv;
						} else
							throw new RuntimeException("action already exists with key: " + actionEntity.getKey());
					}
				}
			}
			else
				throw new RuntimeException("actionCrud is null");
				
		} else
			throw new RuntimeException("action is null");
		return null;
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

}