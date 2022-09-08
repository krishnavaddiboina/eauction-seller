package com.iiht.seller.service;

import java.util.List;
import java.util.Map;

import com.iiht.seller.exception.BiddingException;

import com.iiht.seller.exception.InvalidInputException;
import com.iiht.seller.exception.MongoDBException;
import com.iiht.seller.model.Product;
import com.iiht.seller.model.ProductBids;
import com.iiht.seller.model.ProductResponse;

public interface SellerService {

	String addProduct(Product product, ProductResponse productResponse) throws InvalidInputException, MongoDBException;

	void deleteProduct(String productId, ProductResponse productResponse) throws  BiddingException, InvalidInputException, MongoDBException;

	ProductBids showProductBids(String productId) throws MongoDBException, InvalidInputException;

	List<Product> getAllProducts() throws MongoDBException;

}
