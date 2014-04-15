package com.sales.recorder.model;

public class Product {
	
	private long productId;
	private String productName;
	private double standardPrice;
	
	public Product() {}
	
	public Product(long productId, String productName, double standardPrice) {
		this.productId = productId;
		this.productName = productName;
		this.standardPrice = standardPrice;
	}

	public long getProductId() {
		return productId;
	}
	
	public void setProductId(long productId) {
		this.productId = productId;
	}
	
	public String getProductName() {
		return productName;
	}
	
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public double getStandardPrice() {
		return standardPrice;
	}
	
	public void setStandardPrice(double standardPrice) {
		this.standardPrice = standardPrice;
	}
	
	@Override
	public String toString() {
		return productName;
	}

}
