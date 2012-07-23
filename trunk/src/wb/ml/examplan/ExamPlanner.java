package wb.ml.examplan;

import java.util.Calendar;
import java.util.GregorianCalendar;

import wb.ml.R;
import wb.ml.domain.ExamPlannerDAO;
import wb.ml.domain.ExamPlannerDateDAO;
import wb.ml.domain.ExamPlannerVO;
import wb.ml.domain.SchoolTableDAO;
import wb.ml.domain.SchoolTableVO;
import wb.ml.domain.TimeTableVO;
import wb.ml.timetable.TimeTableActivity;
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
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ExamPlanner extends Activity implements OnItemClickListener, OnItemLongClickListener {
	private GridView mGridView;
	private ExamPlanAdapter adapter;
	private int p;
	private ExamPlannerDAO edao;
	private ExamPlannerVO evo;
	private EditText subject;
	private EditText cover;
	private EditText before;
	private EditText goal;
	private int mYear, mMonth, mDay;
	private String ok = "확인";
	private TextView subjectshow,covershow,beforeshow,goalshow;
	TableLayout relative;
	TableLayout frame;
	boolean showflag = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.examplanner);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		relative = (TableLayout) View.inflate(this, R.layout.examplanneradd, null);
		frame = (TableLayout) findViewById(R.id.examplanerformshow);
		subject = (EditText) relative.findViewById(R.id.editsubject);
		cover = (EditText) relative.findViewById(R.id.editcover);
		before = (EditText) relative.findViewById(R.id.editbefore);
		goal = (EditText) relative.findViewById(R.id.editgoal);
		subjectshow = (TextView) findViewById(R.id.editsubjectshow);
		covershow = (TextView) findViewById(R.id.editcovershow);
		beforeshow = (TextView) findViewById(R.id.editbeforeshow);
		goalshow = (TextView) findViewById(R.id.editgoalshow);
		
		frame.setVisibility(View.INVISIBLE);
		getActionBar().setTitle("시험계획");
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
			relative = (TableLayout) View.inflate(this, R.layout.examplanneradd, null);
			subject = (EditText) relative.findViewById(R.id.editsubject);
			cover = (EditText) relative.findViewById(R.id.editcover);
			before = (EditText) relative.findViewById(R.id.editbefore);
			goal = (EditText) relative.findViewById(R.id.editgoal);
			evo = new ExamPlannerVO();
			
			
			if(edao.exist(position)) {
				
				 if(showflag) {
					evo = edao.select(position);
					if(evo.getSuject().equals(subjectshow.getText())) {
						ok = "수정";
						evo = edao.select(position);
						subject.setText(evo.getSuject());
						cover.setText(evo.getCover());
						before.setText(evo.getBefore());
						goal.setText(evo.getGoal());
						showdialog(position);
						frame.setVisibility(View.INVISIBLE);
						showflag = false;
					} else {
						showBottom(position);
					}
				 } else {
					showBottom(position);
				}
			
			} else {
				frame.setVisibility(View.INVISIBLE);
				showdialog(position);
			}
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
	
	public void showdialog(int position) {
		String day = "",time = "";
		showflag=false;
		switch(position%5){
		case 1: day="1일"; break;
		case 2: day="2일"; break;
		case 3: day="3일"; break;
		case 4: day="4일"; break;
		}
		switch(position/5){
		case 2: time="1교시"; break;
		case 3: time="2교시"; break;
		case 4: time="3교시"; break;
		case 5: time="4교시"; break;
		}
		
		new AlertDialog.Builder(this)
		.setTitle(day+" "+time)
		.setView(relative)
		.setNegativeButton(ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				edao = new ExamPlannerDAO(ExamPlanner.this);
				evo = new ExamPlannerVO();
				if("".equals(subject.getText().toString()) || "".equals(cover.getText().toString()) 
						|| "".equals(before.getText().toString()) || "".equals(goal.getText().toString())) {
					Toast.makeText(ExamPlanner.this, "모두 입력해 주세요.", Toast.LENGTH_SHORT).show();
				} else {
					evo.setPosition(p);
					evo.setSuject(subject.getText().toString());
					evo.setCover(cover.getText().toString());
					evo.setBefore(before.getText().toString());
					evo.setGoal(goal.getText().toString());
					if("수정".equals(ok)) {
						edao.update(evo);
					} else {
						edao.insert(evo);
					}
					refresh();
				}
				
			}
		})
		.setPositiveButton("취소", null)
		.show();
	}
	
	void showBottom(int position) {
		evo = edao.select(position);
		subjectshow.setText(evo.getSuject());
		covershow.setText(evo.getCover());
		beforeshow.setText(evo.getBefore());
		goalshow.setText(evo.getGoal());
		frame.setVisibility(View.VISIBLE);
		showflag=true;
	}
}
