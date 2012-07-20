package wb.ml.domain;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ExamPlannerDateDAO extends SQLiteOpenHelper {
	SQLiteDatabase db;
	
	public ExamPlannerDateDAO(Context context) {
		super(context, "day.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS examplannerdate (_id INTEGER PRIMARY KEY ASC , date TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS examplannerdate");
		onCreate(db);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		db.execSQL("CREATE TABLE IF NOT EXISTS examplannerdate (_id INTEGER PRIMARY KEY ASC , date TEXT)");
	}

	public void insert(String date) {
		db = this.getWritableDatabase();
		db.execSQL("INSERT INTO examplannerdate VALUES (null,\""+date+"\");");
		db.close();
	}
	
	public String select() {
		db = this.getWritableDatabase();
		Cursor cursor;
		cursor = db.rawQuery("SELECT date FROM examplannerdate", null);
		String result = null;
		while(cursor.moveToNext()) {
			result = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return result;
	}

	public void delete() {
		db = this.getWritableDatabase();
		db.delete("examplannerdate", null, null);
		db.close();
		this.close();	
	}
}
