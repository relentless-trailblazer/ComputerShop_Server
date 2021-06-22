package com.computershop.repositories.productRepos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.computershop.dao.product.Monitor;


@Repository
public interface MonitorRepository extends JpaRepository<Monitor, Long>{

	List<Monitor> findByNameContainingIgnoreCase(String searchConvert);

	Optional<Monitor> findByMonitorId(Long monitorId);

	void deleteByMonitorId(Long monitorId);

}
