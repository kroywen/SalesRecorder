package com.sales.recorder.screen;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.sales.recorder.R;
import com.sales.recorder.adapter.SalesmanWiseSaleAdapter;
import com.sales.recorder.dialog.DatePickerFragment;
import com.sales.recorder.model.Sale;
import com.sales.recorder.model.Salesman;
import com.sales.recorder.storage.database.DatabaseHelper;

public class SalesmanWiseSaleScreen extends BaseScreen 
	implements OnDateSetListener 
{
	
	private Spinner salesmanView;
	private EditText dateView;
	private ListView listView;
	private View listHeaderView;
	private TextView grandTotal;
	
	private List<Salesman> salesmans;
	private Salesman salesman;
	private Date date;
	private List<Sale> sales;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.salesman_wise_sale_screen);
		setTitle(R.string.salesman_wise_sale);
		
		salesmans = dbManager.getSalesmans();
		salesman = salesmans.get(0);
		date = new Date();
		selectSales();
		
		initializeViews();
	}
	
	private void selectSales() {
		String selection = DatabaseHelper.KEY_SALESMAN_ID + "=" + 
			salesman.getSalesmanId() + " and " + DatabaseHelper.KEY_SALE_INVOICE_DATE + 
			"='" + dateFormatter.format(date) + "'";
		sales = dbManager.getSales(selection);
	}
	
	private void initializeViews() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		listHeaderView = inflater.inflate(R.layout.salesman_wise_sale_list_header, null);
		
		salesmanView = (Spinner) findViewById(R.id.salesmanView);
		salesmanView.setAdapter(new ArrayAdapter<Salesman>(this,
			android.R.layout.simple_spinner_item, salesmans));
		salesmanView.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				salesman = salesmans.get(position);
				selectSales();
				updateListView();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		
		dateView = (EditText) findViewById(R.id.dateView);
		dateView.setText(dateFormatter.format(date));
		dateView.setOnClickListener(this);
		
		grandTotal = (TextView) findViewById(R.id.grandTotal);
		
		listView = (ListView) findViewById(R.id.listView);
		updateListView();
	}
	
	private void updateListView() {
		listView.setAdapter(null);
		if (sales == null || sales.isEmpty()) {
			if (listView.getHeaderViewsCount() == 1) {
				listView.removeHeaderView(listHeaderView);
			}
		} else {
			if (listView.getHeaderViewsCount() == 0) {
				listView.addHeaderView(listHeaderView);
			}
		}
		
		SalesmanWiseSaleAdapter adapter = new SalesmanWiseSaleAdapter(
			this, sales, salesman.getRoute());
		listView.setAdapter(adapter);
		
		double grandTotalAmount = calculateGrandTotalAmount();
		grandTotal.setText(getString(R.string.grand_total, grandTotalAmount));
	}
	
	private double calculateGrandTotalAmount() {
		if (sales == null || sales.isEmpty()) {
			return 0;
		}
		double total = 0;
		for (Sale sale : sales) {
			total += sale.getPrice();
		}
		return total;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dateView:
			showSelectDateDialog();
			break;
		}
	}
	
	private void showSelectDateDialog() {
		DialogFragment newFragment = new DatePickerFragment();
		Bundle args = new Bundle();
		args.putLong("timeMillis", date.getTime());
		newFragment.setArguments(args);
	    newFragment.show(getFragmentManager(), "datePicker");
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, monthOfYear);
		c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		date = c.getTime();
		dateView.setText(dateFormatter.format(date));
		selectSales();
		updateListView();
	}

}
