package com.computershop.dto;

import java.util.List;

import com.computershop.dao.Product;

public class ProductWithImage {
	private Product product;
	private List<CloudinaryImage> cloudinaryImage;

	public ProductWithImage(Product product, List<CloudinaryImage> cloudinaryImage) {
		super();
		this.product = product;
		this.cloudinaryImage = cloudinaryImage;
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

	public List<CloudinaryImage> getCloudinaryImage() {
		return cloudinaryImage;
	}

	public void setCloudinaryImage(List<CloudinaryImage> cloudinaryImage) {
		this.cloudinaryImage = cloudinaryImage;
	}

}
