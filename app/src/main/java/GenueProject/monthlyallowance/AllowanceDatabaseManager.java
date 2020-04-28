package GenueProject.monthlyallowance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class AllowanceDatabaseManager extends SQLiteOpenHelper
{
	static final private String TAG = "myMain";
	static final private String DB_ALLOWANCE = "Allowance.db";

	static final private String TABLE_CATEGORY = "Category";
	static final private String TABLE_ITEMS = "Item";
	static final private String TABLE_DATE = "Date";
	static final private String TABLE_HISTORY = "History";

	static final private int CATEGORY_SPEND = 0;
	static final private int CATEGORY_EARN = 1;
	static final private int CATEGORY_SAVE = 2;

	static private int DB_VERSION = 1;

	private static AllowanceDatabaseManager allowanceDatabaseManager = null;
	private SQLiteDatabase DB = null;

	public static AllowanceDatabaseManager getInstance(Context context)
	{
		Log.i(TAG, "AllowanceDatabaseManager() getInstance");
		if(allowanceDatabaseManager == null)
		{
			allowanceDatabaseManager = new AllowanceDatabaseManager(context, DB_ALLOWANCE, null, DB_VERSION);
		}
		return allowanceDatabaseManager;
	}


	private AllowanceDatabaseManager(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version)
	{
		super(context, name, factory, version);
		Log.i(TAG, "AllowanceDatabaseManager()");
		DB = this.getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL("PRAGMA foreign_keys=ON");//외부키 활성화

		Log.i(TAG, "onCreate()");
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORY +
				"(" +
				"c_id INTEGER PRIMARY KEY," +
				"category TEXT" +
				");"
		);

		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ITEMS +
				"(" +
				"i_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"item TEXT," +
				"c_id INTEGER," +
				"FOREIGN KEY(c_id) REFERENCES " + TABLE_CATEGORY + "(c_id)" +
				");"
		);

		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_DATE +
				"(" +
				"d_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"year INTEGER," +
				"month INTEGER," +
				"date INTEGER" +
				");"
		);

		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_HISTORY +
				"(" +
				"d_id INTEGER," +
				"i_id INTEGER," +
				"amount INTEGER," +
				"contents TEXT," +
				"FOREIGN KEY(d_id) REFERENCES " + TABLE_DATE + "(c_id)," +
				"FOREIGN KEY(i_id) REFERENCES " + TABLE_ITEMS + "(c_id)" +
				");"
		);

		initTable(db);
	}

	private void initTable(SQLiteDatabase db)
	{
		//oncreate 때 한번만 사용됨

		//TABLE_CATEGORY
		Log.i(TAG, "initTable()");
		ContentValues addRowValue = new ContentValues();
		addRowValue.put("c_id", 0);
		addRowValue.put("category", "지출");
		db.insert(TABLE_CATEGORY, null, addRowValue);
		addRowValue = new ContentValues();
		addRowValue.put("c_id", 1);
		addRowValue.put("category", "수입");
		db.insert(TABLE_CATEGORY, null, addRowValue);
		addRowValue = new ContentValues();
		addRowValue.put("c_id", 2);
		addRowValue.put("category", "저금");
		db.insert(TABLE_CATEGORY, null, addRowValue);

		//TABLE_ITEMS
		addRowValue = new ContentValues();
		addRowValue.put("item", "점심 밥");
		addRowValue.put("c_id", "0");
		db.insert(TABLE_ITEMS, null, addRowValue);
		addRowValue = new ContentValues();
		addRowValue.put("item", "저녁 밥");
		addRowValue.put("c_id", "0");
		db.insert(TABLE_ITEMS, null, addRowValue);
		addRowValue = new ContentValues();
		addRowValue.put("item", "커피");
		addRowValue.put("c_id", "0");
		db.insert(TABLE_ITEMS, null, addRowValue);
		addRowValue = new ContentValues();
		addRowValue.put("item", "용돈");
		addRowValue.put("c_id", "1");
		db.insert(TABLE_ITEMS, null, addRowValue);
	}

	//생성할 때 버전을 높으면
	//낮은 버전의 db는 업그레이드 시켜줘야해
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		//현재는 초기화만 해줌.
		Log.i(TAG, "onUpgrade()");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY + ";");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS + ";");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATE + ";");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY + ";");
		onCreate(db);
	}

	public void show_1()
	{
		Cursor c = DB.rawQuery("SELECT DISTINCT * FROM " + TABLE_CATEGORY + ", " + TABLE_ITEMS + " WHERE " + TABLE_CATEGORY + ".c_id=" + TABLE_ITEMS + ".c_id", null);
		Log.i(TAG, "show_1 결과  행 갯수 : " + c.getCount() + " 열 갯수 : " + c.getColumnCount());
		while(c.moveToNext())
		{
			if(c != null)
			{
				Log.i(TAG, " show_1 내용 : " + c.getColumnName(0) + " | " + c.getColumnName(1) + " | "  + c.getColumnName(2) + " | "  + c.getColumnName(3) + " | " + c.getColumnName(4) + " | ");
				Log.i(TAG, " show_1 내용 : " + c.getInt(0) + " | " + c.getString(1) + " | "  + c.getInt(2) + " | "  + c.getString(3) + " | " + c.getInt(4) + " | ");
			}
		}
	}

	public void show()//삭제
	{
		allowanceDatabaseManager.onUpgrade(DB, DB_VERSION, DB_VERSION);
		Cursor c = allowanceDatabaseManager.query(TABLE_CATEGORY, new String[]{"c_id, category"}, null, null, null, null, null);
		while(c.moveToNext())
		{
			if(c != null)
			{
				Log.i(TAG, "TABLE_CATEGORY 내용 : " + c.getInt(0) + " " + c.getString(1));
			}
		}
		c = DB.query(TABLE_ITEMS, new String[]{"i_id, item, c_id"}, null, null, null, null, null);
		while(c.moveToNext())
		{
			if(c != null)
			{
				Log.i(TAG, "TABLE_ITEMS 내용 : " + c.getInt(0) + " " + c.getString(1) + " " + c.getInt(2));
			}
		}
	}

	public void insert(int category, String item)
	{
		Log.i("myInput", category + " " + item);
		ContentValues addRowValue = new ContentValues();
		addRowValue.put("item", item);
		addRowValue.put("c_id", category);
		DB.insert(TABLE_ITEMS, null, addRowValue);
	}
	//추가
	private long insertDB(String tableName, ContentValues addRowValue)
		{
		return DB.insert(tableName, null, addRowValue);
	}

	//조회용
	public Cursor query(String tableName, String[] colums, String selection, String[] selectionArgs, String groupBy, String having, String orderby)
	{
		//colums : new String[] {"title", "grade"}
		//selection : category = "액션"
		return DB.query(tableName, colums, selection, selectionArgs, groupBy, having, orderby);
	}

	public Cursor rawQuery(String sql, String[] selectionArgs)
	{
		Log.i("category", "rawQuery");
		return DB.rawQuery(sql, selectionArgs);
	}
}
