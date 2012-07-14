package wb.ml.timetable;

import java.util.ArrayList;

import wb.ml.R;
import wb.ml.domain.TimeTableDAO;
import wb.ml.domain.TimeTableVO;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class TimeTableAdd extends Activity {
	ArrayList<String> items;
	TimeTableDAO ttDAO;
	TimeTableVO ttVO;
	ArrayAdapter<String> adapter;
	ListView list;
	ArrayAdapter<CharSequence> adspin;
	int color;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addtimetable);
		ttDAO = new TimeTableDAO(this);
		
		items = ttDAO.select();
		
		getActionBar().setDisplayHomeAsUpEnabled(true);		//뒤로가기
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, items);
		list = (ListView) findViewById(R.id.tablelist);
		list.setAdapter(adapter);
		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		Spinner spin = (Spinner)findViewById(R.id.timetablespinner);	//스피너로 색상 표시
		spin.setPrompt("색상을 고르세요.");
		
		adspin = ArrayAdapter.createFromResource(this, R.array.timetablecolors, android.R.layout.simple_spinner_item);
		adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(adspin);
		
		spin.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				switch(position) {
				case 0 : color = Color.BLUE; break;
				case 1 : color = Color.CYAN; break;
				case 2 : color = Color.DKGRAY; break;
				case 3 : color = Color.GRAY; break;
				case 4 : color = Color.GREEN; break;
				case 5 : color = Color.LTGRAY; break;
				case 6 : color = Color.MAGENTA; break;
				case 7 : color = Color.RED; break;
				case 8 : color = Color.YELLOW; break;
				}
			}
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {		//뒤로가기 아이콘 이벤트 처리
		switch(item.getItemId()) {
		case android.R.id.home : finish(); break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
	public void mOnClick(View v) {
		EditText subject = (EditText) findViewById(R.id.tablesubject);
		EditText teacher = (EditText) findViewById(R.id.tableteacher);
		switch(v.getId()){
		case R.id.timetableadd :
			if((subject.getText().toString().length()!=0) && (teacher.getText().toString().length()!=0)) {	//만약 과목이나 선생님 둘중에 하나가 비어있으면 입력안됨
				String text = subject.getText().toString()+" "+teacher.getText().toString();
				ttVO = new TimeTableVO();
				ttVO.setClss(text);
				ttVO.setColor(color);
				ttDAO.insert(ttVO);
				subject.setText("");		//입력화면을 다시 깨끗하게
				teacher.setText("");
			}
			break;
		case R.id.timetabledelete :	//선택된 리스트뷰의 db 지우기
			int pos;
			pos = list.getCheckedItemPosition();
			if(pos != ListView.INVALID_POSITION) {
				ttDAO.delete(items.get(pos));
				list.clearChoices();
			}
			break;
		}
		items = ttDAO.select();
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, items);
		list.setAdapter(adapter);
		adapter.notifyDataSetChanged();		//refresh같은거라고 보면되요.
	}
}
