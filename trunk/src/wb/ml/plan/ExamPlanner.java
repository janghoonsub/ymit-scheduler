package wb.ml.plan;

import java.util.Calendar;
import java.util.GregorianCalendar;

import wb.ml.R;
import wb.ml.domain.ExamPlannerDAO;
import wb.ml.domain.ExamPlannerDateDAO;
import wb.ml.domain.ExamPlannerVO;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ExamPlanner extends Activity implements OnItemClickListener, OnItemLongClickListener {
	private GridView mGridView;
	private ExamPlanAdapter adapter;
	private View form;
	private int p;
	private ExamPlannerDAO edao;
	private Button okButton;
	private ExamPlannerVO evo;
	private EditText subject;
	private EditText cover;
	private EditText before;
	private EditText goal;
	private int mYear, mMonth, mDay;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.examplanner);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		form = findViewById(R.id.examplanerform);
		form.setVisibility(View.INVISIBLE);
		okButton = (Button) findViewById(R.id.examok);
		subject = (EditText) findViewById(R.id.editsubject);
		cover = (EditText) findViewById(R.id.editcover);
		before = (EditText) findViewById(R.id.editbefore);
		goal = (EditText) findViewById(R.id.editgoal);
		refresh();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		ExamPlannerDateDAO epddao = new ExamPlannerDateDAO(this);
		if(epddao.select() != null) {
			String date = epddao.select();
			String[] d = date.split("-");
			mYear = Integer.parseInt(d[0]);
			mMonth = Integer.parseInt(d[1]);
			mDay = Integer.parseInt(d[2]);
		} else {
			Calendar cal = new GregorianCalendar();
			mYear = cal.get(Calendar.YEAR);
			mMonth = cal.get(Calendar.MONTH);
			mDay = cal.get(Calendar.DATE);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {	//메뉴 이벤트 처리 메소드
		switch(item.getItemId()) {
		case android.R.id.home : finish(); break;	//아이콘 뒤로가기 버튼 이벤트 처리
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		if(position == 5) {
			new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay).show();
			
			
		} else if(position > 10 && (position%5>0 && position%5<5)) {
			reset();
			p = position;
			edao = new ExamPlannerDAO(this);
			if(edao.exist(position)) {
				
				okButton.setText("수정");
				evo = new ExamPlannerVO();
				evo = edao.select(position);
				subject.setText(evo.getSuject());
				cover.setText(evo.getCover());
				before.setText(evo.getBefore());
				goal.setText(evo.getGoal());
			}
			form.setVisibility(View.VISIBLE);
		}
	}
	
	public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
		edao = new ExamPlannerDAO(this);
		ExamPlannerDateDAO epddao = new ExamPlannerDateDAO(this);
		if(position==5 && (epddao.select()!=null)) {
			new AlertDialog.Builder(this)
			.setTitle("날짜를 초기화 하시겠습니까?")
			.setPositiveButton("취소", null)
			.setNegativeButton("확인", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which) {
					ExamPlannerDateDAO epddao = new ExamPlannerDateDAO(ExamPlanner.this);
					epddao.delete();
					Toast.makeText(ExamPlanner.this, "초기화되었습니다.", Toast.LENGTH_SHORT).show();
					refresh();
				}
			})
			.show();
		} else if(edao.exist(position)) {
			evo = new ExamPlannerVO();
			evo = edao.select(position);
			final int p = position;
			
			new AlertDialog.Builder(this)
			.setTitle(evo.getSuject()+"을(를) 삭제하겠습니까?")
			.setPositiveButton("취소", null)
			.setNegativeButton("확인", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					edao.delete(p);
					Toast.makeText(ExamPlanner.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
					refresh();
				}
			})
			.show();
		}
		return true;
	}
	
	public void mOnClick(View v) {
		edao = new ExamPlannerDAO(this);
		evo = new ExamPlannerVO();
		
		switch(v.getId()) {
		case R.id.examok :
			if("".equals(subject.getText().toString()) || "".equals(cover.getText().toString()) 
					|| "".equals(before.getText().toString()) || "".equals(goal.getText().toString())) {
				Toast.makeText(this, "모두 입력해 주세요.", Toast.LENGTH_SHORT).show();
			} else {
				evo.setPosition(p);
				evo.setSuject(subject.getText().toString());
				evo.setCover(cover.getText().toString());
				evo.setBefore(before.getText().toString());
				evo.setGoal(goal.getText().toString());
				if("수정".equals(okButton.getText())) {
					edao.update(evo);
				} else {
					edao.insert(evo);
				}
				refresh();
			}
			break;
		case R.id.examcancel :
			break;
		}
		reset();
		form.setVisibility(View.INVISIBLE);
		okButton.setText("확인");
	}
	
	private void reset() {
		subject.setText("");
		cover.setText("");
		before.setText("");
		goal.setText("");
	}
	
	
	public void refresh() {
		mGridView = (GridView) findViewById(R.id.examplannergridview);
		adapter = new ExamPlanAdapter(this, R.layout.time);
		mGridView.setAdapter(adapter);
		mGridView.setOnItemClickListener(this);
		mGridView.setOnItemLongClickListener(this);
	}
	
	DatePickerDialog.OnDateSetListener mDateSetListener = 	//날짜 설정 리스너
			new DatePickerDialog.OnDateSetListener() {
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					mYear = year;
					mMonth = monthOfYear;
					mDay = dayOfMonth;
					saveDate();
				}
	};
	
	public void saveDate() {
		ExamPlannerDateDAO epddao = new ExamPlannerDateDAO(this);
		if(epddao.select() != null) {
			epddao.delete();
		}
		epddao.insert(mYear+"-"+mMonth+"-"+mDay);
		refresh();
	}

}
