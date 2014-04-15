package com.sales.recorder.model;

public class Customer {
	
	private long customerId;
	private String customerName;
	private String contactNo;
	private String route;
	private double areares;
	
	public Customer() {}

	public Customer(long customerId, String customerName, String contactNo,
			String route, double areares) 
	{
		this.customerId = customerId;
		this.customerName = customerName;
		this.contactNo = contactNo;
		this.route = route;
		this.areares = areares;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public double getAreares() {
		return areares;
	}

	public void setAreares(double areares) {
		this.areares = areares;
	}
	
	@Override
	public String toString() {
		return customerName;
	}

}
