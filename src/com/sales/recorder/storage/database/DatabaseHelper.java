package com.sales.recorder.storage.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	public static final String DATABASE_NAME = "SalesRecorder";
	public static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_PRODUCT = "Product";
	public static final String TABLE_CUSTOMER = "Customer";
	public static final String TABLE_SALESMAN = "Salesman";
	public static final String TABLE_CUSTOMER_PRODUCT_PRICES = "CustomerProductPrices";
	public static final String TABLE_SALE = "Sale";
	public static final String TABLE_SALE_DETAIL = "SaleDetail";
	public static final String TABLE_RECOVERY = "Recovery";
	public static final String TABLE_ARREARS = "Arrears";
	
	public static final String KEY_PRODUCT_ID = "ProductId";
	public static final String KEY_PRODUCT_NAME = "ProductName";
	public static final String KEY_STANDARD_PRICE = "StandardPrice";
	public static final String KEY_CUSTOMER_ID = "CustomerId";
	public static final String KEY_CUSTOMER_NAME = "CustomerName";
	public static final String KEY_CONTACT_NO = "ContactNo";
	public static final String KEY_ROUTE = "Route";
	public static final String KEY_AREARES = "Areares";
	public static final String KEY_CUSTOMER_PRICE_ID = "CustomerPriceId";
	public static final String KEY_CUSTOMER_PRICE = "CustomerPrice";
	public static final String KEY_SALESMAN_ID = "SalesmanId";
	public static final String KEY_SALESMAN_NAME = "SalesmanName";
	public static final String KEY_SALE_INVOICE_ID = "SaleInvoiceId";
	public static final String KEY_SALE_INVOICE_DATE = "SaleInvoiceDate";
	public static final String KEY_PAID_AMOUNT = "PaidAmount";
	public static final String KEY_SALE_DETAIL_ID = "SaleDetailId";
	public static final String KEY_QUANTITY = "Quantity";
	public static final String KEY_PRICE = "Price";
	public static final String KEY_RECOVERY_ID = "RecoveryId";
	public static final String KEY_RECOVERED_AMOUNT = "RecoveredAmount";
	public static final String KEY_RECOVERY_DATE = "RecoveryDate";
	public static final String KEY_ARREARS = "Arrears";
	public static final String KEY_ARREAR_ID = "ArrearId";
	
	public static final String CREATE_TABLE_PRODUCT = 
		"create table if not exists " + TABLE_PRODUCT + " (" +
		KEY_PRODUCT_ID + " integer, " +
		KEY_PRODUCT_NAME + " text, " +
		KEY_STANDARD_PRICE + " real);";
	
	public static final String DROP_TABLE_PRODUCT = 
		"drop table if exists " + TABLE_PRODUCT + ";";
	
	public static final String CREATE_TABLE_CUSTOMER =
		"create table if not exists " + TABLE_CUSTOMER + " (" +
		KEY_CUSTOMER_ID + " integer, " +
		KEY_CUSTOMER_NAME + " text, " +
		KEY_CONTACT_NO + " text, " +
		KEY_ROUTE + " text, " +
		KEY_AREARES + " real);";
	
	public static final String DROP_TABLE_CUSTOMER = 
		"drop table if exists " + TABLE_CUSTOMER + ";";
	
	public static final String CREATE_TABLE_SALESMAN =
		"create table if not exists " + TABLE_SALESMAN + " (" +
		KEY_SALESMAN_ID + " integer, " +
		KEY_SALESMAN_NAME + " text, " +
		KEY_ROUTE + " text);";
	
	public static final String DROP_TABLE_SALESMAN =
		"drop table if exists " + TABLE_SALESMAN + ";";
	
	public static final String CREATE_TABLE_CUSTOMER_PRODUCT_PRICES =
		"create table if not exists " + TABLE_CUSTOMER_PRODUCT_PRICES + " (" +
		KEY_CUSTOMER_PRICE_ID + " integer, " +
		KEY_CUSTOMER_ID + " integer, " +
		KEY_PRODUCT_ID + " integer, " +
		KEY_CUSTOMER_PRICE + " real);";
	
	public static final String DROP_TABLE_CUSTOMER_PRODUCT_PRICES =
		"drop table if exists " + TABLE_CUSTOMER_PRODUCT_PRICES + ";";
	
	public static final String CREATE_TABLE_SALE =
		"create table if not exists " + TABLE_SALE + " (" +
		KEY_SALE_INVOICE_ID + " integer primary key autoincrement, " +
		KEY_SALESMAN_ID + " integer, " +
		KEY_CUSTOMER_ID + " integer, " +
		KEY_SALE_INVOICE_DATE + " text, " +
		KEY_PAID_AMOUNT + " real);";
	
	public static final String DROP_TABLE_SALE =
		"drop table if exists " + TABLE_SALE + ";";
	
	public static final String CREATE_TABLE_SALE_DETAIL =
		"create table if not exists " + TABLE_SALE_DETAIL + " (" +
		KEY_SALE_DETAIL_ID + " integer primary key autoincrement, " +
		KEY_SALE_INVOICE_ID + " integer, " +
		KEY_PRODUCT_ID + " integer, " +
		KEY_QUANTITY + " integer, " +
		KEY_PRICE + " real);";
	
	public static final String DROP_TABLE_SALE_DETAIL =
		"drop table if exists " + TABLE_SALE_DETAIL + ";";
	
	public static final String CREATE_TABLE_RECOVERY =
		"create table if not exists " + TABLE_RECOVERY + " (" +
		KEY_RECOVERY_ID + " integer primary key autoincrement, " +
		KEY_CUSTOMER_ID + " integer, " +
		KEY_RECOVERED_AMOUNT + " real, " +
		KEY_SALESMAN_ID + " integer, " +
		KEY_RECOVERY_DATE + " datetime);";
	
	public static final String DROP_TABLE_RECOVERY =
		"drop table if exists " + TABLE_RECOVERY + ";";
	
	public static final String CREATE_TABLE_ARREARS =
		"create table if not exists " + TABLE_ARREARS + " (" +
		KEY_ARREAR_ID + " integer primary key autoincrement, " +
		KEY_CUSTOMER_ID + " integer, " +
		KEY_SALESMAN_ID + " integer, " +
		KEY_ARREARS + " real);";
	
	public static final String DROP_TABLE_ARREARS =
		"drop table if exists " + TABLE_ARREARS;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_PRODUCT);
		db.execSQL(CREATE_TABLE_CUSTOMER);
		db.execSQL(CREATE_TABLE_SALESMAN);
		db.execSQL(CREATE_TABLE_CUSTOMER_PRODUCT_PRICES);
		db.execSQL(CREATE_TABLE_SALE);
		db.execSQL(CREATE_TABLE_SALE_DETAIL);
		db.execSQL(CREATE_TABLE_RECOVERY);
		db.execSQL(CREATE_TABLE_ARREARS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_TABLE_PRODUCT);
		db.execSQL(DROP_TABLE_CUSTOMER);
		db.execSQL(DROP_TABLE_SALESMAN);
		db.execSQL(DROP_TABLE_CUSTOMER_PRODUCT_PRICES);
		db.execSQL(DROP_TABLE_SALE);
		db.execSQL(DROP_TABLE_SALE_DETAIL);
		db.execSQL(DROP_TABLE_RECOVERY);
		db.execSQL(DROP_TABLE_ARREARS);
		onCreate(db);
	}
	
	

}
