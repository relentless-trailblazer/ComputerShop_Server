package com.computershop.dto.products;

import com.computershop.dto.ProductDTO;

public class CaseDTO {

	private ProductDTO productDTO;

	private String dimensions;

	private String material;

	private String type;

	private String color;

	private String weight;

	private String coolingMethod;


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

	public ProductDTO getProductDTO() {
		return productDTO;
	}

	public void setProduct(ProductDTO productDTO) {
		this.productDTO = productDTO;
	}
	
	
	
}
