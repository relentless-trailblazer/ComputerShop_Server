package com.computershop.controllers;

import java.util.ArrayList;
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
import com.computershop.dto.ProductWithImage;
import com.computershop.exceptions.DuplicateException;
import com.computershop.exceptions.InvalidException;
import com.computershop.exceptions.NotFoundException;
import com.computershop.helpers.ConvertObject;
import com.computershop.repositories.CategoryRepository;


@RestController
@RequestMapping(value = "/api/categories")
public class CategoryController {

	@Autowired
	private CategoryRepository categoryRepository;


	@GetMapping
	public ResponseEntity<?> GetAllCategories(@RequestParam(name = "page", required = false) Integer pageNum,
											  @RequestParam(name = "search", required = false) String search) {
		List<Category> categories = new ArrayList<>();
		
		if(pageNum!=null && search!=null) {
			Page <Category> page = categoryRepository.findAll(PageRequest.of(pageNum.intValue(), 10));
			if (page.getNumberOfElements() == 0) {
                return ResponseEntity.noContent().build();
            }
			List<Category> listAll = page.getContent();
			for(Category category : listAll) {
				if(category.getName().toLowerCase().contains(ConvertObject.fromSlugToString(search))) {
					categories.add(category);
				}
			}
			if(categories.size()==0)
				return ResponseEntity.noContent().build();
			return ResponseEntity.ok().body(categories);
		}
		
		if(search!=null) {
			categories = categoryRepository.findByNameContainingIgnoreCase(ConvertObject.fromSlugToString(search));
			if(categories.size()==0)
				return ResponseEntity.noContent().build();
			return ResponseEntity.ok().body(categories);
		}
		if(pageNum!=null) {
			Page <Category> page = categoryRepository.findAll(PageRequest.of(pageNum.intValue(), 10));
			if (page.getNumberOfElements() == 0) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(page.getContent());
			
		}
		categories = categoryRepository.findAll();
		if (categories.size() == 0) {
            return ResponseEntity.noContent().build();
        }		
		return ResponseEntity.ok().body(categories);
	}
	
	
	/// ok
	@GetMapping("/{category}/products")
    public ResponseEntity<?> getProductsByCategory(@PathVariable("category") String name) {
       
		String categoryName = ConvertObject.fromSlugToString(name);
        List<Category> categories = categoryRepository.findByNameContainingIgnoreCase(categoryName);
        if (categories.size() == 0) {
            throw new NotFoundException("Not found category by : " + categoryName);
        }
        List<Product> products = new LinkedList<Product>();
        List<ProductImage> productImages = new LinkedList<ProductImage>();
        List<ProductWithImage> productsWithImages = new LinkedList<ProductWithImage>();
        for(Category category : categories) {
        	for(Product product : category.getProducts()) {
        		products.add(product);
        		productImages.add(product.getProductImages().get(0));
        	}
        }
        for(int i = 0; i < products.size(); i++) {
        	List<ProductImage> e = new ArrayList<ProductImage>();
        	e.add(0, productImages.get(i));
        	productsWithImages.add(new ProductWithImage(products.get(i), e));
        }
        
        
        
       
        return ResponseEntity.ok().body(productsWithImages);
    }

    @PostMapping
    @PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
    public ResponseEntity<?> createNewCategory(@RequestBody String name) {
        List<Category> oldCategories = categoryRepository.findByNameContainingIgnoreCase(name);
        if (oldCategories.size() != 0) {
            throw new DuplicateException("Category has already exists");
        }
        Category category = new Category();
        category.setName(name);
        Category newCategory = categoryRepository.save(category);

        return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
    }
    
    @PatchMapping("/{categoryId}")
    @PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
    public ResponseEntity<?> editCategory(@RequestBody String name,
                                      @PathVariable("categoryId") Long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (!optionalCategory.isPresent()) {
            throw new NotFoundException("Category not found");
        }
        Category category = optionalCategory.get();
        if (name != null) {
            category.setName(name);
            
        };
        categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.OK).body(category);
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
    public ResponseEntity<?> deleteCategory(@PathVariable("categoryId") Long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (!optionalCategory.isPresent()) {
            throw new NotFoundException("Category not found");
        }

        if (!optionalCategory.get().getProducts().isEmpty()) {
            throw new InvalidException("Delete failed");
        }

        categoryRepository.deleteById(categoryId);

        return ResponseEntity.status(HttpStatus.OK).body(optionalCategory.get());
    }
	
}
