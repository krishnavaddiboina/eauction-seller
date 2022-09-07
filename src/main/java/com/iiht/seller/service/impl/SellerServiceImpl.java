package com.iiht.seller.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iiht.seller.exception.BiddingException;
import com.iiht.seller.exception.InvalidInputException;
import com.iiht.seller.exception.MongoDBException;
import com.iiht.seller.model.Buyer;
import com.iiht.seller.model.Product;
import com.iiht.seller.model.ProductBids;
import com.iiht.seller.model.ProductResponse;
import com.iiht.seller.repository.SellerRepository;
import com.iiht.seller.service.SellerService;
import com.iiht.seller.util.AppConstants;
import com.iiht.seller.validator.BuyerDataValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SellerServiceImpl implements SellerService {

	@Autowired
	SellerRepository sellerRepository;

	@Autowired
	BuyerDataValidator validator;

	@Override
	public String addProduct(Product product, ProductResponse response)
			throws InvalidInputException, MongoDBException {
		
		log.debug("Within addProduct() of SellerService class...");
		validator.validateRequestData(product, response);
		log.info("request data validation successfull...");

		if (product != null && product.getBidEndDate() != null) {
			LocalDate bidEnddate = getFormattedBidEndDate(product.getBidEndDate());
			LocalDate currentDate = LocalDate.now(ZoneId.systemDefault());

			if (!(bidEnddate.isAfter(currentDate))) {
				log.error("Bid end date should be future date");
				throw new InvalidInputException("Bid end date should be future date", response);
			}
		}
		log.info("Going to add the product....");
		 return sellerRepository.addProduct(product);
	}

	@Override
	public void deleteProduct(String productId, ProductResponse response) throws InvalidInputException, BiddingException, MongoDBException {
		log.debug("Within deleteProduct() of SellerServiceImpl class...");
		Product product = sellerRepository.getDataById(productId);

		if (product != null && product.getBidEndDate() != null) {
			LocalDate bidEnddate = getFormattedBidEndDate(product.getBidEndDate());

			LocalDate currentDate = LocalDate.now(ZoneId.systemDefault());

			if (currentDate.isAfter(bidEnddate)) {
				log.error("Bid end date is over. You can't delete it.");
				throw new InvalidInputException("Bid end date is over. You can't delete it.", response);
			}
		}

		boolean flag = sellerRepository.isBidPresentOnProduct(productId);
		if (flag == true) {
			log.error("Bid already present on this product. You can't delete it.");
			throw new BiddingException("Bid already present on this product. You can't delete it.", response);
		}
		
		log.info("Going to delete the product based on product id {}", productId);
		sellerRepository.deleteProduct(productId);
	}

	@Override
	public ProductBids showProductBids(String productId) throws MongoDBException {

		Product product = sellerRepository.getDataById(productId);
		List<Buyer> buyers = sellerRepository.getBuyerDetails(productId);
		ProductBids productBids = new ProductBids();
		productBids.setProduct(product);
		productBids.setBuyers(buyers);
		return productBids;
	}

	public LocalDate getFormattedBidEndDate(Date bidEndDate) {
		String theBidEndDate = bidEndDate.toInstant().atOffset(ZoneOffset.UTC)
				.format(DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT));

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT);
		return LocalDate.parse(theBidEndDate, dtf);
	}

}
