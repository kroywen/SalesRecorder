package com.sales.recorder.model;

public class Salesman {
	
	private long salesmanId;
	private String salesmanName;
	private String route;
	
	public Salesman() {}

	public Salesman(long salesmanId, String salesmanName, String route) {
		this.salesmanId = salesmanId;
		this.salesmanName = salesmanName;
		this.route = route;
	}

	public long getSalesmanId() {
		return salesmanId;
	}

	public void setSalesmanId(long salesmanId) {
		this.salesmanId = salesmanId;
	}

	public String getSalesmanName() {
		return salesmanName;
	}

	public void setSalesmanName(String salesmanName) {
		this.salesmanName = salesmanName;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}
	
	@Override
	public String toString() {
		return salesmanName;
	}

}
