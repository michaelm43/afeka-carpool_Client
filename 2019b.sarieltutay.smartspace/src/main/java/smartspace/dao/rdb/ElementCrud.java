package smartspace.dao.rdb;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;


import smartspace.data.ElementEntity;

public interface ElementCrud extends PagingAndSortingRepository<ElementEntity, String> {
	public List<ElementEntity> findAllByNameLike(
			@Param("pattern") String pattern,
			Pageable pageable);
}



