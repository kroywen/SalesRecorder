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

import com.sales.recorder.R;
import com.sales.recorder.storage.database.DatabaseHelper;

public class DatabaseScreen extends BaseScreen {
	
	private Button importDatabaseBtn;
	private Button exportDatabaseBtn;
	private Button clearDatabaseBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.database_screen);
		setTitle(R.string.database);
		initializeViews();
	}
	
	private void initializeViews() {
		importDatabaseBtn = (Button) findViewById(R.id.importDatabaseBtn);
		importDatabaseBtn.setOnClickListener(this);
		
		exportDatabaseBtn = (Button) findViewById(R.id.exportDatabaseBtn);
		exportDatabaseBtn.setOnClickListener(this);
		
		clearDatabaseBtn = (Button) findViewById(R.id.clearDatabaseBtn);
		clearDatabaseBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.importDatabaseBtn:
			importDatabase();
			break;
		case R.id.exportDatabaseBtn:
			exportDatabase();
			break;
		case R.id.clearDatabaseBtn:
			clearDatabase();
			break;
		}
	}
	
	private void importDatabase() {
		// TODO
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
			File dst = new File(dstDir, DatabaseHelper.DATABASE_NAME + ".db");
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
	
	private void clearDatabase() {
		dbManager.clearUserTables();
		showSuccessDialog(R.string.success, R.string.database_cleared);
	}

}
