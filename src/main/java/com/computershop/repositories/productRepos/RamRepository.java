package com.computershop.repositories.productRepos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.computershop.dao.product.Ram;

@Repository
public interface RamRepository extends JpaRepository<Ram, Long>{

	Optional<Ram> findById(Long ramId);

	List<Ram> findByNameContainingIgnoreCase(String searchConvert);


}
