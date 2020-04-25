package GenueProject.monthlyallowance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class InsertActivity extends Activity
{
	String TAG = "insert";
	static final private int RESULT_CODE_SPEND = 0;//삭제
	static final private int RESULT_CODE_EARN = 1;//삭제
	static final private int RESULT_CODE_SAVE = 2;//삭제

	static final private int RESULT_CODE_INSERT = 0;
	static final private int RESULT_CODE_CANCEL = -1;

	private int result_code;
	private Intent toInputIntent;
	private EditText nameText;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//타이틀 없애기
		setContentView(R.layout.activity_insert);

		Intent intent = getIntent();
		String data = intent.getStringExtra("insert");
		Log.i(TAG, "인텐트 받았엉 " + data);

		toInputIntent = new Intent();
		nameText = findViewById(R.id.insert_name);
	}

	public void onRadioBtnClicked(View v)
	{
		switch(v.getId()) {
			case R.id.insert_spend_btn:
				toInputIntent.putExtra("category", "지출");
				result_code = RESULT_CODE_SPEND;//삭제
				break;
			case R.id.insert_earn_btn:
				toInputIntent.putExtra("category", "수입");
				result_code = RESULT_CODE_EARN;//삭제
				break;
			case R.id.insert_save_btn:
				toInputIntent.putExtra("category", "저금");
				result_code = RESULT_CODE_SAVE;//삭제
				break;
		}
	}

	public void onBtnClicked(View v)
	{
		//만일 라디오 버튼이 하나라도 안 눌리면 추가안되고 항목 정하라는 메세지 떠야함
		switch(v.getId())
		{
			case R.id.insert_cancel_btn:
				//취소 버튼 눌리면 그냥 취소된 상태만 돌려보내
				toInputIntent.removeExtra("category");
				toInputIntent.putExtra("insert", "취소");
				setResult(RESULT_CODE_CANCEL, toInputIntent);
				finish();
				break;
			case R.id.insert_ok_btn:
				toInputIntent.putExtra("insert", "확인");
				toInputIntent.putExtra("name", nameText.getText().toString());
				setResult(RESULT_CODE_INSERT, toInputIntent);
				finish();
				break;
		}
	}

	//백버튼 막기
	@Override
	public void onBackPressed()
	{
		toInputIntent.removeExtra("category");
		toInputIntent.putExtra("insert", "취소");
		setResult(RESULT_CODE_CANCEL, toInputIntent);
		finish();
	}
}
