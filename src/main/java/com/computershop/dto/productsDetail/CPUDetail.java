package com.computershop.dto.productsDetail;

import java.util.List;

import com.computershop.dao.ProductImage;
import com.computershop.dao.product.CPU;

public class CPUDetail {
	private CPU product;
	private List<ProductImage> productImages;

	public CPU getProduct() {
		return product;
	}

	public void setProduct(CPU product) {
		this.product = product;
	}

	public List<ProductImage> getProductImages() {
		return productImages;
	}

	public void setProductImages(List<ProductImage> productImages) {
		this.productImages = productImages;
	}

	public CPUDetail(CPU product, List<ProductImage> productImages) {
		super();
		this.product = product;
		this.productImages = productImages;
	}

	public CPUDetail() {
		super();
	}

}
