package com.iiht.seller.repository;

import java.util.List;

import com.iiht.seller.exception.MongoDBException;
import com.iiht.seller.model.Buyer;
import com.iiht.seller.model.Product;

public interface SellerRepository {
	
	String addProduct(Product product) throws MongoDBException;
	
	boolean isBidPresentOnProduct(String productId) throws MongoDBException;

	List<Buyer> getBuyerDetails(String productId) throws MongoDBException;

	Product getDataById(String productId) throws MongoDBException;

	void deleteProduct(String productId) throws MongoDBException;

	List<Product> getAllProducts() throws MongoDBException;

	
}
