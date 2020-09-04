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

	static final private String TABLE_CATEGORY = "Category";//지출, 수입, 저금
	static final private String TABLE_ITEMS = "Item";//각 카테고리별 항목들
	static final private String TABLE_DATE = "Date";//기입한 날짜. 일일 날짜로 따져
	static final private String TABLE_HISTORY = "History";//전체 내역
	static final private String TABLE_INFO = "Info";//내 정보
	static final private String TABLE_SAVE = "Save";//저금 정보

	static final private int CATEGORY_SPEND = 0;
	static final private int CATEGORY_EARN = 1;
	static final private int CATEGORY_SAVE = 2;
	static final private int CATEGORY_SAVEP = 3;
	static final private int CATEGORY_SAVEM = 4;

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
				"day INTEGER," +
				"week INTEGER" +
				");"
		);

		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_HISTORY +
				"(" +
				"h_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"d_id INTEGER," +
				"i_id INTEGER," +
				"amount INTEGER," +
				"contents TEXT," +
				"duration INTEGER," +
				"FOREIGN KEY(d_id) REFERENCES " + TABLE_DATE + "(d_id)," +
				"FOREIGN KEY(i_id) REFERENCES " + TABLE_ITEMS + "(i_id)" +
				");"
		);

		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_INFO +
				"(" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"year INTEGER," +
				"month INTEGER," +
				"money INTEGER DEFAULT 0," +
				"earn INTEGER DEFAULT 0," +
				"save INTEGER DEFAULT 0," +
				"spend INTEGER DEFAULT 0," +
				"start INTEGER DEFAULT 0" +
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
		addRowValue.put("c_id", CATEGORY_SPEND);
		addRowValue.put("category", "지출");
		db.insert(TABLE_CATEGORY, null, addRowValue);
		addRowValue = new ContentValues();
		addRowValue.put("c_id", CATEGORY_EARN);
		addRowValue.put("category", "수입");
		db.insert(TABLE_CATEGORY, null, addRowValue);
		addRowValue = new ContentValues();
		addRowValue.put("c_id", CATEGORY_SAVE);
		addRowValue.put("category", "저금");
		db.insert(TABLE_CATEGORY, null, addRowValue);

		//TABLE_ITEMS
		addRowValue = new ContentValues();
		addRowValue.put("item", "점심 밥");
		addRowValue.put("c_id", CATEGORY_SPEND);
		db.insert(TABLE_ITEMS, null, addRowValue);
		addRowValue = new ContentValues();
		addRowValue.put("item", "저녁 밥");
		addRowValue.put("c_id", CATEGORY_SPEND);
		db.insert(TABLE_ITEMS, null, addRowValue);
		addRowValue = new ContentValues();
		addRowValue.put("item", "커피");
		addRowValue.put("c_id", CATEGORY_SPEND);
		db.insert(TABLE_ITEMS, null, addRowValue);
		addRowValue = new ContentValues();
		addRowValue.put("item", "용돈");
		addRowValue.put("c_id", CATEGORY_EARN);
		db.insert(TABLE_ITEMS, null, addRowValue);
	}

	//생성할 때 버전을 높으면
	//낮은 버전의 db는 업그레이드 시켜줘야해
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		//현재는 초기화만 해줌.
		Log.i(TAG, "onUpgrade()");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY + ";");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS + ";");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY + ";");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATE + ";");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_INFO + ";");
		onCreate(db);
	}

	public void reset()
	{
		Log.i(TAG, "reset()");
		allowanceDatabaseManager.onUpgrade(DB, DB_VERSION, DB_VERSION);
	}

	public void showAll()
	{
		show_category_table();
		show_items_table();
		show_date_table();
		show_history_table();
		show_info_table();
	}

	public void show_category_table()
	{
		Cursor c = allowanceDatabaseManager.query(TABLE_CATEGORY, new String[]{"c_id, category"}, null, null, null, null, null);
		Log.i(TAG, "TABLE_CATEGORY 내용 --------------- ");
		while(c.moveToNext())
		{
			if(c != null)
			{
				Log.i(TAG, c.getInt(0) + " " + c.getString(1));
			}
		}
		Log.i(TAG, "---------------------------------- ");
	}

	public void show_items_table()
	{
		Cursor c = allowanceDatabaseManager.query(TABLE_ITEMS, new String[]{"i_id, item, c_id"}, null, null, null, null, null);
		Log.i(TAG, "TABLE_ITEMS 내용 -------------------");
		while(c.moveToNext())
		{
			if(c != null)
			{
				Log.i(TAG, c.getInt(0) + " " + c.getString(1) + " " + c.getInt(2));
			}
		}
		Log.i(TAG, "---------------------------------- ");
	}

	public void show_date_table()
	{
		Cursor c = allowanceDatabaseManager.query(TABLE_DATE, new String[]{"d_id, year, month, day"}, null, null, null, null, null);
		Log.i(TAG, "TABLE_DATE 내용 -------------------");
		while(c.moveToNext())
		{
			if(c != null)
			{
				Log.i(TAG, c.getInt(0) + " | " + c.getInt(1) + " | " + c.getInt(2) + " | " + c.getInt(3));
			}
		}
		Log.i(TAG, "---------------------------------- ");
	}

	public void show_history_table()
	{
		Cursor c = allowanceDatabaseManager.query(TABLE_HISTORY, new String[]{"h_id, d_id, i_id, amount, contents"}, null, null, null, null, null);
		Log.i(TAG, "TABLE_HISTORY 내용 -----------------");
		while(c.moveToNext())
		{
			if(c != null)
			{
				Log.i(TAG, c.getInt(0) + " | " + c.getInt(1) + " | " + c.getInt(2) + " | " + c.getInt(3) + " | " + c.getString(4));
			}
		}
		Log.i(TAG, "---------------------------------- ");
	}

	public void show_info_table()
	{
		Cursor c = allowanceDatabaseManager.query(TABLE_INFO, new String[]{"id, year, month, money, save, spend, start"}, null, null, null, null, null);
		Log.i(TAG, "TABLE_INFO 내용 -------------------");
		while(c.moveToNext())
		{
			if(c != null)
			{
				Log.i(TAG, c.getInt(0) + " | " + c.getInt(1) + " | " + c.getInt(2) + " | " + c.getInt(3) + " | " + c.getInt(4) + " | " + c.getInt(5) + " | " + c.getInt(6));
			}
		}
		Log.i(TAG, "---------------------------------- ");
	}

	public void insert(int category, String item)
	{
		Log.i("myInput", category + " " + item);
		ContentValues addRawValue = new ContentValues();
		addRawValue.put("item", item);
		addRawValue.put("c_id", category);
		DB.insert(TABLE_ITEMS, null, addRawValue);
	}

	public void insertToItems(String item, int c_id)
	{
		String sql = "SELECT DISTINCT * " +
					"FROM " + TABLE_ITEMS + " " +
					"WHERE item='" + item + "' " +
					"AND c_id=" + c_id;
		show_items_table();
		Cursor c = rawQuery(sql, null);
		if(c.getCount() != 0)
		{
			return;
		}
		ContentValues addRawValue = new ContentValues();
		addRawValue.put("item", item);
		addRawValue.put("c_id", c_id);
		insertRaw(TABLE_ITEMS, addRawValue);
	}

	public void insertToHistory(int c_id, String item, int year, int month, int day, int amount, String contents, int duration)
	{
		int i_id = -1;
		int d_id = -1;
		int money = 0;
		Log.i(TAG, c_id + " " + item + " " + year + " " + month + " " + day + " " + amount + " " + contents + " " + duration);

		String sql = "SELECT DISTINCT i_id " +
				"FROM " + TABLE_ITEMS + " " +
				"WHERE item='" + item + "' " +
				"AND c_id=" + c_id;
		show_items_table();
		Cursor c = rawQuery(sql, null);
		while(c.moveToNext())
		{
			if(c != null)
			{
				Log.i(TAG, "확인 : " + c.getInt(0));
				i_id = c.getInt(0);
			}
		}

		sql = "SELECT DISTINCT d_id " +
				"FROM " + TABLE_DATE + " " +
				"WHERE year=" + year + " " +
				"AND month=" + month + " " +
				"AND day=" + day + " ";
		show_date_table();
		c = rawQuery(sql, null);
		while(c.moveToNext())
		{
			if(c != null)
			{
				Log.i(TAG, "확인 : " + c.getInt(0));
				d_id = c.getInt(0);
			}
		}

		ContentValues addRawValue = new ContentValues();
		addRawValue.put("d_id", d_id);
		addRawValue.put("i_id", i_id);
		addRawValue.put("amount", amount);
		addRawValue.put("contents", contents);
		addRawValue.put("duration", duration);
		long i = insertRaw(TABLE_HISTORY, addRawValue);
		Log.i(TAG, "집어 넣음 : " + i);
		show_history_table();

		//기한이 있으면 info 부분 적용해줘
		if(duration != 0)
		{
			addRawValue = new ContentValues();
			sql = "SELECT DISTINCT save, earn, spend" +
					"FROM " + TABLE_INFO + " " +
					"WHERE year='" + year + "' " +
					"AND month=" + month;
			c = rawQuery(sql, null);

			if(c_id == CATEGORY_SAVEP)
			{
				while(c.moveToNext())
				{
					money = c.getInt(0);
					Log.i(TAG, "info 테이블에 저장된 save 금액 : " + money);
					addRawValue.put("save", money + amount);
				}
			}
			else if(c_id == CATEGORY_SAVEM)
			{
				while(c.moveToNext())
				{
					money = c.getInt(0);
					Log.i(TAG, "info 테이블에 저장된 save 금액 : " + money);
					addRawValue.put("save", money - amount);
				}
			}
			else if(c_id == CATEGORY_EARN)
			{
				while(c.moveToNext())
				{
					money = c.getInt(1);
					Log.i(TAG, "info 테이블에 저장된 save 금액 : " + money);
					addRawValue.put("earn", money + amount);
				}
			}
			else if(c_id == CATEGORY_SPEND)
			{
				while(c.moveToNext())
				{
					money = c.getInt(2);
					Log.i(TAG, "info 테이블에 저장된 save 금액 : " + money);
					addRawValue.put("spend", money + amount);
				}
			}
			show_info_table();
			updateRaw(TABLE_INFO, addRawValue, year, month);
			show_info_table();
		}
	}

	public long insertToDate(int year, int month, int day, int week)
	{
		String sql = 	"SELECT DISTINCT d_id " +
				"FROM " + TABLE_DATE + " " +
				"WHERE year=" + year + " " +
				"AND month=" + month + " " +
				"AND day=" + day;
		Cursor c = rawQuery(sql, null);
		if(c.getCount() != 0)
		{
			return -2;
		}
		else
		{
			ContentValues addRawValue = new ContentValues();
			addRawValue.put("year", year);
			addRawValue.put("month", month);
			addRawValue.put("day", day);
			addRawValue.put("week", week);
			return insertRaw(TABLE_DATE, addRawValue);
		}
	}

	public void insertToInfo(int year, int month, int money, int save, int spend, int start)
	{
		String sql = 	"SELECT DISTINCT id " +
				"FROM " + TABLE_INFO + " " +
				"WHERE year=" + year + " " +
				"AND month=" + month;
		Cursor c = rawQuery(sql, null);
		if(c.getCount() != 0)
		{
			return;
		}
		ContentValues addRawValue = new ContentValues();
		addRawValue.put("year", year);
		addRawValue.put("month", month);
		addRawValue.put("money", money);
		addRawValue.put("save", save);
		addRawValue.put("spend", spend);
		addRawValue.put("start", start);
		insertRaw(TABLE_INFO, addRawValue);
	}

	//추가
	public long insertRaw(String tableName, ContentValues addRawValue)
	{
		Log.i(TAG, "insertRaw");
		return DB.insert(tableName, null, addRawValue);
	}

	//업글
	public int updateRaw(String tableName, ContentValues updateRawValue, int year, int month)
	{
		Log.i(TAG, "updateRaw");
		return DB.update(tableName, updateRawValue, "year=" + year +  " AND month=" + month, null);
	}

	//조회용
	public Cursor query(String tableName, String[] colums, String selection, String[] selectionArgs, String groupBy, String having, String orderby)
	{
		//colums : new String[] {"title", "grade"}
		//selection : category = "액션"
		return DB.query(tableName, colums, selection, selectionArgs, groupBy, having, orderby);
	}

	//조회용
	public Cursor rawQuery(String sql, String[] selectionArgs)
	{
		Log.i("category", "rawQuery");
		return DB.rawQuery(sql, selectionArgs);
	}
}
