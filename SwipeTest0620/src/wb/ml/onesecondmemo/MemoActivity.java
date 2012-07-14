package wb.ml.onesecondmemo;

import wb.ml.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

public class MemoActivity extends Activity {
	
	ListView listview;
	EditText edittext;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onesecondmemo);
		
		listview = (ListView) findViewById(R.id.onesecondmemolist);
		edittext = (EditText) findViewById(R.id.onesecondmemoedittext);
		
		//listview.setAdapter(adapter);
	}
	
	
	
}
