package com.computershop.dao.product;




import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.computershop.dao.Product;

@Entity
@Table(name = "CPUs")
public class CPU extends Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cpu_id")
	private Long cpuId;

	@Column(name = "code_name", nullable = false)
	private String codeName;

	@Column(name = "cpu_family")
	private String CPUFamily;

	@Column(name = "core")
	private String cores;

	@Column(name = "thread")
	private String threads;

	@Column(name = "base_frequency") // GHz
	private String baseFrequency;

	@Column(name = "max_frequency")
	private String maxFrequency;

	@Column(name = "pci_express")
	private String PCIExpress;

	@Column(name = "bus_speed")
	private String busSpeed;

	@Column(name = "tdp")
	private String tdp;

	@Column(name = "socket")
	private String socket;

	@Column(name = "cache")
	private String cache;

	public CPU(Product product) {
		super(product.getId(), product.getName(), product.getBrand(), product.getProductImages(), product.getRatings(),
				product.getCategory(), product.getManufactures(), product.getDescription(), product.getPrice(),
				product.getSaleOff(), product.getAmount(), product.getQuantitySold(), product.getWarranty(),
				product.getCreateAt(), product.getUpdateAt(), product.getOrderItems());
	}

	public CPU(Product product, Long id2, String codeName, String cPUFamily, String cores, String threads,
			String baseFrequency, String maxFrequency, String pCIExpress, String busSpeed, String tdp, String socket,
			String cache) {
		super(product.getId(), product.getName(), product.getBrand(), product.getProductImages(), product.getRatings(),
				product.getCategory(), product.getManufactures(), product.getDescription(), product.getPrice(),
				product.getSaleOff(), product.getAmount(), product.getQuantitySold(), product.getWarranty(),
				product.getCreateAt(), product.getUpdateAt(), product.getOrderItems());
		this.cpuId = id2;
		this.codeName = codeName;
		this.CPUFamily = cPUFamily;
		this.cores = cores;
		this.threads = threads;
		this.baseFrequency = baseFrequency;
		this.maxFrequency = maxFrequency;
		this.PCIExpress = pCIExpress;
		this.busSpeed = busSpeed;
		this.tdp = tdp;
		this.socket = socket;
		this.cache = cache;
	}

	

	public CPU() {
		super();
	}

	public Long getCpuId() {
		return cpuId;
	}

	public void setCpuId(Long cpuId) {
		this.cpuId = cpuId;
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
