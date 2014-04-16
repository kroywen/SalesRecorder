package com.sales.recorder.dialog;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class DatePickerFragment extends DialogFragment {
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Bundle args = getArguments();
		long timeMillis = args.getLong("timeMillis");
		
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(timeMillis);
		
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		
		return new DatePickerDialog(getActivity(), 
			(OnDateSetListener) getActivity(), year, month, day);
	}

}
