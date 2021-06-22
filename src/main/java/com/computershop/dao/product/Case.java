package com.computershop.dao.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Nationalized;

import com.computershop.dao.Product;

@Entity
@Table(name = "Cases")
public class Case extends Product {
	@Id
	@Column(name = "case_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long caseId;

	@Column(name = "dimensions")
	private String dimensions;

	@Column(name = "material")
	@Nationalized
	private String material;

	@Column(name = "type")
	@Nationalized
	private String type;

	@Column(name = "color")
	@Nationalized
	private String color;

	@Column(name = "weight")
	private String weight;

	@Column(name = "cooling_method")
	@Nationalized
	private String coolingMethod;

	public Case(Product product) {
		super(product.getId(), product.getName(), product.getBrand(), product.getProductImages(), product.getRatings(),
				product.getCategory(), product.getManufactures(), product.getDescription(), product.getPrice(),
				product.getSaleOff(), product.getAmount(), product.getQuantitySold(), product.getWarranty(),
				product.getCreateAt(), product.getUpdateAt(), product.getOrderItems());

	}

	public Case(Product product, Long caseId, String dimensions, String material, String type, String color,
			String weight, String coolingMethod) {
		super(product.getId(), product.getName(), product.getBrand(), product.getProductImages(), product.getRatings(),
				product.getCategory(), product.getManufactures(), product.getDescription(), product.getPrice(),
				product.getSaleOff(), product.getAmount(), product.getQuantitySold(), product.getWarranty(),
				product.getCreateAt(), product.getUpdateAt(), product.getOrderItems());
		this.caseId = caseId;
		this.dimensions = dimensions;
		this.material = material;
		this.type = type;
		this.color = color;
		this.weight = weight;
		this.coolingMethod = coolingMethod;
	}

	public Case() {
		super();
	}

	public Long getCaseId() {
		return caseId;
	}

	public void setCaseId(Long id) {
		this.caseId = id;
	}

	public String getDimensions() {
		return dimensions;
	}

	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getCoolingMethod() {
		return coolingMethod;
	}

	public void setCoolingMethod(String coolingMethod) {
		this.coolingMethod = coolingMethod;
	}

}
