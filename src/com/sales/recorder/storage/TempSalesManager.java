package com.sales.recorder.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sales.recorder.model.Sale;

public class TempSalesManager {
	
	private String DEFAULT_KEY = "default_temp_sales_manager_key";
	
	private static TempSalesManager instance;
	private HashMap<String, List<Sale>> salesKeys;
	private List<Sale> sales;
	private String key;
	
	private TempSalesManager() {
		salesKeys = new HashMap<String, List<Sale>>();
		sales = new ArrayList<Sale>();
		salesKeys.put(DEFAULT_KEY, sales);
		key = DEFAULT_KEY;
	}
	
	public static TempSalesManager getInstance() {
		if (instance == null) {
			instance = new TempSalesManager(); 
		}
		return instance;
	}
	
	public List<Sale> getSales() {
		return sales;
	}
	
	public void addSale(Sale sale) {
		sales.add(sale);
	}
	
	public void removeSale(int position) {
		sales.remove(position);
	}
	
	public void removeSale(Sale sale) {
		sales.remove(sale);
	}
	
	public void clear() {
		sales.clear();
	}
	
	public boolean isEmpty() {
		return sales == null || sales.isEmpty();
	}
	
	public Sale getSale(int position) {
		return sales.get(position);
	}
	
	public void setKey(String key) {
		if (this.key.equals(key)) {
			return;
		}
		if (salesKeys.containsKey(key)) {
			sales = salesKeys.get(key);
		} else {
			sales = new ArrayList<Sale>();
			salesKeys.put(key, sales);
		}
		this.key = key;
	}

}
