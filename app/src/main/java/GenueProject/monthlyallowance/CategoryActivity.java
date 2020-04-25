package GenueProject.monthlyallowance;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryActivity extends Activity
{
	static final private int RESULT_CODE_CANCEL = -1;
	static final private int RESULT_CODE_INSERT = 0;
	static final private int RESULT_CODE_ITEM = 1;

	String TAG = "category";
	RecyclerView recyclerView = null;
	CategoryAdapter categoryAdapter = null;
	ArrayList<CategoryItem> list = new ArrayList<CategoryItem>();

	Intent intent;

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

		categoryAdapter = new CategoryAdapter(list, this);
		categoryAdapter.setOnItemClickListener(new OnCategoryItemClickListener()
		{
			@Override
			public void onItemClick(CategoryAdapter.ViewHolder holder, View view, int position, SparseBooleanArray selectedItems)
			{
				Log.i(TAG, "클릭된 카테고리 position : " + position);
				//클릭되면 파랗게 하고
				//액티비티 꺼
				view.setBackgroundColor(Color.BLUE);
				String name = list.get(position).getName();
				Log.i(TAG, "선택했어 : " + list.get(position).getName());
				Intent toPut = new Intent();
				toPut.putExtra("result", name);
				setResult(RESULT_CODE_ITEM, toPut);
				finish();
			}
		});
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
			case R.id.cancelBtn:
				intent = new Intent();
				intent.putExtra("result", "취소");
				setResult(RESULT_CODE_CANCEL, intent);
				break;
			case R.id.insertBtn:
				intent = new Intent();
				intent.putExtra("result", "오케이");
				setResult(RESULT_CODE_INSERT, intent);
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

	@Override
	public void onBackPressed()
	{
		intent = new Intent();
		intent.putExtra("result", "취소");
		setResult(RESULT_CODE_CANCEL, intent);
		finish();
	}

	@Override
	protected void onStop()
	{
		Log.i(TAG, "category onstop()");
		super.onStop();
	}
}
