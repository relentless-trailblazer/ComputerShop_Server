package com.computershop.dto;

public class ProductDTO {
	private String name;

	private String brand;
	
	private String description;
	
	private Long categoryId;

	private Long price;

	private Integer saleOff;

	private Integer amount;

	private String warranty;

	public ProductDTO() {
	}

	public ProductDTO(String name, String brand, String description, Long categoryId, Long price, Integer saleOff,
			Integer amount, String warranty) {
		super();
		this.name = name;
		this.brand = brand;
		this.description = description;
		this.categoryId = categoryId;
		this.price = price;
		this.saleOff = saleOff;
		this.amount = amount;
		this.warranty = warranty;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public Integer getSaleOff() {
		return saleOff;
	}

	public void setSaleOff(Integer saleOff) {
		this.saleOff = saleOff;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getWarranty() {
		return warranty;
	}

	public void setWarranty(String warranty) {
		this.warranty = warranty;
	}

	
}
