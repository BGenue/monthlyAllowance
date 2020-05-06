package GenueProject.monthlyallowance;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.Calendar;

public class InputActivity extends AppCompatActivity
{
	//액티비티 구분 코드
	static final private int CATEGORY_REQUEST_CODE = 1;
	static final private int INSERT_REQUEST_CODE = 2;
	static final private int CALCULATOR_REQUEST_CODE = 4;

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
	static final private int CATEGORY_SAVE = 2;
	static final private int CATEGORY_SAVEP = 3;
	static final private int CATEGORY_SAVEM = 4;

	private AllowanceDatabaseManager dbManager;

	String TAG = "myInput";

	Button dateBtn;
	private TextView categoryName;
	private TextView durationText;
	private TextView moneyText;



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
	private long amount;
	//내용
	private String contents;
	//기간
	private long duration;
	//저금 +/-
	////////////////////////////////////

	private int touchable;

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
		dbManager = AllowanceDatabaseManager.getInstance(this);

		initView();
		duration = 0;
		amount = 0;
	}

	private void initView()
	{
		dateBtn = findViewById(R.id.input_date_text);
		dateBtn.setText(year + "." + month + "." + day + " (" + week + ")");
		categoryName = findViewById(R.id.input_category_text);
		durationText = findViewById(R.id.input_duration_text);
		moneyText = findViewById(R.id.input_money_text);
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
		categoryName.setText("선택하세요");
		moneyText.setText("입력하세요");
		durationText.setText("0 개월");
		duration = 0;
		amount = 0;
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
				if(category != NEGATIVE)
				{
					intent = new Intent(this, CategoryActivity.class);
					intent.putExtra("category", category);
					startActivityForResult(intent, CATEGORY_REQUEST_CODE);
				}
				else
				{
					Log.i(TAG, "카테고리 눌렸지만 카테고리 선택을 안했음");
				}
				break;
			case R.id.input_money:
				Log.i(TAG, "금액 눌림 "+ amount);
				intent = new Intent(this, myCalculator.class);
				intent.putExtra("money", amount);
				intent.putExtra("type", " 원");
				startActivityForResult(intent, CALCULATOR_REQUEST_CODE);
				break;
			case R.id.input_duration:
				Log.i(TAG, "기한 눌림");
				intent = new Intent(this, myCalculator.class);
				intent.putExtra("money", duration);
				intent.putExtra("type", " 개월");
				startActivityForResult(intent, CALCULATOR_REQUEST_CODE);
				break;
		}
	}

	public void onBtnClick(View v) {
		switch (v.getId()) {
			case R.id.input_save_btn:
				intent = new Intent();
				intent.putExtra("input", "저장");
				intent.putExtra("category", category);
				intent.putExtra("item", item);
				intent.putExtra("year", year);
				intent.putExtra("month", month);
				intent.putExtra("day", day);
				String tmp = moneyText.getText().toString();
				intent.putExtra("amount", amount);
				EditText editText = findViewById(R.id.input_explain_text);
				contents = editText.getText().toString();
				intent.putExtra("contents", contents);
				tmp = durationText.getText().toString();
				int duration = Integer.parseInt(tmp.substring(0, tmp.length()-3));
				intent.putExtra("duration", duration);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch(requestCode)
		{
			case CATEGORY_REQUEST_CODE:
				//카테고리 갔다옴
				//갔다온 코드
				switch(resultCode)
				{
					case RESULT_CODE_CANCEL :
						Log.i(TAG, "category 갔다옴. 취소 눌림 " + data.getStringExtra("result"));
						break;
					case RESULT_CODE_INSERT :
						Log.i(TAG, "category 갔다옴. 추가 눌림 " + data.getStringExtra("result") + " " + data.getIntExtra("category", -1));
						Intent insert = new Intent(this, InsertActivity.class);
						insert.putExtra("category", data.getIntExtra("category", -1));
						startActivityForResult(insert, INSERT_REQUEST_CODE);//insert 으로 넘어가
						break;
					case RESULT_CODE_ITEM :
						Log.i(TAG, "category 갔다옴. 항목 눌림 " + data.getStringExtra("result"));
						item = data.getStringExtra("result");
						categoryName.setText(data.getStringExtra("result"));
						Log.i("myMain", "다 저장되어 있나??? " + category);
						break;
				}
				break;
			case INSERT_REQUEST_CODE:
				switch(resultCode)
				{
					//추가 갔다옴
					case RESULT_CODE_CANCEL:
						//취소
						Log.i(TAG, "추가 갔다옴 " + data.getStringExtra("insert"));
						break;
					case RESULT_CODE_INSERT:
						//확인
						int select;
						Log.i(TAG, "추가 갔다옴 " + data.getStringExtra("insert") + " " + data.getIntExtra("category", NEGATIVE));
						if(data.getStringExtra("item").length() != 0)
						{
							item = data.getStringExtra("item");
							duration = data.getIntExtra("duration", 0);
							durationText.setText(duration + " 개월");
							Log.i(TAG, "항목 " + item + " " + duration);
							categoryName.setText(data.getStringExtra("item"));
							category = data.getIntExtra("category", NEGATIVE);
							//db items 에 추가
							if(category == CATEGORY_SAVEM || category == CATEGORY_SAVEP)
							{
								dbManager.insertToItems(data.getStringExtra("item"), CATEGORY_SAVE);
								//dbManager.insert(CATEGORY_SAVE, data.getStringExtra("item"));
							}
							else
							{
								dbManager.insertToItems(data.getStringExtra("item"), category);
								//dbManager.insert(category, data.getStringExtra("item"));
							}
							dbManager.show_items_table();
							//라디오버튼 체크
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
						break;
				}
				break;
			case CALCULATOR_REQUEST_CODE:
				Log.i(TAG, "계산기 갔다옴 ");
				switch(resultCode)
				{
					case RESULT_CODE_INSERT:
						amount = data.getLongExtra("number", 0);
						String type = data.getStringExtra("type");
						Log.i(TAG, "계산기 갔다옴 " + amount);
						if(type.equals(" 원"))
						{
							moneyText.setText(amount + type);
						}
						else
						{
							durationText.setText(amount + type);
							duration = amount;
						}
						break;
					case RESULT_CODE_CANCEL:
						Log.i(TAG, "계산기 갔다옴 취소 눌림");
						break;
				}
				break;
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
