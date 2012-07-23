package wb.ml.schoolevents;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

public class schoolEventsActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Context c = getApplicationContext();
		BaseViewPager pager = new BaseViewPager(c);
		pager.setPageControl(new PageControl(c));
		pager.setAdapter(new SampleAdapter(c));
		setContentView(pager);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {	//메뉴 이벤트 처리 메소드
		switch(item.getItemId()) {
		case android.R.id.home : finish(); break;	//아이콘 뒤로가기 버튼 이벤트 처리
		}
		return super.onOptionsItemSelected(item);
	}
}
