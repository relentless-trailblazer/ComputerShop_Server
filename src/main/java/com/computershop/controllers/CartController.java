package com.computershop.controllers;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.computershop.dao.Delivery;
import com.computershop.dao.OrderItem;
import com.computershop.dao.Product;
import com.computershop.dao.SaleOrder;
import com.computershop.dao.User;
import com.computershop.dto.CartItemDTO;
import com.computershop.dto.OrderItemDTO;
import com.computershop.exceptions.NotFoundException;
import com.computershop.repositories.DeliveryRepository;
import com.computershop.repositories.OrderItemRepository;
import com.computershop.repositories.ProductRepository;
import com.computershop.repositories.SaleOrderRepository;
import com.computershop.repositories.UserRepository;

@RestController
@RequestMapping(value = "/api/carts")
public class CartController {

	@Autowired
	private SaleOrderRepository saleOrderRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private DeliveryRepository deliveryRepository;
	
	@GetMapping("/users/{userId}")
	public ResponseEntity<?> getOrderItemsByUserId(@PathVariable("userId") Long userId) {
		Optional<User> optionalUser = userRepository.findById(userId);
		if (!optionalUser.isPresent()) {
			throw new NotFoundException("Not found user with userId " + userId);
		}

		Delivery delivery = deliveryRepository.findByIndex("DaThemVaoGio");
		User user = optionalUser.get();

		SaleOrder saleOrder = saleOrderRepository.findByUserIdAndDeliveryId(user.getId(), delivery.getId());

		if (saleOrder == null) {
			throw new NotFoundException("Not found cart with userId " + userId);
		}

		List<CartItemDTO> cartItemDTOs = new LinkedList<>();
		for (int i = 0; i < saleOrder.getOrderItems().size(); i++) {
			OrderItem orderItem = saleOrder.getOrderItems().get(i);
			CartItemDTO cartItemDTO = new CartItemDTO();
			cartItemDTO.setOrderItemId(orderItem.getId());
			cartItemDTO.setProduct(orderItem.getProduct());
			cartItemDTO.setSaleOrder(orderItem.getSaleOrder());
			cartItemDTO.setQuantity(orderItem.getQuantityOrder());
			cartItemDTO.setProductImage(orderItem.getProduct().getProductImages().get(0));
			cartItemDTOs.add(cartItemDTO);
		}

		return ResponseEntity.status(200).body(cartItemDTOs);
	}

	@PostMapping
	public ResponseEntity<?> postCart(@RequestBody OrderItemDTO orderItemDTO) {
		Optional<User> optionalUser = userRepository.findById(orderItemDTO.getUserId()); 
		if (!optionalUser.isPresent()) {
			throw new NotFoundException("Not found user with userId " + orderItemDTO.getUserId());
		}
		Optional<Product> optionalProduct = productRepository.findById(orderItemDTO.getProductId());
		if (!optionalProduct.isPresent()) {
			throw new NotFoundException("Not found product with productId " + orderItemDTO.getProductId());
		}

		Delivery delivery = deliveryRepository.findByIndex("DaThemVaoGio"); 

		User user = optionalUser.get();
		Product product = optionalProduct.get();

		// tìm sản phẩm đã được thêm vào giỏ hàng
		SaleOrder oldSaleOrder = saleOrderRepository.findByUserIdAndDeliveryId(user.getId(), delivery.getId());
		if (oldSaleOrder != null) {

			// sản phẩm đã có trong giỏ hàng
			OrderItem oldOrderItem = orderItemRepository.findBySaleOrderIdAndProductId(oldSaleOrder.getId(),
					product.getId());
			if (oldOrderItem != null) {
				oldOrderItem.setQuantityOrder(oldOrderItem.getQuantityOrder() + orderItemDTO.getQuantity());
				orderItemRepository.save(oldOrderItem);
			} else {
				OrderItem orderItem = new OrderItem();
				orderItem.setSaleOrder(oldSaleOrder);
				orderItem.setProduct(product);
				orderItem.setQuantityOrder(orderItemDTO.getQuantity());
				orderItemRepository.save(orderItem);
			}

			return ResponseEntity.status(201).body(oldSaleOrder.getOrderItems());
		}

		SaleOrder saleOrder = new SaleOrder();
		saleOrder.setUser(user);
		saleOrder.setDelivery(delivery);
		saleOrder.setAddress(user.getAddress());
		saleOrder.setPhone(user.getPhone());

		SaleOrder newSaleOrder = saleOrderRepository.save(saleOrder);

		OrderItem orderItem = new OrderItem();
		orderItem.setSaleOrder(newSaleOrder);
		orderItem.setProduct(product);
		orderItem.setQuantityOrder(orderItemDTO.getQuantity());
		orderItemRepository.save(orderItem);

		newSaleOrder.setOrderItems(Arrays.asList(orderItem));

		return ResponseEntity.status(201).body(newSaleOrder.getOrderItems());
	}
	
}
