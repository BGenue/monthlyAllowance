package GenueProject.monthlyallowance;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryActivity extends Activity
{
	String TAG = "category";
	RecyclerView recyclerView = null;
	CategoryAdapter categoryAdapter = null;
	ArrayList<CategoryItem> list = new ArrayList<CategoryItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//타이틀 없애기
		setContentView(R.layout.activity_category);

		Intent intent = getIntent();
		String data = intent.getStringExtra("category");
		Log.i(TAG, "인텐트 받았엉 " + data);


		recyclerView = findViewById(R.id.category_list);

		categoryAdapter = new CategoryAdapter(list);
		recyclerView.setAdapter(categoryAdapter);

		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		addItem("점심");
		addItem("커피");
		addItem("저녁");

		categoryAdapter.notifyDataSetChanged();
	}

	public void addItem(String name)
	{
		CategoryItem item = new CategoryItem(name);
		list.add(item);
	}

	public void categoryOnClick(View v)
	{
		switch(v.getId())
		{
			case R.id.okBtn:
				Intent intent = new Intent();
				intent.putExtra("result", "오케이");
				setResult(1, intent);
				break;
			case R.id.cancelBtn:
				Intent intent1 = new Intent();
				intent1.putExtra("result", "취소");
				setResult(0, intent1);
				break;
		}
		finish();
	}

	//창 밖에 클릭 시 안닫혀
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if(event.getAction() == MotionEvent.ACTION_OUTSIDE)
		{
			return false;
		}
		return true;
	}

	//백버튼 막기
	@Override
	public void onBackPressed()
	{
		return;
	}
}
