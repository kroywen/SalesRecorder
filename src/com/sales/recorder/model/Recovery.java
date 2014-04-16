package com.sales.recorder.model;

public class Recovery {
	
	private long recoveryId;
	private long customerId;
	private double recoveredAmount;
	private long salesmanId;
	private String recoveryDate;
	
	public Recovery() {}

	public Recovery(long recoveryId, long customerId, double recoveredAmount,
			long salesmanId, String recoveryDate) 
	{
		this.recoveryId = recoveryId;
		this.customerId = customerId;
		this.recoveredAmount = recoveredAmount;
		this.salesmanId = salesmanId;
		this.recoveryDate = recoveryDate;
	}

	public long getRecoveryId() {
		return recoveryId;
	}

	public void setRecoveryId(long recoveryId) {
		this.recoveryId = recoveryId;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public double getRecoveredAmount() {
		return recoveredAmount;
	}

	public void setRecoveredAmount(double recoveredAmount) {
		this.recoveredAmount = recoveredAmount;
	}

	public long getSalesmanId() {
		return salesmanId;
	}

	public void setSalesmanId(long salesmanId) {
		this.salesmanId = salesmanId;
	}

	public String getRecoveryDate() {
		return recoveryDate;
	}

	public void setRecoveryDate(String recoveryDate) {
		this.recoveryDate = recoveryDate;
	}

}
