package com.computershop.dto.products;

import com.computershop.dto.ProductDTO;

public class GraphicCardDTO {
	private ProductDTO productDTO;

	private String dimensions;

	private String weight;

	private String VGAMemory;

	private String bandwidth;

	private String voltage;

	public ProductDTO getProductDTO() {
		return productDTO;
	}

	public void setProductDTO(ProductDTO productDTO) {
		this.productDTO = productDTO;
	}

	public String getDimensions() {
		return dimensions;
	}

	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getVGAMemory() {
		return VGAMemory;
	}

	public void setVGAMemory(String vGAMemory) {
		VGAMemory = vGAMemory;
	}

	public String getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}

	public String getVoltage() {
		return voltage;
	}

	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}

}
