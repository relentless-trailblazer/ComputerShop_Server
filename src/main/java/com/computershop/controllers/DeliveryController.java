package com.computershop.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.computershop.dao.Delivery;
import com.computershop.repositories.DeliveryRepository;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {
	
	@Autowired
	private DeliveryRepository deliveryRepos;
	
	@GetMapping
	public ResponseEntity<?> getAllDeliveries() {
        List<Delivery> deliveries = deliveryRepos.findAll();
        if (deliveries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(deliveries);
    }
	
}
