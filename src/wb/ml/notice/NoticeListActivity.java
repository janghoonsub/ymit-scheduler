package wb.ml.notice;

import java.util.List;
import java.util.Map;

import wb.ml.R;
import wb.ml.domain.NoticeListDAO;
import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class NoticeListActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.noticelist);
		
		NotificationManager NM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		NM.cancel(1);
		
		
		NoticeListDAO ndao = new NoticeListDAO(this);
		List<Map<String, String>> dataList = ndao.select();
		
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, dataList, 
				android.R.layout.simple_list_item_2, new String[]{"desc", "date"}, new int[]{android.R.id.text1, android.R.id.text2});
		ListView listView = (ListView) findViewById(R.id.noicelistview);
		listView.setAdapter(simpleAdapter);
	}
}
