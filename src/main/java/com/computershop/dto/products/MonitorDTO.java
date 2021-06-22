package com.computershop.dto.products;

import com.computershop.dto.ProductDTO;

public class MonitorDTO {
	private ProductDTO productDTO;

	private String screenSize;

	private String maximumResolution;

	private String nativeResolution;

	private String color;

	private String refreshRate;

	private String aspectRatio;

	private String touchScreen;

	public ProductDTO getProductDTO() {
		return productDTO;
	}

	public void setProductDTO(ProductDTO productDTO) {
		this.productDTO = productDTO;
	}

	public String getScreenSize() {
		return screenSize;
	}

	public void setScreenSize(String screenSize) {
		this.screenSize = screenSize;
	}

	public String getMaximumResolution() {
		return maximumResolution;
	}

	public void setMaximumResolution(String maximumResolution) {
		this.maximumResolution = maximumResolution;
	}

	public String getNativeResolution() {
		return nativeResolution;
	}

	public void setNativeResolution(String nativeResolution) {
		this.nativeResolution = nativeResolution;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getRefreshRate() {
		return refreshRate;
	}

	public void setRefreshRate(String refreshRate) {
		this.refreshRate = refreshRate;
	}

	public String getAspectRatio() {
		return aspectRatio;
	}

	public void setAspectRatio(String aspectRatio) {
		this.aspectRatio = aspectRatio;
	}

	public String getTouchScreen() {
		return touchScreen;
	}

	public void setTouchScreen(String touchScreen) {
		this.touchScreen = touchScreen;
	}

}
