package com.computershop.dto;

import java.sql.Timestamp;
import java.util.List;

import com.computershop.dao.Delivery;
import com.computershop.dao.OrderItem;
import com.computershop.dao.ProductImage;
import com.computershop.dao.User;

public class SaleOrderResponseDTO {
	private Long id;

	private String address;

	private ProductImage productImage;

	private String phone;

	private User user;

	private Delivery delivery;

	private List<OrderItem> orderItems;

	private Timestamp createAt;

	private Timestamp updateAt;

	public SaleOrderResponseDTO(Long id, String address, ProductImage productImage, String phone, User user,
			Delivery delivery, List<OrderItem> orderItems, Timestamp createAt, Timestamp updateAt) {
		super();
		this.id = id;
		this.address = address;
		this.productImage = productImage;
		this.phone = phone;
		this.user = user;
		this.delivery = delivery;
		this.orderItems = orderItems;
		this.createAt = createAt;
		this.updateAt = updateAt;
	}

	public SaleOrderResponseDTO() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public ProductImage getProductImage() {
		return productImage;
	}

	public void setProductImage(ProductImage productImage) {
		this.productImage = productImage;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Delivery getDelivery() {
		return delivery;
	}

	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public Timestamp getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Timestamp createAt) {
		this.createAt = createAt;
	}

	public Timestamp getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Timestamp updateAt) {
		this.updateAt = updateAt;
	}

}
