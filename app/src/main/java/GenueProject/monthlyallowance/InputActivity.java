package GenueProject.monthlyallowance;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class InputActivity extends AppCompatActivity
{
	String TAG = "myInput";

	ArrayList arrayList;
	InputMethodManager inputMethodManager;
	EditText empty;
	Spinner s;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input);
		arrayList = new ArrayList<>();
		arrayList.add("철수");
		arrayList.add("영희");
		arrayList.add("녹지");
		arrayList.add("치치");
		arrayList.add("용병");
		arrayList.add("입력");
		inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);//키보드
		empty = findViewById(R.id.empty);
		s = findViewById(R.id.test_spinner);
		ArrayAdapter arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList);
		s.setAdapter(arrayAdapter);
		//inputMethodManager.hideSoftInputFromWindow(empty.getWindowToken(), 0);//키보드 내려
		s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				Toast.makeText(getApplicationContext(),arrayList.get(position)+"가 선택되었습니다.",
						Toast.LENGTH_SHORT).show();
				if(arrayList.get(position).toString().equals("입력"))
				{
					Log.i(TAG, "입력임");
					inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);//키보드 보여줘
				}
				else
				{
					Log.i(TAG, "입력이 아님");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
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
}
