package com.iiht.seller.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

	private String id;
	private String productName;
	private String shortDescription;
	private String detailedDescription;
	private String category;
	private String startingPrice;
	private Date bidEndDate;
	private Seller seller;

}
