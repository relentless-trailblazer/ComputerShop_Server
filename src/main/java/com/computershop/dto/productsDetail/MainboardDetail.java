package com.computershop.dto.productsDetail;

import java.util.List;

import com.computershop.dao.ProductImage;
import com.computershop.dao.product.Mainboard;

public class MainboardDetail {
	private Mainboard mainboard;
	private List<ProductImage> productImages;

	public Mainboard getMainboard() {
		return mainboard;
	}

	public void setMainboard(Mainboard mainboard) {
		this.mainboard = mainboard;
	}

	public List<ProductImage> getProductImages() {
		return productImages;
	}

	public void setProductImages(List<ProductImage> productImages) {
		this.productImages = productImages;
	}

	public MainboardDetail(Mainboard mainboard, List<ProductImage> productImages) {
		super();
		this.mainboard = mainboard;
		this.productImages = productImages;
	}

	public MainboardDetail() {
		super();
	}

}
