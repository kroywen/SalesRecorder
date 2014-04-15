package com.sales.recorder.screen;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sales.recorder.R;

public class MainScreen extends BaseScreen  {
	
	private Button salesEntryBtn;
	private Button recoveryBtn;
	private Button reportsBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_screen);
		initializeViews();
		
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(false);
		}
	}
	
	private void initializeViews() {
		salesEntryBtn = (Button) findViewById(R.id.salesEntryBtn);
		salesEntryBtn.setOnClickListener(this);
		
		recoveryBtn = (Button) findViewById(R.id.recoveryBtn);
		recoveryBtn.setOnClickListener(this);
		
		reportsBtn = (Button) findViewById(R.id.reportsBtn);
		reportsBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.salesEntryBtn:
			Intent intent = new Intent(this, SelectCustomerSalesmanScreen.class);
			startActivity(intent);
			break;
		case R.id.recoveryBtn:
			Toast.makeText(this, "Start Recovery Screen", Toast.LENGTH_SHORT).show();
			break;
		case R.id.reportsBtn:
			intent = new Intent(this, ReportsScreen.class);
			startActivity(intent);
			break;
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
