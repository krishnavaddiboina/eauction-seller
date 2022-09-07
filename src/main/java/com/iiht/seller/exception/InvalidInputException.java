package com.iiht.seller.exception;

import org.springframework.http.HttpStatus;

import com.iiht.seller.model.ProductResponse;
import com.iiht.seller.util.AppConstants;

public class InvalidInputException extends Exception {
	
	private static final long serialVersionUID = 384481467459775775L;
	private String message;
	private ProductResponse productResponse;
	
	public InvalidInputException(String message, ProductResponse response) {
		super(message);
		this.message = message;
		response.setStatus(String.valueOf(HttpStatus.BAD_REQUEST.value()));
		response.setMessage(message);
		this.productResponse = response;
	}

	public String getMessage() {
		return message;
	}

	public ProductResponse getProductResponse() {
		return productResponse;
	}
	
	
}
