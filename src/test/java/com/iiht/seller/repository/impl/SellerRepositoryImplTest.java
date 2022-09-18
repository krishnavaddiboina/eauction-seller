package com.iiht.seller.repository.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import com.iiht.seller.model.Product;
import com.iiht.seller.repository.SellerRepository;

@DataMongoTest
@ContextConfiguration(classes = { SellerRepository.class })
@EnableAutoConfiguration(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class })
public class SellerRepositoryImplTest {
	
	@Mock
	private MongoTemplate mongoTemplate;
	
	@InjectMocks
	SellerRepositoryImpl sellerRepositoryImpl;
	
	@Test
	void testAddProduct() throws MongoDBException {
		Product product = new Product();
		product.setId("123");
		product.setProductName("productName");
		
		Mockito.when(mongoTemplate.save(product)).thenReturn(product);
		assertEquals(product.getId(), sellerRepositoryImpl.addProduct(product));
		
	}

}
