package GenueProject.monthlyallowance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
{
	static final private String TAG = "myMain";

	static final private int INPUT_REQUEST_CODE = 0;
	static final private int CALENDAR_REQUEST_CODE = 1;
	static final private int HISTORY_REQUEST_CODE = 2;
	static final private int MONTHLY_REQUEST_CODE = 3;
	static final private int CALCULATOR_REQUEST_CODE = 4;

	static final private int RESULT_CODE_CANCEL = -1;
	static final private int RESULT_CODE_INSERT = 0;

	static final private int CATEGORY_SPEND = 0;
	static final private int CATEGORY_EARN = 1;
	static final private int CATEGORY_SAVE = 2;
	static final private int CATEGORY_SAVEP = 3;
	static final private int CATEGORY_SAVEM = 4;

	static final private int DAILY = 0;
	static final private int WEEKLY = 1;
	static final private int MONTHLY = 2;

	private Intent intent;

	//광고
	private AdView mAdView;
	private FrameLayout mAdLayout;

	//좌우 화살표
	private TextView arrowL;
	private TextView arrowR;

	//메인 화면 뷰
	private int view_showing;
	private TextView top_month_title;
	private TextView main_top_monthlyMoney_text;
	private TextView main_top_monthlySave_text;
	private TextView main_top_monthlySpend_text;
	private TextView main_top_monthlyUse_text;
	private TextView main_middle_date_title;
	private TextView main_middle_date_text;
	private TextView main_middle_total_spend_text;
	private TextView main_middle_total_earn_text;
	private TextView main_middle_total_save_text;
	private TextView main_middle_last_text;

	//날짜
	private int year;
	private int month;
	private int day;
	private int dayOfWeek;//요일
	private int week;//주

	//TABLE_INFO
	private int money;
	private int save;
	private int spend;
	private int start;

	//db
	static final private String TABLE_CATEGORY = "Category";
	static final private String TABLE_ITEMS = "Item";
	static final private String TABLE_DATE = "Date";
	static final private String TABLE_HISTORY = "History";
	static final private String TABLE_INFO = "Info";//내 정보
	private AllowanceDatabaseManager dbManager;

	private long number;
	private int touchedViewId;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Mobile Ads SDK 초기화
		initAd();

		findView();

		dbManager = AllowanceDatabaseManager.getInstance(this);
		dbManager.reset();//삭제

		getTodayDate();

		initView();

		dbManager.showAll();
	}

	//광고 관련 함수
	//initAd(), loadBanner(), getAdSize()
	private void initAd()
	{
		MobileAds.initialize(this, new OnInitializationCompleteListener() {
			@Override
			public void onInitializationComplete(InitializationStatus initializationStatus) {
				Log.i(TAG, "광고 완료");
			}
		});

		//적응형 배너
		mAdLayout = findViewById(R.id.main_ad_layout);
		mAdView = new AdView(this);
		mAdView.setAdUnitId(getString(R.string.banner_ad_unit_id_test));//banner_ad_unit 으로 바꿔
		mAdLayout.removeAllViews();
		mAdLayout.addView(mAdView);
		loadBanner();

		/*
		삭제해도 될듯
		//스마트 배너
		mAdView = findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);
		 */
	}

	private void loadBanner()
	{
		AdRequest adRequest = new AdRequest.Builder().build();

		AdSize adsize = getAdSize();
		mAdView.setAdSize(adsize);

		mAdView.loadAd(adRequest);
	}

	private AdSize getAdSize()
	{
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);

		float density = outMetrics.density;
		float adWidthPixels = outMetrics.widthPixels;

		int adWidth= (int)(adWidthPixels / density);

		return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
	}

	//뷰 초기화
	private void findView()
	{
		arrowL = findViewById(R.id.main_left_arrow);
		arrowR = findViewById(R.id.main_right_arrow);

		top_month_title = findViewById(R.id.main_top_month_text);
		main_top_monthlyMoney_text = findViewById(R.id.main_top_monthlyMoney_text);
		main_top_monthlySave_text = findViewById(R.id.main_top_monthlySave_text);
		main_top_monthlySpend_text = findViewById(R.id.main_top_monthlySpend_text);
		main_top_monthlyUse_text = findViewById(R.id.main_top_monthlyUse_text);
		main_middle_date_title = findViewById(R.id.main_middle_date_title);
		main_middle_date_text = findViewById(R.id.main_middle_date_text);
		main_middle_total_spend_text = findViewById(R.id.main_middle_total_spend_text);
		main_middle_total_earn_text = findViewById(R.id.main_middle_total_earn_text);
		main_middle_total_save_text = findViewById(R.id.main_middle_total_save_text);
		main_middle_last_text = findViewById(R.id.main_middle_last_text);
	}

	private void getTodayDate()
	{
		Calendar calendar = Calendar.getInstance();//오늘날짜
		year = calendar.get(Calendar.YEAR);//년
		month = calendar.get(Calendar.MONTH)+1;//월
		day = calendar.get(Calendar.DAY_OF_MONTH);//일
		dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);//요일. 일요일 : 1 ~ 토요일 : 7
		week = calendar.get(Calendar.WEEK_OF_MONTH);//이번달의 몇 째주인지

		long i = dbManager.insertToDate(year, month, day, week);
		Log.i(TAG, "날짜 입력함 : " + week);
	}

	//처음 앱 시작하면 할 셋팅
	private void initView()
	{
		view_showing = DAILY;//일별 뷰 보여줄거야
		//수정해야하는 부분
		//윗 부분
		top_month_title.setText(month + "월");
		String sql = 	"SELECT DISTINCT * " +
				"FROM " + TABLE_INFO + " " +
				"WHERE year=" + year + " " +
				"AND month=" + month;
		Cursor c = dbManager.rawQuery(sql, null);
		if(c.getCount() == 0)
		{
			Log.i(TAG, "내 정보 없음 그럼 추가해줄게");
			dbManager.insertToInfo(year, month, 0, 0, 0, 0);
			money = 0;
			save = 0;
			spend = 0;
			start = 0;
		}
		else
		{
			Log.i(TAG, "내 정보 있음");
			while(c.moveToNext())
			{
				if(c != null)
				{
					Log.i(TAG, "내 정보 : " + c.getColumnCount() + c.getColumnIndex("money") + " " + c.getColumnIndex("save") + " " + c.getColumnIndex("spend") + " " + c.getColumnIndex("start"));
					money = c.getInt(3);
					save = c.getInt(4);
					spend = c.getInt(5);
					start = c.getInt(6);
				}
			}
		}
		main_top_monthlyMoney_text.setText(money + " 원");
		main_top_monthlySave_text.setText(save + " 원");
		main_top_monthlySpend_text.setText(spend + " 원");
		main_top_monthlyUse_text.setText(start + " 원");
		initDayView();
	}

	//일별 셋팅
	private void initDayView()
	{
		Log.i(TAG, "initDayView 이다!!!");
		String sql;
		Cursor c;
		int i_id = 0;
		int d_id = 0;
		int daily_spend = 0;
		int daily_earn = 0;
		int daily_save = 0;
		int daily_last = 0;

		main_middle_date_text.setText(month + "월 " + day + "일");

		sql = 	"SELECT DISTINCT " + TABLE_ITEMS + ".c_id, " + TABLE_ITEMS + ".item, " + TABLE_HISTORY + ".amount, " + TABLE_HISTORY + ".contents " +
				"FROM " + TABLE_DATE + ", " + TABLE_HISTORY + ", " + TABLE_ITEMS + " " +
				"WHERE "+ TABLE_DATE + ".year=" + year + " " +
				"AND " + TABLE_DATE + ".month=" + month + " " +
				"AND " + TABLE_DATE + ".day=" + day + " " +
				"AND " + TABLE_DATE + ".d_id=" + TABLE_HISTORY + ".d_id " +
				"AND " + TABLE_ITEMS + ".i_id=" + TABLE_HISTORY + ".i_id " +
				"GROUP BY " + TABLE_ITEMS + ".c_id";
		c = dbManager.rawQuery(sql, null);
		Log.i(TAG, "initDayView 결과 : " + c.getCount() + " " + c.getColumnCount());
		Log.i(TAG,  c.getColumnName(0) + " " + c.getColumnName(1) + " " + c.getColumnName(2) + " " + c.getColumnName(3));
		while(c.moveToNext())
		{
			if(c != null)
			{
				Log.i(TAG,  c.getInt(0) + " " + c.getString(1) + " " + c.getInt(2) + " " + c.getString(3));
				if(c.getInt(0) == CATEGORY_SPEND)
				{
					daily_spend += c.getInt(2);
				}
				else if(c.getInt(0) == CATEGORY_EARN)
				{
					daily_earn += c.getInt(2);
				}
				else if(c.getInt(0) == CATEGORY_SAVEP)
				{
					daily_save += c.getInt(2);
				}
				else if(c.getInt(0) == CATEGORY_SAVEM)
				{
					daily_save -= c.getInt(2);
				}
			}
		}
		main_middle_total_spend_text.setText(daily_spend + " 원");
		main_middle_total_earn_text.setText(daily_earn + " 원");
		main_middle_total_save_text.setText(daily_save + " 원");
		main_middle_last_text.setText((start - daily_spend + daily_earn - daily_save) + " 원");
	}

	//주별 셋팅
	private void initWeekView()
	{
		Log.i(TAG, "initWeekView 이다!!!");
		String sql;
		Cursor c;
		int i_id = 0;
		int d_id = 0;
		int weekly_spend = 0;
		int weekly_earn = 0;
		int weekly_save = 0;
		int weekly_last = 0;

		main_middle_date_text.setText(month + "월 " + week + "주차");


		weekly_last = start - weekly_spend + weekly_earn - weekly_save;
		main_middle_total_spend_text.setText(weekly_spend + " 원");
		main_middle_total_earn_text.setText(weekly_earn + " 원");
		main_middle_total_save_text.setText(weekly_save + " 원");
		main_middle_last_text.setText(weekly_last + " 원");
	}

	//월별 셋팅
	private void initMonthView()
	{
		Log.i(TAG, "initMonthView 이다!!!");
		String sql;
		Cursor c;
		int i_id = 0;
		int d_id = 0;
		int monthly_spend = 0;
		int monthly_earn = 0;
		int monthly_save = 0;
		int monthly_last = 0;

		main_middle_date_text.setText(month + "월 " + "전체");


		monthly_last = start - monthly_spend + monthly_earn - monthly_save;
		main_middle_total_spend_text.setText(monthly_spend + " 원");
		main_middle_total_earn_text.setText(monthly_earn + " 원");
		main_middle_total_save_text.setText(monthly_save + " 원");
		main_middle_last_text.setText(monthly_last + " 원");
	}

	public void onLayoutClicked(View v)
	{
		touchedViewId = v.getId();
		/*수정
		switch(touchedViewId)
		{
			case R.id.main_top_month_text://이건 안해도 괜찮을 것 같아
				Log.i(TAG, "터치터치");
				intent = new Intent(this, MonthlyInfoActivity.class);
				startActivityForResult(intent, MONTHLY_REQUEST_CODE);
				break;
			case R.id.main_top_monthlyMoney_text:
				intent = new Intent(this, myCalculator.class);
				intent.putExtra("name", "money");
				intent.putExtra("money", main_top_monthlyMoney_text.getText().toString());
				startActivityForResult(intent, CALCULATOR_REQUEST_CODE);
				break;
			case R.id.main_top_monthlySave_text:
				intent = new Intent(this, myCalculator.class);
				intent.putExtra("name", "save");
				intent.putExtra("money", main_top_monthlySave_text.getText().toString());
				startActivityForResult(intent, CALCULATOR_REQUEST_CODE);
				break;
			case R.id.main_top_monthlySpend_text:
				intent = new Intent(this, myCalculator.class);
				intent.putExtra("name", "spend");
				intent.putExtra("money", main_top_monthlySpend_text.getText().toString());
				startActivityForResult(intent, CALCULATOR_REQUEST_CODE);
				break;
			case R.id.main_top_monthlyUse_text:
				intent = new Intent(this, myCalculator.class);
				intent.putExtra("name", "start");
				intent.putExtra("money", main_top_monthlyUse_text.getText().toString());
				startActivityForResult(intent, CALCULATOR_REQUEST_CODE);
				break;
		}
		 */
	}

	public void onArrowClicked(View v)
	{
		switch(v.getId())
		{
			case R.id.main_left_arrow:
				Log.i(TAG, "왼쪽 화살표");
				changeMainView(0);
				break;
			case R.id.main_right_arrow:
				Log.i(TAG, "오른쪽 화살표");
				changeMainView(1);
				break;
		}
	}

	private void changeMainView(int i)
	{
		if(main_middle_date_title.getText().equals("월별"))
		{
			if(i == 0)
			{
				main_middle_date_title.setText("일별");
				view_showing = DAILY;
				initDayView();
			}
			else
			{
				main_middle_date_title.setText("주별");
				view_showing = WEEKLY;
				initWeekView();
			}
		}
		else if(main_middle_date_title.getText().equals("주별"))
		{
			if(i == 0)
			{
				main_middle_date_title.setText("월별");
				view_showing = MONTHLY;
				initMonthView();
			}
			else
			{
				main_middle_date_title.setText("일별");
				view_showing = DAILY;
				initDayView();
			}
		}
		else
		{
			if(i == 0)
			{
				main_middle_date_title.setText("주별");
				view_showing = WEEKLY;
				initWeekView();
			}
			else
			{
				main_middle_date_title.setText("월별");
				view_showing = MONTHLY;
				initMonthView();
			}
		}
	}

	public void onMenuClicked(View v)
	{
		switch(v.getId())
		{
			case R.id.main_menu_input:
				Log.i(TAG, "input 버튼 눌림");
				intent = new Intent(this, InputActivity.class);
				intent.putExtra("year", year);
				intent.putExtra("month", month);
				intent.putExtra("day", day);
				intent.putExtra("dayOfWeek", dayOfWeek);
				startActivityForResult(intent, INPUT_REQUEST_CODE);
				break;
			case R.id.main_menu_calendar:
				Log.i(TAG, "input 버튼 눌림");
				intent = new Intent(this, CalendarActivity.class);
				startActivityForResult(intent, CALENDAR_REQUEST_CODE);
				break;
			case R.id.main_menu_history:
				Log.i(TAG, "input 버튼 눌림");
				intent = new Intent(this, HistoryActivity.class);
				startActivityForResult(intent, HISTORY_REQUEST_CODE);
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch(requestCode)
		{
			case INPUT_REQUEST_CODE:
				dbManager.show_items_table();
				switch(resultCode)
				{
					case RESULT_CODE_INSERT:
						Log.i(TAG, "input 갔다옴 ---------------------");
						Log.i(TAG, "" + data.getStringExtra("input"));
						Log.i(TAG, "" + data.getIntExtra("category", -1));
						Log.i(TAG, "" + data.getStringExtra("item"));
						Log.i(TAG, "" + data.getIntExtra("year",0) + " " + data.getIntExtra("month",0) + " " + data.getIntExtra("day",0));
						Log.i(TAG, "" + data.getIntExtra("amount", 0));
						Log.i(TAG, "" + data.getStringExtra("contents"));
						Log.i(TAG, "" + data.getStringExtra("duration"));
						Log.i(TAG, "--------------------------------");
						dbManager.insertToHistory(data.getIntExtra("category", -1), data.getStringExtra("item"), data.getIntExtra("year",0), data.getIntExtra("month",0), data.getIntExtra("day",0), data.getIntExtra("amount", 0), data.getStringExtra("contents"), data.getIntExtra("duration", 0));
						switch(view_showing)
						{
							case DAILY:
								initDayView();
								break;
							case WEEKLY:
								initWeekView();
								break;
							case MONTHLY:
								initMonthView();
								break;
						}
						break;
				}
				break;
			case CALENDAR_REQUEST_CODE:
				Log.i(TAG, "달력 갔다옴 " + data.getStringExtra("calendar"));
				break;
			case HISTORY_REQUEST_CODE:
				Log.i(TAG, "내역 갔다옴 " + data.getStringExtra("history"));
				break;
			case MONTHLY_REQUEST_CODE:
				Log.i(TAG, "내역 갔다옴 " + data.getStringExtra("result"));
				break;
			case CALCULATOR_REQUEST_CODE:
				switch(resultCode)
				{
					case RESULT_CODE_CANCEL:
						//main_top_monthlyUse_text.setText(number + " 원");
						break;
					case RESULT_CODE_INSERT:
						number = data.getLongExtra("number", 0);
						Log.i(TAG, "계산기 갔다옴 " + number);
						TextView text = findViewById(touchedViewId);
						ContentValues contentValues = new ContentValues();
						switch(data.getStringExtra("result"))
						{
							case "money":
								contentValues.put("money", number);
								money = (int)number;
								main_top_monthlyMoney_text.setText(money + " 원");
								break;
							case "save":
								contentValues.put("save", number);
								save = (int)number;
								main_top_monthlySave_text.setText(save + " 원");
								break;
							case "spend":
								contentValues.put("spend", number);
								spend = (int)number;
								main_top_monthlySpend_text.setText(spend + " 원");
								break;
							case "start":
								contentValues.put("start", number);
								start = (int)number;
								main_top_monthlyUse_text.setText(start + " 원");
								break;
						}
						start = money - save - spend;
						main_top_monthlyUse_text.setText(start + " 원");
						main_middle_last_text.setText(start + "");//삭제
						contentValues.put("start", start);
						dbManager.updateRaw(TABLE_INFO, contentValues, year, month);
						switch(view_showing)
						{
							case DAILY:
								initDayView();
								break;
							case WEEKLY:
								initWeekView();
								break;
							case MONTHLY:
								initMonthView();
								break;
						}
						break;
				}
				dbManager.show_info_table();
				break;
		}
	}

	@Override
	protected void onPause()
	{
		if(mAdView != null)
		{
			mAdView.pause();
		}
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if(mAdView != null)
		{
			mAdView.resume();
		}
	}

	@Override
	protected void onDestroy()
	{
		if(mAdView != null)
		{
			mAdView.destroy();
		}
		super.onDestroy();
	}
}
