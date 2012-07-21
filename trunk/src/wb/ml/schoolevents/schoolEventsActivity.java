package wb.ml.schoolevents;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class schoolEventsActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Context c = getApplicationContext();
		BaseViewPager pager = new BaseViewPager(c);
		pager.setPageControl(new PageControl(c));
		pager.setAdapter(new SampleAdapter(c));
		setContentView(pager);
	}

}
