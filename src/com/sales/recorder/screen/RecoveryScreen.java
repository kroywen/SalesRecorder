package com.sales.recorder.screen;

import java.util.Date;
import java.util.List;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.sales.recorder.R;
import com.sales.recorder.model.Customer;
import com.sales.recorder.model.Recovery;
import com.sales.recorder.model.Salesman;

public class RecoveryScreen extends BaseScreen {
	
	private EditText routeView;
	private Spinner customerView;
	private Spinner salesmanView;
	private EditText arrearsView;
	private EditText recoveredAmountView;
	private Button saveBtn;
	private Button cancelBtn;
	
	private List<Customer> customers;
	private List<Salesman> salesmans;
	
	private Customer customer;
	private Salesman salesman;
	private double arrears;
	private int recoveredAmount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recovery_screen);
		setTitle(R.string.recovery);
		
		customers = dbManager.getCustomers();
		customer = customers.get(0);
		salesmans = dbManager.getSalesmans();
		salesman = salesmans.get(0);
		
		initializeViews();
	}
	
	private void initializeViews() {
		routeView = (EditText) findViewById(R.id.routeView);
		
		customerView = (Spinner) findViewById(R.id.customerView);
		customerView.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				customer = customers.get(position);
				recoveredAmount = 0;
				notifyDataChanged();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		customerView.setAdapter(new ArrayAdapter<Customer>(this, 
			android.R.layout.simple_spinner_dropdown_item, customers));
		
		salesmanView = (Spinner) findViewById(R.id.salesmanView);
		salesmanView.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				salesman = salesmans.get(position);
				recoveredAmount = 0;
				notifyDataChanged();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		salesmanView.setAdapter(new ArrayAdapter<Salesman>(this, 
			android.R.layout.simple_spinner_dropdown_item, salesmans));
		
		arrearsView = (EditText) findViewById(R.id.arrearsView);
		
		recoveredAmountView = (EditText) findViewById(R.id.recoveredAmountView);
		recoveredAmountView.setOnClickListener(this);
		
		saveBtn = (Button) findViewById(R.id.saveBtn);
		saveBtn.setOnClickListener(this);
		
		cancelBtn = (Button) findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(this);
	}
	
	private void notifyDataChanged() {
		routeView.setText(customer.getRoute());
		updateArrears();
		updateButtons();
		recoveredAmountView.setText(String.valueOf(recoveredAmount));
	}
	
	private void updateButtons() {
		if (arrears > 0) {
			recoveredAmountView.setEnabled(true);
			saveBtn.setEnabled(recoveredAmount > 0);
		} else {
			recoveredAmountView.setEnabled(false);
			saveBtn.setEnabled(false);
		}
	}
	
	private void updateArrears() {
		double totalArrears = dbManager.getArrears(customer.getCustomerId(), salesman.getSalesmanId());
		double totalRecoveredAmount = dbManager.getRecoveredAmount(
			customer.getCustomerId(), salesman.getSalesmanId());
		arrears = totalArrears - totalRecoveredAmount;
		arrearsView.setText(String.valueOf(arrears));		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.saveBtn:
			saveRecovery();
			break;
		case R.id.cancelBtn:
			showMainScreen();
			break;
		case R.id.recoveredAmountView:
			showSelectRecoveredAmountDialog();
			break;
		}
	}
	
	private void saveRecovery() {
		Recovery recovery = new Recovery(
			0,
			customer.getCustomerId(),
			recoveredAmount,
			salesman.getSalesmanId(),
			dateFormatter.format(new Date())
		);
		dbManager.addRecovery(recovery);
		Toast.makeText(this, R.string.recovery_added, Toast.LENGTH_SHORT).show();
		showMainScreen();
	}
	
	private void showSelectRecoveredAmountDialog() {
		final Dialog dialog = new Dialog(this);
		dialog.setTitle(R.string.select_recovered_amount);
		dialog.setContentView(R.layout.select_number_dialog);
		
		final NumberPicker picker = (NumberPicker) dialog.findViewById(R.id.numberPicker);
		picker.setMinValue(0);
		picker.setMaxValue((int) arrears);
		picker.setValue(recoveredAmount);
		picker.setWrapSelectorWheel(true);
		
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
				recoveredAmount = picker.getValue();
				dialog.dismiss();
				notifyDataChanged();
			}
		});
		
		dialog.show();
	}

}
