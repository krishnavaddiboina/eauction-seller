package com.iiht.seller.service;

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

}
