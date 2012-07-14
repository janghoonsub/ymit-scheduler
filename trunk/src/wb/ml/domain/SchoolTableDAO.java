package wb.ml.domain;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SchoolTableDAO extends SQLiteOpenHelper {
	SQLiteDatabase db;
	ContentValues row;
	
	public SchoolTableDAO(Context context) {
		super(context, "day.db", null, 1);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS schooltable (_id INTEGER PRIMARY KEY ASC , subject TEXT, color INTEGER, position INTEGER)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS schooltable");
		onCreate(db);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		db.execSQL("CREATE TABLE IF NOT EXISTS schooltable (_id INTEGER PRIMARY KEY ASC , subject TEXT, color INTEGER, position INTEGER)");
	}
	
	public void insert(SchoolTableVO stvo) {
		db = this.getWritableDatabase();
		row = new ContentValues();
		row.put("subject", stvo.getSubject());
		row.put("color", stvo.getColor());
		row.put("position", stvo.getPosition());
		
		db.insert("schooltable", null, row);
		db.close();
		this.close();
	}
	
	public ArrayList<SchoolTableVO> select() {
		db = this.getWritableDatabase();
		Cursor cursor;
		cursor = db.rawQuery("SELECT * FROM schooltable", null);
		ArrayList<SchoolTableVO> result = new ArrayList<SchoolTableVO>();
		SchoolTableVO stvo = null;
		while(cursor.moveToNext()) {
			stvo = new SchoolTableVO();
			stvo.setSubject(cursor.getString(1));
			stvo.setColor(cursor.getInt(2));
			stvo.setPosition(cursor.getInt(3));
			result.add(stvo);
		}
		cursor.close();
		db.close();
		return result;
	}
	
	public void delete() {
		db = this.getWritableDatabase();
		db.delete("schooltable", null, null);
		db.close();
		this.close();
	}
}
