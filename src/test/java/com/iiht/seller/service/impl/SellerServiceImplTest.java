package com.iiht.seller.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

import com.iiht.seller.exception.BiddingException;
import com.iiht.seller.exception.InvalidInputException;
import com.iiht.seller.exception.MongoDBException;
import com.iiht.seller.model.Buyer;
import com.iiht.seller.model.Product;
import com.iiht.seller.model.ProductBids;
import com.iiht.seller.model.ProductResponse;
import com.iiht.seller.repository.SellerRepository;
import com.iiht.seller.service.SellerService;
import com.iiht.seller.validator.SellerDataValidator;

@DataMongoTest
@ContextConfiguration(classes = { SellerService.class })
@EnableAutoConfiguration(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class })
class SellerServiceImplTest {
	
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
	void testAddProduct_BidEndDateShouldBeFutureException() throws InvalidInputException {		
		Product product = new Product();
		product.setProductName("productName");
		
		Date date = new Date();	   
	    Calendar c = Calendar.getInstance();
	    c.setTime(date);
	    c.add(Calendar.DATE, -1); //reducing 1day to the current date
	    date = c.getTime();	   
		product.setBidEndDate(date);
		ProductResponse response = new ProductResponse();
		
		Mockito.doNothing().when(validator).validateRequestData(product, response);
		
		InvalidInputException exception = assertThrows(InvalidInputException.class, () -> sellerServiceImpl.addProduct(product, response));
		assertEquals("Bid end date should be future date", exception.getMessage());
	}
	
	@Test
	void testDeleteProduct_BidEndDateOverExceptionCase() throws MongoDBException {
		String productId = "123";
		
		//SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); 
		Product product = new Product();
		product.setProductName("productName");
		
		Date date = new Date();	   
	    Calendar c = Calendar.getInstance();
	    c.setTime(date);
	    c.add(Calendar.DATE, -1); //reducing 1day to the current date
	    date = c.getTime();	   
		product.setBidEndDate(date);
		
		ProductResponse response = new ProductResponse();
		
		
		Mockito.when(sellerRepository.getDataById(Mockito.any())).thenReturn(product);
		
		InvalidInputException exception = assertThrows(InvalidInputException.class, () -> sellerServiceImpl.deleteProduct(productId, response));
		assertEquals("Bid end date is over. You can't delete it.", exception.getMessage());
	}
	
	@Test
	void testDeleteProduct_BidAlreadyPresentExceptionCase() throws MongoDBException {
		String productId = "123";
		
		//SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); 
		Product product = new Product();
		product.setProductName("productName");
		
		Date date = new Date();	   
	    Calendar c = Calendar.getInstance();
	    c.setTime(date);
	    c.add(Calendar.DATE, 1); //reducing 1day to the current date
	    date = c.getTime();	   
		product.setBidEndDate(date);
		
		ProductResponse response = new ProductResponse();
		
		
		Mockito.when(sellerRepository.getDataById(Mockito.any())).thenReturn(product);
		Mockito.when(sellerRepository.isBidPresentOnProduct(Mockito.any())).thenReturn(true);
		
		BiddingException exception = assertThrows(BiddingException.class, () -> sellerServiceImpl.deleteProduct(productId, response));
		assertEquals("Bid already present on this product. You can't delete it.", exception.getMessage());
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
	
	@Test
	void testShowProductBids() throws MongoDBException, InvalidInputException {
		String productId = "123";		
		List<Buyer> buyers = new ArrayList<>();
		Buyer buyer = new Buyer();
		buyer.setFirstName("buyerName");
		buyers.add(buyer);
		
		Product product = new Product();
		product.setProductName("productName");
		
		ProductBids productBids = new ProductBids();
		productBids.setProduct(product);
		productBids.setBuyers(buyers);
		
		Mockito.when(sellerRepository.getDataById(Mockito.any())).thenReturn(product);
		
		Mockito.when(sellerRepository.getBuyerDetails(Mockito.any())).thenReturn(buyers);		
		assertEquals(productBids, sellerServiceImpl.showProductBids(productId));
	}
	
	@Test
	void testGetAllProducts() throws MongoDBException, InvalidInputException, BiddingException {
			
		List<Product> products = new ArrayList<>();		
		Product product = new Product();
		product.setProductName("productName");		
		products.add(product);		
		
		Mockito.when(sellerRepository.getAllProducts()).thenReturn(products);		
		assertEquals(products, sellerServiceImpl.getAllProducts());
	}
	
	@Test
	void testGetFormattedBidEndDateReflectionUtils() {
		ReflectionTestUtils.invokeMethod(sellerServiceImpl, "getFormattedBidEndDate", new Date());
		assertTrue(true);
	}
	
	
	

}
