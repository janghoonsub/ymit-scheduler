package wb.ml.setting;

import java.util.ArrayList;


import wb.ml.R;
import wb.ml.domain.DailyMemoDAO;
import wb.ml.domain.DailyScheduleDAO;
import wb.ml.domain.GoogleEventListDAO;
import wb.ml.domain.NoticeListDAO;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		
		ListView list = (ListView) findViewById(R.id.settinglist);
		ArrayList<String> settinglist = new ArrayList<String>();
		settinglist.add("일정 초기화");
		settinglist.add("개발자 소개");
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, settinglist);
		list.setAdapter(adapter);
		list.setOnItemClickListener(mItemClickListener);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	AdapterView.OnItemClickListener mItemClickListener = 
			new AdapterView.OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					TextView text = new TextView(SettingActivity.this);
					text.setText("일정과 메모, 공지사항이 지워집니다.");
					switch(position) {
					case 0: 
						new AlertDialog.Builder(SettingActivity.this)
						.setTitle("주의 : 정말 삭제하시겠습니까?")
						.setView(text)
						.setNegativeButton("확인", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								DailyScheduleDAO dsdao = new DailyScheduleDAO(SettingActivity.this);
								DailyMemoDAO dmdao = new DailyMemoDAO(SettingActivity.this);
								GoogleEventListDAO geldao = new GoogleEventListDAO(SettingActivity.this);
								NoticeListDAO ndao = new NoticeListDAO(SettingActivity.this);
								
								dsdao.clear();
								dmdao.clear();
								geldao.clear();
								ndao.clear();
								Toast.makeText(SettingActivity.this, "초기화되었습니다", Toast.LENGTH_SHORT).show();
							}
						})
						.setPositiveButton("취소", null)
						.show();

						break;
					case 1: Intent intent = new Intent(SettingActivity.this, IntroduceActivity.class);
						startActivity(intent);
					}
		
				}
	};
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home : finish(); break;
		}
		return super.onOptionsItemSelected(item);
	}
}