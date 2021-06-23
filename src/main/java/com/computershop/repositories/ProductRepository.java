package com.computershop.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.computershop.dao.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findByNameContainingIgnoreCase(String name);
	
	List<Product> findByNameContaining(String name);
	
	Product findByName(String name);
	
	@Query(value = "SELECT e.* FROM Products e ORDER BY e.quantity_sold DESC", nativeQuery = true)
	List<Product> findAllByQuantitySoldByDesc();
	
	
	
	
//	void deleteById(Optional<Case> optionalCase);
	
//	Optional<Product> findById(Long id);

}
