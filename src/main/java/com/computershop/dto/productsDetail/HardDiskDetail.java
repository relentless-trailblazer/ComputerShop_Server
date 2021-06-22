package com.computershop.dto.productsDetail;

import java.util.List;

import com.computershop.dao.ProductImage;
import com.computershop.dao.product.HardDisk;

public class HardDiskDetail {
	private HardDisk hardDisk;
	private List<ProductImage> productImages;

	public HardDisk getHardDisk() {
		return hardDisk;
	}

	public void setHardDisk(HardDisk hardDisk) {
		this.hardDisk = hardDisk;
	}

	public List<ProductImage> getProductImages() {
		return productImages;
	}

	public void setProductImages(List<ProductImage> productImages) {
		this.productImages = productImages;
	}

	public HardDiskDetail(HardDisk hardDisk, List<ProductImage> productImages) {
		super();
		this.hardDisk = hardDisk;
		this.productImages = productImages;
	}

	public HardDiskDetail() {
		super();
	}

}
