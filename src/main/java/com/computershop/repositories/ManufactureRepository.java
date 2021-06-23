package com.computershop.repositories;

import java.util.List;
//import java.util.Optional;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.computershop.dao.Manufacture;

@Repository
public interface ManufactureRepository extends JpaRepository<Manufacture, Long>{
	Optional<Manufacture> findByManufactureId(Long manufactureId);
	
	Manufacture findByManufactureName(String name);

	List<Manufacture> findByManufactureNameContainingIgnoreCase(String name);
	

}
