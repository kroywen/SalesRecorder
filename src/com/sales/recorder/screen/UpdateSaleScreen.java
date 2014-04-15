package com.sales.recorder.screen;

import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.sales.recorder.R;
import com.sales.recorder.model.Customer;
import com.sales.recorder.model.Product;
import com.sales.recorder.model.Sale;

public class UpdateSaleScreen extends BaseScreen {
	
	private EditText routeView;
	private Spinner productView;
	private EditText quantityView;
	private EditText rateView;
	private EditText totalView;
	private EditText paidAmountView;
	private EditText balanceView;
	private Button updateBtn;
	private Button cancelBtn;
	
	private int checked;
	private Customer customer;
	private List<Product> products;
	
	private Product product;
	private double rate;
	private int quantity = 1;
	private double total;
	private int paidAmount = 0;
	private double balance;
	
	private Sale sale;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_sale_screen);
		setTitle(R.string.update_sale);
		getIntentData();
		
		sale = tempSales.getSale(checked);
		if (sale == null) {
			finish();
			return;
		}
		
		products = dbManager.getProducts();
		product = products.get(0);
		customer = dbManager.getCustomerById(sale.getCustomerId());				
		
		initializeViews();
		updateSaleData();
	}
	
	private void initializeViews() {
		routeView = (EditText) findViewById(R.id.routeView);
		routeView.setText(customer.getRoute());
		
		productView = (Spinner) findViewById(R.id.productView);
		productView.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				product = products.get(position);
				notifyDataChanged();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		productView.setAdapter(new ArrayAdapter<Product>(this, 
			android.R.layout.simple_spinner_dropdown_item, products));
		
		quantityView = (EditText) findViewById(R.id.quantityView);
		quantityView.setOnClickListener(this);
		
		rateView = (EditText) findViewById(R.id.rateView);		
		totalView = (EditText) findViewById(R.id.totalView);
		
		paidAmountView = (EditText) findViewById(R.id.paidAmountView);
		paidAmountView.setOnClickListener(this);
		
		balanceView = (EditText) findViewById(R.id.balanceView);
		
		updateBtn = (Button) findViewById(R.id.updateBtn);
		updateBtn.setOnClickListener(this);
		
		cancelBtn = (Button) findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(this);
	}
	
	private void getIntentData() {
		Intent intent = getIntent();
		if (intent != null) {
			checked = intent.getIntExtra(EXTRA_CHECKED, -1);
		}
	}
	
	private void updateSaleData() {
		quantity = sale.getQuantity();
		paidAmount = (int) sale.getPaidAmount();
		
		int productIndex = 0;
		for (int i=0; i<products.size(); i++) {
			Product p = products.get(i);
			if (sale.getProductId() == p.getProductId()) {
				productIndex = i;
				break;
			}
		}
		productView.setSelection(productIndex);
		
		notifyDataChanged();
	}
	
	private void notifyDataChanged() {
		updateQuantityView();
		updateRateView();
		updateTotalView();
		updatePaidAmountView();
		updateBalanceView();
	}
	
	private void updateQuantityView() {
		quantityView.setText(String.valueOf(quantity));
	}
	
	private void updateRateView() {
		double price = dbManager.getCustomerProductPrice(
			sale.getCustomerId(), product.getProductId());
		rate = (price != 0.0) ? price : product.getStandardPrice();
		rateView.setText(String.valueOf(rate));
	}
	
	private void updateTotalView() {
		total = quantity * rate;
		totalView.setText(String.valueOf(total));
	}
	
	private void updatePaidAmountView() {
		paidAmountView.setText(String.valueOf(paidAmount));
	}
	
	private void updateBalanceView() {
		balance = total - paidAmount;
		balanceView.setText(String.valueOf(balance));
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.quantityView:
			showSelectQuantityDialog();
			break;
		case R.id.paidAmountView:
			showSelectPaidAmountDialog();
			break;
		case R.id.updateBtn:
			editSaleEntry();
			break;
		case R.id.cancelBtn:
			finish();
			break;
		}
	}
	
	private void showSelectQuantityDialog() {
		final Dialog dialog = new Dialog(this);
		dialog.setTitle(R.string.select_quantity);
		dialog.setContentView(R.layout.select_number_dialog);
		
		final NumberPicker picker = (NumberPicker) dialog.findViewById(R.id.numberPicker);
		picker.setMinValue(1);
		picker.setMaxValue(1000);
		picker.setValue(quantity);
		picker.setWrapSelectorWheel(false);
		
		Button cancelBtn = (Button) dialog.findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		Button setBtn = (Button) dialog.findViewById(R.id.setBtn);
		setBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				quantity = picker.getValue();
				dialog.dismiss();
				notifyDataChanged();
			}
		});
		
		dialog.show();
	}
	
	private void showSelectPaidAmountDialog() {
		final Dialog dialog = new Dialog(this);
		dialog.setTitle(R.string.select_paid_amount);
		dialog.setContentView(R.layout.select_number_dialog);
		
		final NumberPicker picker = (NumberPicker) dialog.findViewById(R.id.numberPicker);
		picker.setMinValue(0);
		picker.setMaxValue((int) total);
		picker.setValue(paidAmount);
		
		Button cancelBtn = (Button) dialog.findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		Button setBtn = (Button) dialog.findViewById(R.id.setBtn);
		setBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				paidAmount = picker.getValue();
				dialog.dismiss();
				notifyDataChanged();
			}
		});
		
		dialog.show();
	}
	
	private void editSaleEntry() {
		sale.setPaidAmount(paidAmount);
		sale.setProductId(product.getProductId());
		sale.setProductName(product.getProductName());
		sale.setQuantity(quantity);
		sale.setPrice(total);
		
		setResult(RESULT_OK);
		finish();
	}

}
