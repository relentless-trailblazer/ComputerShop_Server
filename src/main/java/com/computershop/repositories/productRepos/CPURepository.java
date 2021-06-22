package com.computershop.repositories.productRepos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.computershop.dao.product.CPU;

@Repository
public interface CPURepository extends JpaRepository<CPU, Long>{

	List<CPU> findByNameContainingIgnoreCase(String searchConvert);

	Optional<CPU> findByCPUId(Long cpuId);

	void deleteByCPUId(Long cpuId);

}
