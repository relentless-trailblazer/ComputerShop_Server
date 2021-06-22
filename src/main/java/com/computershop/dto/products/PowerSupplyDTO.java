package com.computershop.dto.products;

import com.computershop.dto.ProductDTO;

public class PowerSupplyDTO {
	private ProductDTO productDTO;

	private String connectorType; // Sata/Data

	private String dimentions;

	private String inputVoltage;

	private String ratedCurrent; // 7A, 1.5A, 2A, ...

	private String outputVoltage;

	

	public ProductDTO getProductDTO() {
		return productDTO;
	}

	public void setProductDTO(ProductDTO productDTO) {
		this.productDTO = productDTO;
	}

	public String getConnectorType() {
		return connectorType;
	}

	public void setConnectorType(String connectorType) {
		this.connectorType = connectorType;
	}

	public String getDimentions() {
		return dimentions;
	}

	public void setDimentions(String dimentions) {
		this.dimentions = dimentions;
	}

	public String getInputVoltage() {
		return inputVoltage;
	}

	public void setInputVoltage(String inputVoltage) {
		this.inputVoltage = inputVoltage;
	}

	public String getRatedCurrent() {
		return ratedCurrent;
	}

	public void setRatedCurrent(String ratedCurrent) {
		this.ratedCurrent = ratedCurrent;
	}

	public String getOutputVoltage() {
		return outputVoltage;
	}

	public void setOutputVoltage(String outputVoltage) {
		this.outputVoltage = outputVoltage;
	}
	
	
	
	
}
