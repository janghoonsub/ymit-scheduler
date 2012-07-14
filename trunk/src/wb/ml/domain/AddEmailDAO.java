package wb.ml.domain;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AddEmailDAO extends SQLiteOpenHelper {
	SQLiteDatabase db;
	Context context;
	
	public AddEmailDAO(Context context) {
		super(context, "day.db", null, 1);
		this.context = context;
	}
	

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		db.execSQL("CREATE TABLE IF NOT EXISTS googleemail (_id INTEGER PRIMARY KEY ASC , email TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS googleemail");
		onCreate(db);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		db.execSQL("CREATE TABLE IF NOT EXISTS googleemail (_id INTEGER PRIMARY KEY ASC , email TEXT)");
	}
	
	public void insert(String email) {
		db = this.getWritableDatabase();
		db.execSQL("INSERT INTO googleemail VALUES (null,\""+email+"\");");
		db.close();
	}
	
	public String select() {
		db = this.getWritableDatabase();
		Cursor cursor;
		cursor = db.rawQuery("SELECT email FROM googleemail", null);
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
		db.delete("googleemail", null, null);
		db.close();
		this.close();	
	}
}
