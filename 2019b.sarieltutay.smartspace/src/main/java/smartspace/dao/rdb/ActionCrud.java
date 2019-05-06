package smartspace.dao.rdb;


import java.util.List;


import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;

public interface ActionCrud extends PagingAndSortingRepository<ActionEntity, String>  {
	public List<ActionEntity> findAllByActionSmartspaceLike(
			@Param("pattern") String pattern,
			Pageable pageable);
	
}

