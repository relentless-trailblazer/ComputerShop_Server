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
import com.computershop.dao.product.HardDisk;
import com.computershop.dto.products.HardDiskDTO;
import com.computershop.dto.productsDetail.HardDiskDetail;
import com.computershop.exceptions.DuplicateException;
import com.computershop.exceptions.InvalidException;
import com.computershop.exceptions.NotFoundException;
import com.computershop.helpers.ConvertObject;
import com.computershop.helpers.Validate;
import com.computershop.repositories.CategoryRepository;
import com.computershop.repositories.ProductRepository;
import com.computershop.repositories.productRepos.HardDiskRepository;

@RestController
@RequestMapping("/api/products/hard-disks")
public class HardDiskController {
	@Autowired
	private HardDiskRepository hardDiskRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;
	
	@GetMapping
	public ResponseEntity<?> getAllHardDisk(@RequestParam(name = "page", required = false) Integer pageNum,
										@RequestParam(name = "type", required = false) String type,
										@RequestParam(name = "search", required = false) String search) {
		if (search != null) {
			String searchConvert = ConvertObject.fromSlugToString(search);
			List<HardDisk> listHardDisks = hardDiskRepository.findByNameContainingIgnoreCase(searchConvert);
			if (listHardDisks.size() == 0) {
				return ResponseEntity.noContent().build();
			}
			List<HardDiskDetail> listhardDiskDetail = new LinkedList<HardDiskDetail>();
			for (int i = 0; i < listHardDisks.size(); i++) {
				HardDisk getHardDisk = listHardDisks.get(i);
				List<ProductImage> getProductImages = getHardDisk.getProductImages();
				if (getProductImages.size() == 0) {
					getProductImages.add(new ProductImage());
				}
				listhardDiskDetail.add(new HardDiskDetail(getHardDisk, getProductImages));
			}

			return ResponseEntity.ok().body(listhardDiskDetail);
		}
		if (type != null) {
			if (type.compareTo("without-image") == 0) {
				List<HardDisk> listhardDisks = hardDiskRepository.findAll();
				if (listhardDisks.size() == 0) {
					return ResponseEntity.noContent().build();
				}
				return ResponseEntity.ok().body(listhardDisks);
			}
		}
		if (pageNum != null) {
			Page<HardDisk> page = hardDiskRepository.findAll(PageRequest.of(pageNum.intValue(), 20));

			List<HardDisk> listhardDisks = page.getContent();
			if (listhardDisks.size() == 0) {
				return ResponseEntity.noContent().build();
			}
			List<HardDiskDetail> listhardDiskDetail = new LinkedList<HardDiskDetail>();
			for (int i = 0; i < listhardDisks.size(); i++) {
				HardDisk gethardDisk = listhardDisks.get(i);
				List<ProductImage> getProductImages = gethardDisk.getProductImages();
				if (getProductImages.size() == 0) {
					getProductImages.add(new ProductImage());
				}
				listhardDiskDetail.add(new HardDiskDetail(gethardDisk, getProductImages));
			}
			return ResponseEntity.ok().body(listhardDiskDetail);
		}

		List<HardDisk> listHardDisks = hardDiskRepository.findAll();
		if (listHardDisks.size() == 0) {
			return ResponseEntity.noContent().build();
		}
		List<HardDiskDetail> listHardDiskDetail = new LinkedList<HardDiskDetail>();
		for (int i = 0; i < listHardDisks.size(); i++) {
			HardDisk gethardDisk = listHardDisks.get(i);
			List<ProductImage> getProductImages = gethardDisk.getProductImages();
			if (getProductImages.size() == 0) {
				getProductImages.add(new ProductImage());
			}
			listHardDiskDetail.add(new HardDiskDetail(gethardDisk, getProductImages));
		}
		return ResponseEntity.ok().body(listHardDiskDetail);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getHardDiskById(@PathVariable("id") Long id) {
		Optional<HardDisk> optionalhardDisk = hardDiskRepository.findById(id);
		HardDisk hardDiskFound = optionalhardDisk.get();
		if (hardDiskFound == null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		if (hardDiskFound.getProductImages().size() == 0) {
			return ResponseEntity.status(HttpStatus.OK).body(hardDiskFound);
		}
		HardDiskDetail hardDiskDetail = new HardDiskDetail(hardDiskFound, hardDiskFound.getProductImages());

		return ResponseEntity.ok().body(hardDiskDetail);
	}
	
	@PostMapping
	@PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
	public ResponseEntity<?> createNewHardDisk(@RequestBody HardDiskDTO hardDiskDTO) {
		Product oldProduct = productRepository.findByName(hardDiskDTO.getProductDTO().getName());
		if (oldProduct != null) {
			throw new DuplicateException("HardDisk has already exists");
		}

		Optional<Category> optionalCategory = categoryRepository.findById(hardDiskDTO.getProductDTO().getCategoryId());

		if (!optionalCategory.isPresent()) {
			throw new NotFoundException(
					"Not found category with category id " + hardDiskDTO.getProductDTO().getCategoryId());
		}

		Product product = ConvertObject.fromProductDTOToProductDAO(hardDiskDTO.getProductDTO());
		product.setCategory(optionalCategory.get());
		Validate.checkProduct(product);
		Product saveProduct = productRepository.save(product);

		HardDisk newHardDisk = new HardDisk(saveProduct);
		newHardDisk.setInterfaceType(hardDiskDTO.getInterfaceType());
		newHardDisk.setCache(hardDiskDTO.getCache());
		newHardDisk.setCapacity(hardDiskDTO.getCapacity());
		newHardDisk.setSize(hardDiskDTO.getSize());
		newHardDisk.setStyle(hardDiskDTO.getStyle());

		HardDisk saveHardDisk = hardDiskRepository.save(newHardDisk);
		return ResponseEntity.status(HttpStatus.CREATED).body(saveHardDisk);
	}
	
	@PostMapping("/hard-disks-collection")
	@PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
	public ResponseEntity<?> createNewhardDisks(@RequestBody List<HardDiskDTO> hardDiskDTO) {
		List<HardDisk> listHardDisk = new LinkedList<HardDisk>();
		for(int i = 0; i < hardDiskDTO.size(); i++) {
			Product oldProduct = productRepository.findByName(hardDiskDTO.get(i).getProductDTO().getName());
			if (oldProduct != null) {
				throw new DuplicateException("Product has already exists");
			}

			Optional<Category> optionalCategory = categoryRepository.findById(hardDiskDTO.get(i).getProductDTO().getCategoryId());

			if (!optionalCategory.isPresent()) {
				throw new NotFoundException(
						"Not found category with category id " + hardDiskDTO.get(i).getProductDTO().getCategoryId());
			}

			Product product = ConvertObject.fromProductDTOToProductDAO(hardDiskDTO.get(i).getProductDTO());
			product.setCategory(optionalCategory.get());
			Validate.checkProduct(product);
			Product saveProduct = productRepository.save(product);

			HardDisk newHardDisk = new HardDisk(saveProduct);
			newHardDisk.setInterfaceType(hardDiskDTO.get(i).getInterfaceType());
			newHardDisk.setCache(hardDiskDTO.get(i).getCache());
			newHardDisk.setCapacity(hardDiskDTO.get(i).getCapacity());
			newHardDisk.setSize(hardDiskDTO.get(i).getSize());
			newHardDisk.setStyle(hardDiskDTO.get(i).getStyle());

			listHardDisk.add(newHardDisk);
		}
		hardDiskRepository.saveAll(listHardDisk);
		if(listHardDisk.size()==0)
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		return ResponseEntity.status(HttpStatus.CREATED).body(listHardDisk);
		
	}
	
	@PatchMapping({"hardDiskId"})
	@PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
	public ResponseEntity<?> editGraPhicCardById(@RequestBody HardDiskDTO hardDiskDTO, @PathVariable("hardDiskId") Long hardDiskId) {
		Optional<HardDisk> optionalHardDisk = hardDiskRepository.findByHardDiskId(hardDiskId);
		if (!optionalHardDisk.isPresent()) {
			throw new NotFoundException("HardDisk not found");
		}
		HardDisk newHardDisk = optionalHardDisk.get();
		Optional<Product> optionalProduct = productRepository.findById(newHardDisk.getId());
		Product newProduct = optionalProduct.get();
		if (hardDiskDTO.getProductDTO().getName() != null) {
			newHardDisk.setName(hardDiskDTO.getProductDTO().getName().trim().replaceAll("\\s+", " "));
			newProduct.setName(hardDiskDTO.getProductDTO().getName().trim().replaceAll("\\s+", " "));
		}
		if (hardDiskDTO.getProductDTO().getDescription() != null) {
			newHardDisk.setDescription(hardDiskDTO.getProductDTO().getDescription().trim().replaceAll("\\s+", " "));
			newProduct.setDescription(hardDiskDTO.getProductDTO().getDescription().trim().replaceAll("\\s+", " "));
		}
		if (hardDiskDTO.getProductDTO().getCategoryId() != null) {
			Category category = categoryRepository.findById(hardDiskDTO.getProductDTO().getCategoryId()).get();
			newHardDisk.setCategory(category);
			newProduct.setCategory(category);
		}
		if (hardDiskDTO.getProductDTO().getPrice() != null) {
			newHardDisk.setPrice(hardDiskDTO.getProductDTO().getPrice());
			newProduct.setPrice(hardDiskDTO.getProductDTO().getPrice());
		}
		if (hardDiskDTO.getProductDTO().getBrand() != null) {
			newHardDisk.setBrand(hardDiskDTO.getProductDTO().getBrand().trim().replaceAll("\\s+", " "));
			newProduct.setBrand(hardDiskDTO.getProductDTO().getBrand().trim().replaceAll("\\s+", " "));
		}
		if (hardDiskDTO.getProductDTO().getAmount() != null) {
			newHardDisk.setAmount(hardDiskDTO.getProductDTO().getAmount());
			newProduct.setAmount(hardDiskDTO.getProductDTO().getAmount());
		}
		if(hardDiskDTO.getCache()!=null) {
			newHardDisk.setCache(hardDiskDTO.getCache().trim().replaceAll("\\s+", " "));
		}
		if(hardDiskDTO.getCapacity()!=null) {
			newHardDisk.setCapacity(hardDiskDTO.getCapacity().trim().replaceAll("\\s+", " "));
		}
		if(hardDiskDTO.getInterfaceType()!=null) {
			newHardDisk.setInterfaceType(hardDiskDTO.getInterfaceType().trim().replaceAll("\\s+", " "));
		}
		if(hardDiskDTO.getSize()!=null) {
			newHardDisk.setSize(hardDiskDTO.getSize().trim().replaceAll("\\s+", " "));
		}
		if(hardDiskDTO.getStyle()!=null) {
			newHardDisk.setStyle(hardDiskDTO.getStyle().trim().replaceAll("\\s+", " "));
		}
		hardDiskRepository.save(newHardDisk);
		productRepository.save(newProduct);
		return ResponseEntity.status(HttpStatus.OK).body(newHardDisk);
	}
	
	@DeleteMapping("/{hardDiskId}")
	@PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
	public ResponseEntity<?> deleteHardDisk(@PathVariable("hardDiskId") Long hardDiskId) {
		Optional<HardDisk> optionalHardDisk = hardDiskRepository.findByHardDiskId(hardDiskId);
		if (!optionalHardDisk.isPresent()) {
			throw new NotFoundException("HardDisk not found");
		}

		if (!optionalHardDisk.get().getProductImages().isEmpty()) {
			throw new InvalidException("Delete failed");
		}

		productRepository.deleteById(optionalHardDisk.get().getId());
		hardDiskRepository.deleteByHardDiskId(optionalHardDisk.get().getHardDiskId());

		return ResponseEntity.status(HttpStatus.OK).body(optionalHardDisk.get());
	}
}
