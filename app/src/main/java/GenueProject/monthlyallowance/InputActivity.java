package GenueProject.monthlyallowance;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class InputActivity extends AppCompatActivity
{
	//액티비티 구분 코드
	static final private int CATEGORY_REQUEST_CODE = 1;
	static final private int INSERT_REQUEST_CODE = 2;

	//인서트 액티비티
	static final private int RESULT_CODE_SPEND = 0;
	static final private int RESULT_CODE_EARN = 1;
	static final private int RESULT_CODE_SAVE = 2;

	//공용
	static final private int RESULT_CODE_CANCEL = -1;
	static final private int RESULT_CODE_INSERT = 0;

	//카테고리 액티비티
	static final private int RESULT_CODE_ITEM = 1;

	String TAG = "myInput";

	ArrayList arrayList;
	EditText empty;
	Spinner s;

	Button dateBtn;
	TextView categoryTitle;

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
	}

	private void initView()
	{
		dateBtn = findViewById(R.id.input_date_text);
		categoryTitle = findViewById(R.id.input_category_title);
	}

	public void inputOnClick(View v)
	{
		switch(v.getId())
		{
			case R.id.input_date_text:
				Log.i(TAG, "날짜 눌림");
				dialogCalender();
				break;
			case R.id.input_category:
				Log.i(TAG, "카테고리 눌림");
				Intent intent = new Intent(this, CategoryActivity.class);
				intent.putExtra("category", "popup");
				startActivityForResult(intent, CATEGORY_REQUEST_CODE);
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode == CATEGORY_REQUEST_CODE)
		{
			//카테고리 갔다옴
			//갔다온 코드
			if(resultCode == RESULT_CODE_CANCEL)
			{
				Log.i(TAG, "category 갔다옴. 취소 눌림 " + data.getStringExtra("result"));
			}
			else if(resultCode == RESULT_CODE_INSERT)
			{
				Log.i(TAG, "category 갔다옴. 추가 눌림 " + data.getStringExtra("result"));
				Intent insert = new Intent(this, InsertActivity.class);
				insert.putExtra("insert", "popup");
				startActivityForResult(insert, INSERT_REQUEST_CODE);//insert 으로 넘어가
			}
			else if(resultCode == RESULT_CODE_ITEM)
			{
				Log.i(TAG, "category 갔다옴. 항목 눌림 " + data.getStringExtra("result"));
				TextView categoryName = findViewById(R.id.input_category_title);
				categoryName.setText(data.getStringExtra("result"));
			}
		}
		if(requestCode == INSERT_REQUEST_CODE)
		{
			//추가 갔다옴
			if(resultCode == RESULT_CODE_CANCEL)
			{
				//취소
				Log.i(TAG, "추가 갔다옴 " + data.getStringExtra("insert"));
			}
			else if(resultCode == RESULT_CODE_INSERT)
			{
				//확인
				Log.i(TAG, "추가 갔다옴 " + data.getStringExtra("insert") + " " + data.getStringExtra("category"));
				if(data.getStringExtra("name").length() != 0)
				{
					Log.i(TAG, "항목 " + data.getStringExtra("name"));
					TextView categoryName = findViewById(R.id.input_category_title);
					categoryName.setText(data.getStringExtra("name"));
				}
				else
				{
					Log.i(TAG, "돈 안 적음");
					TextView categoryName = findViewById(R.id.input_category_title);
					categoryName.setText("선택하세요");
				}
			}
		}
	}

	private void dialogCalender()
	{
		//일단 달력 열 때마다 오늘 날짜에 포커스
		Calendar c = Calendar.getInstance();//오늘날짜
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		Log.i(TAG, year+" "+month+" "+day + " " + c.get(Calendar.DAY_OF_WEEK));

		/*
		MyCalenderDialog myCalenderDialog = new MyCalenderDialog(this);
		myCalenderDialog.setCancelable(false);
		myCalenderDialog.show();
		 */

		DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener()
		{
			@Override
			public void onDateSet(DatePicker view, int y, int m, int d)
			{
				//날짜를 고르고 확인 버튼 누르면 작동
				year = y;
				month = m;
				day = d;
				String date = year + ".";
				date += month + 1 + ".";

				String week = getWeek(year, month, day);

				date += day + " (" + week + ")";
				Log.i(TAG, date);

				dateBtn.setText(date);
				categoryTitle.setText("선택하세요");
			}
		};

		//year, month, day 기준으로 달력보여줘
		DatePickerDialog dialog = new DatePickerDialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog, mDateSetListener,year, month, day);
		dialog.show();


		/* 고쳐야 할 것

		커스텀 달력으로 바꾸기
		누른 날짜 받기
		 */
	}

	private String getWeek(int year, int month, int day)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		week = calendar.get(Calendar.DAY_OF_WEEK);

		return changeWeek(week);
	}

	private String changeWeek(int w)
	{
		if(w == 1)
		{
			return "일";
		}
		else if(w == 2)
		{
			return "월";
		}
		else if(w == 3)
		{
			return "화";
		}
		else if(w == 4)
		{
			return "수";
		}
		else if(w == 5)
		{
			return "목";
		}
		else if(w == 6)
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

	public void onBtnClick(View v)
	{
		switch(v.getId())
		{
			case R.id.input_save_btn:
				Intent intent = new Intent();
				intent.putExtra("input", "저장");
				setResult(RESULT_CODE_INSERT, intent);
				finish();
				break;
			case R.id.input_cancel_btn:
				Intent intent1 = new Intent();
				intent1.putExtra("input", "취소");
				setResult(RESULT_CODE_CANCEL, intent1);
				finish();
				break;
		}
	}

	//백버튼
	@Override
	public void onBackPressed()
	{
		Intent intent1 = new Intent();
		intent1.putExtra("input", "취소");
		setResult(RESULT_CODE_CANCEL, intent1);
		finish();
	}
}
