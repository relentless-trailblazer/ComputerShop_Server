package com.computershop.dto;

import java.sql.Timestamp;
import java.util.List;

import com.computershop.dao.Delivery;
import com.computershop.dao.User;

public class OrderDetail {

	private Long id;

	private String customerAddress;

	private String phone;

	private User user;

	private Delivery delivery;

	private List<OrderItemDetailDTO> orderItems;

	private Timestamp createAt;

	private Timestamp updateAt;

	public OrderDetail() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
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

	public List<OrderItemDetailDTO> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItemDetailDTO> orderItems) {
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
