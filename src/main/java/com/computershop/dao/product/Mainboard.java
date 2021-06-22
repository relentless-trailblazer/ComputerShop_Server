package com.computershop.dao.product;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.computershop.dao.Product;

@Entity
@Table(name = "Mainboards")
public class Mainboard extends Product {
	@Id
	@Column(name = "mainboard_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long mainboardId;

	@Column(name = "chipset_support")
	private String chipset; // chipset

	@Column(name = "cpu_support")
	private String cpu; // cpu hỗ trợ

	@Column(name = "socket")
	private String socket; // socket ho tro

	@Column(name = "accessories")
	private String accessories; // phu kien

	@Column(name = "formFactors")
	private String formFactors; // kich thuoc

	@Column(name = "OS_support")
	private String OSs; // os ho tro

	public Mainboard(Product product) {
		super(product.getId(), product.getName(), product.getBrand(), product.getProductImages(), product.getRatings(),
				product.getCategory(), product.getManufactures(), product.getDescription(), product.getPrice(),
				product.getSaleOff(), product.getAmount(), product.getQuantitySold(), product.getWarranty(),
				product.getCreateAt(), product.getUpdateAt(), product.getOrderItems());

	}

	public Mainboard(Product product, Long id2, String chipset, String cpu, String socket, String accessories,
			String formFactors, String oSs) {
		super(product.getId(), product.getName(), product.getBrand(), product.getProductImages(), product.getRatings(),
				product.getCategory(), product.getManufactures(), product.getDescription(), product.getPrice(),
				product.getSaleOff(), product.getAmount(), product.getQuantitySold(), product.getWarranty(),
				product.getCreateAt(), product.getUpdateAt(), product.getOrderItems());
		this.mainboardId = id2;
		this.chipset = chipset;
		this.cpu = cpu;
		this.socket = socket;
		this.accessories = accessories;
		this.formFactors = formFactors;
		this.OSs = oSs;
	}

	

	public Long getMainboardId() {
		return mainboardId;
	}

	public void setMainboardId(Long id) {
		this.mainboardId = id;
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
