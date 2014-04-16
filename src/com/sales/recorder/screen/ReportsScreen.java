package com.sales.recorder.screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sales.recorder.R;

public class ReportsScreen extends BaseScreen {
	
	private Button customerInvoiceBtn;
	private Button routeWiseSaleBtn;
	private Button salesmanWiseSaleBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.reports);
		setContentView(R.layout.reports_screen);
		initializeViews();
	}
	
	private void initializeViews() {
		customerInvoiceBtn = (Button) findViewById(R.id.customerInvoiceBtn);
		customerInvoiceBtn.setOnClickListener(this);
		
		routeWiseSaleBtn = (Button) findViewById(R.id.routeWiseSaleBtn);
		routeWiseSaleBtn.setOnClickListener(this);
		
		salesmanWiseSaleBtn = (Button) findViewById(R.id.salesmanWiseSaleBtn);
		salesmanWiseSaleBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.customerInvoiceBtn:
			Intent intent = new Intent(this, CustomerInvoiceScreen.class);
			startActivity(intent);
			break;
		case R.id.routeWiseSaleBtn:
			// TODO
			break;
		case R.id.salesmanWiseSaleBtn:
			intent = new Intent(this, SalesmanWiseSaleScreen.class);
			startActivity(intent);
			break;
		}
	}
	
	

}
