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
import com.computershop.dao.product.Ram;
import com.computershop.dto.products.RamDTO;
import com.computershop.dto.productsDetail.RamDetail;
import com.computershop.exceptions.DuplicateException;
import com.computershop.exceptions.InvalidException;
import com.computershop.exceptions.NotFoundException;
import com.computershop.helpers.ConvertObject;
import com.computershop.helpers.Validate;
import com.computershop.repositories.CategoryRepository;
import com.computershop.repositories.ProductRepository;
import com.computershop.repositories.productRepos.RamRepository;

@RestController
@RequestMapping("/api/products/rams")
public class RamController {
	@Autowired
	private RamRepository ramRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	
	
	@GetMapping
	public ResponseEntity<?> getAllRams(@RequestParam(name = "page", required = false) Integer pageNum,
			@RequestParam(name = "type", required = false) String type,
			@RequestParam(name = "search", required = false) String search) {
		if (search != null) {
			String searchConvert = ConvertObject.fromSlugToString(search);
			List<Ram> listRams = ramRepository.findByNameContainingIgnoreRam(searchConvert);
			if (listRams.size() == 0) {
				return ResponseEntity.noContent().build();
			}
			List<RamDetail> listRamDetail = new LinkedList<RamDetail>();
			for (int i = 0; i < listRams.size(); i++) {
				Ram getRam = listRams.get(i);
				List<ProductImage> getProductImages = getRam.getProductImages();
				if (getProductImages.size() == 0) {
					getProductImages.add(new ProductImage());
				}
				listRamDetail.add(new RamDetail(getRam, getProductImages));
			}

			return ResponseEntity.ok().body(listRamDetail);
		}
		if (type != null) {
			if (type.compareTo("without-image") == 0) {
				List<Ram> rams = ramRepository.findAll();
				if (rams.size() == 0) {
					return ResponseEntity.noContent().build();
				}
				return ResponseEntity.ok().body(rams);
			}
		}
		if (pageNum != null) {
			Page<Ram> page = ramRepository.findAll(PageRequest.of(pageNum.intValue(), 20));

			List<Ram> listRams = page.getContent();
			if (listRams.size() == 0) {
				return ResponseEntity.noContent().build();
			}
			List<RamDetail> listRamDetail = new LinkedList<RamDetail>();
			for (int i = 0; i < listRams.size(); i++) {
				Ram getRam = listRams.get(i);
				List<ProductImage> getProductImages = getRam.getProductImages();
				if (getProductImages.size() == 0) {
					getProductImages.add(new ProductImage());
				}
				listRamDetail.add(new RamDetail(getRam, getProductImages));
			}
			return ResponseEntity.ok().body(listRamDetail);
		}

		List<Ram> listRams = ramRepository.findAll();
		if (listRams.size() == 0) {
			return ResponseEntity.noContent().build();
		}
		List<RamDetail> listRamDetail = new LinkedList<RamDetail>();
		for (int i = 0; i < listRams.size(); i++) {
			Ram getRam = listRams.get(i);
			List<ProductImage> getProductImages = getRam.getProductImages();
			if (getProductImages.size() == 0) {
				getProductImages.add(new ProductImage());
			}
			listRamDetail.add(new RamDetail(getRam, getProductImages));
		}
		return ResponseEntity.ok().body(listRamDetail);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getRamById(@PathVariable("id") Long id) {
		Optional<Ram> optionalRam = ramRepository.findById(id);
		Ram ramFound = optionalRam.get();
		if (ramFound == null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		if (ramFound.getProductImages().size() == 0) {
			return ResponseEntity.status(HttpStatus.OK).body(ramFound);
		}
		RamDetail ramDetail = new RamDetail(ramFound, ramFound.getProductImages());

		return ResponseEntity.ok().body(ramDetail);
	}
	
	
	
	@PostMapping
	@PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
	public ResponseEntity<?> createNewRam(@RequestBody RamDTO ramDTO) {
		Product oldProduct = productRepository.findByName(ramDTO.getProductDTO().getName());
		if (oldProduct != null) {
			throw new DuplicateException("Product has already exists");
		}

		Optional<Category> optionalCategory = categoryRepository.findById(ramDTO.getProductDTO().getCategoryId());

		if (!optionalCategory.isPresent()) {
			throw new NotFoundException(
					"Not found category with category id " + ramDTO.getProductDTO().getCategoryId());
		}

		Product product = ConvertObject.fromProductDTOToProductDAO(ramDTO.getProductDTO());
		product.setCategory(optionalCategory.get());
		Validate.checkProduct(product);
		Product saveProduct = productRepository.save(product);

		Ram newRam = new Ram(saveProduct);
		newRam.setPartNumber(ramDTO.getPartNumber());
		newRam.setCapacity(ramDTO.getCapacity());
		newRam.setDimmType(ramDTO.getDimmType());
		newRam.setTypeOfBus(ramDTO.getTypeOfBus());
		newRam.setDDR(ramDTO.getDDR());

		Ram saveRam = ramRepository.save(newRam);
		return ResponseEntity.status(HttpStatus.CREATED).body(saveRam);
	}
	
	
	@PostMapping("/rams-collection")
	@PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
	public ResponseEntity<?> createNewRams(@RequestBody List<RamDTO> ramDTO) {
		List<Ram> listRam = new LinkedList<Ram>();
		for (int i = 0; i < ramDTO.size(); i++) {
			Product oldProduct = productRepository.findByName(ramDTO.get(i).getProductDTO().getName());
			if (oldProduct != null) {
				throw new DuplicateException("Product has already exists");
			}

			Optional<Category> optionalCategory = categoryRepository
					.findById(ramDTO.get(i).getProductDTO().getCategoryId());

			if (!optionalCategory.isPresent()) {
				throw new NotFoundException(
						"Not found category with category id " + ramDTO.get(i).getProductDTO().getCategoryId());
			}

			Product product = ConvertObject.fromProductDTOToProductDAO(ramDTO.get(i).getProductDTO());
			product.setCategory(optionalCategory.get());
			Validate.checkProduct(product);
			Product saveProduct = productRepository.save(product);

			Ram newRam = new Ram(saveProduct);
			newRam.setPartNumber(ramDTO.get(i).getPartNumber());
			newRam.setCapacity(ramDTO.get(i).getCapacity());
			newRam.setDimmType(ramDTO.get(i).getDimmType());
			newRam.setTypeOfBus(ramDTO.get(i).getTypeOfBus());
			newRam.setDDR(ramDTO.get(i).getDDR());
			

			listRam.add(newRam);

		}
		ramRepository.saveAll(listRam);
		if (listRam.size() == 0)
			ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		return ResponseEntity.status(HttpStatus.CREATED).body(listRam);
	}
	
	@PatchMapping("/{ramId}")
	@PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
	public ResponseEntity<?> editRam(@RequestBody RamDTO ramDTO, @PathVariable("ramId") Long ramId) {
		Optional<Ram> optionalRam = ramRepository.findByRamId(ramId);
		if (!optionalRam.isPresent()) {
			throw new NotFoundException("Ram not found");
		}
		Ram newRam = optionalRam.get();
		Optional<Product> optionalProduct = productRepository.findById(newRam.getId());
		Product newProduct = optionalProduct.get();
		if (ramDTO.getProductDTO().getName() != null) {
			newRam.setName(ramDTO.getProductDTO().getName().trim().replaceAll("\\s+", " "));
			newProduct.setName(ramDTO.getProductDTO().getName().trim().replaceAll("\\s+", " "));
		}
		if (ramDTO.getProductDTO().getDescription() != null) {
			newRam.setDescription(ramDTO.getProductDTO().getDescription().trim().replaceAll("\\s+", " "));
			newProduct.setDescription(ramDTO.getProductDTO().getDescription().trim().replaceAll("\\s+", " "));
		}
		if (ramDTO.getProductDTO().getCategoryId() != null) {
			Category category = categoryRepository.findById(ramDTO.getProductDTO().getCategoryId()).get();
			newRam.setCategory(category);
			newProduct.setCategory(category);
		}
		if (ramDTO.getProductDTO().getPrice() != null) {
			newRam.setPrice(ramDTO.getProductDTO().getPrice());
			newProduct.setPrice(ramDTO.getProductDTO().getPrice());
		}
		if (ramDTO.getProductDTO().getBrand() != null) {
			newRam.setBrand(ramDTO.getProductDTO().getBrand().trim().replaceAll("\\s+", " "));
			newProduct.setBrand(ramDTO.getProductDTO().getBrand().trim().replaceAll("\\s+", " "));
		}
		if (ramDTO.getProductDTO().getAmount() != null) {
			newRam.setAmount(ramDTO.getProductDTO().getAmount());
			newProduct.setAmount(ramDTO.getProductDTO().getAmount());
		}
		if (ramDTO.getPartNumber() != null) {
			newRam.setPartNumber(ramDTO.getPartNumber().trim().replaceAll("\\s+", " "));
		}
		if (ramDTO.getCapacity() != null) {
			newRam.setCapacity(ramDTO.getCapacity().trim().replaceAll("\\s+", " "));
		}
		if (ramDTO.getDimmType() != null) {
			newRam.setDimmType(ramDTO.getDimmType().trim().replaceAll("\\s+", " "));
		}
		if (ramDTO.getTypeOfBus() != null) {
			newRam.setTypeOfBus(ramDTO.getTypeOfBus().trim().replaceAll("\\s+", " "));
		}
		if (ramDTO.getDDR() != null) {
			newRam.setDDR(ramDTO.getDDR().trim().replaceAll("\\s+", " "));
		}
		
		ramRepository.save(newRam);
		productRepository.save(newProduct);
		return ResponseEntity.status(HttpStatus.OK).body(newRam);
	}
	
	@DeleteMapping("/{ramId}")
    @PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
    public ResponseEntity<?> deleteRam(@PathVariable("RamId") Long ramId) {
        Optional<Ram> optionalRam = ramRepository.findByRamId(ramId);
        if (!optionalRam.isPresent()) {
            throw new NotFoundException("Product not found");
        }

        if (!optionalRam.get().getProductImages().isEmpty()) {
            throw new InvalidException("Delete failed");
        }
        
        productRepository.deleteById(optionalRam.get().getId());
        ramRepository.deleteByRamId(optionalRam.get().getRamId());
        return ResponseEntity.status(HttpStatus.OK).body(optionalRam.get());
    }
	
}
