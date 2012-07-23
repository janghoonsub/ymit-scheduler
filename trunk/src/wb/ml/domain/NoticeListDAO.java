package wb.ml.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoticeListDAO extends SQLiteOpenHelper {
	SQLiteDatabase db;
	
	public NoticeListDAO(Context context) {
		super(context, "day.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS noticelist (_id INTEGER PRIMARY KEY ASC , date TEXT, desc TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS noticelist");
		onCreate(db);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		db.execSQL("CREATE TABLE IF NOT EXISTS noticelist (_id INTEGER PRIMARY KEY ASC , date TEXT, desc TEXT)");
	}
	
	public void insert(NoticeListVO nvo) {
		db = this.getWritableDatabase();
		db.execSQL("INSERT INTO noticelist VALUES (null,\""+nvo.getDate()+"\",\""+nvo.getDesc()+"\");");
		db.close();
	}
	
	
	public List<Map<String, String>> select() {
		db = this.getWritableDatabase();
		Cursor cursor;
		cursor = db.rawQuery("SELECT date, desc FROM noticelist", null);
		List<Map<String, String>> dataList = new ArrayList<Map<String,String>>();
		while(cursor.moveToNext()) { 
			Map<String, String> data = new HashMap<String, String>();
			data.put("date", cursor.getString(0));
			data.put("desc", cursor.getString(1));
			
			dataList.add(data);
		}
		cursor.close();
		db.close();
		return dataList;
	}
	
	public void delete(String schedule, String time) {
		db = this.getWritableDatabase();
		db.execSQL("DELETE FROM noticelist WHERE desc=\""+schedule+"\" AND date=\""+time+"\"");
		db.close();
	}
	
	public void clear() {
		db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS noticelist");
		db.close();
	}
	
}
