package wb.ml.domain;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ExamPlannerDAO extends SQLiteOpenHelper {
	SQLiteDatabase db;
	ContentValues row;
	
	public ExamPlannerDAO(Context context) {
		super(context, "day.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS examplanner (position INTEGER PRIMARY KEY ASC , subject TEXT, cover TEXT, before TEXT, goal TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS googleemail");
		onCreate(db);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		db.execSQL("CREATE TABLE IF NOT EXISTS examplanner (position INTEGER PRIMARY KEY ASC , subject TEXT, cover TEXT, before TEXT, goal TEXT)");
	}
	
	public void insert(ExamPlannerVO evo) {
		db = this.getWritableDatabase();
		row = new ContentValues();
		
		row.put("position", evo.getPosition());
		row.put("subject", evo.getSuject());
		row.put("cover", evo.getCover());
		row.put("before", evo.getBefore());
		row.put("goal", evo.getGoal());
		db.insert("examplanner", null, row);
		db.close();
	}
	
	public void update(ExamPlannerVO evo) {
		db = this.getWritableDatabase();
		db.execSQL("UPDATE examplanner SET subject=\""+evo.getSuject()+"\" ," +
				" cover=\""+evo.getCover()+"\" ," +
				" before=\""+evo.getBefore()+"\" ," +
				" goal=\""+evo.getGoal()+"\" WHERE position ="+evo.getPosition()+"");
		db.close();
	}
	
	public ExamPlannerVO select(int position) {
		db = this.getWritableDatabase();
		Cursor cursor;
		cursor = db.rawQuery("SELECT * FROM examplanner WHERE position="+position+"", null);
		ExamPlannerVO evo = null;
		while(cursor.moveToNext()) {
			evo = new ExamPlannerVO();
			evo.setPosition(cursor.getInt(0));
			evo.setSuject(cursor.getString(1));
			evo.setCover(cursor.getString(2));
			evo.setBefore(cursor.getString(3));
			evo.setGoal(cursor.getString(4));
		}
		cursor.close();
		db.close();
		return evo;
	}
	
	public void delete(int position) {
		db = this.getWritableDatabase();
		db.execSQL("DELETE FROM examplanner WHERE position="+position+"");
		db.close();
	}
	
	public boolean exist(int position) {
		boolean result = false;
		db = this.getWritableDatabase();
		Cursor cursor;
		cursor = db.rawQuery("SELECT * FROM examplanner WHERE position="+position+"", null);
		if(cursor.getCount() == 0) {
			result = false;
		} else {
			result = true;
		}
		cursor.close();
		db.close();
		return result;
	}
}
