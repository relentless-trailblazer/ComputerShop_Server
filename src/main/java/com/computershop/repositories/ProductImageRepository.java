package com.computershop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.computershop.dao.ProductImage;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
	
	ProductImage findByImageId(Long imageId);

	@Query(value = "delete from ProductImages where image_id = ?1", nativeQuery = true)
	void deleteByImageId(Long imageId);

	
	
}
