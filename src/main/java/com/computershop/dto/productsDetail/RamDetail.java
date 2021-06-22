package com.computershop.dto.productsDetail;

import java.util.List;

import com.computershop.dao.ProductImage;
import com.computershop.dao.product.Ram;

public class RamDetail {
	private Ram ram;
	private List<ProductImage> productImages;

	public Ram getRam() {
		return ram;
	}

	public void setRam(Ram ram) {
		this.ram = ram;
	}

	public List<ProductImage> getProductImages() {
		return productImages;
	}

	public void setProductImages(List<ProductImage> productImages) {
		this.productImages = productImages;
	}

	public RamDetail(Ram ram, List<ProductImage> productImages) {
		super();
		this.ram = ram;
		this.productImages = productImages;
	}

	public RamDetail() {
		super();
	}

}
