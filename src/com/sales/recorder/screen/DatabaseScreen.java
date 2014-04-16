package com.sales.recorder.screen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import com.sales.recorder.R;
import com.sales.recorder.storage.Settings;
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
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			String path = Environment.getExternalStorageDirectory().getAbsolutePath() +
				File.separator + DatabaseHelper.DATABASE_NAME + 
				File.separator + "Data.xls";
			File file = new File(path);
			if (file.exists()) {
				new ImportDataTask(file).execute();
			} else {
				showDialog(R.string.file_not_exists, 
					R.string.database_import_file_not_exists);
			}
		} else {
			showDialog(R.string.error, R.string.media_not_mounted);
		}
	}
	
	private void exportDatabase() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			showDialog(R.string.error, R.string.media_read_only);
		} else if (state.equals(Environment.MEDIA_MOUNTED)) {
			File dstDir = new File(Environment.getExternalStorageDirectory(), DatabaseHelper.DATABASE_NAME + File.separator);
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
			    
			    showDialog(getString(R.string.success), 
			    	getString(R.string.database_exported, dst.getAbsolutePath()));
			} catch (Exception e) {
				e.printStackTrace();
				showDialog(R.string.error, R.string.error_copying_database);
			} finally {
				if (fromChannel != null) 
					try { fromChannel.close(); } catch (IOException ex) {}
		        if (toChannel != null) 
		        	try { toChannel.close(); } catch (IOException ex) {}
			}
		} else {
			showDialog(R.string.error, R.string.media_not_mounted);
		}
	}
	
	private void clearDatabase() {
		dbManager.clearUserTables();
		showDialog(R.string.success, R.string.database_cleared);
	}
	
	class ImportDataTask extends AsyncTask<Void, Void, Void> {
		
		private File file;
		
		public ImportDataTask(File file) {
			this.file = file;
		}
		
		@Override
		protected void onPreExecute() {
			showProgressDialog(getString(R.string.importing_data));
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				dbManager.importData(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			settings.setBoolean(Settings.DATA_IMPORTED, true);
			hideProgressDialog();
			showDialog(R.string.success, R.string.database_imported);
		}
		
	}

}
