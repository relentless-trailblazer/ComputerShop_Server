package com.computershop.dto.productsDetail;

import java.util.List;

import com.computershop.dao.ProductImage;
import com.computershop.dao.product.Monitor;

public class MonitorDetail {
	private Monitor monitor;
	private List<ProductImage> productImages;

	public MonitorDetail(Monitor monitor, List<ProductImage> productImages) {
		super();
		this.monitor = monitor;
		this.productImages = productImages;
	}

	public MonitorDetail() {
		super();
	}

	public Monitor getMonitor() {
		return monitor;
	}

	public void setMonitor(Monitor monitor) {
		this.monitor = monitor;
	}

	public List<ProductImage> getProductImages() {
		return productImages;
	}

	public void setProductImages(List<ProductImage> productImages) {
		this.productImages = productImages;
	}

}
