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
import android.widget.RadioButton;
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

	static final private int NEGATIVE = -1;
	//디비 관련
	static final private int CATEGORY_SPEND = 0;
	static final private int CATEGORY_EARN = 1;
	static final private int CATEGORY_SAVEP = 2;
	static final private int CATEGORY_SAVEM = 3;

	private AllowanceDatabaseManager dbManager;

	String TAG = "myInput";

	Button dateBtn;
	private TextView categoryName;


	////////////////////////////////////
	//// main으로 보내줄 데이터들
	Intent intent;
	//라디오버튼
	private int category;//지출 : 0  수입 : 1 저금 : 2
	//보여줄 날짜
	private int year;
	private int month;
	private int day;
	private int dayOfWeek;//요일
	private String week;//요일
	//항목
	private String item;
	//금액
	private int amount;
	//내용
	private String contents;
	//저금 +/-
	private int minus;
	////////////////////////////////////

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.i(TAG, "oncreate()");
		setContentView(R.layout.activity_input);
		Intent intent = getIntent();
		category = NEGATIVE;
		year = intent.getIntExtra("year", 0);
		month = intent.getIntExtra("month", 0);
		day = intent.getIntExtra("day", 0);
		dayOfWeek = intent.getIntExtra("dayOfWeek", 0);
		week = changeWeek(dayOfWeek);
		initView();
		dbManager = AllowanceDatabaseManager.getInstance(this);
	}

	private void initView()
	{
		dateBtn = findViewById(R.id.input_date_text);
		dateBtn.setText(year + "." + month + "." + day + " (" + week + ")");
		categoryName = findViewById(R.id.input_category_text);
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
				Log.i(TAG, "다 저장되어 있나??? " + year);
				if(category != NEGATIVE)
				{
					Intent intent = new Intent(this, CategoryActivity.class);
					intent.putExtra("category", category);
					startActivityForResult(intent, CATEGORY_REQUEST_CODE);
				}
				else
				{
					Log.i(TAG, "카테고리 눌렸지만 카테고리 선택을 안했음");
				}
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
				insert.putExtra("category", category);
				startActivityForResult(insert, INSERT_REQUEST_CODE);//insert 으로 넘어가
			}
			else if(resultCode == RESULT_CODE_ITEM)
			{
				Log.i(TAG, "category 갔다옴. 항목 눌림 " + data.getStringExtra("result"));
				categoryName.setText(data.getStringExtra("result"));
				Log.i(TAG, "다 저장되어 있나??? " + year);
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
				Log.i(TAG, "추가 갔다옴 " + data.getStringExtra("insert") + " " + data.getIntExtra("category", NEGATIVE));
				if(data.getStringExtra("item").length() != 0)
				{
					Log.i(TAG, "항목 " + data.getStringExtra("item"));
					categoryName.setText(data.getStringExtra("item"));
					category = data.getIntExtra("category", NEGATIVE);
					dbManager.insert(category, data.getStringExtra("item"));
					if(category == CATEGORY_SPEND)
					{
						RadioButton radioButton = findViewById(R.id.spend_radio_button);
						radioButton.setChecked(true);
					}
					else if(category == CATEGORY_EARN)
					{
						RadioButton radioButton = findViewById(R.id.earn_radio_button);
						radioButton.setChecked(true);
					}
					else if(category == CATEGORY_SAVEP)
					{
						RadioButton radioButton = findViewById(R.id.saveP_radio_button);
						radioButton.setChecked(true);
					}
					else if(category == CATEGORY_SAVEM)
					{
						RadioButton radioButton = findViewById(R.id.saveM_radio_button);
						radioButton.setChecked(true);
					}
				}
				else
				{
					Log.i(TAG, "돈 안 적음");
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

		//달력에서 날짜 선택 후 확인 시
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
				categoryName.setText("선택하세요");
			}
		};

		//year, month, day 기준으로 달력보여줘
		DatePickerDialog dialog = new DatePickerDialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog, mDateSetListener, year, month, day);
		dialog.show();


		/* 고쳐야 할 것

		커스텀 달력으로 바꾸기
		 */
	}

	private String getWeek(int year, int month, int day)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

		return changeWeek(dayOfWeek);
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
				category = CATEGORY_SPEND;
				break;
			case R.id.earn_radio_button:
				Log.i(TAG, "수입");
				category = CATEGORY_EARN;
				break;
			case R.id.saveP_radio_button:
				Log.i(TAG, "저금 플러스");
				category = CATEGORY_SAVEP;
				break;
			case R.id.saveM_radio_button:
				Log.i(TAG, "저금 마이너스");
				category = CATEGORY_SAVEM;
				break;
		}
	}

	public void onBtnClick(View v) {
		switch (v.getId()) {
			case R.id.input_save_btn:
				intent = new Intent();
				intent.putExtra("input", "저장");
				intent.putExtra("category", category);
				intent.putExtra("year", year);
				intent.putExtra("month", month);
				intent.putExtra("day", day);
				EditText moneyText = findViewById(R.id.input_money_text);
				amount = Integer.parseInt(moneyText.getText().toString());
				intent.putExtra("amount", amount);
				EditText editText = findViewById(R.id.input_explain_text);
				contents = editText.getText().toString();
				intent.putExtra("contents", contents);
				intent.putExtra("minus", minus);
				setResult(RESULT_CODE_INSERT, intent);
				finish();
				break;
			case R.id.input_cancel_btn:
				intent = new Intent();
				intent.putExtra("input", "취소");
				setResult(RESULT_CODE_CANCEL, intent);
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
