package com.computershop.controllers.productsControllers;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.computershop.dao.Category;
import com.computershop.dao.Product;
import com.computershop.dao.ProductImage;
import com.computershop.dao.product.CPU;
import com.computershop.dto.products.CPUDTO;
import com.computershop.dto.productsDetail.CPUDetail;
import com.computershop.exceptions.DuplicateException;
import com.computershop.exceptions.InvalidException;
import com.computershop.exceptions.NotFoundException;
import com.computershop.helpers.ConvertObject;
import com.computershop.helpers.Validate;
import com.computershop.repositories.CategoryRepository;
import com.computershop.repositories.ProductRepository;
import com.computershop.repositories.productRepos.CPURepository;

@RestController
@RequestMapping("/api/products/cpus")
public class CPUController {
	@Autowired
	private CPURepository cpuRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@GetMapping
	public ResponseEntity<?> getAllCPU(@RequestParam(name = "page", required = false) Integer pageNum,
										@RequestParam(name = "type", required = false) String type,
										@RequestParam(name = "search", required = false) String search) {
		if (search != null) {
			String searchConvert = ConvertObject.fromSlugToString(search);
			List<CPU> listCpus = cpuRepository.findByNameContainingIgnoreCase(searchConvert);
			if (listCpus.size() == 0) {
				return ResponseEntity.noContent().build();
			}
			List<CPUDetail> listCpuDetail = new LinkedList<CPUDetail>();
			for (int i = 0; i < listCpus.size(); i++) {
				CPU getCPU = listCpus.get(i);
				List<ProductImage> getProductImages = getCPU.getProductImages();
				if (getProductImages.size() == 0) {
					getProductImages.add(new ProductImage());
				}
				listCpuDetail.add(new CPUDetail(getCPU, getProductImages));
			}

			return ResponseEntity.ok().body(listCpuDetail);
		}
		if (type != null) {
			if (type.compareTo("without-image") == 0) {
				List<CPU> listCpus = cpuRepository.findAll();
				if (listCpus.size() == 0) {
					return ResponseEntity.noContent().build();
				}
				return ResponseEntity.ok().body(listCpus);
			}
		}
		if (pageNum != null) {
			Page<CPU> page = cpuRepository.findAll(PageRequest.of(pageNum.intValue(), 20));

			List<CPU> listCpus = page.getContent();
			if (listCpus.size() == 0) {
				return ResponseEntity.noContent().build();
			}
			List<CPUDetail> listCpuDetail = new LinkedList<CPUDetail>();
			for (int i = 0; i < listCpus.size(); i++) {
				CPU getCpu = listCpus.get(i);
				List<ProductImage> getProductImages = getCpu.getProductImages();
				if (getProductImages.size() == 0) {
					getProductImages.add(new ProductImage());
				}
				listCpuDetail.add(new CPUDetail(getCpu, getProductImages));
			}
			return ResponseEntity.ok().body(listCpuDetail);
		}

		List<CPU> listCpus = cpuRepository.findAll();
		if (listCpus.size() == 0) {
			return ResponseEntity.noContent().build();
		}
		List<CPUDetail> listCpuDetail = new LinkedList<CPUDetail>();
		for (int i = 0; i < listCpus.size(); i++) {
			CPU getCpu = listCpus.get(i);
			List<ProductImage> getProductImages = getCpu.getProductImages();
			if (getProductImages.size() == 0) {
				getProductImages.add(new ProductImage());
			}
			listCpuDetail.add(new CPUDetail(getCpu, getProductImages));
		}
		return ResponseEntity.ok().body(listCpuDetail);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getCPUById(@PathVariable("id") Long id) {
		Optional<CPU> optionalCpu = cpuRepository.findById(id);
		CPU cpuFound = optionalCpu.get();
		if (cpuFound == null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		if (cpuFound.getProductImages().size() == 0) {
			return ResponseEntity.status(HttpStatus.OK).body(cpuFound);
		}
		CPUDetail cpuDetail = new CPUDetail(cpuFound, cpuFound.getProductImages());

		return ResponseEntity.ok().body(cpuDetail);
	}

	@PostMapping
	@PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
	public ResponseEntity<?> createNewCPU(@RequestBody CPUDTO cpuDTO) {
		Product oldProduct = productRepository.findByName(cpuDTO.getProductDTO().getName());
		if (oldProduct != null) {
			throw new DuplicateException("Product has already exists");
		}

		Optional<Category> optionalCategory = categoryRepository.findById(cpuDTO.getProductDTO().getCategoryId());

		if (!optionalCategory.isPresent()) {
			throw new NotFoundException(
					"Not found category with category id " + cpuDTO.getProductDTO().getCategoryId());
		}

		Product product = ConvertObject.fromProductDTOToProductDAO(cpuDTO.getProductDTO());
		product.setCategory(optionalCategory.get());
		Validate.checkProduct(product);
		Product saveProduct = productRepository.save(product);

		CPU newCPU = new CPU(saveProduct);
		newCPU.setCodeName(cpuDTO.getCodeName());
		newCPU.setCache(cpuDTO.getCache());
		newCPU.setCPUFamily(cpuDTO.getCPUFamily());
		newCPU.setCores(cpuDTO.getCores());
		newCPU.setPCIExpress(cpuDTO.getPCIExpress());
		newCPU.setThreads(cpuDTO.getThreads());
		newCPU.setTdp(cpuDTO.getTdp());
		newCPU.setBaseFrequency(cpuDTO.getBaseFrequency());
		newCPU.setMaxFrequency(cpuDTO.getMaxFrequency());
		newCPU.setBusSpeed(cpuDTO.getBusSpeed());
		newCPU.setSocket(cpuDTO.getSocket());
		CPU saveCPU = cpuRepository.save(newCPU);

		return ResponseEntity.status(HttpStatus.CREATED).body(saveCPU);
	}

	
	@PostMapping("/cpus-collection")
	@PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
	public ResponseEntity<?> createNewCPUs(@RequestBody List<CPUDTO> cpuDTO) {
		List<CPU> listCPU = new LinkedList<CPU>();
		for (int i = 0; i < listCPU.size(); i++) {
			Product oldProduct = productRepository.findByName(cpuDTO.get(i).getProductDTO().getName());
			if (oldProduct != null) {
				throw new DuplicateException("Product has already exists");
			}

			Optional<Category> optionalCategory = categoryRepository
					.findById(cpuDTO.get(i).getProductDTO().getCategoryId());

			if (!optionalCategory.isPresent()) {
				throw new NotFoundException(
						"Not found category with category id " + cpuDTO.get(i).getProductDTO().getCategoryId());
			}

			Product product = ConvertObject.fromProductDTOToProductDAO(cpuDTO.get(i).getProductDTO());
			product.setCategory(optionalCategory.get());
			Validate.checkProduct(product);
			Product saveProduct = productRepository.save(product);

			CPU newCPU = new CPU(saveProduct);
			newCPU.setCodeName(cpuDTO.get(i).getCodeName());
			newCPU.setCache(cpuDTO.get(i).getCache());
			newCPU.setCPUFamily(cpuDTO.get(i).getCPUFamily());
			newCPU.setCores(cpuDTO.get(i).getCores());
			newCPU.setPCIExpress(cpuDTO.get(i).getPCIExpress());
			newCPU.setThreads(cpuDTO.get(i).getThreads());
			newCPU.setTdp(cpuDTO.get(i).getTdp());
			newCPU.setBaseFrequency(cpuDTO.get(i).getBaseFrequency());
			newCPU.setMaxFrequency(cpuDTO.get(i).getMaxFrequency());
			newCPU.setBusSpeed(cpuDTO.get(i).getBusSpeed());
			newCPU.setSocket(cpuDTO.get(i).getSocket());
			CPU saveCPU = cpuRepository.save(newCPU);

			listCPU.add(saveCPU);
		}
		cpuRepository.saveAll(listCPU);
		if (listCPU.size() == 0)
			ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		return ResponseEntity.status(HttpStatus.CREATED).body(listCPU);
	}

	
	@PatchMapping("/{cpuId}")
	@PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
	public ResponseEntity<?> editCPU(@RequestBody CPUDTO cpuDTO, @PathVariable("cpuId") Long cpuId) {
		Optional<CPU> optionalCpu = cpuRepository.findByCPUId(cpuId);
		if (!optionalCpu.isPresent()) {
			throw new NotFoundException("CPU not found");
		}
		CPU newCPU = optionalCpu.get();
		Optional<Product> optionalProduct = productRepository.findById(newCPU.getId());
		Product newProduct = optionalProduct.get();
		if (cpuDTO.getProductDTO().getName() != null) {
			newCPU.setName(cpuDTO.getProductDTO().getName().trim().replaceAll("\\s+", " "));
			newProduct.setName(cpuDTO.getProductDTO().getName().trim().replaceAll("\\s+", " "));
		}
		if (cpuDTO.getProductDTO().getDescription() != null) {
			newCPU.setDescription(cpuDTO.getProductDTO().getDescription().trim().replaceAll("\\s+", " "));
			newProduct.setDescription(cpuDTO.getProductDTO().getDescription().trim().replaceAll("\\s+", " "));
		}
		if (cpuDTO.getProductDTO().getCategoryId() != null) {
			Category category = categoryRepository.findById(cpuDTO.getProductDTO().getCategoryId()).get();
			newCPU.setCategory(category);
			newProduct.setCategory(category);
		}
		if (cpuDTO.getProductDTO().getPrice() != null) {
			newCPU.setPrice(cpuDTO.getProductDTO().getPrice());
			newProduct.setPrice(cpuDTO.getProductDTO().getPrice());
		}
		if (cpuDTO.getProductDTO().getBrand() != null) {
			newCPU.setBrand(cpuDTO.getProductDTO().getBrand().trim().replaceAll("\\s+", " "));
			newProduct.setBrand(cpuDTO.getProductDTO().getBrand().trim().replaceAll("\\s+", " "));
		}
		if (cpuDTO.getProductDTO().getAmount() != null) {
			newCPU.setAmount(cpuDTO.getProductDTO().getAmount());
			newProduct.setAmount(cpuDTO.getProductDTO().getAmount());
		}
		if (cpuDTO.getCodeName() != null) {
			newCPU.setCodeName(cpuDTO.getCodeName().trim().replaceAll("\\s+", " "));
		}
		if (cpuDTO.getCPUFamily() != null) {
			newCPU.setCPUFamily(cpuDTO.getCPUFamily().trim().replaceAll("\\s+", " "));
		}
		if (cpuDTO.getCores() != null) {
			newCPU.setCores(cpuDTO.getCores().trim().replaceAll("\\s+", " "));
		}
		if (cpuDTO.getThreads() != null) {
			newCPU.setThreads(cpuDTO.getThreads().trim().replaceAll("\\s+", " "));
		}
		if (cpuDTO.getBaseFrequency() != null) {
			newCPU.setBaseFrequency(cpuDTO.getBaseFrequency().trim().replaceAll("\\s+", " "));
		}
		if (cpuDTO.getMaxFrequency() != null) {
			newCPU.setMaxFrequency(cpuDTO.getMaxFrequency().trim().replaceAll("\\s+", " "));
		}
		if (cpuDTO.getPCIExpress() != null) {
			newCPU.setPCIExpress(cpuDTO.getPCIExpress().trim().replaceAll("\\s+", " "));
		}
		if (cpuDTO.getTdp() != null) {
			newCPU.setTdp(cpuDTO.getTdp().trim().replaceAll("\\s+", " "));
		}
		if (cpuDTO.getSocket() != null) {
			newCPU.setSocket(cpuDTO.getSocket().trim().replaceAll("\\s+", " "));
		}
		if (cpuDTO.getCache() != null) {
			newCPU.setSocket(cpuDTO.getSocket().trim().replaceAll("\\s+", " "));
		}
		if (cpuDTO.getBusSpeed() != null) {
			newCPU.setBusSpeed(cpuDTO.getBusSpeed().trim().replaceAll("\\s+", " "));
		}
		cpuRepository.save(newCPU);
		productRepository.save(newProduct);
		return ResponseEntity.status(HttpStatus.OK).body(newCPU);
	}

	
	@DeleteMapping("/{cpuId}")
	@PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
	public ResponseEntity<?> deleteCase(@PathVariable("cpuId") Long cpuId) {
		Optional<CPU> optionalCPU = cpuRepository.findByCPUId(cpuId);
		if (!optionalCPU.isPresent()) {
			throw new NotFoundException("Product not found");
		}

		if (!optionalCPU.get().getProductImages().isEmpty()) {
			throw new InvalidException("Delete failed");
		}

		productRepository.deleteById(optionalCPU.get().getId());
		cpuRepository.deleteByCPUId(optionalCPU.get().getCpuId());

		return ResponseEntity.status(HttpStatus.OK).body(optionalCPU.get());
	}
}
