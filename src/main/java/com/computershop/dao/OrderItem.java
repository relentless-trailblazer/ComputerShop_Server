package com.computershop.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "OderItems")
public class OrderItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_item_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private SaleOrder saleOrder;

	@Column(name = "quantity_order", nullable = false)
	private Integer QuantityOrder;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	public OrderItem(Long id, SaleOrder saleOrder, Integer quantityOrder, Product product) {
		super();
		this.id = id;
		this.saleOrder = saleOrder;
		QuantityOrder = quantityOrder;
		this.product = product;
	}

	public OrderItem() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SaleOrder getSaleOrder() {
		return saleOrder;
	}

	public void setSaleOrder(SaleOrder saleOrder) {
		this.saleOrder = saleOrder;
	}

	public Integer getQuantityOrder() {
		return QuantityOrder;
	}

	public void setQuantityOrder(Integer quantityOrder) {
		QuantityOrder = quantityOrder;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
