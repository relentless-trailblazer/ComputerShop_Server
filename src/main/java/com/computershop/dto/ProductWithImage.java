package com.computershop.dto;

import java.util.List;

import com.computershop.dao.Product;
import com.computershop.dao.ProductImage;

public class ProductWithImage {
	private Product product;
	private List<ProductImage> productImages;

	public ProductWithImage(Product product, List<ProductImage> productImages) {
		super();
		this.product = product;
		this.productImages = productImages;
	}

	public ProductWithImage() {
		super();
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public List<ProductImage> getProductImages() {
		return productImages;
	}

	public void setProductImages(List<ProductImage> productImages) {
		this.productImages = productImages;
	}

}
