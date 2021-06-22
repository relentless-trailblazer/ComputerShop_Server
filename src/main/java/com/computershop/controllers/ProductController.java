package com.computershop.controllers;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.computershop.dao.Product;
import com.computershop.dao.ProductImage;
import com.computershop.helpers.ConvertObject;
import com.computershop.repositories.ProductRepository;

@RestController
@RequestMapping("/api/products")
@Transactional(rollbackFor = Exception.class)
public class ProductController {

    @Autowired
    private ProductRepository productRepository;


    @GetMapping
    public ResponseEntity<?> getAllProducts(@RequestParam(name = "page", required = false) Integer pageNum,
                                            @RequestParam(name = "type", required = false) String type,
                                            @RequestParam(name = "search", required = false) String search) {
        if (search != null) {
        	String searchConvert = ConvertObject.fromSlugToString(search);
            List<Product> products = productRepository.findByNameContainingIgnoreCase(searchConvert);

            if (products.size() == 0) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(products);
        }
        if (type != null) {
            if (type.compareTo("without-image") == 0) {
                List<Product> products = productRepository.findAll();

                if (products.size() == 0) {
                    return ResponseEntity.noContent().build();
                }
                return ResponseEntity.ok().body(products);
            }
        }
        if (pageNum != null) {
            Page<Product> page = productRepository.findAll(PageRequest.of(pageNum.intValue(), 20));
            //
            List<ProductImage> listProdcutImages = new LinkedList<ProductImage>();

            List<Product> listProducts = page.getContent();
            for (int i = 0; i < listProducts.size(); i++) {
                if (listProducts.get(i).getProductImages().isEmpty()) {
                    continue;
                }
                listProdcutImages.add(listProducts.get(i).getProductImages().get(0));
            }
            
            if (page.getNumberOfElements() == 0) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.ok().body(listProdcutImages);
        }

        List<Product> products = productRepository.findAll();
        
        List<ProductImage> listProductImages = new LinkedList<ProductImage>();

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getProductImages().isEmpty()) {
                continue;
            }
            listProductImages.add(products.get(i).getProductImages().get(0));
        }
        

        if (products.size() == 0) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(listProductImages);
    }

}