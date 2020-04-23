package GenueProject.monthlyallowance;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class InputActivity extends AppCompatActivity
{
	String TAG = "myInput";

	ArrayList arrayList;
	InputMethodManager inputMethodManager;
	EditText empty;
	Spinner s;

	Button dateBtn;

	//보여줄 날짜
	private int year;
	private int month;
	private int day;
	private int week;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input);
		initView();


		arrayList = new ArrayList<>();
		arrayList.add("철수");
		arrayList.add("영희");
		arrayList.add("녹지");
		arrayList.add("치치");
		arrayList.add("용병");
		arrayList.add("입력");
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);//키보드
		empty = findViewById(R.id.empty);
		//s = findViewById(R.id.test_spinner);
		//ArrayAdapter arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList);
		//s.setAdapter(arrayAdapter);
		//inputMethodManager.hideSoftInputFromWindow(empty.getWindowToken(), 0);//키보드 내려
		/*
		s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				Toast.makeText(getApplicationContext(),arrayList.get(position)+"가 선택되었습니다.",
						Toast.LENGTH_SHORT).show();
				if(arrayList.get(position).toString().equals("입력"))
				{
					Log.i(TAG, "입력임");
					///////////////////
					empty.post(new Runnable()
					{
						@Override
						public void run()
						{
							boolean b = empty.requestFocus();
							boolean a = inputMethodManager.showSoftInput(empty, InputMethodManager.HIDE_IMPLICIT_ONLY);
							Log.i(TAG, "키보드 결과 " + b + a);
							Log.i(TAG, "입력값 : " + empty.getText());
						}
					});
					//empty.requestFocus();
					//boolean a = inputMethodManager.showSoftInput(empty, InputMethodManager.HIDE_IMPLICIT_ONLY);
					//Log.i(TAG, "키보드 결과 " + a);
					//Log.i(TAG, "입력값 : " + empty.getText());
					//inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);//키보드 보여줘\
					/////////////////
				}
				else
				{
					Log.i(TAG, "입력이 아님");
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		*/
		/*
		boolean b = empty.requestFocus();
		boolean a = inputMethodManager.showSoftInput(empty, InputMethodManager.HIDE_IMPLICIT_ONLY);
		Log.i(TAG, "키보드 결과 " + b + a);
		Log.i(TAG, "입력값 : " + empty.getText());
		 */
	}

	private void initView()
	{
		dateBtn = findViewById(R.id.input_date_text);
	}

	public void inputOnClick(View v)
	{
		switch(v.getId())
		{
			case R.id.input_date_text:
				Log.i(TAG, "날짜 눌림");
				dialogCalender();
				break;
			case R.id.input_category_text:
				Log.i(TAG, "카테고리 눌림");
				Intent intent = new Intent(this, CategoryActivity.class);
				intent.putExtra("category", "popup");
				startActivityForResult(intent, 1);
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode == 1)
		{
			//카테고리 갔다와
			//갔다온 코드
			if(resultCode == 0)
			{
				Log.i(TAG, "category 갔다옴. 취소 눌림 " + data.getStringExtra("result"));
			}
			else if(resultCode == 1)
			{
				Log.i(TAG, "category 갔다옴. 확인 눌림" + data.getStringExtra("result"));
			}
		}
	}

	private void dialogCalender()
	{
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		Log.i(TAG, year+" "+month+" "+day + " " + c.get(Calendar.DAY_OF_WEEK));

		/*
		MyCalenderDialog myCalenderDialog = new MyCalenderDialog(this);
		myCalenderDialog.setCancelable(false);
		myCalenderDialog.show();
		 */

		DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener()
		{
			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
			{
				//날짜를 고르고 확인 버튼 누르면 작동
				String date = year + ".";
				date += month + 1 + ".";

				String week = changeWeek(year, month, dayOfMonth);

				date += dayOfMonth + " (" + week + ")";
				Log.i(TAG, date);
				dateBtn.setText(date);
			}
		};

		//year, month, day 기준으로 달력보여줘
		DatePickerDialog dialog = new DatePickerDialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog, mDateSetListener,year, month, day);
		dialog.show();


		/* 고쳐야 할 것

		커스텀 달력으로 바꾸기
		캘린더를 통해 확인한 날짜 받기

		 */
	}

	private String changeWeek(int year, int month, int day)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

		if(dayOfWeek == 1)
		{
			return "일";
		}
		else if(dayOfWeek == 2)
		{
			return "월";
		}
		else if(dayOfWeek == 3)
		{
			return "화";
		}
		else if(dayOfWeek == 4)
		{
			return "수";
		}
		else if(dayOfWeek == 5)
		{
			return "목";
		}
		else if(dayOfWeek == 6)
		{
			return "금";
		}
		else
		{
			return "토";
		}
	}

	public void onRadioClicked(View v)
	{
		switch(v.getId())
		{
			case R.id.spend_radio_button:
				Log.i(TAG, "지출");
				break;
			case R.id.earn_radio_button:
				Log.i(TAG, "수입");
				break;
			case R.id.saveP_radio_button:
				Log.i(TAG, "저금 플러스");
				break;
			case R.id.saveM_radio_button:
				Log.i(TAG, "저금 마이너스");
				break;
		}
	}
}
