package com.sales.recorder.screen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sales.recorder.R;
import com.sales.recorder.storage.database.DatabaseHelper;

public class ReportsScreen extends BaseScreen {
	
	private Button customerInvoiceBtn;
	private Button routeWiseSaleBtn;
	private Button salesmanWiseSaleBtn;
	private Button clearDatabaseBtn;
	private Button exportDatabaseBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.reports);
		setContentView(R.layout.reports_screen);
		initializeViews();
	}
	
	private void initializeViews() {
		customerInvoiceBtn = (Button) findViewById(R.id.customerInvoiceBtn);
		customerInvoiceBtn.setOnClickListener(this);
		
		routeWiseSaleBtn = (Button) findViewById(R.id.routeWiseSaleBtn);
		routeWiseSaleBtn.setOnClickListener(this);
		
		salesmanWiseSaleBtn = (Button) findViewById(R.id.salesmanWiseSaleBtn);
		salesmanWiseSaleBtn.setOnClickListener(this);
		
		clearDatabaseBtn = (Button) findViewById(R.id.clearDatabaseBtn);
		clearDatabaseBtn.setOnClickListener(this);
		
		exportDatabaseBtn = (Button) findViewById(R.id.exportDatabaseBtn);
		exportDatabaseBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.customerInvoiceBtn:
			// TODO
			break;
		case R.id.routeWiseSaleBtn:
			// TODO
			break;
		case R.id.salesmanWiseSaleBtn:
			// TODO
			break;
		case R.id.clearDatabaseBtn:
			clearDatabase();
			break;
		case R.id.exportDatabaseBtn:
			exportDatabase();
			break;
		}
	}
	
	private void clearDatabase() {
		dbManager.clearUserTables();
		Toast.makeText(this, R.string.database_cleared, Toast.LENGTH_SHORT).show();
	}
	
	private void exportDatabase() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			showErrorDialog(R.string.media_read_only);
		} else if (state.equals(Environment.MEDIA_MOUNTED)) {
			File dstDir = new File(Environment.getExternalStorageDirectory(), "Sales Recorder/");
			if (!dstDir.exists()) {
				dstDir.mkdirs();
			}
			File dst = new File(dstDir, DatabaseHelper.DATABASE_NAME);
			File src = getDatabasePath(DatabaseHelper.DATABASE_NAME);
			FileChannel fromChannel = null, toChannel = null;
			try {
				FileInputStream in = new FileInputStream(src);
				fromChannel = in.getChannel();
			    FileOutputStream out = new FileOutputStream(dst);
			    toChannel = out.getChannel();
			    fromChannel.transferTo(0, fromChannel.size(), toChannel); 
			    
			    showSuccessDialog(getString(R.string.success), 
			    	getString(R.string.database_exported, dst.getAbsolutePath()));
			} catch (Exception e) {
				e.printStackTrace();
				showErrorDialog(R.string.error_copying_database);
			} finally {
				if (fromChannel != null) 
					try { fromChannel.close(); } catch (IOException ex) {}
		        if (toChannel != null) 
		        	try { toChannel.close(); } catch (IOException ex) {}
			}
		} else {
			showErrorDialog(R.string.media_not_mounted);
		}
	}

}
