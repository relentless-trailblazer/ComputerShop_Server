package com.computershop.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.computershop.dao.Delivery;
import com.computershop.dao.SaleOrder;

@Repository
public interface SaleOrderRepository extends JpaRepository<SaleOrder, Long>{
	SaleOrder findByUserIdAndDeliveryId(Long userId, Long deliveryId);
	
	List<SaleOrder> findByUserIdAndDelivery(Long userId, Delivery delivery);
	
	List<SaleOrder> findByDeliveryId(Long deliveryId);
	
	List<SaleOrder> findByDeliveryOrderByCreateAtDesc(Delivery delivery);
}
