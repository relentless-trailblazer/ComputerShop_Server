package com.computershop.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.computershop.dao.Delivery;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long>{
	Optional<Delivery> findById(Long Id);
	
	Delivery findByIndex(String Index);
}
