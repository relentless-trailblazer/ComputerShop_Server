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
import com.computershop.dao.product.Mainboard;
import com.computershop.dto.products.MainboardDTO;
import com.computershop.dto.productsDetail.MainboardDetail;
import com.computershop.exceptions.DuplicateException;
import com.computershop.exceptions.InvalidException;
import com.computershop.exceptions.NotFoundException;
import com.computershop.helpers.ConvertObject;
import com.computershop.helpers.Validate;
import com.computershop.repositories.CategoryRepository;
import com.computershop.repositories.ProductRepository;
import com.computershop.repositories.productRepos.MainboardRepository;

@RestController
@RequestMapping("/api/products/mainboards")
public class MainboardController {
	@Autowired
	private MainboardRepository mainboardRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;
	
	@GetMapping
	public ResponseEntity<?> getAllMainboard(@RequestParam(name = "page", required = false) Integer pageNum,
										@RequestParam(name = "type", required = false) String type,
										@RequestParam(name = "search", required = false) String search) {
		if (search != null) {
			String searchConvert = ConvertObject.fromSlugToString(search);
			List<Mainboard> listMainboards = mainboardRepository.findByNameContainingIgnoreCase(searchConvert);
			if (listMainboards.size() == 0) {
				return ResponseEntity.noContent().build();
			}
			List<MainboardDetail> listmainboardDetail = new LinkedList<MainboardDetail>();
			for (int i = 0; i < listMainboards.size(); i++) {
				Mainboard getMainboard = listMainboards.get(i);
				List<ProductImage> getProductImages = getMainboard.getProductImages();
				if (getProductImages.size() == 0) {
					getProductImages.add(new ProductImage());
				}
				listmainboardDetail.add(new MainboardDetail(getMainboard, getProductImages));
			}

			return ResponseEntity.ok().body(listMainboards);
		}
		if (type != null) {
			if (type.compareTo("without-image") == 0) {
				List<Mainboard> listmainboards = mainboardRepository.findAll();
				if (listmainboards.size() == 0) {
					return ResponseEntity.noContent().build();
				}
				return ResponseEntity.ok().body(listmainboards);
			}
		}
		if (pageNum != null) {
			Page<Mainboard> page = mainboardRepository.findAll(PageRequest.of(pageNum.intValue(), 20));

			List<Mainboard> listmainboards = page.getContent();
			if (listmainboards.size() == 0) {
				return ResponseEntity.noContent().build();
			}
			List<MainboardDetail> listmainboardDetail = new LinkedList<MainboardDetail>();
			for (int i = 0; i < listmainboards.size(); i++) {
				Mainboard getMainboard = listmainboards.get(i);
				List<ProductImage> getProductImages = getMainboard.getProductImages();
				if (getProductImages.size() == 0) {
					getProductImages.add(new ProductImage());
				}
				listmainboardDetail.add(new MainboardDetail(getMainboard, getProductImages));
			}
			return ResponseEntity.ok().body(listmainboardDetail);
		}

		List<Mainboard> listMainboards = mainboardRepository.findAll();
		if (listMainboards.size() == 0) {
			return ResponseEntity.noContent().build();
		}
		List<MainboardDetail> listMainboardDetail = new LinkedList<MainboardDetail>();
		for (int i = 0; i < listMainboards.size(); i++) {
			Mainboard getMainboard = listMainboards.get(i);
			List<ProductImage> getProductImages = getMainboard.getProductImages();
			if (getProductImages.size() == 0) {
				getProductImages.add(new ProductImage());
			}
			listMainboardDetail.add(new MainboardDetail(getMainboard, getProductImages));
		}
		return ResponseEntity.ok().body(listMainboardDetail);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getMainboardById(@PathVariable("id") Long id) {
		Optional<Mainboard> optionalMainboard = mainboardRepository.findById(id);
		Mainboard mainboardFound = optionalMainboard.get();
		if (mainboardFound == null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		if (mainboardFound.getProductImages().size() == 0) {
			return ResponseEntity.status(HttpStatus.OK).body(mainboardFound);
		}
		MainboardDetail mainboardDetail = new MainboardDetail(mainboardFound, mainboardFound.getProductImages());

		return ResponseEntity.ok().body(mainboardDetail);
	}
	
	@PostMapping
	@PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
	public ResponseEntity<?> createNewMainboard(@RequestBody MainboardDTO mainboardDTO) {
		Product oldProduct = productRepository.findByName(mainboardDTO.getProductDTO().getName());
		if (oldProduct != null) {
			throw new DuplicateException("Mainboard has already exists");
		}

		Optional<Category> optionalCategory = categoryRepository.findById(mainboardDTO.getProductDTO().getCategoryId());

		if (!optionalCategory.isPresent()) {
			throw new NotFoundException(
					"Not found category with category id " + mainboardDTO.getProductDTO().getCategoryId());
		}

		Product product = ConvertObject.fromProductDTOToProductDAO(mainboardDTO.getProductDTO());
		product.setCategory(optionalCategory.get());
		Validate.checkProduct(product);
		Product saveProduct = productRepository.save(product);

		Mainboard newMainboard = new Mainboard(saveProduct);
		newMainboard.setChipset(mainboardDTO.getChipset());
		newMainboard.setCpu(mainboardDTO.getCpu());
		newMainboard.setFormFactors(mainboardDTO.getFormFactors());
		newMainboard.setSocket(mainboardDTO.getSocket());
		newMainboard.setOSs(mainboardDTO.getOSs());
		newMainboard.setAccessories(mainboardDTO.getAccessories());

		Mainboard saveMainboard = mainboardRepository.save(newMainboard);
		return ResponseEntity.status(HttpStatus.CREATED).body(saveMainboard);
	}
	
	@PostMapping("/mainboard-collection")
	@PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
	public ResponseEntity<?> createNewMainboards(@RequestBody List<MainboardDTO> mainboardDTO) {
		List<Mainboard> listMainboard = new LinkedList<Mainboard>();
		for(int i = 0; i < mainboardDTO.size(); i++) {
			Product oldProduct = productRepository.findByName(mainboardDTO.get(i).getProductDTO().getName());
			if (oldProduct != null) {
				throw new DuplicateException("Product has already exists");
			}

			Optional<Category> optionalCategory = categoryRepository.findById(mainboardDTO.get(i).getProductDTO().getCategoryId());

			if (!optionalCategory.isPresent()) {
				throw new NotFoundException(
						"Not found category with category id " + mainboardDTO.get(i).getProductDTO().getCategoryId());
			}

			Product product = ConvertObject.fromProductDTOToProductDAO(mainboardDTO.get(i).getProductDTO());
			product.setCategory(optionalCategory.get());
			Validate.checkProduct(product);
			Product saveProduct = productRepository.save(product);

			Mainboard newMainboard = new Mainboard(saveProduct);
			newMainboard.setChipset(mainboardDTO.get(i).getChipset());
			newMainboard.setCpu(mainboardDTO.get(i).getCpu());
			newMainboard.setFormFactors(mainboardDTO.get(i).getFormFactors());
			newMainboard.setSocket(mainboardDTO.get(i).getSocket());
			newMainboard.setOSs(mainboardDTO.get(i).getOSs());
			newMainboard.setAccessories(mainboardDTO.get(i).getAccessories());

			listMainboard.add(newMainboard);
		}
		mainboardRepository.saveAll(listMainboard);
		if(listMainboard.size()==0)
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		return ResponseEntity.status(HttpStatus.CREATED).body(listMainboard);
		
	}
	
	@PatchMapping({"mainboardId"})
	@PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
	public ResponseEntity<?> editGraPhicCardById(@RequestBody MainboardDTO mainboardDTO, @PathVariable("mainboardId") Long mainboardId) {
		Optional<Mainboard> optionalMainboard = mainboardRepository.findByMainboardId(mainboardId);
		if (!optionalMainboard.isPresent()) {
			throw new NotFoundException("Mainboard not found");
		}
		Mainboard newMainboard = optionalMainboard.get();
		Optional<Product> optionalProduct = productRepository.findById(newMainboard.getId());
		Product newProduct = optionalProduct.get();
		if (mainboardDTO.getProductDTO().getName() != null) {
			newMainboard.setName(mainboardDTO.getProductDTO().getName().trim().replaceAll("\\s+", " "));
			newProduct.setName(mainboardDTO.getProductDTO().getName().trim().replaceAll("\\s+", " "));
		}
		if (mainboardDTO.getProductDTO().getDescription() != null) {
			newMainboard.setDescription(mainboardDTO.getProductDTO().getDescription().trim().replaceAll("\\s+", " "));
			newProduct.setDescription(mainboardDTO.getProductDTO().getDescription().trim().replaceAll("\\s+", " "));
		}
		if (mainboardDTO.getProductDTO().getCategoryId() != null) {
			Category category = categoryRepository.findById(mainboardDTO.getProductDTO().getCategoryId()).get();
			newMainboard.setCategory(category);
			newProduct.setCategory(category);
		}
		if (mainboardDTO.getProductDTO().getPrice() != null) {
			newMainboard.setPrice(mainboardDTO.getProductDTO().getPrice());
			newProduct.setPrice(mainboardDTO.getProductDTO().getPrice());
		}
		if (mainboardDTO.getProductDTO().getBrand() != null) {
			newMainboard.setBrand(mainboardDTO.getProductDTO().getBrand().trim().replaceAll("\\s+", " "));
			newProduct.setBrand(mainboardDTO.getProductDTO().getBrand().trim().replaceAll("\\s+", " "));
		}
		if (mainboardDTO.getProductDTO().getAmount() != null) {
			newMainboard.setAmount(mainboardDTO.getProductDTO().getAmount());
			newProduct.setAmount(mainboardDTO.getProductDTO().getAmount());
		}
		
		if(mainboardDTO.getAccessories()!=null) {
			newMainboard.setAccessories(mainboardDTO.getAccessories().trim().replaceAll("\\s+", " "));
		}
		if(mainboardDTO.getChipset()!=null) {
			newMainboard.setChipset(mainboardDTO.getChipset().trim().replaceAll("\\s+", " "));
		}
		if(mainboardDTO.getCpu()!=null) {
			newMainboard.setCpu(mainboardDTO.getCpu().trim().replaceAll("\\s+", " "));
		}
		if(mainboardDTO.getFormFactors()!=null) {
			newMainboard.setFormFactors(mainboardDTO.getFormFactors().trim().replaceAll("\\s+", " "));
		}
		if(mainboardDTO.getOSs()!=null) {
			newMainboard.setOSs(mainboardDTO.getOSs().trim().replaceAll("\\s+", " "));
		}
		if(mainboardDTO.getSocket()!=null) {
			newMainboard.setSocket(mainboardDTO.getSocket().trim().replaceAll("\\s+", " "));
		}
		mainboardRepository.save(newMainboard);
		productRepository.save(newProduct);
		return ResponseEntity.status(HttpStatus.OK).body(newMainboard);
	}
	
	@DeleteMapping("/{mainboardId}")
	@PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
	public ResponseEntity<?> deleteMainboard(@PathVariable("mainboardId") Long mainboardId) {
		Optional<Mainboard> optionalMainboard = mainboardRepository.findByMainboardId(mainboardId);
		if (!optionalMainboard.isPresent()) {
			throw new NotFoundException("Mainboard not found");
		}

		if (!optionalMainboard.get().getProductImages().isEmpty()) {
			throw new InvalidException("Delete failed");
		}

		productRepository.deleteById(optionalMainboard.get().getId());
		mainboardRepository.deleteByMainboardId(optionalMainboard.get().getMainboardId());

		return ResponseEntity.status(HttpStatus.OK).body(optionalMainboard.get());
	}
}
