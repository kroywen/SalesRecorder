package com.sales.recorder.model;

public class Sale {
	
	private long saleInvoiceId;
	private long salesmanId;
	private long customerId;
	private String saleInvoiceDate;
	private double paidAmount;
	private long saleDetailId;
	private long productId;
	private String productName;
	private int quantity;
	private double price;
	
	public Sale() {}
	
	public Sale(long saleInvoiceId, long salesmanId, long customerId,
			String saleInvoiceDate, double paidAmount, long saleDetailId,
			long productId, String productName, int quantity, double price) 
	{
		this.saleInvoiceId = saleInvoiceId;
		this.salesmanId = salesmanId;
		this.customerId = customerId;
		this.saleInvoiceDate = saleInvoiceDate;
		this.paidAmount = paidAmount;
		this.saleDetailId = saleDetailId;
		this.productId = productId;
		this.productName = productName;
		this.quantity = quantity;
		this.price = price;
	}

	public long getSaleInvoiceId() {
		return saleInvoiceId;
	}

	public void setSaleInvoiceId(long saleInvoiceId) {
		this.saleInvoiceId = saleInvoiceId;
	}

	public long getSalesmanId() {
		return salesmanId;
	}

	public void setSalesmanId(long salesmanId) {
		this.salesmanId = salesmanId;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getSaleInvoiceDate() {
		return saleInvoiceDate;
	}

	public void setSaleInvoiceDate(String saleInvoiceDate) {
		this.saleInvoiceDate = saleInvoiceDate;
	}

	public double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public long getSaleDetailId() {
		return saleDetailId;
	}

	public void setSaleDetailId(long saleDetailId) {
		this.saleDetailId = saleDetailId;
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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

}