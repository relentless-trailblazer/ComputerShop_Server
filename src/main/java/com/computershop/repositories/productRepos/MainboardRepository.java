package com.computershop.repositories.productRepos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.computershop.dao.product.Mainboard;

@Repository
public interface MainboardRepository extends JpaRepository<Mainboard, Long>{

	List<Mainboard> findByNameContainingIgnoreCase(String searchConvert);

	Optional<Mainboard> findByMainboardId(Long mainboardId);

	void deleteByMainboardId(Long mainboardId);

}
