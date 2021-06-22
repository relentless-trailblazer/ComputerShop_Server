package com.computershop.repositories.productRepos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.computershop.dao.product.Case;

@Repository
public interface CaseRepository extends JpaRepository<Case, Long>{
	 List<Case> findByNameContaining(String name);

	 List<Case> findByNameContainingIgnoreCase(String searchConvert);

	Optional<Case> findByCaseId(Long caseId);

	void deleteByCaseId(Long caseId);
}
