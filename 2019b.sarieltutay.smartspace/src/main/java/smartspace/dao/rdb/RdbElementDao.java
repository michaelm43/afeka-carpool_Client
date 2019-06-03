package smartspace.dao.rdb;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import smartspace.dao.EnhancedElementDao;
import smartspace.data.ElementEntity;

@Repository
public class RdbElementDao implements EnhancedElementDao<String> {

	private GenericIdGeneratorCrud genericIdGeneratorCrud;
	private ElementCrud elementCrud;

	@Autowired
	public RdbElementDao(ElementCrud elementCrud, GenericIdGeneratorCrud genericIdGeneratorCrud) {
		super();
		this.elementCrud = elementCrud;
		this.genericIdGeneratorCrud = genericIdGeneratorCrud;
	}

	@Override
	@Transactional
	public ElementEntity create(ElementEntity elementEntity) {
		if (elementEntity != null) {
			if (!this.elementCrud.existsById(elementEntity.getKey())) {
				if (elementCrud != null) {
					if (!this.elementCrud.existsById(elementEntity.getKey())) {
						ElementEntity rv = this.elementCrud.save(elementEntity);
						return rv;
					} else
						throw new RuntimeException("element already exists with key: " + elementEntity.getKey());
				} else
					throw new RuntimeException("elementactionCrud is null");
			}
		} else
			throw new RuntimeException("element cant be null");
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<ElementEntity> readById(String elementKey) {
		// SQL: SELECT
		return this.elementCrud.findById(elementKey);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<ElementEntity> readByIdNotExpired(String elementKey) {
		// SQL: SELECT
		return this.elementCrud.findByKeyAndExpired(elementKey, false);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementEntity> readAll() {
		List<ElementEntity> rv = new ArrayList<>();

		// SQL: SELECT
		this.elementCrud.findAll().forEach(rv::add);

		return rv;
	}

	@Override
	@Transactional
	public void update(ElementEntity element) {
		ElementEntity existing = this.readById(element.getKey())
				.orElseThrow(() -> new RuntimeException("not element to update"));
		if (element.getCreatorEmail() != null)
			existing.setCreatorEmail(element.getCreatorEmail());

		if (element.getCreatorSmartspace() != null)
			existing.setCreatorSmartspace(element.getCreatorSmartspace());

		if (element.getLocation() != null)
			existing.setLocation(element.getLocation());

		if (element.getName() != null)
			existing.setName(element.getName());

		if (element.getType() != null)
			existing.setType(element.getType());

		if (element.getMoreAttributes() != null)
			existing.setMoreAttributes(element.getMoreAttributes());

		// SQL: UPDATE
		this.elementCrud.save(existing);
	}

	@Override
	@Transactional
	public void deleteByKey(String elementKey) {
		if (!elementKey.equals(null))
			if (this.elementCrud.existsById(elementKey))
				this.elementCrud.deleteById(elementKey);
	}

	@Override
	public void delete(ElementEntity elementEntity) {
		deleteByKey(elementEntity.getKey());
	}

	@Override
	public void deleteAll() {
		this.elementCrud.deleteAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementEntity> readAll(int size, int page) {
		return this.elementCrud.findAll(PageRequest.of(page, size)).getContent();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementEntity> readAll(String sortBy, int size, int page) {
		return this.elementCrud.findAll(PageRequest.of(page, size, Direction.ASC, sortBy)).getContent();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementEntity> readMessageWithSmartspaceContaining(String smartspace, int size, int page) {
		return this.elementCrud.findAllByNameLike("%" + smartspace + "%", PageRequest.of(page, size));
	}

	@Override
	@Transactional
	public ElementEntity createImportAction(ElementEntity entity) {
		return this.elementCrud.save(entity);
	}

	@Override
	public List<ElementEntity> readAllNotExpierd(int size, int page) {
		return this.elementCrud.findAllByExpired(false, PageRequest.of(page, size));
	}

	@Override
	public List<ElementEntity> readAllUsingLocation(int x, int y, int distance, int size, int page) {
		return this.elementCrud.findAllByLocation_XBetweenAndLocation_YBetween(x - distance, x + distance, y - distance,
				y + distance, PageRequest.of(page, size));
	}

	@Override
	public List<ElementEntity> readAllUsingLocationNotExpired(int x, int y, int distance, int size, int page) {
		return this.elementCrud.findAllByExpiredAndLocation_XBetweenAndLocation_YBetween(false, x - distance,
				x + distance, y - distance, y + distance, PageRequest.of(page, size));
	}

	@Override
	public List<ElementEntity> readAllUsingName(String name, int size, int page) {
		return this.elementCrud.findAllByName(name, PageRequest.of(page, size));
	}

	@Override
	public List<ElementEntity> readAllUsingNameNotExpired(String name, int size, int page) {
		return this.elementCrud.findAllByNameAndExpired(name, false, PageRequest.of(page, size));
	}

	@Override
	public List<ElementEntity> readAllUsingType(String type, int size, int page) {
		return this.elementCrud.findAllByType(type, PageRequest.of(page, size));
	}

	@Override
	public List<ElementEntity> readAllUsingTypeNotExpired(String type, int size, int page) {
		return this.elementCrud.findAllByExpiredAndType(false, type, PageRequest.of(page, size));
	}

	@Override
	@Transactional
	public ElementEntity createWithId(ElementEntity elementEntity, Long id) {
		elementEntity.setKey(elementEntity.getElementSmartspace() + "=" + id);
		return this.create(elementEntity);
	}

}