package com.iiht.seller.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;

import com.iiht.seller.SellerApplication;
import com.iiht.seller.exception.BiddingException;
import com.iiht.seller.exception.InvalidInputException;
import com.iiht.seller.exception.MongoDBException;
import com.iiht.seller.model.Product;
import com.iiht.seller.model.ProductBids;
import com.iiht.seller.model.ProductDTO;
import com.iiht.seller.model.ProductResponse;
import com.iiht.seller.model.Seller;
import com.iiht.seller.service.SellerService;

@DataMongoTest
@ContextConfiguration(classes = { SellerApplication.class })
@EnableAutoConfiguration(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class })
class SellerControllerTest {

	@Mock
	SellerService sellerService;

	@InjectMocks
	SellerController sellerController;

	@Test
	void testAddProduct() throws InvalidInputException, MongoDBException {
		ProductResponse productResponse = new ProductResponse();
		productResponse.setStatus("201");
		ProductDTO productDto = new ProductDTO();
		productDto.setProductName("productName");
		productDto.setShortDescription("shortDesc");
		productDto.setDetailedDescription("detailedDesc");
		productDto.setCategory("Painting");
		productDto.setStartingPrice("100");
		productDto.setBidEndDate(new Date());

		Seller seller = new Seller();
		seller.setFirstName("firstName");
		seller.setLastName("lastName");
		seller.setPhone("8374893243");
		seller.setEmail("test@email.com");
		productDto.setSeller(seller);

		Product product = new Product();
		BeanUtils.copyProperties(productDto, product);

		Mockito.when(sellerService.addProduct(Mockito.any(), Mockito.any())).thenReturn(productResponse.getStatus());
		assertEquals(productResponse.getStatus(), sellerController.addProduct(productDto).getBody().getStatus());

	}
	
	@Test
	void testDeleteProduct() throws BiddingException, InvalidInputException, MongoDBException {
		String productId = "123";
		ProductResponse productResponse = new ProductResponse();
		productResponse.setStatus("200");
		Mockito.doNothing().when(sellerService).deleteProduct(Mockito.any(), Mockito.any());
		assertEquals(productResponse.getStatus(), sellerController.deleteProduct(productId).getBody().getStatus());
	}

	@Test
	void testShowProductBids() throws MongoDBException, InvalidInputException {
		String productId = "123";
		ProductBids productBids = new ProductBids();
		Product product = new Product();
		product.setProductName("productName");
		productBids.setProduct(product);
		Mockito.when(sellerService.showProductBids(Mockito.any())).thenReturn(productBids);
		assertEquals(product.getProductName(), sellerController.showProductBids(productId).getBody().getProduct().getProductName());
	}
	
	@Test
	void testGetAllProducts() throws MongoDBException {
		List<Product> productList = new ArrayList<>();
		Product product = new Product();
		product.setProductName("productName");
		productList.add(product);
		Mockito.when(sellerService.getAllProducts()).thenReturn(productList);
		assertEquals(productList.size(), sellerController.getAllProducts().getBody().size());
	}


}
