package smartspace.dao.rdb;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;


import smartspace.data.ElementEntity;
import smartspace.data.Location;

public interface ElementCrud extends PagingAndSortingRepository<ElementEntity, String> {
	public List<ElementEntity> findAllByNameLike(
			@Param("pattern") String pattern,
			Pageable pageable);
	
	public List<ElementEntity> findAllByName(
			@Param("name") String name,
			Pageable pageable);
	
	public List<ElementEntity> findAllByNameAndExpired(
			@Param("name") String name, @Param("expired") boolean expired,
			Pageable pageable);
	
	public List<ElementEntity> findAllByLocationBetween(
			@Param("locationmin") Location locationmin, @Param("locationmax") Location locationmax, 
			Pageable pageable);

	public List<ElementEntity> findAllByExpired(@Param("expired") boolean expired, Pageable pageable);
	
	public List<ElementEntity> findAllByExpiredAndLocationBetween(@Param("expired") boolean expired,
			@Param("locationmin") Location locationmin, @Param("locationmax") Location locationmax,
			Pageable pageable);
	
	public List<ElementEntity> findAllByType(@Param("type") String type, Pageable pageable);
	
	public List<ElementEntity> findAllByExpiredAndType(@Param("expired") boolean expired,
			@Param("type") String type, Pageable pageable);
}