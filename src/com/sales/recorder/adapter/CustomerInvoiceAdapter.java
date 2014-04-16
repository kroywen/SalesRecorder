package com.sales.recorder.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sales.recorder.R;
import com.sales.recorder.model.Sale;

public class CustomerInvoiceAdapter extends BaseAdapter {
	
	private Context context;
	private List<Sale> sales;
	private String route;
	
	public CustomerInvoiceAdapter(Context context, List<Sale> sales, String route) {
		this.context = context;
		this.sales = sales;
		this.route = route;
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
			convertView = inflater.inflate(R.layout.customer_invoice_list_item, null);
		}
		
		Sale sale = getItem(position);
		
		TextView salesmanView = (TextView) convertView.findViewById(R.id.salesmanView);
		salesmanView.setText(sale.getSalesmanName());
		
		TextView productView = (TextView) convertView.findViewById(R.id.productView);
		productView.setText(sale.getProductName());
		
		TextView rateView = (TextView) convertView.findViewById(R.id.rateView);
		rateView.setText(String.valueOf(sale.getPrice() / sale.getQuantity()));
		
		TextView quantityView = (TextView) convertView.findViewById(R.id.quantityView);
		quantityView.setText(String.valueOf(sale.getQuantity()));
		
		TextView routeView = (TextView) convertView.findViewById(R.id.routeView);
		routeView.setText(route);
		
		TextView totalView = (TextView) convertView.findViewById(R.id.totalView);
		totalView.setText(String.valueOf(sale.getPrice()));
		
		return convertView;
	}

}
