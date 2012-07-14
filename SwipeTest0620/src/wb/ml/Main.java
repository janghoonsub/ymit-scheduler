package wb.ml;

import wb.ml.domain.AccessTokenDAO;
import wb.ml.googlelogin.GetOtherCalendar;
import wb.ml.googlelogin.GoogleLoginActivity;
import wb.ml.onesecondmemo.MemoActivity;
import wb.ml.schedule.SwipeTestActivity;
import wb.ml.timetable.TimeTableActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class Main extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Intent intent = new Intent(this, SwipeTestActivity.class);
		startActivity(intent);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		invalidateOptionsMenu();
		AccessTokenDAO accessTokenDAO = new AccessTokenDAO(this);
		Intent intent = new Intent(this, Trigger.class);
		if(accessTokenDAO.exist()) {
			startService(intent);
		} else {
			stopService(intent);
			Trigger.run=false;
		}
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		AccessTokenDAO adao = new AccessTokenDAO(this);
		MenuInflater menuInflater = getMenuInflater();
		if(adao.exist()) {
			menuInflater.inflate(R.menu.login, menu);
		} else {
			menuInflater.inflate(R.menu.logout, menu);
		}
		return super.onPrepareOptionsMenu(menu);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch(item.getItemId()) {
		case R.id.logoutkey : 
			intent = new Intent(this, GoogleLoginActivity.class);
			startActivity(intent);
			break;
		case R.id.loginkey :
			AccessTokenDAO adao = new AccessTokenDAO(this);
			adao.delete1();
			Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
			invalidateOptionsMenu();
			intent = new Intent(this, Trigger.class);
			stopService(intent);
			Trigger.run=false;
			break;
		case R.id.setcalendar :
			intent = new Intent(this, GetOtherCalendar.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	public void mOnClick(View v) {
		Intent intent;
		switch(v.getId()) {
		case R.id.swipeTestActivity :
			intent = new Intent(this, SwipeTestActivity.class);
			startActivity(intent);
			break;
		case R.id.timetable : 
			intent = new Intent(this, TimeTableActivity.class);
			startActivity(intent);
			break;
		case R.id.onesecondmemobutton :
			intent = new Intent(this, MemoActivity.class);
			startActivity(intent);
			break;
		}
	}
	
	
}
