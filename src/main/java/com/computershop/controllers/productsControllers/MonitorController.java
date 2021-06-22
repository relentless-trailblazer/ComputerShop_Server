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
import com.computershop.dao.product.Monitor;
import com.computershop.dto.products.MonitorDTO;
import com.computershop.dto.productsDetail.MonitorDetail;
import com.computershop.exceptions.DuplicateException;
import com.computershop.exceptions.InvalidException;
import com.computershop.exceptions.NotFoundException;
import com.computershop.helpers.ConvertObject;
import com.computershop.helpers.Validate;
import com.computershop.repositories.CategoryRepository;
import com.computershop.repositories.ProductRepository;
import com.computershop.repositories.productRepos.MonitorRepository;

@RestController
@RequestMapping("/api/products/monitors")
public class MonitorController {
	@Autowired
	private MonitorRepository monitorRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@GetMapping
	public ResponseEntity<?> getAllMonitor(@RequestParam(name = "page", required = false) Integer pageNum,
			@RequestParam(name = "type", required = false) String type,
			@RequestParam(name = "search", required = false) String search) {
		if (search != null) {
			String searchConvert = ConvertObject.fromSlugToString(search);
			List<Monitor> listMonitors = monitorRepository.findByNameContainingIgnoreCase(searchConvert);
			if (listMonitors.size() == 0) {
				return ResponseEntity.noContent().build();
			}
			List<MonitorDetail> listmonitorDetail = new LinkedList<MonitorDetail>();
			for (int i = 0; i < listMonitors.size(); i++) {
				Monitor getMonitor = listMonitors.get(i);
				List<ProductImage> getProductImages = getMonitor.getProductImages();
				if (getProductImages.size() == 0) {
					getProductImages.add(new ProductImage());
				}
				listmonitorDetail.add(new MonitorDetail(getMonitor, getProductImages));
			}

			return ResponseEntity.ok().body(listMonitors);
		}
		if (type != null) {
			if (type.compareTo("without-image") == 0) {
				List<Monitor> listmonitors = monitorRepository.findAll();
				if (listmonitors.size() == 0) {
					return ResponseEntity.noContent().build();
				}
				return ResponseEntity.ok().body(listmonitors);
			}
		}
		if (pageNum != null) {
			Page<Monitor> page = monitorRepository.findAll(PageRequest.of(pageNum.intValue(), 20));

			List<Monitor> listmonitors = page.getContent();
			if (listmonitors.size() == 0) {
				return ResponseEntity.noContent().build();
			}
			List<MonitorDetail> listmonitorDetail = new LinkedList<MonitorDetail>();
			for (int i = 0; i < listmonitors.size(); i++) {
				Monitor getMonitor = listmonitors.get(i);
				List<ProductImage> getProductImages = getMonitor.getProductImages();
				if (getProductImages.size() == 0) {
					getProductImages.add(new ProductImage());
				}
				listmonitorDetail.add(new MonitorDetail(getMonitor, getProductImages));
			}
			return ResponseEntity.ok().body(listmonitorDetail);
		}

		List<Monitor> listMonitors = monitorRepository.findAll();
		if (listMonitors.size() == 0) {
			return ResponseEntity.noContent().build();
		}
		List<MonitorDetail> listMonitorDetail = new LinkedList<MonitorDetail>();
		for (int i = 0; i < listMonitors.size(); i++) {
			Monitor getMonitor = listMonitors.get(i);
			List<ProductImage> getProductImages = getMonitor.getProductImages();
			if (getProductImages.size() == 0) {
				getProductImages.add(new ProductImage());
			}
			listMonitorDetail.add(new MonitorDetail(getMonitor, getProductImages));
		}
		return ResponseEntity.ok().body(listMonitorDetail);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getMonitorById(@PathVariable("id") Long id) {
		Optional<Monitor> optionalMonitor = monitorRepository.findById(id);
		Monitor monitorFound = optionalMonitor.get();
		if (monitorFound == null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		if (monitorFound.getProductImages().size() == 0) {
			return ResponseEntity.status(HttpStatus.OK).body(monitorFound);
		}
		MonitorDetail monitorDetail = new MonitorDetail(monitorFound, monitorFound.getProductImages());

		return ResponseEntity.ok().body(monitorDetail);
	}

	@PostMapping
	@PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
	public ResponseEntity<?> createNewMonitor(@RequestBody MonitorDTO monitorDTO) {
		Product oldProduct = productRepository.findByName(monitorDTO.getProductDTO().getName());
		if (oldProduct != null) {
			throw new DuplicateException("Monitor has already exists");
		}

		Optional<Category> optionalCategory = categoryRepository.findById(monitorDTO.getProductDTO().getCategoryId());

		if (!optionalCategory.isPresent()) {
			throw new NotFoundException(
					"Not found category with category id " + monitorDTO.getProductDTO().getCategoryId());
		}

		Product product = ConvertObject.fromProductDTOToProductDAO(monitorDTO.getProductDTO());
		product.setCategory(optionalCategory.get());
		Validate.checkProduct(product);
		Product saveProduct = productRepository.save(product);

		Monitor newMonitor = new Monitor(saveProduct);
		newMonitor.setColor(monitorDTO.getColor());
		newMonitor.setAspectRatio(monitorDTO.getAspectRatio());
		newMonitor.setMaximumResolution(monitorDTO.getMaximumResolution());
		newMonitor.setNativeResolution(monitorDTO.getNativeResolution());
		newMonitor.setRefreshRate(monitorDTO.getRefreshRate());
		newMonitor.setScreenSize(monitorDTO.getScreenSize());
		newMonitor.setTouchScreen(monitorDTO.getTouchScreen());

		Monitor saveMonitor = monitorRepository.save(newMonitor);
		return ResponseEntity.status(HttpStatus.CREATED).body(saveMonitor);
	}

	@PostMapping("/monitor-collection")
	@PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
	public ResponseEntity<?> createNewMonitors(@RequestBody List<MonitorDTO> monitorDTO) {
		List<Monitor> listMonitor = new LinkedList<Monitor>();
		for (int i = 0; i < monitorDTO.size(); i++) {
			Product oldProduct = productRepository.findByName(monitorDTO.get(i).getProductDTO().getName());
			if (oldProduct != null) {
				throw new DuplicateException("Product has already exists");
			}

			Optional<Category> optionalCategory = categoryRepository
					.findById(monitorDTO.get(i).getProductDTO().getCategoryId());

			if (!optionalCategory.isPresent()) {
				throw new NotFoundException(
						"Not found category with category id " + monitorDTO.get(i).getProductDTO().getCategoryId());
			}

			Product product = ConvertObject.fromProductDTOToProductDAO(monitorDTO.get(i).getProductDTO());
			product.setCategory(optionalCategory.get());
			Validate.checkProduct(product);
			Product saveProduct = productRepository.save(product);

			Monitor newMonitor = new Monitor(saveProduct);
			newMonitor.setColor(monitorDTO.get(i).getColor());
			newMonitor.setAspectRatio(monitorDTO.get(i).getAspectRatio());
			newMonitor.setMaximumResolution(monitorDTO.get(i).getMaximumResolution());
			newMonitor.setNativeResolution(monitorDTO.get(i).getNativeResolution());
			newMonitor.setRefreshRate(monitorDTO.get(i).getRefreshRate());
			newMonitor.setScreenSize(monitorDTO.get(i).getScreenSize());
			newMonitor.setTouchScreen(monitorDTO.get(i).getTouchScreen());

			listMonitor.add(newMonitor);
		}
		monitorRepository.saveAll(listMonitor);
		if (listMonitor.size() == 0)
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		return ResponseEntity.status(HttpStatus.CREATED).body(listMonitor);

	}

	@PatchMapping({ "monitorId" })
	@PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
	public ResponseEntity<?> editGraPhicCardById(@RequestBody MonitorDTO monitorDTO,
			@PathVariable("monitorId") Long monitorId) {
		Optional<Monitor> optionalMonitor = monitorRepository.findByMonitorId(monitorId);
		if (!optionalMonitor.isPresent()) {
			throw new NotFoundException("Monitor not found");
		}
		Monitor newMonitor = optionalMonitor.get();
		Optional<Product> optionalProduct = productRepository.findById(newMonitor.getId());
		Product newProduct = optionalProduct.get();
		if (monitorDTO.getProductDTO().getName() != null) {
			newMonitor.setName(monitorDTO.getProductDTO().getName().trim().replaceAll("\\s+", " "));
			newProduct.setName(monitorDTO.getProductDTO().getName().trim().replaceAll("\\s+", " "));
		}
		if (monitorDTO.getProductDTO().getDescription() != null) {
			newMonitor.setDescription(monitorDTO.getProductDTO().getDescription().trim().replaceAll("\\s+", " "));
			newProduct.setDescription(monitorDTO.getProductDTO().getDescription().trim().replaceAll("\\s+", " "));
		}
		if (monitorDTO.getProductDTO().getCategoryId() != null) {
			Category category = categoryRepository.findById(monitorDTO.getProductDTO().getCategoryId()).get();
			newMonitor.setCategory(category);
			newProduct.setCategory(category);
		}
		if (monitorDTO.getProductDTO().getPrice() != null) {
			newMonitor.setPrice(monitorDTO.getProductDTO().getPrice());
			newProduct.setPrice(monitorDTO.getProductDTO().getPrice());
		}
		if (monitorDTO.getProductDTO().getBrand() != null) {
			newMonitor.setBrand(monitorDTO.getProductDTO().getBrand().trim().replaceAll("\\s+", " "));
			newProduct.setBrand(monitorDTO.getProductDTO().getBrand().trim().replaceAll("\\s+", " "));
		}
		if (monitorDTO.getProductDTO().getAmount() != null) {
			newMonitor.setAmount(monitorDTO.getProductDTO().getAmount());
			newProduct.setAmount(monitorDTO.getProductDTO().getAmount());
		}
		if (monitorDTO.getColor() != null) {
			newMonitor.setColor(monitorDTO.getColor().trim().replaceAll("\\s+", " "));
		}
		if (monitorDTO.getAspectRatio() != null) {
			newMonitor.setAspectRatio(monitorDTO.getAspectRatio().trim().replaceAll("\\s+", " "));
		}
		if (monitorDTO.getMaximumResolution() != null) {
			newMonitor.setMaximumResolution(monitorDTO.getMaximumResolution().trim().replaceAll("\\s+", " "));
		}
		if (monitorDTO.getNativeResolution() != null) {
			newMonitor.setNativeResolution(monitorDTO.getNativeResolution().trim().replaceAll("\\s+", " "));
		}
		if (monitorDTO.getRefreshRate() != null) {
			newMonitor.setRefreshRate(monitorDTO.getRefreshRate().trim().replaceAll("\\s+", " "));
		}
		if (monitorDTO.getScreenSize() != null) {
			newMonitor.setScreenSize(monitorDTO.getScreenSize().trim().replaceAll("\\s+", " "));
		}
		if (monitorDTO.getTouchScreen() != null) {
			newMonitor.setTouchScreen(monitorDTO.getTouchScreen().trim().replaceAll("\\s+", " "));
		}
		monitorRepository.save(newMonitor);
		productRepository.save(newProduct);
		return ResponseEntity.status(HttpStatus.OK).body(newMonitor);
	}
	
	@DeleteMapping("/{monitorId}")
	@PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
	public ResponseEntity<?> deleteMonitor(@PathVariable("monitorId") Long monitorId) {
		Optional<Monitor> optionalMonitor = monitorRepository.findByMonitorId(monitorId);
		if (!optionalMonitor.isPresent()) {
			throw new NotFoundException("Monitor not found");
		}

		if (!optionalMonitor.get().getProductImages().isEmpty()) {
			throw new InvalidException("Delete failed");
		}

		productRepository.deleteById(optionalMonitor.get().getId());
		monitorRepository.deleteByMonitorId(optionalMonitor.get().getMonitorId());

		return ResponseEntity.status(HttpStatus.OK).body(optionalMonitor.get());
	}

}
