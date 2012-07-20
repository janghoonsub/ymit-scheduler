package wb.ml.schedule;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import wb.ml.R;
import wb.ml.domain.DailyMemoDAO;
import wb.ml.domain.DailyMemoVO;
import wb.ml.domain.DailyScheduleDAO;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ScheduleActivity extends Activity {
	ActionBar mActionBar;
	static ViewPager mViewPager; 
	static TabAdapter mTabAdapter; 
	Calendar cal = new GregorianCalendar();
	Context context = this;
	static String today = null;
	static int position;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);
        
        //ViewPager 지정
        mViewPager = (ViewPager) findViewById(R.id.pager);
        //커스텀 TabAdapter 생성
        mTabAdapter = new TabAdapter(this,mViewPager);
        
        //어댑터 연결
        mViewPager.setAdapter(mTabAdapter);
        mViewPager.setOnPageChangeListener(mTabAdapter);
        
        today = SetCurrentTime();	//현재시간 가져오기
        
        //액션바 추가
        mActionBar = getActionBar();
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        //탭 추가
        mTabAdapter.addTab(mActionBar.newTab().setText("DAILY"));
        mTabAdapter.addTab(mActionBar.newTab().setText("WEEKLY"));
        mTabAdapter.addTab(mActionBar.newTab().setText("MONTHLY"));
        position = 0;
    }
    
    @Override
    protected void onResume() {			//일정을 추가하고 자동으로 새로고침되기 위해서 onResume을 설정해줌.
    	super.onResume();
    	refresh();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {		//액션바에서 아이콘들을 추가하기 위해서 res/menu/menu.xml 파일을 만든다음에 여기에 이렇게 추가한 거에요.
    	MenuInflater menuInflater = getMenuInflater();
    	menuInflater.inflate(R.menu.menu, menu);
    	
    	return super.onCreateOptionsMenu(menu);
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {	//액션바 메뉴 클릭 설정
    	String mDate = mTabAdapter.getFirstTitle();
		String[] a = mDate.split("-");	//현재 액션바에 있는 날짜 가져와서 '-'를 기준으로 split.
		
		switch(item.getItemId()) {	//새로운 액티비티를 추가하기 위해선 AndroidManifest.xml에서 꼭 새로 만든 액티비티를 지정해줘야해요.
		case android.R.id.home : finish(); break;		//원래메인화면 뒤로가기
		case R.id.menuadd : 
    		Intent intent = new Intent(this, AddSchedule.class);	//새로운 activity를 만들기 위해 Intent 사용
    		startActivity(intent);	//새로운 액티비티 시작
    		break;
    	case R.id.menuprev :		//'<'버튼
    		switch(position) {
    		case 0 :
	    		cal.set(Integer.parseInt(a[0]), (Integer.parseInt(a[1])-1), Integer.parseInt(a[2]));	//연-월-일 순으로 cal에 날짜지정
	    		cal.add(Calendar.DAY_OF_MONTH,-1);	//하루 앞으로
	    		today = SetCurrentTime();	//바뀐날짜 today에 다시 설정
	    		mTabAdapter.setFirstTitle(today);
	    		refresh();	//새로고침
	    		break;
    		case 1 : 
    			mTabAdapter.changeTitle(1,false);
    			break;
    		case 2 : 
    			mTabAdapter.changeTitle(2,false);
    			break;
    		}
    		break;
    	case R.id.menunext :	//'>'버튼
    		switch(position) {
    		case 0:
	    		cal.set(Integer.parseInt(a[0]), (Integer.parseInt(a[1])-1), Integer.parseInt(a[2]));	//cal에 날짜지정
	    		cal.add(Calendar.DAY_OF_MONTH,+1);	//하루 뒤로
	    		today = SetCurrentTime();
	    		mTabAdapter.setFirstTitle(today);
	    		refresh();
	    		break;
    		case 1 : 
    			mTabAdapter.changeTitle(1,true);
    			break;
    		case 2 : 
    			mTabAdapter.changeTitle(2,true);
    			break;
    		}
    		break;
    	}
    	return super.onOptionsItemSelected(item);
    }
    
    
    public void mOnClick(View v) {	//버튼클릭시 실행하도록 정의해놓은 메소드 --> xml에서 onClick으로 지정
    	switch(v.getId()) {	//눌린 버튼의 id 가져옴
    	case R.id.dailyadd :	//일일메모추가 버튼
    		final LinearLayout linear = (LinearLayout) View.inflate(this, R.layout.dailymemoadd, null);	//레이아웃을 가지고 온다
    		final EditText edit = (EditText) linear.findViewById(R.id.dailymemoadd);
    		final TextView txt = (TextView) findViewById(R.id.firstmemo);

    		//final InputMethodManager mImm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    		
    		edit.setText(txt.getText().toString());	//dialog떴을 때 원래 메모를 가져와 설정한다
    		edit.setSelection(txt.getText().length());	//커서위치 끝으로 설정
    		//mImm.showSoftInput(edit, 0);  
    		new AlertDialog.Builder(this)	//dialog를 만든다
    		.setTitle("메모를 추가하세요")
    		.setView(linear)
    		.setNegativeButton("확인", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {	//확인버튼 클릭시
					
					txt.setText(edit.getText().toString());	//원래 화면의 TextView를 바꾼다.
					//mImm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
					DailyMemoVO dvo = new DailyMemoVO();
					DailyMemoDAO dmdao = new DailyMemoDAO(context);	//db사용하기 위해 호출
					dvo.setDate(today);	//메모날짜 세팅
					dvo.setMemo(edit.getText().toString());	//메모내용 세팅
					dmdao.delete(today);	//메모수정전 원래 데이터 삭제
					dmdao.insert(dvo);	//db에 추가
				}
			})
			.setPositiveButton("취소", null)
			.show();
    		break;
    	case R.id.dailydeleteallbutton :
    		new AlertDialog.Builder(this)
    		.setTitle("모두삭제하겠습니까?")
    		.setNegativeButton("확인", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					DailyScheduleDAO ddao = new DailyScheduleDAO(ScheduleActivity.this);
					ddao.deleteAll(today);
					refresh();
					Toast.makeText(ScheduleActivity.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
				}
    		})
    		.setPositiveButton("취소", null)
    		.show();
    		break;
    	}
    	
    }
    
    public String SetCurrentTime() {		//현재시간을 string으로 반환
    	SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
    	return s.format(cal.getTime());
    }
    
    public static void refresh() {		//새로고침
    	mViewPager.setAdapter(mTabAdapter);
        mViewPager.setOnPageChangeListener(mTabAdapter);
        mTabAdapter.notifyDataSetChanged();
        mTabAdapter.refresh(position);
    }
    
    
}