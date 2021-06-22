package com.computershop.dao.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.computershop.dao.Product;
@Entity
@Table(name = "PowerSupplies")
public class PowerSupply extends Product {
	@Id
	@Column(name = "power_supply_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long powerSupplyId;

	@Column(name = "connector_type")
	private String connectorType; // Sata/Data

	@Column
	private String dimentions;

	@Column(name = "input_voltage")
	private String inputVoltage;

	@Column(name = "rated_current")
	private String ratedCurrent; // 7A, 1.5A, 2A, ...

	@Column(name = "output_voltage")
	private String outputVoltage;

	public PowerSupply(Product product) {
		super(product.getId(), product.getName(), product.getBrand(), product.getProductImages(), product.getRatings(),
				product.getCategory(), product.getManufactures(), product.getDescription(), product.getPrice(),
				product.getSaleOff(), product.getAmount(), product.getQuantitySold(), product.getWarranty(),
				product.getCreateAt(), product.getUpdateAt(), product.getOrderItems());

	}

	public PowerSupply(Product product, Long id2, String connectorType, String dimentions, String inputVoltage,
			String ratedCurrent, String outputVoltage) {
		super(product.getId(), product.getName(), product.getBrand(), product.getProductImages(), product.getRatings(),
				product.getCategory(), product.getManufactures(), product.getDescription(), product.getPrice(),
				product.getSaleOff(), product.getAmount(), product.getQuantitySold(), product.getWarranty(),
				product.getCreateAt(), product.getUpdateAt(), product.getOrderItems());
		this.powerSupplyId = id2;
		this.connectorType = connectorType;
		this.dimentions = dimentions;
		this.inputVoltage = inputVoltage;
		this.ratedCurrent = ratedCurrent;
		this.outputVoltage = outputVoltage;
	}


	public PowerSupply() {
		super();
	}

	public Long getPowerSupplyId() {
		return powerSupplyId;
	}

	public void setPowerSupplyId(Long id) {
		this.powerSupplyId = id;
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
