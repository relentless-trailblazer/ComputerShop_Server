package com.computershop.dao.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.computershop.dao.Product;

@Entity
@Table(name = "HardDisks")
public class HardDisk extends Product {
	@Id
	@Column(name = "hard_disk_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long hardDiskId;

	@Column(name = "interface_type")
	private String interfaceType;

	@Column
	private String cache;

	@Column
	private String capacity;

	@Column
	private String style; // HDD, SSD ...

	@Column
	private String size;

	public HardDisk(Product product) {
		super(product.getId(), product.getName(), product.getBrand(), product.getProductImages(), product.getRatings(),
				product.getCategory(), product.getManufactures(), product.getDescription(), product.getPrice(),
				product.getSaleOff(), product.getAmount(), product.getQuantitySold(), product.getWarranty(),
				product.getCreateAt(), product.getUpdateAt(), product.getOrderItems());

	}

	public HardDisk(Product product, Long id2, String interfaceType, String cache, String capacity, String style,
			String size) {
		super(product.getId(), product.getName(), product.getBrand(), product.getProductImages(), product.getRatings(),
				product.getCategory(), product.getManufactures(), product.getDescription(), product.getPrice(),
				product.getSaleOff(), product.getAmount(), product.getQuantitySold(), product.getWarranty(),
				product.getCreateAt(), product.getUpdateAt(), product.getOrderItems());
		this.hardDiskId = id2;
		this.interfaceType = interfaceType;
		this.cache = cache;
		this.capacity = capacity;
		this.style = style;
		this.size = size;
	}

	public HardDisk() {
		super();
	}

	public Long getHardDiskId() {
		return hardDiskId;
	}

	public void setHardDiskId(Long id) {
		this.hardDiskId = id;
	}

	public String getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}

	public String getCache() {
		return cache;
	}

	public void setCache(String cache) {
		this.cache = cache;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

}
