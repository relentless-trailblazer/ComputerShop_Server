package com.computershop.dto.products;

import com.computershop.dto.ProductDTO;

public class RamDTO {
	private ProductDTO productDTO;
	
	private String partNumber; // Mã SP

	private String capacity; // dung lượng (8gb/16gb..)

	private String DDR; // DDR4, DDR3, ....

	private String typeOfBus; // Buss 2400, Bus 2133,

	private String DimmType; // UDIMM, RDIMM...

	

	public ProductDTO getProductDTO() {
		return productDTO;
	}

	public void setProductDTO(ProductDTO productDTO) {
		this.productDTO = productDTO;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public String getDDR() {
		return DDR;
	}

	public void setDDR(String dDR) {
		DDR = dDR;
	}

	public String getTypeOfBus() {
		return typeOfBus;
	}

	public void setTypeOfBus(String typeOfBus) {
		this.typeOfBus = typeOfBus;
	}

	public String getDimmType() {
		return DimmType;
	}

	public void setDimmType(String dimmType) {
		DimmType = dimmType;
	}
	
	
}
