package GenueProject.monthlyallowance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AllowanceDatabase extends SQLiteOpenHelper
{
	static final String DB_ALLOWANCE = "Allowance.db";
	static final String TABLE_ID = "Id";
	static final String TABLE_TOTAL = "Total";
	static final String TABLE_EARN_CATEGORY = "Earn";
	static final String TABLE_SPEND_CATEGORY = "Spend";
	static final String TABLE_SAVE_CATEGIRY = "Save";

	Context context = null;

	private static AllowanceDatabase dbManager = null;
	private SQLiteDatabase db = null;

	public static AllowanceDatabase getInstance(Context context)
	{
		if(dbManager == null)
		{
			dbManager = new AllowanceDatabase(context);
		}
		return dbManager;
	}

	private AllowanceDatabase(Context context)
	{
		super(context);
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
