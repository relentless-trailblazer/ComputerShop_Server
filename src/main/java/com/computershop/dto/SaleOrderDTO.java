package com.computershop.dto;

public class SaleOrderDTO {
	private Long userId;
	private Long saleOrderId;

	public SaleOrderDTO() {
	}

	public SaleOrderDTO(Long userId, Long saleOrderId) {
		super();
		this.userId = userId;
		this.saleOrderId = saleOrderId;
	}

	public Long getSaleOrderId() {
		return saleOrderId;
	}

	public void setSaleOrderId(Long saleOrderId) {
		this.saleOrderId = saleOrderId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
