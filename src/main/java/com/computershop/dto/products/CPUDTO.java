package com.computershop.dto.products;

import com.computershop.dto.ProductDTO;

public class CPUDTO {
	private ProductDTO productDTO;
	
	private String codeName;

	private String CPUFamily;

	private String cores;

	private String threads;

	private String baseFrequency;

	private String maxFrequency;

	private String PCIExpress;

	private String busSpeed;

	private String tdp;

	private String socket;

	private String cache;

	

	public ProductDTO getProductDTO() {
		return productDTO;
	}

	public void setProductDTO(ProductDTO productDTO) {
		this.productDTO = productDTO;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public String getCPUFamily() {
		return CPUFamily;
	}

	public void setCPUFamily(String cPUFamily) {
		CPUFamily = cPUFamily;
	}

	public String getCores() {
		return cores;
	}

	public void setCores(String cores) {
		this.cores = cores;
	}

	public String getThreads() {
		return threads;
	}

	public void setThreads(String threads) {
		this.threads = threads;
	}

	public String getBaseFrequency() {
		return baseFrequency;
	}

	public void setBaseFrequency(String baseFrequency) {
		this.baseFrequency = baseFrequency;
	}

	public String getMaxFrequency() {
		return maxFrequency;
	}

	public void setMaxFrequency(String maxFrequency) {
		this.maxFrequency = maxFrequency;
	}

	public String getPCIExpress() {
		return PCIExpress;
	}

	public void setPCIExpress(String pCIExpress) {
		PCIExpress = pCIExpress;
	}

	public String getBusSpeed() {
		return busSpeed;
	}

	public void setBusSpeed(String busSpeed) {
		this.busSpeed = busSpeed;
	}

	public String getTdp() {
		return tdp;
	}

	public void setTdp(String tdp) {
		this.tdp = tdp;
	}

	public String getSocket() {
		return socket;
	}

	public void setSocket(String socket) {
		this.socket = socket;
	}

	public String getCache() {
		return cache;
	}

	public void setCache(String cache) {
		this.cache = cache;
	}
	
	
}
