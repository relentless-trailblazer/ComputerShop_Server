package com.computershop.dto.productsDetail;

import java.util.List;

import com.computershop.dao.ProductImage;
import com.computershop.dao.product.Case;

public class CaseDetail {
	private Case product;
	private List<ProductImage> productImages;

	public Case getProduct() {
		return product;
	}

	public void setProduct(Case product) {
		this.product = product;
	}

	public List<ProductImage> getProductImages() {
		return productImages;
	}

	public void setProductImages(List<ProductImage> productImages) {
		this.productImages = productImages;
	}

	public CaseDetail(Case product, List<ProductImage> productImages) {
		super();
		this.product = product;
		this.productImages = productImages;
	}

	public CaseDetail() {
		super();
	}

}
