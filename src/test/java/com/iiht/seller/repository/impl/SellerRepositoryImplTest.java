package com.iiht.seller.repository.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;

import com.iiht.seller.exception.MongoDBException;
import com.iiht.seller.model.Buyer;
import com.iiht.seller.model.Product;
import com.iiht.seller.repository.SellerRepository;

@DataMongoTest
@ContextConfiguration(classes = { SellerRepository.class })
@EnableAutoConfiguration(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class })
class SellerRepositoryImplTest {
	
	@Mock
	private MongoTemplate mongoTemplate;
	
	@InjectMocks
	SellerRepositoryImpl sellerRepositoryImpl;
	
	@Test
	void testAddProduct() throws MongoDBException {
		Product product = new Product();
		product.setId("123");
		product.setProductName("productName");
		
		Mockito.when(mongoTemplate.save(Mockito.any())).thenReturn(product);
		assertEquals(product.getId(), sellerRepositoryImpl.addProduct(product));
		
	}
	
	@Test
	void testGetDataById() throws MongoDBException {
		String productId = "123";
		Product product = new Product();
		product.setProductName("productName");
		
		Mockito.when(mongoTemplate.findById(Mockito.any(), Mockito.any())).thenReturn(product);
		assertEquals(product, sellerRepositoryImpl.getDataById(productId));
		
	}
	
	@Test
	void testIsBidPresentOnProduct() throws MongoDBException {
		String productId = "123";
		List<Object> buyers = new ArrayList<>();
		Buyer buyer = new Buyer();
		buyer.setFirstName("buyerName");
		buyers.add(buyer);		
		
		Mockito.when(mongoTemplate.find(Mockito.any(), Mockito.any())).thenReturn(buyers);
		assertEquals(true, sellerRepositoryImpl.isBidPresentOnProduct(productId));
		
	}
	
	@Test
	void testGetBuyerDetails() throws MongoDBException {
		String productId = "123";
		List<Object> buyers = new ArrayList<>();
		Buyer buyer = new Buyer();
		buyer.setFirstName("buyerName");
		buyers.add(buyer);		
		
		Mockito.when(mongoTemplate.find(Mockito.any(), Mockito.any())).thenReturn(buyers);
		assertEquals(buyers, sellerRepositoryImpl.getBuyerDetails(productId));
		
	}
	
	@Test
	void testDeleteProduct() throws MongoDBException {
		String productId = "6325cbe094f8637d8925f0e4";
		Product product = new Product();		
		product.setProductName("productName");	
		
		Mockito.when(mongoTemplate.findAndRemove(Mockito.any(), Mockito.any())).thenReturn(product);		
		sellerRepositoryImpl.deleteProduct(productId);		
		verify(mongoTemplate,times(1)).findAndRemove(Mockito.any(), Mockito.any());
		
	}
	
	@Test
	void testGetAllProducts() throws MongoDBException {		
		List<Object> products = new ArrayList<>();
		Product product = new Product();
		product.setProductName("productName");			
		
		Mockito.when(mongoTemplate.findAll(Mockito.any())).thenReturn(products);
		assertEquals(products, sellerRepositoryImpl.getAllProducts());
		
	}

}
