package com.computershop.controllers.productsControllers;

//import java.util.ArrayList;
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
import com.computershop.dao.product.Case;
import com.computershop.dto.products.CaseDTO;
import com.computershop.dto.productsDetail.CaseDetail;
import com.computershop.exceptions.DuplicateException;
import com.computershop.exceptions.InvalidException;
import com.computershop.exceptions.NotFoundException;
import com.computershop.helpers.ConvertObject;
import com.computershop.helpers.Validate;
import com.computershop.repositories.CategoryRepository;
import com.computershop.repositories.ProductRepository;
import com.computershop.repositories.productRepos.CaseRepository;


@RestController
@RequestMapping("/api/products/cases")
public class CaseController {
	@Autowired
	private CaseRepository caseRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@GetMapping
	public ResponseEntity<?> getAllCases(@RequestParam(name = "page", required = false) Integer pageNum,
			@RequestParam(name = "type", required = false) String type,
			@RequestParam(name = "search", required = false) String search) {
		if (search != null) {
			String searchConvert = ConvertObject.fromSlugToString(search);
			List<Case> listCases = caseRepository.findByNameContainingIgnoreCase(searchConvert);
			if (listCases.size() == 0) {
				return ResponseEntity.noContent().build();
			}
			List<CaseDetail> listCaseDetail = new LinkedList<CaseDetail>();
			for (int i = 0; i < listCases.size(); i++) {
				Case getCase = listCases.get(i);
				List<ProductImage> getProductImages = getCase.getProductImages();
				if (getProductImages.size() == 0) {
					getProductImages.add(new ProductImage());
				}
				listCaseDetail.add(new CaseDetail(getCase, getProductImages));
			}

			return ResponseEntity.ok().body(listCaseDetail);
		}
		if (type != null) {
			if (type.compareTo("without-image") == 0) {
				List<Case> cases = caseRepository.findAll();
				if (cases.size() == 0) {
					return ResponseEntity.noContent().build();
				}
				return ResponseEntity.ok().body(cases);
			}
		}
		if (pageNum != null) {
			Page<Case> page = caseRepository.findAll(PageRequest.of(pageNum.intValue(), 20));

			List<Case> listCases = page.getContent();
			if (listCases.size() == 0) {
				return ResponseEntity.noContent().build();
			}
			List<CaseDetail> listCaseDetail = new LinkedList<CaseDetail>();
			for (int i = 0; i < listCases.size(); i++) {
				Case getCase = listCases.get(i);
				List<ProductImage> getProductImages = getCase.getProductImages();
				if (getProductImages.size() == 0) {
					getProductImages.add(new ProductImage());
				}
				listCaseDetail.add(new CaseDetail(getCase, getProductImages));
			}
			return ResponseEntity.ok().body(listCaseDetail);
		}

		List<Case> listCases = caseRepository.findAll();
		if (listCases.size() == 0) {
			return ResponseEntity.noContent().build();
		}
		List<CaseDetail> listCaseDetail = new LinkedList<CaseDetail>();
		for (int i = 0; i < listCases.size(); i++) {
			Case getCase = listCases.get(i);
			List<ProductImage> getProductImages = getCase.getProductImages();
			if (getProductImages.size() == 0) {
				getProductImages.add(new ProductImage());
			}
			listCaseDetail.add(new CaseDetail(getCase, getProductImages));
		}
		return ResponseEntity.ok().body(listCaseDetail);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getCaseById(@PathVariable("id") Long id) {
		Optional<Case> optionalCase = caseRepository.findById(id);
		Case caseFound = optionalCase.get();
		if (caseFound == null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		if (caseFound.getProductImages().size() == 0) {
			return ResponseEntity.status(HttpStatus.OK).body(caseFound);
		}
		CaseDetail caseDetail = new CaseDetail(caseFound, caseFound.getProductImages());

		return ResponseEntity.ok().body(caseDetail);
	}

	@PostMapping
	@PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
	public ResponseEntity<?> createNewCase(@RequestBody CaseDTO caseDTO) {
		Product oldProduct = productRepository.findByName(caseDTO.getProductDTO().getName());
		if (oldProduct != null) {
			throw new DuplicateException("Product has already exists");
		}

		Optional<Category> optionalCategory = categoryRepository.findById(caseDTO.getProductDTO().getCategoryId());

		if (!optionalCategory.isPresent()) {
			throw new NotFoundException(
					"Not found category with category id " + caseDTO.getProductDTO().getCategoryId());
		}

		Product product = ConvertObject.fromProductDTOToProductDAO(caseDTO.getProductDTO());
		product.setCategory(optionalCategory.get());
		Validate.checkProduct(product);
		Product saveProduct = productRepository.save(product);

		Case newCase = new Case(saveProduct);
		newCase.setDimensions(caseDTO.getDimensions());
		newCase.setMaterial(caseDTO.getMaterial());
		newCase.setType(caseDTO.getType());
		newCase.setColor(caseDTO.getColor());
		newCase.setWeight(caseDTO.getWeight());
		newCase.setCoolingMethod(caseDTO.getCoolingMethod());
		Case saveCase = caseRepository.save(newCase);

		return ResponseEntity.status(HttpStatus.CREATED).body(saveCase);
	}

	@PostMapping("/cases-collection")
	@PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
	public ResponseEntity<?> createNewCases(@RequestBody List<CaseDTO> caseDTO) {
		List<Case> listCase = new LinkedList<Case>();
		for (int i = 0; i < caseDTO.size(); i++) {
			Product oldProduct = productRepository.findByName(caseDTO.get(i).getProductDTO().getName());
			if (oldProduct != null) {
				throw new DuplicateException("Product has already exists");
			}

			Optional<Category> optionalCategory = categoryRepository
					.findById(caseDTO.get(i).getProductDTO().getCategoryId());

			if (!optionalCategory.isPresent()) {
				throw new NotFoundException(
						"Not found category with category id " + caseDTO.get(i).getProductDTO().getCategoryId());
			}

			Product product = ConvertObject.fromProductDTOToProductDAO(caseDTO.get(i).getProductDTO());
			product.setCategory(optionalCategory.get());
			Validate.checkProduct(product);
			Product saveProduct = productRepository.save(product);

			Case newCase = new Case(saveProduct);
			newCase.setDimensions(caseDTO.get(i).getDimensions());
			newCase.setMaterial(caseDTO.get(i).getMaterial());
			newCase.setType(caseDTO.get(i).getType());
			newCase.setColor(caseDTO.get(i).getColor());
			newCase.setWeight(caseDTO.get(i).getWeight());
			newCase.setCoolingMethod(caseDTO.get(i).getCoolingMethod());
			

			listCase.add(newCase);

		}
		caseRepository.saveAll(listCase);
		if (listCase.size() == 0)
			ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		return ResponseEntity.status(HttpStatus.CREATED).body(listCase);
	}

	@PatchMapping("/{caseId}")
	@PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
	public ResponseEntity<?> editCase(@RequestBody CaseDTO caseDTO, @PathVariable("caseId") Long caseId) {
		Optional<Case> optionalCase = caseRepository.findByCaseId(caseId);
		if (!optionalCase.isPresent()) {
			throw new NotFoundException("Case not found");
		}
		Case newCase = optionalCase.get();
		Optional<Product> optionalProduct = productRepository.findById(newCase.getId());
		Product newProduct = optionalProduct.get();
		if (caseDTO.getProductDTO().getName() != null) {
			newCase.setName(caseDTO.getProductDTO().getName().trim().replaceAll("\\s+", " "));
			newProduct.setName(caseDTO.getProductDTO().getName().trim().replaceAll("\\s+", " "));
		}
		if (caseDTO.getProductDTO().getDescription() != null) {
			newCase.setDescription(caseDTO.getProductDTO().getDescription().trim().replaceAll("\\s+", " "));
			newProduct.setDescription(caseDTO.getProductDTO().getDescription().trim().replaceAll("\\s+", " "));
		}
		if (caseDTO.getProductDTO().getCategoryId() != null) {
			Category category = categoryRepository.findById(caseDTO.getProductDTO().getCategoryId()).get();
			newCase.setCategory(category);
			newProduct.setCategory(category);
		}
		if (caseDTO.getProductDTO().getPrice() != null) {
			newCase.setPrice(caseDTO.getProductDTO().getPrice());
			newProduct.setPrice(caseDTO.getProductDTO().getPrice());
		}
		if (caseDTO.getProductDTO().getBrand() != null) {
			newCase.setBrand(caseDTO.getProductDTO().getBrand().trim().replaceAll("\\s+", " "));
			newProduct.setBrand(caseDTO.getProductDTO().getBrand().trim().replaceAll("\\s+", " "));
		}
		if (caseDTO.getProductDTO().getAmount() != null) {
			newCase.setAmount(caseDTO.getProductDTO().getAmount());
			newProduct.setAmount(caseDTO.getProductDTO().getAmount());
		}
		if (caseDTO.getColor() != null) {
			newCase.setColor(caseDTO.getColor().trim().replaceAll("\\s+", " "));
		}
		if (caseDTO.getCoolingMethod() != null) {
			newCase.setCoolingMethod(caseDTO.getCoolingMethod().trim().replaceAll("\\s+", " "));
		}
		if (caseDTO.getDimensions() != null) {
			newCase.setDimensions(caseDTO.getDimensions().trim().replaceAll("\\s+", " "));
		}
		if (caseDTO.getMaterial() != null) {
			newCase.setMaterial(caseDTO.getMaterial().trim().replaceAll("\\s+", " "));
		}
		if (caseDTO.getWeight() != null) {
			newCase.setWeight(caseDTO.getWeight().trim().replaceAll("\\s+", " "));
		}
		if (caseDTO.getType() != null) {
			newCase.setType(caseDTO.getType().trim().replaceAll("\\s+", " "));
		}
		caseRepository.save(newCase);
		productRepository.save(newProduct);
		return ResponseEntity.status(HttpStatus.OK).body(newCase);
	}
	
    @DeleteMapping("/{caseId}")
    @PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
    public ResponseEntity<?> deleteCase(@PathVariable("CaseId") Long caseId) {
        Optional<Case> optionalCase = caseRepository.findByCaseId(caseId);
        if (!optionalCase.isPresent()) {
            throw new NotFoundException("Product not found");
        }

        if (!optionalCase.get().getProductImages().isEmpty()) {
            throw new InvalidException("Delete failed");
        }
        
        productRepository.deleteById(optionalCase.get().getId());
        caseRepository.deleteByCaseId(optionalCase.get().getCaseId());
        return ResponseEntity.status(HttpStatus.OK).body(optionalCase.get());
    }
}
