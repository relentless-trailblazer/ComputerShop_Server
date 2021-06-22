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
import com.computershop.dao.product.GraphicCard;
import com.computershop.dto.products.GraphicCardDTO;
import com.computershop.dto.productsDetail.GraphicCardDetail;
import com.computershop.exceptions.DuplicateException;
import com.computershop.exceptions.InvalidException;
import com.computershop.exceptions.NotFoundException;
import com.computershop.helpers.ConvertObject;
import com.computershop.helpers.Validate;
import com.computershop.repositories.CategoryRepository;
import com.computershop.repositories.ProductRepository;
import com.computershop.repositories.productRepos.GraphicCardRepository;

@RestController
@RequestMapping("/api/products/graphic-cards")
public class GraphicCardController {
	@Autowired
	private GraphicCardRepository graphicCardRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;
	
	@GetMapping
	public ResponseEntity<?> getAllGraphicCard(@RequestParam(name = "page", required = false) Integer pageNum,
										@RequestParam(name = "type", required = false) String type,
										@RequestParam(name = "search", required = false) String search) {
		if (search != null) {
			String searchConvert = ConvertObject.fromSlugToString(search);
			List<GraphicCard> listGraphicCards = graphicCardRepository.findByNameContainingIgnoreCase(searchConvert);
			if (listGraphicCards.size() == 0) {
				return ResponseEntity.noContent().build();
			}
			List<GraphicCardDetail> listGraphicCardDetail = new LinkedList<GraphicCardDetail>();
			for (int i = 0; i < listGraphicCards.size(); i++) {
				GraphicCard getGraphicCard = listGraphicCards.get(i);
				List<ProductImage> getProductImages = getGraphicCard.getProductImages();
				if (getProductImages.size() == 0) {
					getProductImages.add(new ProductImage());
				}
				listGraphicCardDetail.add(new GraphicCardDetail(getGraphicCard, getProductImages));
			}

			return ResponseEntity.ok().body(listGraphicCardDetail);
		}
		if (type != null) {
			if (type.compareTo("without-image") == 0) {
				List<GraphicCard> listGraphicCards = graphicCardRepository.findAll();
				if (listGraphicCards.size() == 0) {
					return ResponseEntity.noContent().build();
				}
				return ResponseEntity.ok().body(listGraphicCards);
			}
		}
		if (pageNum != null) {
			Page<GraphicCard> page = graphicCardRepository.findAll(PageRequest.of(pageNum.intValue(), 20));

			List<GraphicCard> listGraphicCards = page.getContent();
			if (listGraphicCards.size() == 0) {
				return ResponseEntity.noContent().build();
			}
			List<GraphicCardDetail> listGraphicCardDetail = new LinkedList<GraphicCardDetail>();
			for (int i = 0; i < listGraphicCards.size(); i++) {
				GraphicCard getGraphicCard = listGraphicCards.get(i);
				List<ProductImage> getProductImages = getGraphicCard.getProductImages();
				if (getProductImages.size() == 0) {
					getProductImages.add(new ProductImage());
				}
				listGraphicCardDetail.add(new GraphicCardDetail(getGraphicCard, getProductImages));
			}
			return ResponseEntity.ok().body(listGraphicCardDetail);
		}

		List<GraphicCard> listGraphicCards = graphicCardRepository.findAll();
		if (listGraphicCards.size() == 0) {
			return ResponseEntity.noContent().build();
		}
		List<GraphicCardDetail> listGraphicCardDetail = new LinkedList<GraphicCardDetail>();
		for (int i = 0; i < listGraphicCards.size(); i++) {
			GraphicCard getGraphicCard = listGraphicCards.get(i);
			List<ProductImage> getProductImages = getGraphicCard.getProductImages();
			if (getProductImages.size() == 0) {
				getProductImages.add(new ProductImage());
			}
			listGraphicCardDetail.add(new GraphicCardDetail(getGraphicCard, getProductImages));
		}
		return ResponseEntity.ok().body(listGraphicCardDetail);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getGraphicCardById(@PathVariable("id") Long id) {
		Optional<GraphicCard> optionalGraphicCard = graphicCardRepository.findById(id);
		GraphicCard graphicCardFound = optionalGraphicCard.get();
		if (graphicCardFound == null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		if (graphicCardFound.getProductImages().size() == 0) {
			return ResponseEntity.status(HttpStatus.OK).body(graphicCardFound);
		}
		GraphicCardDetail graphicCardDetail = new GraphicCardDetail(graphicCardFound, graphicCardFound.getProductImages());

		return ResponseEntity.ok().body(graphicCardDetail);
	}
	
	@PostMapping
	@PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
	public ResponseEntity<?> createNewGraphicCard(@RequestBody GraphicCardDTO graphicCardDTO) {
		Product oldProduct = productRepository.findByName(graphicCardDTO.getProductDTO().getName());
		if (oldProduct != null) {
			throw new DuplicateException("Graphic Card has already exists");
		}

		Optional<Category> optionalCategory = categoryRepository.findById(graphicCardDTO.getProductDTO().getCategoryId());

		if (!optionalCategory.isPresent()) {
			throw new NotFoundException(
					"Not found category with category id " + graphicCardDTO.getProductDTO().getCategoryId());
		}

		Product product = ConvertObject.fromProductDTOToProductDAO(graphicCardDTO.getProductDTO());
		product.setCategory(optionalCategory.get());
		Validate.checkProduct(product);
		Product saveProduct = productRepository.save(product);

		GraphicCard newGraphicCard = new GraphicCard(saveProduct);
		newGraphicCard.setDimensions(graphicCardDTO.getDimensions());
		newGraphicCard.setWeight(graphicCardDTO.getWeight());
		newGraphicCard.setVGAMemory(graphicCardDTO.getVGAMemory());
		newGraphicCard.setBandwidth(graphicCardDTO.getBandwidth());
		newGraphicCard.setVoltage(graphicCardDTO.getVoltage());

		GraphicCard saveGraphicCard = graphicCardRepository.save(newGraphicCard);
		return ResponseEntity.status(HttpStatus.CREATED).body(saveGraphicCard);
	}
	
	@PostMapping("/graphic-cards-collection")
	@PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
	public ResponseEntity<?> createNewGraphicCards(@RequestBody List<GraphicCardDTO> graphicCardDTO) {
		List<GraphicCard> listGraphicCard = new LinkedList<GraphicCard>();
		for(int i = 0; i < graphicCardDTO.size(); i++) {
			Product oldProduct = productRepository.findByName(graphicCardDTO.get(i).getProductDTO().getName());
			if (oldProduct != null) {
				throw new DuplicateException("Graphic card has already exists");
			}

			Optional<Category> optionalCategory = categoryRepository.findById(graphicCardDTO.get(i).getProductDTO().getCategoryId());

			if (!optionalCategory.isPresent()) {
				throw new NotFoundException(
						"Not found category with category id " + graphicCardDTO.get(i).getProductDTO().getCategoryId());
			}

			Product product = ConvertObject.fromProductDTOToProductDAO(graphicCardDTO.get(i).getProductDTO());
			product.setCategory(optionalCategory.get());
			Validate.checkProduct(product);
			Product saveProduct = productRepository.save(product);

			GraphicCard newGraphicCard = new GraphicCard(saveProduct);
			newGraphicCard.setDimensions(graphicCardDTO.get(i).getDimensions());
			newGraphicCard.setWeight(graphicCardDTO.get(i).getWeight());
			newGraphicCard.setVGAMemory(graphicCardDTO.get(i).getVGAMemory());
			newGraphicCard.setBandwidth(graphicCardDTO.get(i).getBandwidth());
			newGraphicCard.setVoltage(graphicCardDTO.get(i).getVoltage());

			listGraphicCard.add(newGraphicCard);
		}
		graphicCardRepository.saveAll(listGraphicCard);
		if(listGraphicCard.size()==0)
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		return ResponseEntity.status(HttpStatus.CREATED).body(listGraphicCard);
		
	}
	
	@PatchMapping({"graphicCardId"})
	@PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
	public ResponseEntity<?> editGraPhicCardById(@RequestBody GraphicCardDTO graphicCardDTO, @PathVariable("graphicCardId") Long graphicCardId) {
		Optional<GraphicCard> optionalGraphicCard = graphicCardRepository.findByGraphicCardId(graphicCardId);
		if (!optionalGraphicCard.isPresent()) {
			throw new NotFoundException("GraphicCard not found");
		}
		GraphicCard newGraphicCard = optionalGraphicCard.get();
		Optional<Product> optionalProduct = productRepository.findById(newGraphicCard.getId());
		Product newProduct = optionalProduct.get();
		if (graphicCardDTO.getProductDTO().getName() != null) {
			newGraphicCard.setName(graphicCardDTO.getProductDTO().getName().trim().replaceAll("\\s+", " "));
			newProduct.setName(graphicCardDTO.getProductDTO().getName().trim().replaceAll("\\s+", " "));
		}
		if (graphicCardDTO.getProductDTO().getDescription() != null) {
			newGraphicCard.setDescription(graphicCardDTO.getProductDTO().getDescription().trim().replaceAll("\\s+", " "));
			newProduct.setDescription(graphicCardDTO.getProductDTO().getDescription().trim().replaceAll("\\s+", " "));
		}
		if (graphicCardDTO.getProductDTO().getCategoryId() != null) {
			Category category = categoryRepository.findById(graphicCardDTO.getProductDTO().getCategoryId()).get();
			newGraphicCard.setCategory(category);
			newProduct.setCategory(category);
		}
		if (graphicCardDTO.getProductDTO().getPrice() != null) {
			newGraphicCard.setPrice(graphicCardDTO.getProductDTO().getPrice());
			newProduct.setPrice(graphicCardDTO.getProductDTO().getPrice());
		}
		if (graphicCardDTO.getProductDTO().getBrand() != null) {
			newGraphicCard.setBrand(graphicCardDTO.getProductDTO().getBrand().trim().replaceAll("\\s+", " "));
			newProduct.setBrand(graphicCardDTO.getProductDTO().getBrand().trim().replaceAll("\\s+", " "));
		}
		if (graphicCardDTO.getProductDTO().getAmount() != null) {
			newGraphicCard.setAmount(graphicCardDTO.getProductDTO().getAmount());
			newProduct.setAmount(graphicCardDTO.getProductDTO().getAmount());
		}
		if(graphicCardDTO.getDimensions() != null) {
			newGraphicCard.setDimensions(graphicCardDTO.getDimensions().trim().replaceAll("\\s+", " "));
		}
		if(graphicCardDTO.getBandwidth() != null) {
			newGraphicCard.setBandwidth(graphicCardDTO.getBandwidth().trim().replaceAll("\\s+", " "));
		}
		if(graphicCardDTO.getVGAMemory() != null) {
			newGraphicCard.setVGAMemory(graphicCardDTO.getVGAMemory().trim().replaceAll("\\s+", " "));
		}
		if(graphicCardDTO.getVoltage() != null) {
			newGraphicCard.setVoltage(graphicCardDTO.getVoltage().trim().replaceAll("\\s+", " "));
		}
		if(graphicCardDTO.getWeight() != null) {
			newGraphicCard.setWeight(graphicCardDTO.getWeight().trim().replaceAll("\\s+", " "));
		}
		graphicCardRepository.save(newGraphicCard);
		productRepository.save(newProduct);
		return ResponseEntity.status(HttpStatus.OK).body(newGraphicCard);
	}
	
	@DeleteMapping("/{graphicCardId}")
	@PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
	public ResponseEntity<?> deleteGraphicCard(@PathVariable("graphicCardId") Long graphicCardId) {
		Optional<GraphicCard> optionalGraphicCard = graphicCardRepository.findByGraphicCardId(graphicCardId);
		if (!optionalGraphicCard.isPresent()) {
			throw new NotFoundException("Graphic card not found");
		}

		if (!optionalGraphicCard.get().getProductImages().isEmpty()) {
			throw new InvalidException("Delete failed");
		}

		productRepository.deleteById(optionalGraphicCard.get().getId());
		graphicCardRepository.deleteByGraphicCardId(optionalGraphicCard.get().getGraphicCardId());

		return ResponseEntity.status(HttpStatus.OK).body(optionalGraphicCard.get());
	}
}
