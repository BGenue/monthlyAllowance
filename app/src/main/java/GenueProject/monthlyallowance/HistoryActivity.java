package GenueProject.monthlyallowance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity
{
	static final private int RESULT_CODE_CANCEL = -1;

	private LayoutInflater layoutInflater;
	private FrameLayout frameLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//타이틀 없애기
		setContentView(R.layout.activity_history);

		//디폴트로 일자별 화면 보여줘
		setView();

		//자동으로 오늘 날짜 찍어줌
	}

	public void onRadioClicked(View v)
	{
		switch(v.getId())
		{
			case R.id.history_day_btn:
				changeView(0);
				break;
			case R.id.history_spend_btn:
				changeView(1);
				break;
			case R.id.history_earn_btn:
				changeView(2);
				break;
			case R.id.history_save_btn:
				changeView(3);
				break;
		}
	}

	private void setView()
	{
		layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		frameLayout = findViewById(R.id.history_body);
		if(frameLayout.getChildCount() > 0)
		{
			//프레임레이아웃에 뷰가 있으면 삭제해
			frameLayout.removeViewAt(0);
		}
		View view = layoutInflater.inflate(R.layout.activity_history_day, frameLayout, false);
		frameLayout.addView(view);
	}

	private void changeView(int index)
	{
		layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		frameLayout = findViewById(R.id.history_body);
		if(frameLayout.getChildCount() > 0)
		{
			//프레임레이아웃에 뷰가 있으면 삭제해
			frameLayout.removeViewAt(0);
		}

		View view = null;
		switch(index)
		{
			case 0:
				view = layoutInflater.inflate(R.layout.activity_history_day, frameLayout, false);
				break;
			case 1:
				view = layoutInflater.inflate(R.layout.activity_history_spend, frameLayout, false);
				break;
			case 2:
				view = layoutInflater.inflate(R.layout.activity_history_earn, frameLayout, false);
				break;
			case 3:
				view = layoutInflater.inflate(R.layout.activity_history_save, frameLayout, false);
				break;
		}

		if(view != null)
		{
			frameLayout.addView(view);
		}
	}

	public void onBtnClick(View v)
	{
		switch(v.getId())
		{
			case R.id.history_cancel_btn:
				Intent intent = new Intent();
				intent.putExtra("history", "취소");
				setResult(RESULT_CODE_CANCEL, intent);
				finish();
				break;
		}
	}

	@Override
	public void onBackPressed()
	{
		Intent intent = new Intent();
		intent.putExtra("history", "취소");
		setResult(RESULT_CODE_CANCEL, intent);
		finish();
	}
}
