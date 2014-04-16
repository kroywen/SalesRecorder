package com.sales.recorder.screen;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.sales.recorder.R;
import com.sales.recorder.storage.Settings;
import com.sales.recorder.storage.TempSalesManager;
import com.sales.recorder.storage.database.DatabaseManager;

@SuppressLint("SimpleDateFormat")
public class BaseScreen extends Activity implements OnClickListener {
	
	public static final int UPDATE_SALE_REQUEST_CODE = 0;
	
	public static final String EXTRA_CUSTOMER_ID = "extra_customer_id";
	public static final String EXTRA_SALESMAN_ID = "extra_salesman_id";
	public static final String EXTRA_CHECKED = "extra_checked";
	
	private ProgressDialog progress;
	
	protected DatabaseManager dbManager;
	protected Settings settings;
	protected TempSalesManager tempSales;
	
	protected SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbManager = DatabaseManager.newInstance(this);
		settings = Settings.newInstance(this);
		tempSales = TempSalesManager.getInstance();
		
		ActionBar actionbar = getActionBar();
		if (actionbar != null) {
			actionbar.setDisplayHomeAsUpEnabled(true);
		}
		
		boolean dataImported = settings.getBoolean(Settings.DATA_IMPORTED);
		if (!dataImported) {
			new ImportDataTask().execute();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO override in subclasses
	}
	
	protected void showProgressDialog(String message) {
		if (progress == null) {
			progress = new ProgressDialog(this);
			if (!TextUtils.isEmpty(message)) {
				progress.setMessage(message);
			}
			progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progress.setCancelable(false);
		}
		if (!progress.isShowing()) {
			progress.show();
		}
	}
	
	protected void hideProgressDialog() {
		if (progress != null) {
			if (progress.isShowing()) {
				progress.dismiss();
			}
		}
	}
	
	protected void showErrorDialog(int messageId) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.error)
			.setMessage(messageId)
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			})
			.create()
			.show();
	}
	
	protected void showSuccessDialog(int titleId, int messageId) {
		showSuccessDialog(getString(titleId), getString(messageId));
	}
	
	protected void showSuccessDialog(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.success)
			.setMessage(message)
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			})
			.create()
			.show();
	}
	
	protected void showMainScreen() {
		Intent intent = new Intent(this, MainScreen.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	class ImportDataTask extends AsyncTask<Void, Void, Void> {
		
		@Override
		protected void onPreExecute() {
			showProgressDialog(getString(R.string.importing_data));
		}

		@Override
		protected Void doInBackground(Void... params) {
			dbManager.importData();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			settings.setBoolean(Settings.DATA_IMPORTED, true);
			hideProgressDialog();
		}
		
	}

}
