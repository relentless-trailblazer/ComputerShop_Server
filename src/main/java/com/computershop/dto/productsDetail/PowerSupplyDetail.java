package com.computershop.dto.productsDetail;

import java.util.List;

import com.computershop.dao.ProductImage;
import com.computershop.dao.product.PowerSupply;

public class PowerSupplyDetail {
	private PowerSupply PowerSupply;
	private List<ProductImage> productImages;

	public PowerSupplyDetail(com.computershop.dao.product.PowerSupply powerSupply, List<ProductImage> productImages) {
		super();
		PowerSupply = powerSupply;
		this.productImages = productImages;
	}

	public PowerSupplyDetail() {
		super();
	}

	public PowerSupply getPowerSupply() {
		return PowerSupply;
	}

	public void setPowerSupply(PowerSupply powerSupply) {
		PowerSupply = powerSupply;
	}

	public List<ProductImage> getProductImages() {
		return productImages;
	}

	public void setProductImages(List<ProductImage> productImages) {
		this.productImages = productImages;
	}

}
