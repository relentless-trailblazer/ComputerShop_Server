package com.computershop.repositories.productRepos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.computershop.dao.product.PowerSupply;

@Repository
public interface PowerSupplyRepository extends JpaRepository<PowerSupply, Long>{

	List<PowerSupply> findByNameContainingIgnoreCase(String searchConvert);

	Optional<PowerSupply> findByPowerSupplyId(Long powerSupplyId);

	void deleteByPowerSupplyId(Long powerSupplyId);

}
