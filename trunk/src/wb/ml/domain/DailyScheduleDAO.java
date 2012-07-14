package wb.ml.domain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DailyScheduleDAO extends SQLiteOpenHelper {
	SQLiteDatabase db;
	ContentValues row;
	Context main;
	
	public DailyScheduleDAO(Context activity) {
		super(activity, "day.db", null, 1);
		main = activity;
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS  dailylist (_id INTEGER PRIMARY KEY ASC , date TEXT , schedule TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS dailylist");
		onCreate(db);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		db.execSQL("CREATE TABLE IF NOT EXISTS  dailylist (_id INTEGER PRIMARY KEY ASC , date TEXT , schedule TEXT)");
	}
	
	public void insert(DailyScheduleVO dsvo) {
		db = this.getWritableDatabase();
		row = new ContentValues();
		row.put("date", dsvo.getDate());
		row.put("schedule", dsvo.getSchedule());
		
		db.insert("dailylist", null, row);
		db.close();
		this.close();
	}
	
	public ArrayList<DailyScheduleVO> select(String date) {
		db = this.getReadableDatabase();
		Cursor cursor;
		cursor = db.rawQuery("SELECT date, schedule FROM dailylist", null);
		ArrayList<DailyScheduleVO> result = new ArrayList<DailyScheduleVO>();
		DailyScheduleVO dailyvo;
		while(cursor.moveToNext()) {
			if(cursor.getString(0).indexOf(date) >= 0) {
				String[] d = cursor.getString(0).split(" ");
				dailyvo = new DailyScheduleVO();
				dailyvo.setDate(d[1]);
				dailyvo.setSchedule(cursor.getString(1));
				result.add(dailyvo);
			}
		}
		cursor.close();
		db.close();
		return result;
	}
	
	
	public ArrayList<DailyScheduleVO> weekSelect(Calendar[] weekDate) {
		ArrayList<DailyScheduleVO> result = new ArrayList<DailyScheduleVO>();
		DailyScheduleVO dailyvo;
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
		for(int i=0; i < weekDate.length ; i++) {
			ArrayList<DailyScheduleVO> dsvo = select(s.format(weekDate[i].getTime()));
			String temp = "";
			for(int j=0 ; j < dsvo.size() ; j++) {
				temp = temp + dsvo.get(j).getDate() + " " + dsvo.get(j).getSchedule() + "\n";
			}
			dailyvo = new DailyScheduleVO();
			dailyvo.setDate(s.format(weekDate[i].getTime()));
			dailyvo.setSchedule(temp);
			result.add(dailyvo);
		}
		return result;
	}
	
	public ArrayList<String> monthSelect(String month) {
		db = this.getReadableDatabase();
		Cursor cursor;
		cursor = db.rawQuery("SELECT date FROM dailylist", null);
		ArrayList<String> result = new ArrayList<String>();
		while(cursor.moveToNext()) {
			if(cursor.getString(0).indexOf(month) >= 0) {
				String[] d = cursor.getString(0).split(" ");
				String[] d1 = d[0].split("-");
				result.add(d1[2]);
			}
		}
		cursor.close();
		db.close();
		return result;
	}
	
	
	public void delete(String time, String schedule) {
		db = this.getWritableDatabase();
		
		//db.delete("dailylist", schedule,null );
		db.execSQL("DELETE FROM dailylist WHERE schedule=\""+schedule+"\" AND date=\""+time+"\"");
		db.close();
	}
	
	public int getId(String time, String schedule) {
		db = this.getReadableDatabase();
		Cursor cursor;
		int result = -1;
		cursor = db.rawQuery("SELECT _id FROM dailylist WHERE schedule=\""+schedule+"\" AND date=\""+time+"\"", null);
		while(cursor.moveToNext()) {
			result = cursor.getInt(0);
		}
		cursor.close();
		db.close();
		return result;
	}
	
	public void update(int id,DailyScheduleVO dvo) {
		db = this.getWritableDatabase();
		db.execSQL("UPDATE dailylist SET schedule=\""+dvo.getSchedule()+"\" , date=\""+dvo.getDate()+"\" WHERE _id =\""+id+"\"");
		db.close();
	}
	
}
