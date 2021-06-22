package com.computershop.repositories.productRepos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.computershop.dao.product.Ram;

@Repository
public interface RamRepository extends JpaRepository<Ram, Long>{

	List<Ram> findByNameContainingIgnoreRam(String searchConvert);

	Optional<Ram> findByRamId(Long ramId);

	void deleteByRamId(Long ramId);

}
