package com.sales.recorder.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sales.recorder.R;
import com.sales.recorder.model.Sale;

public class SalesDetailAdapter extends BaseAdapter {
	
	private Context context;
	private List<Sale> sales;
	private boolean[] checkedItems;
	
	public SalesDetailAdapter(Context context, List<Sale> sales) {
		this.context = context;
		this.sales = sales;
		checkedItems = new boolean[sales.size()];
	}

	@Override
	public int getCount() {
		return sales.size();
	}

	@Override
	public Sale getItem(int position) {
		return sales.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater)
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.sales_detail_list_item, null); 
		}
		
		Sale sale = getItem(position);
		
		TextView productName = (TextView) convertView.findViewById(R.id.productName);
		productName.setText(sale.getProductName());
		
		TextView quantity = (TextView) convertView.findViewById(R.id.quantity);
		quantity.setText(String.valueOf(sale.getQuantity()));
		
		TextView rate = (TextView) convertView.findViewById(R.id.rate);
		rate.setText(String.valueOf(sale.getPrice() / sale.getQuantity()));
		
		TextView total = (TextView) convertView.findViewById(R.id.total);
		total.setText(String.valueOf(sale.getPrice()));
		
		CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
		checkBox.setChecked(checkedItems[position]);
		
		return convertView;
	}
	
	public void changeChecked(int position) {
		checkedItems[position] = !checkedItems[position];
		notifyDataSetChanged();
	}
	
	public boolean isChecked(int position) {
		return checkedItems[position];
	}
	
	public void checkAll() {
		for (int i=0; i<checkedItems.length; i++) {
			checkedItems[i] = true;
		}
		notifyDataSetChanged();
	}
	
	public void uncheckAll() {
		for (int i=0; i<checkedItems.length; i++) {
			checkedItems[i] = false;
		}
		notifyDataSetChanged();
	}
	
	public boolean isAllChecked() {
		boolean allChecked = true;
		for (int i=0; i<checkedItems.length; i++) {
			if (!checkedItems[i]) {
				allChecked = false;
				break;
			}
		}
		return allChecked;
	}
	
	public int getCheckedCount() {
		int count = 0;
		for (int i=0; i<checkedItems.length; i++) {
			if (checkedItems[i]) {
				count++;
			}
		}
		return count;
	}
	
	public boolean[] getCheckedItems() {
		return checkedItems;
	}
	
	public int getFirstCheckedPosition() {
		for (int i=0; i<checkedItems.length; i++) {
			if (checkedItems[i]) {
				return i;
			}
		}
		return -1;
	}

}
