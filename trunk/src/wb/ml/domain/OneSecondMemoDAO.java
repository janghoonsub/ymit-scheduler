package wb.ml.domain;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OneSecondMemoDAO extends SQLiteOpenHelper {
	SQLiteDatabase db;
	ContentValues row;
	Context main;
	
	public OneSecondMemoDAO(Context activity) {
		super(activity,"day.db",null,1);
		main = activity;
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		db.execSQL("CREATE TABLE IF NOT EXISTS onesecondmemo (_id INTEGER PRIMARY KEY ASC , memo TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS dailymemo");
		onCreate(db);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		db.execSQL("CREATE TABLE IF NOT EXISTS onesecondmemo (_id INTEGER PRIMARY KEY ASC , memo TEXT)");
	}
	
	public void insert(String memo) {
		db = this.getWritableDatabase();
		db.execSQL("INSERT INTO onesecondmemo VALUES (null,\""+memo+"\");");
		db.close();
	}
	
	public ArrayList<String> selectall() {
		db = this.getReadableDatabase();
		Cursor cursor;
		cursor = db.rawQuery("SELECT memo FROM onesecondmemo",null);
		ArrayList<String> result = new ArrayList<String>();
		
		while(cursor.moveToNext()){
			result.add(cursor.getString(0));
		}
		db.close();
		return result;
	}
	
	public void deleteall(){
		db = this.getReadableDatabase();
		db.delete("onesecondmemo",null,null);
		db.close();
	}
	
	public void delete(ArrayList<String> memo){
		db = this.getWritableDatabase();
		for(int i=0;i<memo.size();i++) {
			db.execSQL("DELETE FROM onesecondmemo WHERE memo=\""+memo.get(i)+"\"");
		}
		db.close();
	}
}
