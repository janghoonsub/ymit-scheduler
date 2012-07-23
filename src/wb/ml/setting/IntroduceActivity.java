package wb.ml.setting;

import wb.ml.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

public class IntroduceActivity extends Activity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.introduce);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home : finish(); break;
		}
		return super.onOptionsItemSelected(item);
	}
}
