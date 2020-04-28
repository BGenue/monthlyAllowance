package GenueProject.monthlyallowance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioButton;

public class InsertActivity extends Activity
{
	String TAG = "insert";

	static final private int RESULT_CODE_INSERT = 0;
	static final private int RESULT_CODE_CANCEL = -1;

	static final private int CATEGORY_SPEND = 0;
	static final private int CATEGORY_EARN = 1;
	static final private int CATEGORY_SAVEP = 2;
	static final private int CATEGORY_SAVEM = 3;

	static final private int NEGATIVE = -1;

	private Intent toInputIntent;
	private EditText nameText;

	private int category;
	private String item;

	AllowanceDatabaseManager dbManager;

	private RadioButton radioButton;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//타이틀 없애기
		setContentView(R.layout.activity_insert);

		Intent intent = getIntent();
		category = intent.getIntExtra("category", NEGATIVE);
		Log.i(TAG, "인텐트 받았엉 " + category);

		if(category == CATEGORY_SPEND)
		{
			radioButton = findViewById(R.id.insert_spend_btn);
		}
		else if(category == CATEGORY_EARN)
		{
			radioButton = findViewById(R.id.insert_earn_btn);
		}
		else if(category == CATEGORY_SAVEP || category == CATEGORY_SAVEM)
		{
			radioButton = findViewById(R.id.insert_save_btn);
		}
		radioButton.setChecked(true);

		dbManager = AllowanceDatabaseManager.getInstance(this);

		toInputIntent = new Intent();
		nameText = findViewById(R.id.insert_name);
	}

	public void onRadioBtnClicked(View v)
	{
		switch(v.getId()) {
			case R.id.insert_spend_btn:
				category = CATEGORY_SPEND;
				break;
			case R.id.insert_earn_btn:
				category = CATEGORY_EARN;
				break;
			case R.id.insert_save_btn:
				category = CATEGORY_SAVEP;//디폴트는 P
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
				toInputIntent.putExtra("category", category);
				item = nameText.getText().toString();
				if(item.length() != 0)
				{
					toInputIntent.putExtra("item", item);
					setResult(RESULT_CODE_INSERT, toInputIntent);
					finish();
				}
				else
				{
					Log.i(TAG, "입력해햐해");
				}
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
