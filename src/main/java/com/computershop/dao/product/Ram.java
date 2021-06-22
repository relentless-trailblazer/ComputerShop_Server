package com.computershop.dao.product;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.computershop.dao.Product;

@Entity
@Table(name = "Rams")
public class Ram extends Product {
	@Id
	@Column(name = "ram_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ramId;

	@Column(name = "part_number")
	private String partNumber; // Mã SP

	@Column
	private String capacity; // dung lượng (8gb/16gb..)

	@Column
	private String DDR; // DDR4, DDR3, ....

	@Column(name = "type_of_bus")
	private String typeOfBus; // Buss 2400, Bus 2133,

	@Column(name = "dimm_type")
	private String DimmType; // UDIMM, RDIMM...

	public Ram(Product product) {
		super(product.getId(), product.getName(), product.getBrand(), product.getProductImages(), product.getRatings(),
				product.getCategory(), product.getManufactures(), product.getDescription(), product.getPrice(),
				product.getSaleOff(), product.getAmount(), product.getQuantitySold(), product.getWarranty(),
				product.getCreateAt(), product.getUpdateAt(), product.getOrderItems());

	}

	public Ram(Product product, Long id2, String partNumber, String capacity, String dDR, String typeOfBus,
			String dimmType) {
		super(product.getId(), product.getName(), product.getBrand(), product.getProductImages(), product.getRatings(),
				product.getCategory(), product.getManufactures(), product.getDescription(), product.getPrice(),
				product.getSaleOff(), product.getAmount(), product.getQuantitySold(), product.getWarranty(),
				product.getCreateAt(), product.getUpdateAt(), product.getOrderItems());
		this.ramId = id2;
		this.partNumber = partNumber;
		this.capacity = capacity;
		this.DDR = dDR;
		this.typeOfBus = typeOfBus;
		this.DimmType = dimmType;
	}

	public Ram() {
		super();
	}

	public Long getRamId() {
		return ramId;
	}

	public void setRamId(Long id) {
		this.ramId = id;
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
