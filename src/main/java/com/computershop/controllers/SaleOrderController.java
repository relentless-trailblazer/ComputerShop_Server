package com.computershop.controllers;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.computershop.dao.Delivery;
import com.computershop.dao.OrderItem;
import com.computershop.dao.ProductImage;
import com.computershop.dao.SaleOrder;
import com.computershop.dto.DeliveryDTO;
import com.computershop.dto.OrderDetail;
import com.computershop.dto.OrderItemDetailDTO;
import com.computershop.dto.SaleOrderResponseDTO;
import com.computershop.exceptions.NotFoundException;
import com.computershop.repositories.DeliveryRepository;
import com.computershop.repositories.SaleOrderRepository;

@RestController
@RequestMapping("/api/sale-orders")
public class SaleOrderController {
	@Autowired
	private SaleOrderRepository saleOrderRepository;
	
	@Autowired
	private DeliveryRepository deliveryRepository;
	
	// ok
	@GetMapping
	public ResponseEntity<?> getAllSaleOrders(@RequestParam(name = "search", required = false) Long saleOrderId,
	                                          @RequestParam(name = "deliveryId", required = false) Long deliveryId,
	                                          @RequestParam(name = "recent", required = false) String recent) {
	    if (recent != null) {
	        if (recent.compareTo("true") == 0) {
	            Delivery delivery = deliveryRepository.findByIndex("DaGiao");
	
	            if (delivery == null) {
	                throw new NotFoundException("Not found delivery by index DaGiao");
	            }
	
	            List<SaleOrder> saleOrders = saleOrderRepository.findByDeliveryOrderByCreateAtDesc(delivery);
	            if (saleOrders.isEmpty()) {
	                return ResponseEntity.status(204).build();
	            }
	
	            List<SaleOrderResponseDTO> saleOrdersResponseDTO = new LinkedList<>();
	
	            for (int i = 0; i < saleOrders.size(); i++) {
	                SaleOrderResponseDTO saleOrderResponseDTO = new SaleOrderResponseDTO();
	                saleOrderResponseDTO.setId(saleOrders.get(i).getId());
	                saleOrderResponseDTO.setCreateAt(saleOrders.get(i).getCreateAt());
	                saleOrderResponseDTO.setUpdateAt(saleOrders.get(i).getUpdateAt());
	                saleOrderResponseDTO.setAddress(saleOrders.get(i).getAddress());
	                saleOrderResponseDTO.setDelivery(saleOrders.get(i).getDelivery());
	                saleOrderResponseDTO.setPhone(saleOrders.get(i).getPhone());
	                saleOrderResponseDTO.setOrderItems(saleOrders.get(i).getOrderItems());
	                saleOrderResponseDTO.setUser(saleOrders.get(i).getUser());
	                saleOrderResponseDTO.setProductImage(saleOrders.get(i).getOrderItems().get(0).getProduct().getProductImages().get(0));
	                saleOrdersResponseDTO.add(saleOrderResponseDTO);
	            }
	
	            return ResponseEntity.status(200).body(saleOrdersResponseDTO);
	        }
	        
	        
	    }
	    if (deliveryId != null) {
	        List<SaleOrder> saleOrders = saleOrderRepository.findByDeliveryId(deliveryId);
	        if (saleOrders.isEmpty()) {
	            return ResponseEntity.status(204).build();
	        }
	        return ResponseEntity.status(200).body(saleOrders);
	    }
	    if (saleOrderId != null) {
	        Optional<SaleOrder> saleOrders = saleOrderRepository.findById(saleOrderId);
	        if (!saleOrders.isPresent()) {
	            return ResponseEntity.status(204).build();
	        }
	        return ResponseEntity.status(200).body(Collections.singletonList(saleOrders.get()));
	    }
	    List<SaleOrder> saleOrders = saleOrderRepository.findAll();
	    if (saleOrders.isEmpty()) {
	        return ResponseEntity.status(204).build();
	    }
	
	    return ResponseEntity.status(200).body(saleOrders);
	}
	
	
    @GetMapping("/{saleOrderId}")
    public ResponseEntity<?> getSaleOrderById(@PathVariable("saleOrderId") Long saleOrderId) {
        Optional<SaleOrder> optionalSaleOrder = saleOrderRepository.findById(saleOrderId);
        if (!optionalSaleOrder.isPresent()) {
            throw new NotFoundException("Not found sale order by id " + saleOrderId);
        }

        SaleOrder saleOrder = optionalSaleOrder.get();

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setId(saleOrder.getId());
        orderDetail.setCreateAt(saleOrder.getCreateAt());
        orderDetail.setUpdateAt(saleOrder.getUpdateAt());
        orderDetail.setCustomerAddress(saleOrder.getAddress());
        orderDetail.setDelivery(saleOrder.getDelivery());
        orderDetail.setUser(saleOrder.getUser());
        orderDetail.setPhone(saleOrder.getPhone());

        List<OrderItemDetailDTO> orderItemDetailDTOs = new LinkedList<>();

        for (int i = 0; i < saleOrder.getOrderItems().size(); i++) {
            OrderItem orderItem = saleOrder.getOrderItems().get(i);
            ProductImage productImage = orderItem.getProduct().getProductImages().get(0);

            OrderItemDetailDTO orderItemDetailDTO = new OrderItemDetailDTO();
            orderItemDetailDTO.setOrderItem(orderItem);
            orderItemDetailDTO.setProductImage(productImage);

            orderItemDetailDTOs.add(orderItemDetailDTO);
        }

        orderDetail.setOrderItems(orderItemDetailDTOs);

        return ResponseEntity.status(200).body(orderDetail);
    }

    @PatchMapping("/{saleOrderId}")
    public ResponseEntity<?> editSaleOrderDelivery(@PathVariable("saleOrderId") Long saleOrderId, @RequestBody DeliveryDTO deliveryDTO) {

        Optional<SaleOrder> optionalSaleOrder = saleOrderRepository.findById(saleOrderId);
        if (!optionalSaleOrder.isPresent()) {
            throw new NotFoundException("Not found sale order by saleOrderId " + saleOrderId);
        }

        Optional<Delivery> optionalDelivery = deliveryRepository.findById(deliveryDTO.getDeliveryId());
        if (!optionalDelivery.isPresent()) {
            throw new NotFoundException("Not found delivery by deliveryId " + deliveryDTO.getDeliveryId());
        }

        SaleOrder saleOrder = optionalSaleOrder.get();
        saleOrder.setDelivery(optionalDelivery.get());

        SaleOrder newSaleOrder = saleOrderRepository.save(saleOrder);

        return ResponseEntity.status(200).body(newSaleOrder);
    }
}
