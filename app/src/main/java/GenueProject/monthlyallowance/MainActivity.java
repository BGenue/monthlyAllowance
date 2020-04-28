package GenueProject.monthlyallowance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

	static final private int RESULT_CODE_CANCEL = -1;


	//광고
	private AdView mAdView;
	private FrameLayout mAdLayout;

	//좌우 화살표
	private TextView arrowL;
	private TextView arrowR;

	//메인 화면 뷰
	private TextView top_month_title;
	private TextView main_middle_title;
	private TextView main_middle_text;
	private TextView main_total_spend_text;
	private TextView main_total_earn_text;
	private TextView main_total_save_text;
	private TextView main_last_text;

	//날짜
	private int year;
	private int month;
	private int day;
	private int dayOfWeek;//요일
	private int week;//주

	//db
	static final private String TABLE_CATEGORY = "Category";
	static final private String TABLE_ITEMS = "Item";
	static final private String TABLE_DATE = "Date";
	static final private String TABLE_HISTORY = "History";
	private AllowanceDatabaseManager dbManager;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Mobile Ads SDK 초기화
		initAd();

		initView();

		getTodayDate();

		dbManager = AllowanceDatabaseManager.getInstance(this);
		dbManager.show();//삭제
		dbManager.show_1();
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
	private void initView()
	{
		arrowL = findViewById(R.id.main_left_arrow);
		arrowR = findViewById(R.id.main_right_arrow);

		top_month_title = findViewById(R.id.main_top_month_text);
		main_middle_title = findViewById(R.id.main_middle_title);
		main_middle_text = findViewById(R.id.main_middle_text);
		main_total_spend_text = findViewById(R.id.main_total_spend_text);
		main_total_earn_text = findViewById(R.id.main_total_earn_text);
		main_total_save_text = findViewById(R.id.main_total_save_text);
		main_last_text = findViewById(R.id.main_last_text);
	}

	private void changeMainView(int i)
	{
		if(main_middle_title.getText().equals("월별"))
		{
			if(i == 0)
			{
				main_middle_title.setText("일별");
				main_middle_text.setText(month + "월 " + day + "일");
				main_total_spend_text.setText("일별 지출");
				main_total_earn_text.setText("일별 수입");
				main_total_save_text.setText("일별 저금");
				main_last_text.setText("일별 잔금");
			}
			else
			{
				main_middle_title.setText("주별");
				main_middle_text.setText(month + "월" + week + "주차");
				main_total_spend_text.setText("주별 지출");
				main_total_earn_text.setText("주별 수입");
				main_total_save_text.setText("주별 저금");
				main_last_text.setText("주별 잔금");
			}
		}
		else if(main_middle_title.getText().equals("주별"))
		{
			if(i == 0)
			{
				main_middle_title.setText("월별");
				main_middle_text.setText(month + "월 전체");
				main_total_spend_text.setText("월별 지출");
				main_total_earn_text.setText("월별 수입");
				main_total_save_text.setText("월별 저금");
				main_last_text.setText("월별 잔금");
			}
			else
			{
				main_middle_title.setText("일별");
				main_middle_text.setText(month+"월 " + day + "일");
				main_total_spend_text.setText("일별 지출");
				main_total_earn_text.setText("일별 수입");
				main_total_save_text.setText("일별 저금");
				main_last_text.setText("일별 잔금");
			}
		}
		else
		{
			if(i == 0)
			{
				main_middle_title.setText("주별");
				main_middle_text.setText(month + "월" + week + "주");
				main_total_spend_text.setText("주별 지출");
				main_total_earn_text.setText("주별 수입");
				main_total_save_text.setText("주별 저금");
				main_last_text.setText("주별 잔금");
			}
			else
			{
				main_middle_title.setText("월별");
				main_middle_text.setText(month + "월 전체");
				main_total_spend_text.setText("월별 지출");
				main_total_earn_text.setText("월별 수입");
				main_total_save_text.setText("월별 저금");
				main_last_text.setText("월별 잔금");
			}
		}
	}

	public void clickHandler(View v)
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

	public void onMenuClicked(View v)
	{
		switch(v.getId())
		{
			case R.id.main_menu_input:
				Log.i(TAG, "input 버튼 눌림");
				Intent intent = new Intent(this, InputActivity.class);
				intent.putExtra("year", year);
				intent.putExtra("month", month);
				intent.putExtra("day", day);
				intent.putExtra("dayOfWeek", dayOfWeek);
				startActivityForResult(intent, INPUT_REQUEST_CODE);
				break;
			case R.id.main_menu_calendar:
				Log.i(TAG, "input 버튼 눌림");
				Intent intent1 = new Intent(this, CalendarActivity.class);
				startActivityForResult(intent1, CALENDAR_REQUEST_CODE);
				break;
			case R.id.main_menu_history:
				Log.i(TAG, "input 버튼 눌림");
				Intent intent2 = new Intent(this, HistoryActivity.class);
				startActivityForResult(intent2, HISTORY_REQUEST_CODE);
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch(requestCode)
		{
			case INPUT_REQUEST_CODE:
				Log.i(TAG, "input 갔다옴 " + data.getStringExtra("input"));
				break;
			case CALENDAR_REQUEST_CODE:
				Log.i(TAG, "달력 갔다옴 " + data.getStringExtra("calendar"));
				break;
			case HISTORY_REQUEST_CODE:
				Log.i(TAG, "내역 갔다옴 " + data.getStringExtra("history"));
				break;
		}
	}

	private void getTodayDate()
	{
		Calendar calendar = Calendar.getInstance();//오늘날짜
		year = calendar.get(Calendar.YEAR);//년
		month = calendar.get(Calendar.MONTH)+1;//월
		day = calendar.get(Calendar.DAY_OF_MONTH);//일
		dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);//요일. 일요일 : 1 ~ 토요일 : 7
		week = calendar.get(Calendar.WEEK_OF_MONTH);//이번달의 몇 째주인지

		top_month_title.setText(month + "월 시작");
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
