package com.sales.recorder.screen;

import java.util.Iterator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sales.recorder.R;
import com.sales.recorder.adapter.SalesDetailAdapter;
import com.sales.recorder.model.Sale;

public class SaleDetailScreen extends BaseScreen implements OnItemClickListener,
	OnCheckedChangeListener 
{
	
	private ListView salesList;
	private TextView grandTotal;
	private Button saveBtn;
	private Button updateBtn;
	private Button deleteBtn;
	private Button cancelBtn;
	private View listHeaderView;
	private CheckBox checkAll;
	
	private SalesDetailAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sale_detail_screen);
		setTitle(R.string.sales_detail);
		initializeViews();
	}
	
	private void initializeViews() {
		listHeaderView = findViewById(R.id.listHeaderView);
		
		checkAll = (CheckBox) listHeaderView.findViewById(R.id.checkAll);
		checkAll.setOnCheckedChangeListener(this);
		
		salesList = (ListView) findViewById(R.id.salesList);
		salesList.setOnItemClickListener(this);
		updateSalesList();
		
		grandTotal = (TextView) findViewById(R.id.grandTotal);
		updateGrandTotal();
		
		saveBtn = (Button) findViewById(R.id.saveBtn);
		saveBtn.setOnClickListener(this);
		
		updateBtn = (Button) findViewById(R.id.updateBtn);
		updateBtn.setOnClickListener(this);
		
		deleteBtn = (Button) findViewById(R.id.deleteBtn);
		deleteBtn.setOnClickListener(this);
		
		cancelBtn = (Button) findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(this);
		
		updateButtons();
	}
	
	private void updateSalesList() {
		listHeaderView.setVisibility(tempSales.isEmpty() ? View.GONE : View.VISIBLE);
		adapter = new SalesDetailAdapter(this, tempSales.getSales());
		salesList.setAdapter(adapter);
	}
	
	private void updateGrandTotal() {
		double grandTotalPrice = computeGrandTotalPrice();
		grandTotal.setText(getString(R.string.grand_total, grandTotalPrice));
	}
	
	private double computeGrandTotalPrice() {
		if (tempSales.isEmpty()) {
			return 0;
		}
		double total = 0;
		for (Sale sale : tempSales.getSales()) {
			total += sale.getPrice();
		}
		return total;
	}
	
	private void updateButtons() {
		saveBtn.setEnabled(!tempSales.isEmpty());
		cancelBtn.setEnabled(!tempSales.isEmpty());
		int checkedCount = adapter.getCheckedCount();
		updateBtn.setEnabled(checkedCount == 1);
		deleteBtn.setEnabled(checkedCount > 0);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.saveBtn:
			saveTempSales();
			break;
		case R.id.updateBtn:
			updateTempSale();
			break;
		case R.id.deleteBtn:
			deleteTempSales();
			break;
		case R.id.cancelBtn:
			clearTempSales();
			break;
		}
	}
	
	private void notifyDataSetChanged() {
		updateSalesList();
		updateGrandTotal();
		updateButtons();
	}
	
	private void clearTempSales() {
		tempSales.clear();
		notifyDataSetChanged();
	}
	
	private void saveTempSales() {
		dbManager.addSales(tempSales.getSales());
		Toast.makeText(this, R.string.sales_saved, Toast.LENGTH_SHORT).show();
		tempSales.clear();
		showMainScreen();
	}
	
	private void updateTempSale() {
		Intent intent = new Intent(this, UpdateSaleScreen.class);
		intent.putExtra(EXTRA_CHECKED, adapter.getFirstCheckedPosition());
		startActivityForResult(intent, UPDATE_SALE_REQUEST_CODE);
	}
	
	private void deleteTempSales() {
		boolean[] checked = adapter.getCheckedItems();
		int i=0;
		Iterator<Sale> iterator = tempSales.getSales().iterator();
		while (iterator.hasNext()) {
			iterator.next();
			if (checked[i]) {
				iterator.remove();
			}
			i++;
		}
		notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		adapter.changeChecked(position);
		int checkedCount = adapter.getCheckedCount();
		if (checkedCount == adapter.getCount()) {
			checkAll.setChecked(true);
		} else if (checkedCount == 0) {
			checkAll.setChecked(false);
		}
		updateButtons();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == UPDATE_SALE_REQUEST_CODE) {
			checkAll.setOnCheckedChangeListener(null);
			checkAll.setChecked(false);
			checkAll.setOnCheckedChangeListener(this);
			notifyDataSetChanged();
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			adapter.checkAll();
		} else {
			adapter.uncheckAll();
		}
		updateButtons();
	}

}
