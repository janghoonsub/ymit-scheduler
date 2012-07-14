package wb.ml.timetable;

import java.util.ArrayList;

import wb.ml.R;
import wb.ml.domain.SchoolTableDAO;
import wb.ml.domain.SchoolTableVO;
import wb.ml.domain.TimeTableDAO;
import wb.ml.domain.TimeTableVO;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class TimeTableActivity extends Activity implements OnItemClickListener {
	GridView mGridView;
	TimeTableAdapter adapter;
	ListView list;
	ArrayList<String> items;
	TimeTableDAO ttDAO;
	int po;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timetable);
		
		View mybutton = getLayoutInflater().inflate(R.layout.timetablebutton, null);
		
		ActionBar actionBar = getActionBar();
		actionBar.setCustomView(mybutton);			//커스텀 액션바를 사용한 것
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);	//아이콘에 뒤로가기 버튼 추가
		
		refresh();
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {	//메뉴 이벤트 처리 메소드
		switch(item.getItemId()) {
		case android.R.id.home : finish(); break;	//아이콘 뒤로가기 버튼 이벤트 처리
		}
		return super.onOptionsItemSelected(item);
	}

	public void onItemClick(AdapterView<?> parent, View v, int position, long arg3) {		//그리드뷰로 만든 시간표 이벤트 처리 메소드
		ttDAO = new TimeTableDAO(this);
		items = ttDAO.select();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, items);
		LinearLayout linear = (LinearLayout) View.inflate(this, R.layout.tabledialoglist, null);
		list = (ListView) linear.findViewById(R.id.tabledialoglist);
		list.setAdapter(adapter);
		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		//list.setSelection(0);
		po = position;
		if(!(position < 7 || position%7==0)){
			String week="", period="";
			switch(position%7) {				//클릭한 번호를 이용해서 월요일, 몇교시인지 구함
			case 1 : week="월요일"; break;
			case 2 : week="화요일"; break;
			case 3 : week="수요일"; break;
			case 4 : week="목요일"; break;
			case 5 : week="금요일"; break;
			case 6 : week="토요일"; break;
			}
			switch(position/7) {
			case 1 : period="1교시"; break;
			case 2 : period="2교시"; break;
			case 3 : period="3교시"; break;
			case 4 : period="4교시"; break;
			case 5 : period="5교시"; break;
			case 6 : period="6교시"; break;
			case 7 : period="7교시"; break;
			}
			
			new AlertDialog.Builder(this)
			.setTitle(week+" "+period)
			.setView(linear)
			.setNegativeButton("확인", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					if(list.getCheckedItemPosition()==AdapterView.INVALID_POSITION) {
						Toast.makeText(TimeTableActivity.this, "과목을 선택해주세요", 0).show();
					} else {
						TimeTableVO vo = ttDAO.select2(items.get(list.getCheckedItemPosition()));	//클릭한 곳의 텍스트값을 구해서 그것의 vo를 구함
						SchoolTableDAO stdao = new SchoolTableDAO(TimeTableActivity.this);
						SchoolTableVO stvo = new SchoolTableVO();
						stvo.setColor(vo.getColor());
						stvo.setSubject(vo.getClss().charAt(0)+"");
						stvo.setPosition(po);
						stdao.insert(stvo);									//시간표 위치와 색깔 이름 저장
						refresh();
					}
				}
			})
			.setPositiveButton("취소", null)
			.show();
			refresh();
		}
	}
	
	public void mOnClick(View v) {	// 메뉴버튼들 이벤트 처리 메소드
		switch(v.getId()) {
		case R.id.tablesettingbutton : //과목 추가하는 액티비티로 넘어가는 버튼
			Intent intent = new Intent(this, TimeTableAdd.class);
			startActivity(intent);
			break;
		case R.id.tabledelbutton :		//시간표 db를 다 날림
			new AlertDialog.Builder(this)
			.setTitle("정말 삭제하시겠습니까?")
			.setNegativeButton("취소", null)
			.setPositiveButton("확인", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					SchoolTableDAO dao = new SchoolTableDAO(TimeTableActivity.this);
					dao.delete();
					refresh();
				}
			})
			.show();
		}
	}
	
	public void refresh() {		//새로고침
		mGridView = (GridView) findViewById(R.id.timetableGridView);
		adapter = new TimeTableAdapter(this, R.layout.time);
		mGridView.setAdapter(adapter);
		mGridView.setOnItemClickListener(this);
	}
}
