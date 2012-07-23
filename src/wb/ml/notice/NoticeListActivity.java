package wb.ml.notice;

import java.util.List;
import java.util.Map;

import wb.ml.R;
import wb.ml.domain.NoticeListDAO;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class NoticeListActivity extends Activity {
	List<Map<String, String>> dataList;
	ListView listView;
	SimpleAdapter simpleAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.noticelist);
		
		NotificationManager NM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		NM.cancel(1);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("공지사항");
		
		refresh();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {	//메뉴 이벤트 처리 메소드
		switch(item.getItemId()) {
		case android.R.id.home : finish(); break;	//아이콘 뒤로가기 버튼 이벤트 처리
		}
		return super.onOptionsItemSelected(item);
	}
	
	AdapterView.OnItemLongClickListener mOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
		public boolean onItemLongClick(android.widget.AdapterView<?> parent, View view, final int position, long id) {
			TextView textview = new TextView(NoticeListActivity.this);
			textview.setText("*주의* 한 번 받았던 공지사항은 다시 받을 수 없습니다. 받았던 공지사항을 다시 받으시려면 일정 초기화를 해야 합니다.");
			new AlertDialog.Builder(NoticeListActivity.this)
			.setTitle("정말 삭제하시겠습니까?")
			.setView(textview)
			.setNegativeButton("확인", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					NoticeListDAO nldao = new NoticeListDAO(NoticeListActivity.this);
					
					nldao.delete(dataList.get(position).get("desc"), dataList.get(position).get("date"));
					refresh();
				}
			})
			.setPositiveButton("취소",null)
			.show();
			return true;
		}
	};
	
	public void refresh() {
		NoticeListDAO ndao = new NoticeListDAO(this);
		dataList = ndao.select();
		
		simpleAdapter = new SimpleAdapter(this, dataList, 
				android.R.layout.simple_list_item_2, new String[]{"desc", "date"}, new int[]{android.R.id.text1, android.R.id.text2});
		listView = (ListView) findViewById(R.id.noicelistview);
		listView.setAdapter(simpleAdapter);
		listView.setOnItemLongClickListener(mOnItemLongClickListener);
	}
}
