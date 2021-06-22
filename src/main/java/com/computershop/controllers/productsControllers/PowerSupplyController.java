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
import com.computershop.dao.product.PowerSupply;
import com.computershop.dto.products.PowerSupplyDTO;
import com.computershop.dto.productsDetail.PowerSupplyDetail;
import com.computershop.exceptions.DuplicateException;
import com.computershop.exceptions.InvalidException;
import com.computershop.exceptions.NotFoundException;
import com.computershop.helpers.ConvertObject;
import com.computershop.helpers.Validate;
import com.computershop.repositories.CategoryRepository;
import com.computershop.repositories.ProductRepository;
import com.computershop.repositories.productRepos.PowerSupplyRepository;

@RestController
@RequestMapping("/api/products/power-supplies")
public class PowerSupplyController {
	@Autowired
	private PowerSupplyRepository powerSupplyRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;
	
	
	@GetMapping
	public ResponseEntity<?> getAllPowerSupply(@RequestParam(name = "page", required = false) Integer pageNum,
										@RequestParam(name = "type", required = false) String type,
										@RequestParam(name = "search", required = false) String search) {
		if (search != null) {
			String searchConvert = ConvertObject.fromSlugToString(search);
			List<PowerSupply> listPowerSupplies = powerSupplyRepository.findByNameContainingIgnoreCase(searchConvert);
			if (listPowerSupplies.size() == 0) {
				return ResponseEntity.noContent().build();
			}
			List<PowerSupplyDetail> listpowerSupplyDetail = new LinkedList<PowerSupplyDetail>();
			for (int i = 0; i < listPowerSupplies.size(); i++) {
				PowerSupply getPowerSupply = listPowerSupplies.get(i);
				List<ProductImage> getProductImages = getPowerSupply.getProductImages();
				if (getProductImages.size() == 0) {
					getProductImages.add(new ProductImage());
				}
				listpowerSupplyDetail.add(new PowerSupplyDetail(getPowerSupply, getProductImages));
			}

			return ResponseEntity.ok().body(listpowerSupplyDetail);
		}
		if (type != null) {
			if (type.compareTo("without-image") == 0) {
				List<PowerSupply> listPowerSupplies = powerSupplyRepository.findAll();
				if (listPowerSupplies.size() == 0) {
					return ResponseEntity.noContent().build();
				}
				return ResponseEntity.ok().body(listPowerSupplies);
			}
		}
		if (pageNum != null) {
			Page<PowerSupply> page = powerSupplyRepository.findAll(PageRequest.of(pageNum.intValue(), 20));

			List<PowerSupply> listPowerSupplies = page.getContent();
			if (listPowerSupplies.size() == 0) {
				return ResponseEntity.noContent().build();
			}
			List<PowerSupplyDetail> listPowerSupplyDetail = new LinkedList<PowerSupplyDetail>();
			for (int i = 0; i < listPowerSupplies.size(); i++) {
				PowerSupply getPowerSupply = listPowerSupplies.get(i);
				List<ProductImage> getProductImages = getPowerSupply.getProductImages();
				if (getProductImages.size() == 0) {
					getProductImages.add(new ProductImage());
				}
				listPowerSupplyDetail.add(new PowerSupplyDetail(getPowerSupply, getProductImages));
			}
			return ResponseEntity.ok().body(listPowerSupplyDetail);
		}

		List<PowerSupply> listPowerSupplies = powerSupplyRepository.findAll();
		if (listPowerSupplies.size() == 0) {
			return ResponseEntity.noContent().build();
		}
		List<PowerSupplyDetail> listPowerSupplyDetail = new LinkedList<PowerSupplyDetail>();
		for (int i = 0; i < listPowerSupplies.size(); i++) {
			PowerSupply getPowerSupply = listPowerSupplies.get(i);
			List<ProductImage> getProductImages = getPowerSupply.getProductImages();
			if (getProductImages.size() == 0) {
				getProductImages.add(new ProductImage());
			}
			listPowerSupplyDetail.add(new PowerSupplyDetail(getPowerSupply, getProductImages));
		}
		return ResponseEntity.ok().body(listPowerSupplyDetail);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getPowerSupplyById(@PathVariable("id") Long id) {
		Optional<PowerSupply> optionalPowerSupply = powerSupplyRepository.findById(id);
		PowerSupply powerSupplyFound = optionalPowerSupply.get();
		if (powerSupplyFound == null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		if (powerSupplyFound.getProductImages().size() == 0) {
			return ResponseEntity.status(HttpStatus.OK).body(powerSupplyFound);
		}
		PowerSupplyDetail powerSupplyDetail = new PowerSupplyDetail(powerSupplyFound, powerSupplyFound.getProductImages());

		return ResponseEntity.ok().body(powerSupplyDetail);
	}
	
	
	@PostMapping
	@PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
	public ResponseEntity<?> createNewPowerSupply(@RequestBody PowerSupplyDTO powerSupplyDTO) {
		Product oldProduct = productRepository.findByName(powerSupplyDTO.getProductDTO().getName());
		if (oldProduct != null) {
			throw new DuplicateException("PowerSupply has already exists");
		}

		Optional<Category> optionalCategory = categoryRepository.findById(powerSupplyDTO.getProductDTO().getCategoryId());

		if (!optionalCategory.isPresent()) {
			throw new NotFoundException(
					"Not found category with category id " + powerSupplyDTO.getProductDTO().getCategoryId());
		}

		Product product = ConvertObject.fromProductDTOToProductDAO(powerSupplyDTO.getProductDTO());
		product.setCategory(optionalCategory.get());
		Validate.checkProduct(product);
		Product saveProduct = productRepository.save(product);

		PowerSupply newPowerSupply = new PowerSupply(saveProduct);
		newPowerSupply.setConnectorType(powerSupplyDTO.getConnectorType());
		newPowerSupply.setDimentions(powerSupplyDTO.getDimentions());
		newPowerSupply.setInputVoltage(powerSupplyDTO.getInputVoltage());
		newPowerSupply.setOutputVoltage(powerSupplyDTO.getOutputVoltage());
		newPowerSupply.setRatedCurrent(powerSupplyDTO.getRatedCurrent());

		PowerSupply savePowerSupply = powerSupplyRepository.save(newPowerSupply);
		return ResponseEntity.status(HttpStatus.CREATED).body(savePowerSupply);
	}
	
	@PostMapping("/power-supplies-collection")
	@PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
	public ResponseEntity<?> createNewPowerSupplies(@RequestBody List<PowerSupplyDTO> powerSuppliesDTO) {
		List<PowerSupply> listPowerSupplies = new LinkedList<PowerSupply>();
		for(int i = 0; i < powerSuppliesDTO.size(); i++) {
			Product oldProduct = productRepository.findByName(powerSuppliesDTO.get(i).getProductDTO().getName());
			if (oldProduct != null) {
				throw new DuplicateException("Product has already exists");
			}

			Optional<Category> optionalCategory = categoryRepository.findById(powerSuppliesDTO.get(i).getProductDTO().getCategoryId());

			if (!optionalCategory.isPresent()) {
				throw new NotFoundException(
						"Not found category with category id " + powerSuppliesDTO.get(i).getProductDTO().getCategoryId());
			}

			Product product = ConvertObject.fromProductDTOToProductDAO(powerSuppliesDTO.get(i).getProductDTO());
			product.setCategory(optionalCategory.get());
			Validate.checkProduct(product);
			Product saveProduct = productRepository.save(product);

			PowerSupply newPowerSupply = new PowerSupply(saveProduct);
			newPowerSupply.setConnectorType(powerSuppliesDTO.get(i).getConnectorType());
			newPowerSupply.setDimentions(powerSuppliesDTO.get(i).getDimentions());
			newPowerSupply.setInputVoltage(powerSuppliesDTO.get(i).getInputVoltage());
			newPowerSupply.setOutputVoltage(powerSuppliesDTO.get(i).getOutputVoltage());
			newPowerSupply.setRatedCurrent(powerSuppliesDTO.get(i).getRatedCurrent());

			listPowerSupplies.add(newPowerSupply);
		}
		powerSupplyRepository.saveAll(listPowerSupplies);
		if(listPowerSupplies.size()==0)
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		return ResponseEntity.status(HttpStatus.CREATED).body(listPowerSupplies);
		
	}

	@PatchMapping({"powerSupplyId"})
	@PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
	public ResponseEntity<?> editGraPhicCardById(@RequestBody PowerSupplyDTO powerSupplyDTO, @PathVariable("powerSupplyId") Long powerSupplyId) {
		Optional<PowerSupply> optionalPowerSupply = powerSupplyRepository.findByPowerSupplyId(powerSupplyId);
		if (!optionalPowerSupply.isPresent()) {
			throw new NotFoundException("PowerSupply not found");
		}
		PowerSupply newPowerSupply = optionalPowerSupply.get();
		Optional<Product> optionalProduct = productRepository.findById(newPowerSupply.getId());
		Product newProduct = optionalProduct.get();
		if (powerSupplyDTO.getProductDTO().getName() != null) {
			newPowerSupply.setName(powerSupplyDTO.getProductDTO().getName().trim().replaceAll("\\s+", " "));
			newProduct.setName(powerSupplyDTO.getProductDTO().getName().trim().replaceAll("\\s+", " "));
		}
		if (powerSupplyDTO.getProductDTO().getDescription() != null) {
			newPowerSupply.setDescription(powerSupplyDTO.getProductDTO().getDescription().trim().replaceAll("\\s+", " "));
			newProduct.setDescription(powerSupplyDTO.getProductDTO().getDescription().trim().replaceAll("\\s+", " "));
		}
		if (powerSupplyDTO.getProductDTO().getCategoryId() != null) {
			Category category = categoryRepository.findById(powerSupplyDTO.getProductDTO().getCategoryId()).get();
			newPowerSupply.setCategory(category);
			newProduct.setCategory(category);
		}
		if (powerSupplyDTO.getProductDTO().getPrice() != null) {
			newPowerSupply.setPrice(powerSupplyDTO.getProductDTO().getPrice());
			newProduct.setPrice(powerSupplyDTO.getProductDTO().getPrice());
		}
		if (powerSupplyDTO.getProductDTO().getBrand() != null) {
			newPowerSupply.setBrand(powerSupplyDTO.getProductDTO().getBrand().trim().replaceAll("\\s+", " "));
			newProduct.setBrand(powerSupplyDTO.getProductDTO().getBrand().trim().replaceAll("\\s+", " "));
		}
		if (powerSupplyDTO.getProductDTO().getAmount() != null) {
			newPowerSupply.setAmount(powerSupplyDTO.getProductDTO().getAmount());
			newProduct.setAmount(powerSupplyDTO.getProductDTO().getAmount());
		}
		if(powerSupplyDTO.getConnectorType()!=null) {
			newPowerSupply.setConnectorType(powerSupplyDTO.getConnectorType().trim().replaceAll("\\s+", " "));
		}
		if(powerSupplyDTO.getDimentions()!=null) {
			newPowerSupply.setDimentions(powerSupplyDTO.getDimentions().trim().replaceAll("\\s+", " "));
		}
		if(powerSupplyDTO.getInputVoltage()!=null) {
			newPowerSupply.setInputVoltage(powerSupplyDTO.getInputVoltage().trim().replaceAll("\\s+", " "));
		}
		if(powerSupplyDTO.getOutputVoltage()!=null) {
			newPowerSupply.setOutputVoltage(powerSupplyDTO.getOutputVoltage().trim().replaceAll("\\s+", " "));
		}
		if(powerSupplyDTO.getRatedCurrent()!=null) {
			newPowerSupply.setRatedCurrent(powerSupplyDTO.getRatedCurrent().trim().replaceAll("\\s+", " "));
		}
		powerSupplyRepository.save(newPowerSupply);
		productRepository.save(newProduct);
		return ResponseEntity.status(HttpStatus.OK).body(newPowerSupply);
	}
	
	
	@DeleteMapping("/{powerSupplyId}")
	@PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
	public ResponseEntity<?> deletePowerSupply(@PathVariable("powerSupplyId") Long powerSupplyId) {
		Optional<PowerSupply> optionalPowerSupply = powerSupplyRepository.findByPowerSupplyId(powerSupplyId);
		if (!optionalPowerSupply.isPresent()) {
			throw new NotFoundException("PowerSupply not found");
		}

		if (!optionalPowerSupply.get().getProductImages().isEmpty()) {
			throw new InvalidException("Delete failed");
		}

		productRepository.deleteById(optionalPowerSupply.get().getId());
		powerSupplyRepository.deleteByPowerSupplyId(optionalPowerSupply.get().getPowerSupplyId());

		return ResponseEntity.status(HttpStatus.OK).body(optionalPowerSupply.get());
	}

}
