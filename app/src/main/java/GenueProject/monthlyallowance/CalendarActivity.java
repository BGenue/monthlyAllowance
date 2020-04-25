package GenueProject.monthlyallowance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

public class CalendarActivity extends AppCompatActivity
{
	static final private int RESULT_CODE_CANCEL = -1;

	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//타이틀 없애기
		setContentView(R.layout.activity_calendar);

		//자동으로 오늘 날짜 찍어줌
	}

	public void onBtnClick(View v)
	{
		switch(v.getId())
		{
			case R.id.calendar_cancel_btn:
				intent = new Intent();
				intent.putExtra("calendar", "취소");
				setResult(RESULT_CODE_CANCEL, intent);
				finish();
				break;
		}
	}

	@Override
	public void onBackPressed()
	{
		intent = new Intent();
		intent.putExtra("calendar", "취소");
		setResult(RESULT_CODE_CANCEL, intent);
		finish();
	}
}
