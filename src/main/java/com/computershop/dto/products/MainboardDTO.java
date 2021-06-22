package com.computershop.dto.products;

import com.computershop.dto.ProductDTO;

public class MainboardDTO {
	private ProductDTO productDTO;

	private String chipset; // chipset

	private String cpu; // cpu hỗ trợ

	private String socket; // socket ho tro

	private String accessories; // phu kien

	private String formFactors; // kich thuoc

	private String OSs; // os ho tro

	public ProductDTO getProductDTO() {
		return productDTO;
	}

	public void setProductDTO(ProductDTO productDTO) {
		this.productDTO = productDTO;
	}

	public String getChipset() {
		return chipset;
	}

	public void setChipset(String chipset) {
		this.chipset = chipset;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public String getSocket() {
		return socket;
	}

	public void setSocket(String socket) {
		this.socket = socket;
	}

	public String getAccessories() {
		return accessories;
	}

	public void setAccessories(String accessories) {
		this.accessories = accessories;
	}

	public String getFormFactors() {
		return formFactors;
	}

	public void setFormFactors(String formFactors) {
		this.formFactors = formFactors;
	}

	public String getOSs() {
		return OSs;
	}

	public void setOSs(String oSs) {
		OSs = oSs;
	}

}
