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

public class MainActivity extends AppCompatActivity
{
	static final private String TAG = "main";
	//광고
	private AdView mAdView;
	private FrameLayout mAdLayout;

	//좌우 화살표
	private TextView arrowL;
	private TextView arrowR;

	//메인 화면 뷰
	private TextView bottom_month_text;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Mobile Ads SDK 초기화
		initAd();
		initView();
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
		arrowL = findViewById(R.id.main_bottom_left_arrow);
		arrowR = findViewById(R.id.main_bottom_right_arrow);

		View.OnClickListener Listener = new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				switch(v.getId())
				{
					case R.id.main_bottom_left_arrow:
						Log.i(TAG, "왼쪽 화살표");
						changeMainView(0);
						break;
					case R.id.main_bottom_right_arrow:
						Log.i(TAG, "오른쪽 화살표");
						changeMainView(1);
						break;
				}
			}
		};
		arrowL.setOnClickListener(Listener);
		arrowR.setOnClickListener(Listener);

		bottom_month_text = findViewById(R.id.main_bottom_month_text);
	}

	private void changeMainView(int i)
	{
		if(bottom_month_text.getText().equals("월별"))
		{
			if(i == 0)
			{
				bottom_month_text.setText("일별");
			}
			else
			{
				bottom_month_text.setText("주별");
			}
		}
		else if(bottom_month_text.getText().equals("주별"))
		{
			if(i == 0)
			{
				bottom_month_text.setText("월별");
			}
			else
			{
				bottom_month_text.setText("일별");
			}
		}
		else
		{
			if(i == 0)
			{
				bottom_month_text.setText("주별");
			}
			else
			{
				bottom_month_text.setText("월별");
			}
		}
	}

	protected void clickHandler(View v)
	{
		switch(v.getId())
		{
			case R.id.main_bottom_left_arrow:
				Log.i(TAG, "왼쪽 화살표");
				break;
			case R.id.main_bottom_right_arrow:
				Log.i(TAG, "오른쪽 화살표");
				break;
		}
	}

	public void onClickMenuButton(View v)
	{
		switch(v.getId())
		{
			case R.id.main_menu_input:
				Log.i(TAG, "input 버튼 눌림");
				Intent intent = new Intent(this, InputActivity.class);
				startActivity(intent);
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
		super.onDestroy();
		if(mAdView != null)
		{
			mAdView.destroy();
		}
		super.onDestroy();
	}
}
