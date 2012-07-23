package wb.ml.schedule;

import java.util.Calendar;
import java.util.GregorianCalendar;

import wb.ml.R;
import wb.ml.R.id;
import wb.ml.R.layout;
import wb.ml.domain.DailyScheduleDAO;
import wb.ml.domain.DailyScheduleVO;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddSchedule extends Activity {
	private TextView mText;
	private EditText mTitle;
	private int mYear, mMonth, mDay, mHour, mMinute;
	private String temp;
	boolean flag;
	String[] tt;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addschedule);
		
		Intent intent = getIntent();
		
		mText = (TextView) findViewById(R.id.date);
		mTitle = (EditText) findViewById(R.id.addscheduleTitle);
		
		getActionBar().setTitle("일정 추가");
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		if(intent.hasExtra("data")) {
			tt = intent.getStringArrayExtra("data");
			String[] today = tt[0].split(" ");
			String[] date = today[0].split("-");
			String[] time = today[1].split(":");
			mYear = Integer.parseInt(date[0]);
			mMonth = Integer.parseInt(date[1])-1;
			mDay = Integer.parseInt(date[2]);
			mHour = Integer.parseInt(time[0]);
			mMinute = Integer.parseInt(time[1]);
			mTitle.setText(tt[1]);
			mTitle.setSelection(tt[1].length());
			Button button = (Button) findViewById(R.id.addscheduleOk);
			button.setText("일정 변경");
			flag = true;
		} else {
			Calendar cal = new GregorianCalendar();
			temp = ScheduleActivity.today;
			String[] date = temp.split("-");
			mYear = Integer.parseInt(date[0]);		//today 에 있던 날짜 가져오기
			mMonth = Integer.parseInt(date[1])-1;
			mDay = Integer.parseInt(date[2]);
			mHour = cal.get(Calendar.HOUR_OF_DAY);
			mMinute = cal.get(Calendar.MINUTE);		
			flag = false;
		}
		updateNow();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {	//메뉴 이벤트 처리 메소드
		switch(item.getItemId()) {
		case android.R.id.home : finish(); break;	//아이콘 뒤로가기 버튼 이벤트 처리
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void mOnClick(View v) {
		switch(v.getId()) {
		case R.id.pickdate :	//날짜 설정
			new DatePickerDialog(AddSchedule.this, mDateSetListener, mYear, mMonth, mDay).show();
			break;
		case R.id.picktime :	//시간 설정
			new TimePickerDialog(AddSchedule.this, mTimeSetListener, mHour, mMinute, true).show();		//false --> 24시간 또는 오전오후 표시
			break;
		case R.id.addscheduleCancel :	//취소 버튼
			finish();
			break;
		case R.id.addscheduleOk :		//일정 추가 버튼
			String titleTemp = mTitle.getText().toString();
			if(titleTemp==null || "".equals(titleTemp)){	//만약 일정 제목을 안적었으면 적으라고 표시하기만 함
				Toast.makeText(this, "일정 제목을 입력하세요", Toast.LENGTH_SHORT).show();
			} else {		//DB에 넣고 현재 열린 페이지를 닫음.		
				DailyScheduleVO dvo = new DailyScheduleVO();	
				DailyScheduleDAO ddao = new DailyScheduleDAO(this);
				dvo.setSchedule(titleTemp);
				dvo.setDate(temp);
				if(flag) {
					ddao.update(ddao.getId(tt[0], tt[1]), dvo);
					finish();
				} else {
					ddao.insert(dvo);
					ScheduleActivity.mTabAdapter.notifyDataSetChanged();
					finish();
				}
			}
			break;
		}
	}
	
	DatePickerDialog.OnDateSetListener mDateSetListener = 	//날짜 설정 리스너
			new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year,
				int monthOfYear, int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateNow();
		}
	};
	
	TimePickerDialog.OnTimeSetListener mTimeSetListener = 	//시간 설정 리스너	-- 확인 눌렀을 때 현재값 받아옴
			new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, 
				int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			updateNow();
		}
	};
	
	
	void updateNow() {		//String.format은 저 형식으로 자료값을 표시한다는것. c언어의 printf랑 비슷해서 어렵진 않을거에요.
		String month="", day="", hour="", minute ="";
		if((mMonth+1) < 10) {
			month = "0"+(mMonth+1);
		} else {
			month = mMonth+1+"";
		}
		if(mDay < 10) {
			day = "0"+mDay;
		} else {
			day = mDay+"";
		}
		if(mHour < 10) {
			hour = "0"+mHour;
		} else {
			hour = mHour+"";
		}
		if(mMinute < 10) {
			minute = "0"+mMinute;
		} else {
			minute = mMinute+"";
		}
		
		mText.setText(mYear+"년 "+month+"월 "+day+"일 "+hour+" : "+minute);
		temp = mYear+"-"+month+"-"+day+" "+hour+":"+minute;
	}
}
