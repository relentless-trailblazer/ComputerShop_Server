package com.computershop.repositories.productRepos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.computershop.dao.product.HardDisk;

@Repository
public interface HardDiskRepository extends JpaRepository<HardDisk, Long>{

	List<HardDisk> findByNameContainingIgnoreCase(String name);

	Optional<HardDisk> findByHardDiskId(Long hardDiskId);

	void deleteByHardDiskId(Long hardDiskId);

}
