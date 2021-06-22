package com.computershop.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
//import java.util.Locale.Category;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

//import com.cloudinary.Cloudinary;
import com.computershop.dao.Product;
import com.computershop.dao.ProductImage;
import com.computershop.dto.ProductWithImage;
import com.computershop.exceptions.NotFoundException;
import com.computershop.helpers.ConvertObject;
import com.computershop.repositories.CategoryRepository;
import com.computershop.repositories.ProductImageRepository;
import com.computershop.repositories.ProductRepository;
import com.computershop.services.CloudinaryService;

@RestController
@RequestMapping("/api/product-images")
public class ProductImageController {
	

	@Autowired
	private ProductImageRepository productImageRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private CloudinaryService cloudinaryService;
	
	@GetMapping
    public ResponseEntity<?> getProductImages(@RequestParam(name = "pid", required = false) Long productId,
                                              @RequestParam(name = "search", required = false) String search,
                                              @RequestParam(name = "category", required = false) String category) {
       
		List<Product> products = new LinkedList<Product>();
		if(search!=null && category!=null) {
			products = productRepository.findByNameContainingIgnoreCase(ConvertObject.fromSlugToString(search));
			
			List<ProductWithImage> productsWithImages = new ArrayList<ProductWithImage>();
			for (int i = 0; i < products.size(); i++) {
				String productCategory = products.get(i).getCategory().getName().toLowerCase();
                if (!products.get(i).getProductImages().isEmpty() && productCategory.compareToIgnoreCase(category)==0 ) {
                	ProductWithImage productWithImage = new ProductWithImage(products.get(i), products.get(i).getProductImages());
                	productsWithImages.add(productWithImage);
                }
            }
			if (productsWithImages.size() == 0) 
                return ResponseEntity.noContent().build();
            return ResponseEntity.ok().body(productsWithImages);
		
		}
		if(productId!=null) {
			Optional<Product> optionalProduct = productRepository.findById(productId);
			if(!optionalProduct.isPresent())
				throw new NotFoundException("Not found product with id " + productId +"!");
			ProductWithImage productWithImages = new ProductWithImage(optionalProduct.get(), optionalProduct.get().getProductImages());
            return ResponseEntity.ok().body(productWithImages);
		
		}
		
		if(search!=null) {
			products = productRepository.findByNameContainingIgnoreCase(ConvertObject.fromSlugToString(search));
			List<ProductWithImage> productsWithImages = new ArrayList<ProductWithImage>();
			for (int i = 0; i < products.size(); i++) {
                if (!products.get(i).getProductImages().isEmpty()) {
                	ProductWithImage productWithImage = new ProductWithImage(products.get(i), products.get(i).getProductImages());
                	productsWithImages.add(productWithImage);
                }
            }
			if (productsWithImages.size() == 0) 
                return ResponseEntity.noContent().build();
            return ResponseEntity.ok().body(productsWithImages);
		
		}
		
		if(category!=null) {
			List<com.computershop.dao.Category> categories = categoryRepository.findByNameContainingIgnoreCase(ConvertObject.fromSlugToString(category));
				
			for (com.computershop.dao.Category cat : categories) {
			       for(Product prd : cat.getProducts()) {
			       	products.add(prd);
			       }    
			}
			
			
			List<ProductWithImage> productsWithImages = new ArrayList<ProductWithImage>();
			for (int i = 0; i < products.size(); i++) {
                if (!products.get(i).getProductImages().isEmpty()) {
                	ProductWithImage productWithImage = new ProductWithImage(products.get(i), products.get(i).getProductImages());
                	productsWithImages.add(productWithImage);
                }
            }
			if (productsWithImages.size() == 0) 
                return ResponseEntity.noContent().build();
            return ResponseEntity.ok().body(productsWithImages);
		
		}
		
        if(products.size()==0)
        	return ResponseEntity.noContent().build();
        return ResponseEntity.ok().body(products);
	}

	@GetMapping("/best-selling")
    public ResponseEntity<?> getProductsBestSelling() {
        List<Product> products = productRepository.findAllByQuantitySoldByDesc();
        if (products.size() == 0) {
            return ResponseEntity.noContent().build();
        }
        List<ProductImage> productImages = new LinkedList<>();
        for (int i = 0; i < products.size(); i++) {
            ProductImage productImage = new ProductImage();
            productImage.setImageId(products.get(i).getProductImages().get(0).getImageId());
            productImage.setImageLink(products.get(i).getProductImages().get(0).getImageLink());
            productImage.setCreateAt(products.get(i).getProductImages().get(0).getCreateAt());
            productImage.setUpdateAt(products.get(i).getProductImages().get(0).getUpdateAt());
            productImage.setProduct(products.get(i));
            productImages.add(productImage);
        }
        return ResponseEntity.ok().body(productImages);
    }    
	
	@GetMapping("/{imageId}")
    public ResponseEntity<?> getImageById(@PathVariable("imageId") Long imageId) {
        Optional<ProductImage> optionalProductImage = productImageRepository.findById(imageId);
        if (!optionalProductImage.isPresent()) {
            throw new NotFoundException("Product Image not found");
        }
        return ResponseEntity.ok().body(optionalProductImage.get());
    }
	
    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    @PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
    public ResponseEntity<?> createNewImages(@RequestParam("productId") Long productId,
                                             @RequestParam("files") MultipartFile[] files) throws IOException {
        
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (!optionalProduct.isPresent()) {
        	throw new NotFoundException("Not found product with id " + productId +"!");
        }
        Product product = optionalProduct.get();
        List<ProductImage> productImages = cloudinaryService.uploadProductImage(files, product);
       
        return ResponseEntity.status(201).body(productImages);
    }
	

    @DeleteMapping("/{imageId}")
    @Transactional(rollbackFor = Exception.class)
    @PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
    public ResponseEntity<?> deleteProductImageById(@PathVariable("imageId") Long imageId) throws IOException {
        Optional<ProductImage> optionalProductImage = productImageRepository.findById(imageId);
        if (!optionalProductImage.isPresent()) {
            throw new NotFoundException("Product Image not found");
        }
        ProductImage productImage = optionalProductImage.get();

        cloudinaryService.deleteFile(productImage.getPublicId());
        productImageRepository.deleteById(imageId);

        return ResponseEntity.status(200).body(optionalProductImage.get());
    }

    @DeleteMapping("/product/{productId}")
    @PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
    public ResponseEntity<?> deleteProductImagesByProductId(@PathVariable("productId") Long productId) throws IOException {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (!optionalProduct.isPresent()) {
            throw new NotFoundException("Product not found by product id " + productId);
        }
        List<ProductImage> productImages = cloudinaryService.deleteProductImageByProduct(optionalProduct.get());
        
        
        return ResponseEntity.status(200).body(productImages);
    }
}
    

