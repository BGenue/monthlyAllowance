package GenueProject.monthlyallowance;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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

	static final private int CATEGORY_SAVEP = 2;
	static final private int CATEGORY_SAVEM = 3;

	static final private String TABLE_CATEGORY = "Category";
	static final private String TABLE_ITEMS = "Item";
	static final private String TABLE_DATE = "Date";
	static final private String TABLE_HISTORY = "History";

	String TAG = "category";
	RecyclerView recyclerView = null;
	CategoryAdapter categoryAdapter = null;
	ArrayList<CategoryItem> list = new ArrayList<CategoryItem>();

	AllowanceDatabaseManager dbManager;

	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//타이틀 없애기
		setContentView(R.layout.activity_category);

		Intent intent = getIntent();
		int category = intent.getIntExtra("category", -1);
		if(category == CATEGORY_SAVEM)
		{
			category = CATEGORY_SAVEP;
		}
		Log.i(TAG, "인텐트 받았엉 " + category);
		setRecyclerview();

		//db에서 필요한 테이블 받아와
		dbManager = AllowanceDatabaseManager.getInstance(this);
		String sql = 	"SELECT DISTINCT * " +
						"FROM " + TABLE_CATEGORY + ", " + TABLE_ITEMS + " " +
						"WHERE "+ TABLE_CATEGORY + ".c_id=" + TABLE_ITEMS + ".c_id" + " " +
						"AND " + TABLE_CATEGORY + ".c_id=" + category;
		Cursor c = dbManager.rawQuery(sql, null);
		Log.i(TAG, " 행 : " + c.getCount() + " 열 : " + c.getColumnCount());
		while(c.moveToNext())
		{
			if(c != null)
			{
				Log.i(TAG, c.getColumnName(0) + " | " + c.getColumnName(1) + " | " + c.getColumnName(2) + " | " + c.getColumnName(3) + " | " + c.getColumnName(4));
				Log.i(TAG, c.getInt(0) + " | " + c.getString(1) + " | " + c.getInt(2) + " | " + c.getString(3) + " | " + c.getInt(4));
				addItem(c.getString(3));
			}
		}
	}

	private void setRecyclerview()
	{
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

		//addItem("점심");
		//addItem("커피");
		//addItem("저녁");

		//categoryAdapter.notifyDataSetChanged();
	}

	public void addItem(String name)
	{
		CategoryItem item = new CategoryItem(name);
		list.add(item);
		categoryAdapter.notifyDataSetChanged();
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
