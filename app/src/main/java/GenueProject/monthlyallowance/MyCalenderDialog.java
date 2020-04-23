package GenueProject.monthlyallowance;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

//커스텀 달력

public class MyCalenderDialog extends Dialog implements DatePickerDialog.OnDateSetListener
{
	String TAG = "myInput";

	MyCalenderDialog myCalenderDialog;

	public MyCalenderDialog(@NonNull Context context)
	{
		super(context);
		Log.i(TAG, "캘린더 생성자");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		Log.i(TAG, "캘린더 onCreate");
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
	{
		String date = year + "년";
		date += (month + 1) + "월";
		date += dayOfMonth + "일";

		Log.i(TAG, date);
	}
}
