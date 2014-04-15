package com.sales.recorder.storage.database;

import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sales.recorder.model.Customer;
import com.sales.recorder.model.Product;
import com.sales.recorder.model.Sale;
import com.sales.recorder.model.Salesman;

public class DatabaseManager {
	
	private SQLiteDatabase db;
	private Context context;
	private NumberFormat nf;
	
	private DatabaseManager(Context context) {
		this.context = context;
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
		nf = NumberFormat.getInstance();
	}
	
	public static DatabaseManager newInstance(Context context) {
		return new DatabaseManager(context);
	}
	
	public void importData() {
		try {
			AssetManager assets = context.getAssets();
			InputStream is = assets.open("Data.xls");
			Workbook wb = Workbook.getWorkbook(is);
			
			insertProducts(wb);
			insertCustomers(wb);
			insertCustomerProductPrices(wb);
			insertSalesmans(wb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void insertProducts(Workbook wb) {
		Sheet sheet = wb.getSheet(0);
		int rows = sheet.getRows();
		for (int i=2; i<rows; i++) {
			try {
				Cell[] row = sheet.getRow(i);
				ContentValues values = new ContentValues();
				values.put(DatabaseHelper.KEY_PRODUCT_ID, 
					Long.parseLong(row[0].getContents()));
				values.put(DatabaseHelper.KEY_PRODUCT_NAME, row[1].getContents());
				values.put(DatabaseHelper.KEY_STANDARD_PRICE, 
					nf.parse(row[2].getContents()).doubleValue());
				db.insert(DatabaseHelper.TABLE_PRODUCT, null, values);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void insertCustomers(Workbook wb) {
		Sheet sheet = wb.getSheet(1);
		int rows = sheet.getRows();
		for (int i=3; i<rows; i++) {
			try {
				Cell[] row = sheet.getRow(i);
				ContentValues values = new ContentValues();
				values.put(DatabaseHelper.KEY_CUSTOMER_ID, 
					Long.parseLong(row[0].getContents()));
				values.put(DatabaseHelper.KEY_CUSTOMER_NAME, row[1].getContents());
				values.put(DatabaseHelper.KEY_CONTACT_NO, row[2].getContents());
				values.put(DatabaseHelper.KEY_ROUTE, row[3].getContents());
				values.put(DatabaseHelper.KEY_AREARES, 
					nf.parse(row[4].getContents()).doubleValue());
				db.insert(DatabaseHelper.TABLE_CUSTOMER, null, values);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void insertCustomerProductPrices(Workbook wb) {
		Sheet sheet = wb.getSheet(2);
		int rows = sheet.getRows();
		for (int i=2; i<rows; i++) {
			try {
				Cell[] row = sheet.getRow(i);
				ContentValues values = new ContentValues();
				values.put(DatabaseHelper.KEY_CUSTOMER_PRICE_ID, 
					Long.parseLong(row[0].getContents()));
				values.put(DatabaseHelper.KEY_CUSTOMER_ID, 
					Long.parseLong(row[1].getContents()));
				values.put(DatabaseHelper.KEY_PRODUCT_ID, 
					Long.parseLong(row[2].getContents()));
				values.put(DatabaseHelper.KEY_CUSTOMER_PRICE, 
					nf.parse(row[3].getContents()).doubleValue());
				db.insert(DatabaseHelper.TABLE_CUSTOMER_PRODUCT_PRICES, null, values);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void insertSalesmans(Workbook wb) {
		Sheet sheet = wb.getSheet(3);
		int rows = sheet.getRows();
		for (int i=2; i<rows; i++) {
			try {
				Cell[] row = sheet.getRow(i);
				ContentValues values = new ContentValues();
				values.put(DatabaseHelper.KEY_SALESMAN_ID, 
					Long.parseLong(row[0].getContents()));
				values.put(DatabaseHelper.KEY_SALESMAN_NAME, row[1].getContents());
				values.put(DatabaseHelper.KEY_ROUTE, row[2].getContents());
				db.insert(DatabaseHelper.TABLE_SALESMAN, null, values);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<Customer> getCustomers() {
		List<Customer> customers = new ArrayList<Customer>();
		try {
			Cursor c = db.query(DatabaseHelper.TABLE_CUSTOMER, 
				null, null, null, null, null, null);
			if (c != null && c.moveToFirst()) {
				do {
					Customer customer = new Customer(
						c.getLong(c.getColumnIndex(DatabaseHelper.KEY_CUSTOMER_ID)),
						c.getString(c.getColumnIndex(DatabaseHelper.KEY_CUSTOMER_NAME)),
						c.getString(c.getColumnIndex(DatabaseHelper.KEY_CONTACT_NO)),
						c.getString(c.getColumnIndex(DatabaseHelper.KEY_ROUTE)),
						c.getDouble(c.getColumnIndex(DatabaseHelper.KEY_AREARES))
					);
					customers.add(customer);
				} while (c.moveToNext());
			}
			if (c != null && !c.isClosed()) {
				c.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customers;
	}
	
	public Customer getCustomerById(long customerId) {
		Customer customer = null;
		try {
			String selection = DatabaseHelper.KEY_CUSTOMER_ID + "=" + customerId;
			Cursor c = db.query(DatabaseHelper.TABLE_CUSTOMER, null, 
				selection, null, null, null, null);
			if (c != null && c.moveToFirst()) {
				customer = new Customer(
					c.getLong(c.getColumnIndex(DatabaseHelper.KEY_CUSTOMER_ID)),
					c.getString(c.getColumnIndex(DatabaseHelper.KEY_CUSTOMER_NAME)),
					c.getString(c.getColumnIndex(DatabaseHelper.KEY_CONTACT_NO)),
					c.getString(c.getColumnIndex(DatabaseHelper.KEY_ROUTE)),
					c.getDouble(c.getColumnIndex(DatabaseHelper.KEY_AREARES))
				);
			}
			if (c != null && !c.isClosed()) {
				c.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customer;
	}
	
	public List<Salesman> getSalesmans() {
		List<Salesman> salesmans = new ArrayList<Salesman>();
		try {
			Cursor c = db.query(DatabaseHelper.TABLE_SALESMAN, 
				null, null, null, null, null, null);
			if (c != null && c.moveToFirst()) {
				do {
					Salesman salesman = new Salesman(
						c.getLong(c.getColumnIndex(DatabaseHelper.KEY_SALESMAN_ID)),
						c.getString(c.getColumnIndex(DatabaseHelper.KEY_SALESMAN_NAME)),
						c.getString(c.getColumnIndex(DatabaseHelper.KEY_ROUTE))
					);
					salesmans.add(salesman);
				} while (c.moveToNext());
			}
			if (c != null && !c.isClosed()) {
				c.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesmans;
	}
	
	public List<Product> getProducts() {
		List<Product> products = new ArrayList<Product>();
		try {
			Cursor c = db.query(DatabaseHelper.TABLE_PRODUCT, 
				null, null, null, null, null, null);
			if (c != null && c.moveToFirst()) {
				do {
					Product product = new Product(
						c.getLong(c.getColumnIndex(DatabaseHelper.KEY_PRODUCT_ID)),
						c.getString(c.getColumnIndex(DatabaseHelper.KEY_PRODUCT_NAME)),
						c.getDouble(c.getColumnIndex(DatabaseHelper.KEY_STANDARD_PRICE))
					);
					products.add(product);
				} while (c.moveToNext());
			}
			if (c != null && !c.isClosed()) {
				c.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return products;
	}
	
	public double getCustomerProductPrice(long customerId, long productId) {
		double price = 0;
		try {
			String selection = DatabaseHelper.KEY_CUSTOMER_ID + "=" + customerId +
				" and " + DatabaseHelper.KEY_PRODUCT_ID + "=" + productId;
			Cursor c = db.query(DatabaseHelper.TABLE_CUSTOMER_PRODUCT_PRICES, null, 
				selection, null, null, null, null);
			if (c != null && c.moveToFirst()) {
				price = c.getDouble(c.getColumnIndex(DatabaseHelper.KEY_CUSTOMER_PRICE));
			}
			if (c != null && !c.isClosed()) {
				c.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return price;
	}
	
	public void addSales(List<Sale> sales) {
		if (sales == null || sales.isEmpty()) {
			return;
		}
		for (Sale sale : sales) {
			long saleInvioceId = addSale(sale);
			if (saleInvioceId != 0) {
				sale.setSaleInvoiceId(saleInvioceId);
				addSaleDetail(sale);
			}
		}
	}
	
	public long addSale(Sale sale) {
		if (sale == null) {
			return 0;
		}
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.KEY_SALESMAN_ID, sale.getSalesmanId());
		values.put(DatabaseHelper.KEY_CUSTOMER_ID, sale.getCustomerId());
		values.put(DatabaseHelper.KEY_SALE_INVOICE_DATE, sale.getSaleInvoiceDate());
		values.put(DatabaseHelper.KEY_PAID_AMOUNT, sale.getPaidAmount());
		return db.insert(DatabaseHelper.TABLE_SALE, null, values);
	}
	
	public long addSaleDetail(Sale sale) {
		if (sale == null) {
			return 0;
		}
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.KEY_SALE_INVOICE_ID, sale.getSaleInvoiceId());
		values.put(DatabaseHelper.KEY_PRODUCT_ID, sale.getProductId());
		values.put(DatabaseHelper.KEY_QUANTITY, sale.getQuantity());
		values.put(DatabaseHelper.KEY_PRICE, sale.getPrice());
		return db.insert(DatabaseHelper.TABLE_SALE_DETAIL, null, values);
	}
	
	public void clearUserTables() {
		db.delete(DatabaseHelper.TABLE_SALE, null, null);
		db.delete(DatabaseHelper.TABLE_SALE_DETAIL, null, null);
		db.delete(DatabaseHelper.TABLE_RECOVERY, null, null);
	}

}
