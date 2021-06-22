package com.computershop.dto;

import com.computershop.dao.OrderItem;
import com.computershop.dao.ProductImage;

public class OrderItemDetailDTO {
	private OrderItem orderItem;

	private ProductImage productImage;

	public OrderItemDetailDTO() {
	}

	public OrderItemDetailDTO(OrderItem orderItem, ProductImage productImage) {
		super();
		this.orderItem = orderItem;
		this.productImage = productImage;
	}

	public OrderItem getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(OrderItem orderItem) {
		this.orderItem = orderItem;
	}

	public ProductImage getProductImage() {
		return productImage;
	}

	public void setProductImage(ProductImage productImage) {
		this.productImage = productImage;
	}

}
