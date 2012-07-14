package wb.ml.domain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DailyMemoDAO extends SQLiteOpenHelper {
	SQLiteDatabase db;
	ContentValues row;
	Context main;

	public DailyMemoDAO(Context activity) {
		super(activity, "day.db", null, 1);
		main = activity;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS dailymemo (_id INTEGER PRIMARY KEY ASC , date TEXT , memo TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS dailymemo");
		onCreate(db);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		db.execSQL("CREATE TABLE IF NOT EXISTS dailymemo (_id INTEGER PRIMARY KEY ASC , date TEXT , memo TEXT)");
	}
	
	public void insert(DailyMemoVO dmvo){
		db = this.getWritableDatabase();
		row = new ContentValues();
		row.put("date",dmvo.getDate());
		row.put("memo",dmvo.getMemo());
		
		db.insert("dailymemo", null, row);
		db.close();
		this.close();
	}
	
	public String select(String date) {
		db = this.getReadableDatabase();
		Cursor cursor;
		cursor = db.rawQuery("SELECT memo FROM dailymemo WHERE date=\""+date+"\"", null);
		String result = null;
		while(cursor.moveToNext()) {
			result = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return result;
	}
	
	public void delete(String date) {
		db = this.getWritableDatabase();
		//db.delete("dailymemo", null, null);
		db.execSQL("DELETE FROM dailymemo WHERE date=\""+date+"\"");
		db.close();
	}
		
		
}
