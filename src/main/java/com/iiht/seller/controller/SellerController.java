package com.iiht.seller.controller;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.iiht.seller.exception.DeleteBidException;
import com.iiht.seller.exception.InvalidInputException;
import com.iiht.seller.exception.MongoDBException;
import com.iiht.seller.model.Product;
import com.iiht.seller.model.ProductBids;
import com.iiht.seller.model.ProductDTO;
import com.iiht.seller.model.ProductResponse;
import com.iiht.seller.service.SellerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class SellerController {

	@Autowired
	SellerService sellerService;

	@PostMapping("/add-product")
	public ResponseEntity<ProductResponse> addProduct(@RequestBody ProductDTO productDto)
			throws InvalidInputException, MongoDBException {
		log.info("Received product in the controller...product is {}", productDto);
		ProductResponse productResponse = new ProductResponse();

		Product product = new Product();
		BeanUtils.copyProperties(productDto, product);
		String productId = sellerService.addProduct(product, productResponse);
		log.info("After adding the product, product id is {} ", productId);
		productResponse.setResponseTime(new Date());
		if (productId != null) {
			productResponse.setMessage("Product created successfully");
			productResponse.setStatus(String.valueOf(HttpStatus.CREATED.value()));
			return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.CREATED);
		} else {
			log.info("Product id is getting null");
			productResponse.setMessage("Not able to add the product");
			productResponse.setStatus(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
			return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@DeleteMapping("/delete/{productId}")
	public ResponseEntity<ProductResponse> deleteProduct(@PathVariable String productId) throws DeleteBidException, InvalidInputException, MongoDBException {
		log.info("Got the product id {} to delete...", productId);
		ProductResponse productResponse = new ProductResponse();
		sellerService.deleteProduct(productId, productResponse);
		
		productResponse.setMessage("Product deleted successfully");
		productResponse.setStatus(String.valueOf(HttpStatus.OK.value()));
		productResponse.setResponseTime(new Date());
		return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.OK);

	}

	@GetMapping("/show-bids/{productId}")
	public ProductBids showProductBids(@PathVariable String productId) {
		ProductBids productBids = null;
		try {
			productBids = sellerService.showProductBids(productId);
		} catch (Exception e) {
			// log.error("Error while deleting product {}", e.getMessage());
			e.getMessage();
		}
		return productBids;
	}

}
