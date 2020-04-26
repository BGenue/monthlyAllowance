package GenueProject.monthlyallowance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AllowanceDatabase extends SQLiteOpenHelper
{
	static final private String DB_ALLOWANCE = "Allowance.db";
	static final private String TABLE_ID = "Id";
	static final private String TABLE_TOTAL = "Total";
	static final private String TABLE_EARN_CATEGORY = "Earn";
	static final private String TABLE_SPEND_CATEGORY = "Spend";
	static final private String TABLE_SAVE_CATEGIRY = "Save";
	static private int DB_VERSION = 1;

	Context context = null;

	private static AllowanceDatabase dbManager = null;
	private SQLiteDatabase db = null;

	public static AllowanceDatabase getInstance(Context context)
	{
		if(dbManager == null)
		{
			dbManager = new AllowanceDatabase(context, DB_ALLOWANCE, null, DB_VERSION);
		}
		return dbManager;
	}

	private AllowanceDatabase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version)
	{
		super(context, name, factory, version);
		this.context = context;
		db = this.context.openOrCreateDatabase(DB_ALLOWANCE, context.MODE_PRIVATE, null);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ID +
				"(" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"category TEXT" +
				");"
		);

		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_TOTAL +
				"(" +
				"year INTEGER," +
				"month INTEGER," +
				"day INTEGER," +
				"category TEXT," +
				"title TEXT," +
				"price INTEGER," +
				"contents TEXT" +
				");"
		);

		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_EARN_CATEGORY +
				"(" +
				"id INTEGER," +
				"title TEXT" +
				");"
		);

		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SPEND_CATEGORY +
				"(" +
				"id INTEGER," +
				"title TEXT" +
				");"
		);

		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SAVE_CATEGIRY +
				"(" +
				"id INTEGER," +
				"title TEXT" +
				");"
		);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE " + TABLE_TOTAL + ";");
		db.execSQL("DROP TABLE " + TABLE_ID + ";");
		db.execSQL("DROP TABLE " + TABLE_EARN_CATEGORY + ";");
		db.execSQL("DROP TABLE " + TABLE_SPEND_CATEGORY + ";");
		db.execSQL("DROP TABLE " + TABLE_SAVE_CATEGIRY + ";");
		onCreate(db);
	}
}
