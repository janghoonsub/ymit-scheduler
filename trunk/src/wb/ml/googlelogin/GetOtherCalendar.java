package wb.ml.googlelogin;

import wb.ml.R;
import wb.ml.domain.AddEmailDAO;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GetOtherCalendar extends Activity {
	TextView getEmail;
	EditText setEmail;
	Button addEmail;
	Button delEmail;
	AddEmailDAO adao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.getothercalendar);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		getEmail = (TextView)findViewById(R.id.getemail);
		setEmail = (EditText)findViewById(R.id.setemail);
		adao = new AddEmailDAO(this);
		addEmail = (Button) findViewById(R.id.addemail);
		delEmail = (Button) findViewById(R.id.delemail);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		refresh();
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home : finish(); break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void mOnClick(View v) {
		switch(v.getId()) {
		case R.id.addemail : 
			adao.insert(setEmail.getText().toString());
			refresh();
			break;
		case R.id.delemail :
			adao.delete();
			getEmail.setText("없음");
			setEmail.setText("");
			refresh();
			break;
		}
	}
	
	public void refresh() {
		addEmail.setEnabled(false);
		delEmail.setEnabled(false);
		if(adao.select() != null) {
			getEmail.setText(adao.select());
			setEmail.setText(adao.select());
			delEmail.setEnabled(true);
		} else {
			addEmail.setEnabled(true);
		}
	}
}
