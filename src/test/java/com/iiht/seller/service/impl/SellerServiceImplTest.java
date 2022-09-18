package com.iiht.seller.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;

import com.iiht.seller.exception.BiddingException;
import com.iiht.seller.exception.InvalidInputException;
import com.iiht.seller.exception.MongoDBException;
import com.iiht.seller.model.Product;
import com.iiht.seller.model.ProductResponse;
import com.iiht.seller.repository.SellerRepository;
import com.iiht.seller.service.SellerService;
import com.iiht.seller.validator.SellerDataValidator;

@DataMongoTest
@ContextConfiguration(classes = { SellerService.class })
@EnableAutoConfiguration(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class })
public class SellerServiceImplTest {
	
	@Mock
	SellerRepository sellerRepository;

	@Mock
	SellerDataValidator validator;
	
	@InjectMocks
	SellerServiceImpl sellerServiceImpl;
	
	@Test
	void testAddProduct() throws InvalidInputException, MongoDBException {
		String productId = "123";
		Product product = new Product();
		product.setProductName("productName");		
		ProductResponse response = new ProductResponse();
		
		Mockito.doNothing().when(validator).validateRequestData(product, response);
		Mockito.when(sellerRepository.addProduct(Mockito.any())).thenReturn(productId);
		
		assertEquals(productId, sellerServiceImpl.addProduct(product, response));
	}
	
	@Test
	void testDeleteProduct_BidEndDateOverExceptionCase() throws MongoDBException, InvalidInputException, BiddingException {
		String productId = "123";
		
		//SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); 
		Product product = new Product();
		product.setProductName("productName");
		product.setBidEndDate(new Date());
		System.out.println("Date = "+product.getBidEndDate());
		ProductResponse response = new ProductResponse();	
		
		Mockito.when(sellerRepository.getDataById(Mockito.any())).thenReturn(product);
		
		InvalidInputException exception = assertThrows(InvalidInputException.class, () -> sellerServiceImpl.deleteProduct(productId, response));
		assertEquals("Bid end date is over. You can't delete it.", exception.getMessage());
	}
	
	@Test
	void testDeleteProduct() throws MongoDBException, InvalidInputException, BiddingException {
		String productId = "123";		
		
		Product product = new Product();
		product.setProductName("productName");
		
		Date date = new Date();	   
	    Calendar c = Calendar.getInstance();
	    c.setTime(date);
	    c.add(Calendar.DATE, 1); //adding 1day extra to the current date
	    date = c.getTime();
	    
		product.setBidEndDate(date);
		ProductResponse response = new ProductResponse();	
		
		Mockito.when(sellerRepository.getDataById(Mockito.any())).thenReturn(product);
		
		Mockito.when(sellerRepository.isBidPresentOnProduct(Mockito.any())).thenReturn(false);
		
		sellerServiceImpl.deleteProduct(productId, response);
		verify(sellerRepository,times(1)).deleteProduct(productId);
	}
	
	

}
