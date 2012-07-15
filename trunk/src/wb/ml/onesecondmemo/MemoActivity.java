package wb.ml.onesecondmemo;

import java.util.ArrayList;

import wb.ml.R;
import wb.ml.domain.OneSecondMemoDAO;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MemoActivity extends Activity {

	ListView listview;
	EditText edittext;
	ArrayList<String> memo;
	ArrayAdapter<String> adapter;
	String string;
	OneSecondMemoDAO odao;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onesecondmemo);

		listview = (ListView) findViewById(R.id.onesecondmemolist);
		edittext = (EditText) findViewById(R.id.onesecondmemoedittext);
		odao = new OneSecondMemoDAO(this);
		ActionBar actionbar = getActionBar();
		actionbar.setTitle("1초메모");
		actionbar.setDisplayHomeAsUpEnabled(true);
		refresh();

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {	//메뉴 이벤트 처리 메소드
		switch(item.getItemId()) {
		case android.R.id.home : finish(); break;	//아이콘 뒤로가기 버튼 이벤트 처리
		}
		return super.onOptionsItemSelected(item);
	}

	void refresh() {
		odao = new OneSecondMemoDAO(this);
		memo = odao.selectall();
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, memo);
		listview.setAdapter(adapter);
		listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}

	public void mOnClick(View v) {
		switch(v.getId()){
		case R.id.onesecondaddbutton :
			string = edittext.getText().toString();
			if(string.equals("")) {
				Toast.makeText(this, "내용을 입력해주세요", Toast.LENGTH_SHORT).show();
			} else {

				odao.insert(string);
				refresh();
				edittext.setText("");
			}
			break;
		case R.id.onesecondmemodeleteall :
			new AlertDialog.Builder(this)
			.setTitle("정말 삭제하시겠습니까?")
			.setNegativeButton("취소", null)
			.setPositiveButton("확인", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					odao.deleteall();
					refresh();
				}
			})
			.show();
			break;
		case R.id.onesecondmemodelete :
			ArrayList<String> temp = new ArrayList<String>();
			SparseBooleanArray sb = listview.getCheckedItemPositions();
			if (sb.size() != 0) {
				for (int i = listview.getCount() - 1; i >= 0 ; i--) {
					if (sb.get(i)) {
						temp.add(memo.get(i));
					}
				}
				odao.delete(temp);
				listview.clearChoices();
				refresh();
				break;
			}
		}
	}



}
