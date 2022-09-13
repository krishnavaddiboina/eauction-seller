package com.iiht.seller.repository.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.iiht.seller.exception.MongoDBException;
import com.iiht.seller.model.Buyer;
import com.iiht.seller.model.Product;
import com.iiht.seller.repository.SellerRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class SellerRepositoryImpl implements SellerRepository {
	@Autowired
	MongoTemplate mongoTemplate;	
	
	@Override
	public String addProduct(Product product) throws MongoDBException {	
		log.debug("Within addProduct() of SellerRepositoryImpl class...");
		String productId = null;
		try {
			Product theProduct = mongoTemplate.save(product);
			if(theProduct != null) {
				return theProduct.getId();
			}
		}catch(Exception exception) {
			log.error("Error occured while saving the product. Error is {}", exception.getMessage());
			throw new MongoDBException("Error occured while saving the product in mongo db");
		}
		return productId;
	}
	
	@Override
	public Product getDataById(String productId) throws MongoDBException {
		log.debug("Within getDataById() of SellerRepositoryImpl class...");
		Product product = null;
		try {
			product = mongoTemplate.findById(productId, Product.class);
			log.info("Based on the given product id got the product {}", product);
		}catch(Exception exception) {
			log.error("Error occured while getting data by product id. Error is {}", exception.getMessage());
			throw new MongoDBException("Error occured while getting data by product id in mongo db");
		}
		return product;
	}

	@Override
	public boolean isBidPresentOnProduct(String productId) throws MongoDBException {
		log.debug("Within isBidPresentOnProduct() of SellerRepositoryImpl class...");
		boolean flag = false;
		try {
			Query query = new Query(Criteria.where("productId").is(productId));
			List<Buyer> buyers = mongoTemplate.find(query, Buyer.class);
			if (buyers != null && buyers.size() > 0) {
				flag = true;
				return flag;
			} 
		}catch(Exception exception) {
			log.error("Error occured while knowing the bid present on the product or not. Error is {}", exception.getMessage());
			throw new MongoDBException("Error occured while knowing the bid present on the product or not in mongo db");
		}
		return flag;
	}

	@Override
	public List<Buyer> getBuyerDetails(String productId) throws MongoDBException {
		log.debug("Within getBuyerDetails() of SellerRepositoryImpl class...");
		try {
			Query query = new Query(Criteria.where("productId").is(productId)).with(Sort.by(Sort.Direction.DESC, "bidAmount"));			
			return mongoTemplate.find(query, Buyer.class);
		}catch(Exception exception) {
			log.error("Error occured while getting buyer details. Error is {}", exception.getMessage());
			throw new MongoDBException("Error occured while getting buyer details in mongo db");
		}
	}

	@Override
	public void deleteProduct(String productId) throws MongoDBException {
		log.debug("Within deleteProduct() of SellerRepositoryImpl class...");
		try {
			Query query = new Query(Criteria.where("productId").is(new ObjectId(productId)));
			mongoTemplate.findAndRemove(query, Product.class);
			log.info("Product deleted successfully...");
		}catch(Exception exception) {
			log.error("Error occured while deleting product. Error is {}", exception.getMessage());
			throw new MongoDBException("Error occured while deleting product in mongo db");
		}
	}

	@Override
	public List<Product> getAllProducts() throws MongoDBException {
		log.debug("Within getAllProducts() of SellerRepositoryImpl class...");
		try {			
			return mongoTemplate.findAll(Product.class);
		}catch(Exception exception) {
			log.error("Error occured while getting all products. Error is {}", exception.getMessage());
			throw new MongoDBException("Error occured while getting all products in mongo db");
		}
	}	

	

}
