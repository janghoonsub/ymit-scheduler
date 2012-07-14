package wb.ml.domain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AccessTokenDAO extends SQLiteOpenHelper {
	SQLiteDatabase db;
	ContentValues row;

	public AccessTokenDAO(Context context) {
		super(context, "day.db", null, 1);
	}	

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS  accesstoken (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
				"accessToken TEXT, expiresIn INTEGER, refreshToken TEXT, currentTime INTEGER);");
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		db.execSQL("CREATE TABLE IF NOT EXISTS  accesstoken (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
				"accessToken TEXT, expiresIn INTEGER, refreshToken TEXT, currentTime INTEGER);");		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS accesstoken");
		onCreate(db);
	}

	public void insert(AccessTokenVO atvo) {
		db = this.getWritableDatabase();
		row = new ContentValues();

		row.put("accessToken", atvo.getAccessToken());
		row.put("expiresIn", atvo.getExpiresIn());
		row.put("refreshToken", atvo.getRefreshToken());
		row.put("currentTime", atvo.getCurrentTime());
		db.insert("accesstoken", null, row);
		db.close();
		this.close();
	}

	public boolean delete1() {
		db = this.getWritableDatabase();
		boolean ret = false;
		// delete 메서드로 삭제
		Integer result = db.delete("accesstoken", null, null);
		if(result == 1){
			ret = true;
		}
		db.close();
		this.close();
		return ret;
	}

	// 작성자 : 송태영
	// 내용 : DB에 토큰이 있는지 확인 하는 메소드, 임시적인 것임 차후에 validate로 교체 필요
	// update : 2012-2-23
	public boolean exist() {
		db = this.getReadableDatabase();
		Cursor cursor;
		boolean ret = false;
		AccessTokenVO atvo = AccessTokenVO.getInstance();
		cursor = db.rawQuery("SELECT * FROM accesstoken", null);
		if(cursor.getCount() == 0){
			ret = false;
		} else{
			ret = true;
		}
		cursor.close();
		db.close();
		this.close();
		return ret;
	}
	
	public boolean select() {
		db = this.getReadableDatabase();
		Cursor cursor;
		boolean ret = false;
		AccessTokenVO atvo = AccessTokenVO.getInstance();
		cursor = db.rawQuery("SELECT * FROM accesstoken", null);
		if(cursor.getCount()==0){
			 ret = false;
		}else{
			ret = true;
			while(cursor.moveToNext()){
				atvo.setAccessToken(cursor.getString(1));
				atvo.setExpiresIn(cursor.getLong(2));
				atvo.setRefreshToken(cursor.getString(3));
				atvo.setCurrentTime(cursor.getLong(4));
			}
		}
		cursor.close();
		db.close();
		this.close();
		return ret;
	}
	//작성자 : 정승현
	//설명 : Token이 사용되는 시간을 업데이트 한다. 
	//작성일 : 2012-03-12
	public void updateCurrentTime(){
		db = this.getWritableDatabase();

		db.execSQL("UPDATE accesstoken SET currentTime= "+System.currentTimeMillis()+" ");
		
		db.close();
		this.close();
		
	}
}	
