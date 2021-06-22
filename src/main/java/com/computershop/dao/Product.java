package com.computershop.dao;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Products")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private Long id;

	@Column(name = "name", nullable = false)
	@Nationalized
	private String name;

	@Column(name = "brand", nullable = false)
	@Nationalized
	private String brand;

	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<ProductImage> productImages;

	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<ProductRating> Ratings;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<OrderItem> orderItems;

	@ManyToOne
	@JoinColumn(name = "manufacture_id")
	private Category manufactures;

	@Column(name = "description")
	@Nationalized
	private String description;

	@Column(name = "price", nullable = false)
	private Long price;

	@Column(name = "sale_off", nullable = false)
	private Integer saleOff;

	@Column(name = "amount", nullable = false)
	private Integer amount;

	@Column(name = "quantity_sold", nullable = false)
	private Integer quantitySold;

	@Column(name = "warranty", nullable = false)
	private String warranty;

	@CreationTimestamp
	private Timestamp createAt;

	@UpdateTimestamp
	private Timestamp updateAt;

	public Product(Long id, String name, String brand, List<ProductImage> productImages, List<ProductRating> ratings,
			Category category, Category manufactures, String description, Long price,
			Integer saleOff, Integer amount, Integer quantitySold, String warranty, Timestamp createAt,
			Timestamp updateAt, List<OrderItem> orderItems) {
		super();
		this.id = id;
		this.name = name;
		this.brand = brand;
		this.productImages = productImages;
		this.Ratings = ratings;
		this.category = category;
		this.orderItems = orderItems;
		this.manufactures = manufactures;
		this.description = description;
		this.price = price;
		this.saleOff = saleOff;
		this.amount = amount;
		this.quantitySold = quantitySold;
		this.warranty = warranty;
		this.createAt = createAt;
		this.updateAt = updateAt;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public Product() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public List<ProductImage> getProductImages() {
		return productImages;
	}

	public void setProductImages(List<ProductImage> productImages) {
		this.productImages = productImages;
	}

	public List<ProductRating> getRatings() {
		return Ratings;
	}

	public void setRatings(List<ProductRating> ratings) {
		Ratings = ratings;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Integer getQuantitySold() {
		return quantitySold;
	}

	public void setQuantitySold(Integer quantitySold) {
		this.quantitySold = quantitySold;
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

	public String getWarranty() {
		return warranty;
	}

	public void setWarranty(String warranty) {
		this.warranty = warranty;
	}

	public Category getManufactures() {
		return manufactures;
	}

	public void setManufactures(Category manufactures) {
		this.manufactures = manufactures;
	}

}
