package wb.ml.domain;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GoogleEventListDAO extends SQLiteOpenHelper {
	SQLiteDatabase db;
	ContentValues row;
	
	public GoogleEventListDAO(Context context) {
		super(context, "day.db", null, 1);
	}
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS googleeventlist (_id INTEGER PRIMARY KEY ASC , id TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS googleeventlist");
		onCreate(db);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		db.execSQL("CREATE TABLE IF NOT EXISTS googleeventlist (_id INTEGER PRIMARY KEY ASC , id TEXT)");
	}
	
	public ArrayList<String> search() {
		db = this.getWritableDatabase();
		Cursor cursor;
		cursor = db.rawQuery("SELECT id FROM googleeventlist", null);
		ArrayList<String> result = new ArrayList<String>();
		while(cursor.moveToNext()) {
			result.add(cursor.getString(0));
		}
		cursor.close();
		db.close();
		return result;
	}
	
	public void insert(String id) {
		db = this.getWritableDatabase();
		row = new ContentValues();
		row.put("id", id);
		db.insert("googleeventlist", null, row);
		row.clear();
		db.close();
		this.close();
	}

}
