package com.sales.recorder.storage.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sales.recorder.model.Customer;
import com.sales.recorder.model.Product;
import com.sales.recorder.model.Recovery;
import com.sales.recorder.model.Sale;
import com.sales.recorder.model.Salesman;

public class DatabaseManager {
	
	private SQLiteDatabase db;
	private NumberFormat nf;
	
	private DatabaseManager(Context context) {
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
		nf = NumberFormat.getInstance();
	}
	
	public static DatabaseManager newInstance(Context context) {
		return new DatabaseManager(context);
	}
	
	public void importData(File file) throws FileNotFoundException {
		importData(new FileInputStream(file));
	}
	
	public void importData(InputStream is) {
		try {
			Workbook wb = Workbook.getWorkbook(is);
			
			db.beginTransaction();
			for (Sheet sheet : wb.getSheets()) {
				if (DatabaseHelper.TABLE_PRODUCT
					.equalsIgnoreCase(sheet.getName())) 
				{
					updateProducts(sheet);
				} else if (DatabaseHelper.TABLE_CUSTOMER
					.equalsIgnoreCase(sheet.getName())) 
				{
					insertCustomers(sheet);
				} else if (DatabaseHelper.TABLE_CUSTOMER_PRODUCT_PRICES
					.equalsIgnoreCase(sheet.getName()))
				{
					insertCustomerProductPrices(sheet);
				} else if (DatabaseHelper.TABLE_SALESMAN
					.equalsIgnoreCase(sheet.getName()))
				{
					insertSalesmans(sheet);
				}
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			if (is != null) {
				try { is.close(); } catch (IOException ex) {}
			}
		}
	}
	
	private void updateProducts(Sheet sheet) {
		int rows = sheet.getRows();
		if (rows <= 1) {
			return;
		}
		for (int i=1; i<rows; i++) {
			try {
				Cell[] row = sheet.getRow(i);
				long productId = Long.parseLong(row[0].getContents());
				ContentValues values = prepareProductValues(row);
				String rawQuery = "select count(*) from " + DatabaseHelper.TABLE_PRODUCT +
					" where " + DatabaseHelper.KEY_PRODUCT_ID + "=" + productId;
				Cursor c = db.rawQuery(rawQuery, null);
				if (c != null && c.moveToFirst()) {
					int count = c.getInt(0);
					if (count == 0) {
						db.insert(DatabaseHelper.TABLE_PRODUCT, null, values);
					} else {
						String whereClause = DatabaseHelper.KEY_PRODUCT_ID + "=" + productId;
						db.update(DatabaseHelper.TABLE_PRODUCT, values, whereClause, null);
					}	
				}
				if (c != null && !c.isClosed()) {
					c.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private ContentValues prepareProductValues(Cell[] row) throws ParseException 
	{
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.KEY_PRODUCT_ID, 
			Long.parseLong(row[0].getContents()));
		values.put(DatabaseHelper.KEY_PRODUCT_NAME, row[1].getContents());
		values.put(DatabaseHelper.KEY_STANDARD_PRICE, 
			nf.parse(row[2].getContents()).doubleValue());
		return values;
	}
	
	private void insertCustomers(Sheet sheet) {
		int rows = sheet.getRows();
		if (rows <= 1) {
			return;
		}
		for (int i=1; i<rows; i++) {
			try {
				Cell[] row = sheet.getRow(i);
				long customerId = Long.parseLong(row[0].getContents());
				ContentValues values = prepareCustomerValues(row);
				String rawQuery = "select count(*) from " + DatabaseHelper.TABLE_CUSTOMER +
					" where " + DatabaseHelper.KEY_CUSTOMER_ID + "=" + customerId;
				Cursor c = db.rawQuery(rawQuery, null);
				if (c != null && c.moveToFirst()) {
					int count = c.getInt(0);
					if (count == 0) {
						db.insert(DatabaseHelper.TABLE_CUSTOMER, null, values);
					} else {
						String whereClause = DatabaseHelper.KEY_CUSTOMER_ID + "=" + customerId;
						db.update(DatabaseHelper.TABLE_CUSTOMER, values, whereClause, null);
					}	
				}
				if (c != null && !c.isClosed()) {
					c.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private ContentValues prepareCustomerValues(Cell[] row) throws ParseException
	{
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.KEY_CUSTOMER_ID, 
			Long.parseLong(row[0].getContents()));
		values.put(DatabaseHelper.KEY_CUSTOMER_NAME, row[1].getContents());
		values.put(DatabaseHelper.KEY_CONTACT_NO, row[2].getContents());
		values.put(DatabaseHelper.KEY_ROUTE, row[3].getContents());
		values.put(DatabaseHelper.KEY_AREARES, 
			nf.parse(row[4].getContents()).doubleValue());
		return values;
	}
	
	private void insertCustomerProductPrices(Sheet sheet) {
		int rows = sheet.getRows();
		if (rows <= 1) {
			return;
		}
		for (int i=1; i<rows; i++) {
			try {
				Cell[] row = sheet.getRow(i);
				long customerPriceId = Long.parseLong(row[0].getContents());
				ContentValues values = prepareCustomerProductPricesValues(row);
				String rawQuery = "select count(*) from " + 
					DatabaseHelper.TABLE_CUSTOMER_PRODUCT_PRICES +
					" where " + DatabaseHelper.KEY_CUSTOMER_PRICE_ID + "=" + customerPriceId;
				Cursor c = db.rawQuery(rawQuery, null);
				if (c != null && c.moveToFirst()) {
					int count = c.getInt(0);
					if (count == 0) {
						db.insert(DatabaseHelper.TABLE_CUSTOMER_PRODUCT_PRICES, null, values);
					} else {
						String whereClause = DatabaseHelper.KEY_CUSTOMER_PRICE_ID + "=" + 
							customerPriceId;
						db.update(DatabaseHelper.TABLE_CUSTOMER_PRODUCT_PRICES, values, whereClause, null);
					}	
				}
				if (c != null && !c.isClosed()) {
					c.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private ContentValues prepareCustomerProductPricesValues(Cell[] row) 
		throws ParseException 
	{
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.KEY_CUSTOMER_PRICE_ID, 
			Long.parseLong(row[0].getContents()));
		values.put(DatabaseHelper.KEY_CUSTOMER_ID, 
			Long.parseLong(row[1].getContents()));
		values.put(DatabaseHelper.KEY_PRODUCT_ID, 
			Long.parseLong(row[2].getContents()));
		values.put(DatabaseHelper.KEY_CUSTOMER_PRICE, 
			nf.parse(row[3].getContents()).doubleValue());
		return values;
	}
	
	private void insertSalesmans(Sheet sheet) {
		int rows = sheet.getRows();
		if (rows <= 1) {
			return;
		}
		for (int i=1; i<rows; i++) {
			try {
				Cell[] row = sheet.getRow(i);
				long salesmanId = Long.parseLong(row[0].getContents());
				ContentValues values = prepareSalesmanValues(row);
				String rawQuery = "select count(*) from " + DatabaseHelper.TABLE_SALESMAN +
					" where " + DatabaseHelper.KEY_SALESMAN_ID + "=" + salesmanId;
				Cursor c = db.rawQuery(rawQuery, null);
				if (c != null && c.moveToFirst()) {
					int count = c.getInt(0);
					if (count == 0) {
						db.insert(DatabaseHelper.TABLE_SALESMAN, null, values);
					} else {
						String whereClause = DatabaseHelper.KEY_SALESMAN_ID + "=" + salesmanId;
						db.update(DatabaseHelper.TABLE_SALESMAN, values, whereClause, null);
					}	
				}
				if (c != null && !c.isClosed()) {
					c.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private ContentValues prepareSalesmanValues(Cell[] row) throws ParseException {
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.KEY_SALESMAN_ID, 
			Long.parseLong(row[0].getContents()));
		values.put(DatabaseHelper.KEY_SALESMAN_NAME, row[1].getContents());
		values.put(DatabaseHelper.KEY_ROUTE, row[2].getContents());
		return values;
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
	
	public Salesman getSalesmanById(long salesmanId) {
		Salesman salesman = null;
		try {
			String selection = DatabaseHelper.KEY_SALESMAN_ID + "=" + salesmanId;
			Cursor c = db.query(DatabaseHelper.TABLE_SALESMAN, null, 
				selection, null, null, null, null);
			if (c != null && c.moveToFirst()) {
				salesman = new Salesman(
					c.getLong(c.getColumnIndex(DatabaseHelper.KEY_SALESMAN_ID)),
					c.getString(c.getColumnIndex(DatabaseHelper.KEY_SALESMAN_NAME)),
					c.getString(c.getColumnIndex(DatabaseHelper.KEY_ROUTE))
				);
			}
			if (c != null && !c.isClosed()) {
				c.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesman;
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
	
	public List<Sale> getSales(String selection) {
		List<Sale> sales = new ArrayList<Sale>();
		try {
			String rawQuery = "select * from " + DatabaseHelper.TABLE_SALE + 
				" sale inner join " + DatabaseHelper.TABLE_SALE_DETAIL + " detail on " +
				"(sale." + DatabaseHelper.KEY_SALE_INVOICE_ID + "=detail." + 
				DatabaseHelper.KEY_SALE_INVOICE_ID + ") where " + selection;
			Cursor c = db.rawQuery(rawQuery, null);
			if (c != null && c.moveToFirst()) {
				do {
					long productId = c.getLong(c.getColumnIndex(DatabaseHelper.KEY_PRODUCT_ID));
					long salesmanId = c.getLong(c.getColumnIndex(DatabaseHelper.KEY_SALESMAN_ID));
					long customerId = c.getLong(c.getColumnIndex(DatabaseHelper.KEY_CUSTOMER_ID));
					Sale sale = new Sale(
						c.getLong(c.getColumnIndex(DatabaseHelper.KEY_SALE_INVOICE_ID)),
						salesmanId,
						getSalesmanName(salesmanId),
						customerId,
						getCustomerName(customerId),
						c.getString(c.getColumnIndex(DatabaseHelper.KEY_SALE_INVOICE_DATE)),
						c.getDouble(c.getColumnIndex(DatabaseHelper.KEY_PAID_AMOUNT)),
						c.getLong(c.getColumnIndex(DatabaseHelper.KEY_SALE_DETAIL_ID)),
						productId,
						getProductName(productId),
						c.getInt(c.getColumnIndex(DatabaseHelper.KEY_QUANTITY)),
						c.getDouble(c.getColumnIndex(DatabaseHelper.KEY_PRICE))
					);
					sales.add(sale);
				} while (c.moveToNext());
			}
			if (c != null && !c.isClosed()) {
				c.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sales;
	}
	
	private String getProductName(long productId) {
		String productName = null;
		try {
			String[] columns = new String[] { DatabaseHelper.KEY_PRODUCT_NAME };
			String selection = DatabaseHelper.KEY_PRODUCT_ID + "=" + productId;
			Cursor c = db.query(DatabaseHelper.TABLE_PRODUCT, columns, selection, null, null, null, null);
			if (c != null && c.moveToFirst()) {
				productName = c.getString(c.getColumnIndex(DatabaseHelper.KEY_PRODUCT_NAME));
			}
			if (c != null && !c.isClosed()) {
				c.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return productName;
	}
	
	private String getSalesmanName(long salesmanId) {
		String salesmanName = null;
		try {
			String[] columns = new String[] { DatabaseHelper.KEY_SALESMAN_NAME };
			String selection = DatabaseHelper.KEY_SALESMAN_ID + "=" + salesmanId;
			Cursor c = db.query(DatabaseHelper.TABLE_SALESMAN, columns, selection, null, null, null, null);
			if (c != null && c.moveToFirst()) {
				salesmanName = c.getString(c.getColumnIndex(DatabaseHelper.KEY_SALESMAN_NAME));
			}
			if (c != null && !c.isClosed()) {
				c.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesmanName;
	}
	
	private String getCustomerName(long customerId) {
		String customerName = null;
		try {
			String[] columns = new String[] { DatabaseHelper.KEY_CUSTOMER_NAME };
			String selection = DatabaseHelper.KEY_CUSTOMER_ID + "=" + customerId;
			Cursor c = db.query(DatabaseHelper.TABLE_CUSTOMER, columns, selection, null, null, null, null);
			if (c != null && c.moveToFirst()) {
				customerName = c.getString(c.getColumnIndex(DatabaseHelper.KEY_CUSTOMER_NAME));
			}
			if (c != null && !c.isClosed()) {
				c.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customerName;
	}
	
	public void addSales(List<Sale> sales) {
		if (sales == null || sales.isEmpty()) {
			return;
		}
		double arrears = 0;
		long customerId = 0, salesmanId = 0;
		for (Sale sale : sales) {
			customerId = sale.getCustomerId();
			salesmanId = sale.getSalesmanId();
			arrears += sale.getPrice() - sale.getPaidAmount();
			long saleInvioceId = addSale(sale);
			if (saleInvioceId != 0) {
				sale.setSaleInvoiceId(saleInvioceId);
				addSaleDetail(sale);
			}
		}
		insertOrUpdateArrears(customerId, salesmanId, arrears);
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
		db.delete(DatabaseHelper.TABLE_ARREARS, null, null);
	}
	
	public double getArrears(long customerId, long salesmanId) {
		double arrears = 0;
		try {
			String selection = DatabaseHelper.KEY_CUSTOMER_ID + "=" + customerId +
				" and " + DatabaseHelper.KEY_SALESMAN_ID + "=" + salesmanId;
			Cursor c = db.query(DatabaseHelper.TABLE_ARREARS, null, 
				selection, null, null, null, null);
			if (c != null && c.moveToFirst()) {
				arrears = c.getDouble(c.getColumnIndex(DatabaseHelper.KEY_ARREARS));
			}
			if (c != null && !c.isClosed()) {
				c.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrears;
	}
	
	public double getRecoveredAmount(long customerId, long salesmanId) {
		double recoveredAmount = 0;
		try {
			String rawQuery = "select sum(" + DatabaseHelper.KEY_RECOVERED_AMOUNT + ") from " +
				DatabaseHelper.TABLE_RECOVERY + " where " + 
				DatabaseHelper.KEY_CUSTOMER_ID + "=" + customerId +
				" and " + DatabaseHelper.KEY_SALESMAN_ID + "=" + salesmanId;
			Cursor c = db.rawQuery(rawQuery, null);
			if (c != null && c.moveToFirst()) {
				recoveredAmount = c.getDouble(0);
			}
			if (c != null && !c.isClosed()) {
				c.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return recoveredAmount;
	}
	
	public void insertOrUpdateArrears(long customerId, long salesmanId, double arrears) {
		if (customerId == 0 || salesmanId == 0) {
			return;
		}
		try {
			String selection = DatabaseHelper.KEY_CUSTOMER_ID + "=" + customerId +
				" and " + DatabaseHelper.KEY_SALESMAN_ID + "=" + salesmanId;
			Cursor c = db.query(DatabaseHelper.TABLE_ARREARS, null, 
				selection, null, null, null, null);
			if (c != null && c.moveToFirst()) {
				double newArrears = arrears + c.getDouble(
					c.getColumnIndex(DatabaseHelper.KEY_ARREARS));
				updateArrears(customerId, salesmanId, newArrears);
			} else {
				insertArrears(customerId, salesmanId, arrears);
			}
			if (c != null && !c.isClosed()) {
				c.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void insertArrears(long customerId, long salesmanId, double arrears) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.KEY_CUSTOMER_ID, customerId);
		values.put(DatabaseHelper.KEY_SALESMAN_ID, salesmanId);
		values.put(DatabaseHelper.KEY_ARREARS, arrears);
		db.insert(DatabaseHelper.TABLE_ARREARS, null, values);
	}
	
	public void updateArrears(long customerId, long salesmanId, double arrears) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.KEY_CUSTOMER_ID, customerId);
		values.put(DatabaseHelper.KEY_SALESMAN_ID, salesmanId);
		values.put(DatabaseHelper.KEY_ARREARS, arrears);
		String whereClause = DatabaseHelper.KEY_CUSTOMER_ID + "=" + customerId +
			" and " + DatabaseHelper.KEY_SALESMAN_ID + "=" + salesmanId;
		db.update(DatabaseHelper.TABLE_ARREARS, values, whereClause, null);
	}
	
	public void addRecovery(Recovery recovery) {
		if (recovery == null) {
			return;
		}
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.KEY_CUSTOMER_ID, recovery.getCustomerId());
		values.put(DatabaseHelper.KEY_SALESMAN_ID, recovery.getSalesmanId());
		values.put(DatabaseHelper.KEY_RECOVERED_AMOUNT, recovery.getRecoveredAmount());
		values.put(DatabaseHelper.KEY_RECOVERY_DATE, recovery.getRecoveryDate());
		db.insert(DatabaseHelper.TABLE_RECOVERY, null, values);
	}

}
