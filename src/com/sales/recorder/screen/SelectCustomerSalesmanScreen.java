package com.sales.recorder.screen;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.sales.recorder.R;
import com.sales.recorder.model.Customer;
import com.sales.recorder.model.Salesman;

public class SelectCustomerSalesmanScreen extends BaseScreen {
	
	private Spinner customerView;
	private Spinner salesmanView;
	private Button nextBtn;
	private Button cancelBtn;
	
	private List<Customer> customers;
	private List<Salesman> salesmans;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_customer_salesman_screen);
		
		customers = dbManager.getCustomers();
		salesmans = dbManager.getSalesmans();
		
		initializeViews();
	}
	
	private void initializeViews() {
		customerView = (Spinner) findViewById(R.id.customerView);
		customerView.setAdapter(new ArrayAdapter<Customer>(this, 
			android.R.layout.simple_spinner_dropdown_item, customers));
		
		salesmanView = (Spinner) findViewById(R.id.salesmanView);
		salesmanView.setAdapter(new ArrayAdapter<Salesman>(this,
			android.R.layout.simple_spinner_dropdown_item, salesmans));
		
		nextBtn = (Button) findViewById(R.id.nextBtn);
		nextBtn.setOnClickListener(this);
		
		cancelBtn = (Button) findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.nextBtn:
			startAddEntryScreen();
			break;
		case R.id.cancelBtn:
			finish();
			break;
		}
	}
	
	private void startAddEntryScreen() {
		Intent intent = new Intent(this, AddSalesScreen.class);
		
		Customer customer = customers.get(
			customerView.getSelectedItemPosition());
		intent.putExtra(EXTRA_CUSTOMER_ID, customer.getCustomerId());
		
		Salesman salesman = salesmans.get(
			salesmanView.getSelectedItemPosition());
		intent.putExtra(EXTRA_SALESMAN_ID, salesman.getSalesmanId());
		
		String key = customer.getCustomerName() + salesman.getSalesmanName();
		tempSales.setKey(key);
		
		startActivity(intent);
	}

}
