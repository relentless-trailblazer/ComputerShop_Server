package com.computershop.repositories;

import java.util.List;
//import java.util.Optional;
//import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.computershop.dao.Product;
//import com.computershop.dao.product.Case;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findByNameContainingIgnoreCase(String name);
	
	List<Product> findByNameContaining(String name);
	
	Product findByName(String name);
	
	List<Product> findAllByQuantitySoldByDesc();

//	void deleteById(Optional<Case> optionalCase);
	
//	Optional<Product> findById(Long id);

}
