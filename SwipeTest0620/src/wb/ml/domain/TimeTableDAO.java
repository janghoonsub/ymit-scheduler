package wb.ml.domain;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class TimeTableDAO extends SQLiteOpenHelper {
	SQLiteDatabase db;
	ContentValues row;
	Context mContext;
	
	public TimeTableDAO(Context context) {
		super(context, "day.db", null, 1);
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS timetable (_id INTEGER PRIMARY KEY ASC , clss TEXT , color INTEGER)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS timetable");
		onCreate(db);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		db.execSQL("CREATE TABLE IF NOT EXISTS timetable (_id INTEGER PRIMARY KEY ASC , clss TEXT , color INTEGER)");
	}
	
	public void insert(TimeTableVO ttvo) {
		db = this.getWritableDatabase();
		row = new ContentValues();
		row.put("clss", ttvo.getClss());
		row.put("color", ttvo.getColor());
		
		db.insert("timetable", null, row);
		db.close();
		this.close();
	}
	
	public ArrayList<String> select() {
		db = this.getWritableDatabase();
		Cursor cursor;
		cursor = db.rawQuery("SELECT clss FROM timetable", null);
		ArrayList<String> result = new ArrayList<String>();
		while(cursor.moveToNext()) {
			result.add(cursor.getString(0));
		}
		cursor.close();
		db.close();
		return result;
	}
	
	public TimeTableVO select2(String clss){
		db = this.getWritableDatabase();
		Cursor cursor;
		TimeTableVO vo= new TimeTableVO();
		cursor = db.rawQuery("SELECT * FROM timetable WHERE clss=\""+clss+"\"", null);
		while(cursor.moveToNext()) {
			vo.setClss(cursor.getString(1));
			vo.setColor(cursor.getInt(2));
		}
		cursor.close();
		db.close();
		return vo;
	}
	
	public void delete(String clss) {
		db = this.getWritableDatabase();
		db.execSQL("DELETE FROM timetable WHERE clss=\""+clss+"\"");
		db.close();
	}
}
