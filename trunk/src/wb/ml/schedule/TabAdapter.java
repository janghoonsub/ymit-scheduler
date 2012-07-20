package wb.ml.schedule;

import java.util.ArrayList;
import java.util.Calendar;

import wb.ml.R;
import wb.ml.domain.DailyMemoDAO;
import wb.ml.domain.DailyScheduleDAO;
import wb.ml.domain.DailyScheduleVO;
import wb.ml.schedule.calendar.CalendarAdapter;
import wb.ml.schedule.calendar.DayInfo;
import wb.ml.schedule.day.Arrays;
import wb.ml.schedule.day.ListAdapter;
import wb.ml.schedule.week.WeekAdapter;
import wb.ml.schedule.week.WeekInfo;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.*;

//화면 옆으로 슬라이드와 탭클릭을 설정하는 클래스
public class TabAdapter extends PagerAdapter implements TabListener,
		OnPageChangeListener {
	private TextView firstTitle, thirdTitle;
	private LayoutInflater layoutInflater;	//화면을 미리 만들어 준비하는 아이
	private ActionBar mActionBar;	//main의 액션바
	private ViewPager mViewPager;	//main에서 넘겨받은 viewpager
	private ArrayList<Arrays> dailyarray;	//listview에 넣을 데이터
	private Context main;
	private Arrays array;
	private AlertDialog editdialog;	//수정.삭제 dialog
	private GridView mGvCalendar;
	private ArrayList<DayInfo> mDayList;
	private CalendarAdapter mCalendarAdapter;
	private ArrayList<WeekInfo> mWeekList;
	private WeekAdapter mWeekAdapter;
	private ListView weekListView;
	private TextView weekMemo;
	
	Calendar mLastMonthCalendar;
	Calendar mThisMonthCalendar;
	Calendar mNextMonthCalendar;
	
	Calendar mThisWeekCalendar;
	
	public TabAdapter(Activity activity, ViewPager viewPager) {		//생성자
		layoutInflater = LayoutInflater.from(activity);
		mActionBar = activity.getActionBar();
		mViewPager = viewPager;
		mViewPager.setAdapter(this);
		mViewPager.setOnPageChangeListener(this);
		main = activity;
	}

	
	public String getFirstTitle() {
		return (String) firstTitle.getText();
	}
	public void setFirstTitle(String t) {
		firstTitle.setText("");
		firstTitle.setText(t);
	}
	public String getThirdTitle() {
		return (String) thirdTitle.getText();
	}
	public void setThirdTitle(String t) {
		thirdTitle.setText("");
		thirdTitle.setText(t);
	}

	
	//탭 추가를 위하여 직접 만든 메소드
	public void addTab(ActionBar.Tab tab) {
		tab.setTabListener(this);
		mActionBar.addTab(tab);
		
	}
	
	// OnPageChangeListener 의 메소드들
	public void onPageScrollStateChanged(int arg0) {}
	public void onPageScrolled(int arg0, float arg1, int arg2) {}
	public void onPageSelected(int position) {		//페이지를 넘기면 탭도 같이 움직이게 하는 메소드
		mActionBar.setSelectedNavigationItem(position);
		ScheduleActivity.position=position;
	}

	// TabListener 의 메소드들
	public void onTabReselected(Tab tab, FragmentTransaction ft) {}
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {}
	public void onTabSelected(Tab tab, FragmentTransaction ft) {		//탭 클릭시 화면을 넘기는 메소드
		if("DAILY".equals(tab.getText())) {
			mViewPager.setCurrentItem(0);
			ScheduleActivity.position=0;

		} else if("WEEKLY".equals(tab.getText())) {
			mViewPager.setCurrentItem(1);
			ScheduleActivity.position=1;

		} else if("MONTHLY".equals(tab.getText())) {
			mViewPager.setCurrentItem(2);
			ScheduleActivity.position=2;
			ScheduleActivity.refresh();
		}

	}

	// PagerAdapter의 메소드들
	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {}	//saveState() 상태에서 저장했던 Adapter와 page를 복구 한다. 
	@Override
	public Parcelable saveState() {return null;}	//현재 UI 상태를 저장하기 위해 Adapter와 Page 관련 인스턴스 상태를 저장 한다.
	@Override
	public void startUpdate(View arg0) {}		//페이지 변경이 시작될때 호출 되는 메소드
	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {} //View 객체를 삭제 한다.
	@Override
	public void finishUpdate(View arg0) {}	//페이지 변경이 완료되었을때 호출 된다.
	@Override
	public int getCount() {	return 3; }	//현재 PagerAdapter에서 관리할 갯수를 반환 하는 메소드 --> 만들어야할 화면이 3개이므로 3 리턴
	@Override
	public Object instantiateItem(View view, int position) {		//ViewPager에서 사용할 뷰객체 생성 및 등록 한다.  
		View v = null;
		switch(position) {
		case 0: 
			v = layoutInflater.inflate(R.layout.first, null);	//화면 준비
			dailyarray = new ArrayList<Arrays>();
	        Arrays day;	//dailyarray 에 집어넣기 위한 틀
	        DailyScheduleDAO ddao = new DailyScheduleDAO(main);		//db 쓰기위해 호출
	        ArrayList<DailyScheduleVO> show = ddao.select(ScheduleActivity.today);		//today의 지정된 날짜의 시간,스케줄을 가지고 온다
	        for(int i=0; i < show.size(); i++) {
	        	day= new Arrays(show.get(i).getDate(), show.get(i).getSchedule());
	        	dailyarray.add(day);		//day를 통해 dailyarray에 넣어준다
	        }
	        ListAdapter adapter = new ListAdapter(main, R.layout.customlist, dailyarray);		//어댑터 준비
	        ListView mylist = (ListView) v.findViewById(R.id.dailylist);	// 띄울 listview의 id 갖고옴
	        mylist.setOnItemLongClickListener(mOnItemLongClickListener);	// listview 에 longclicklistener 지정
	        mylist.setAdapter(adapter);		//준비된 어댑터를 연결
	        DailyMemoDAO dmdao = new DailyMemoDAO(main);		//db준비
	        String temp = dmdao.select(ScheduleActivity.today);	//오늘 메모 갖고옴
	        final TextView txt = (TextView) v.findViewById(R.id.firstmemo);		//메모 띄울 textview
	        txt.setText(temp);	//지정
	        firstTitle =(TextView)v.findViewById(R.id.firsttitle);
	        setFirstTitle(ScheduleActivity.today);
			break;
		case 1:
			v = layoutInflater.inflate(R.layout.second, null);
			weekListView = (ListView) v.findViewById(R.id.secondlistview);
			mWeekList = new ArrayList<WeekInfo>();
			mThisWeekCalendar = Calendar.getInstance();
			//주간 메모
			weekMemo = (TextView) v.findViewById(R.id.weekmemo);
			getWeek(mThisWeekCalendar);
			break;
		case 2:
			v = layoutInflater.inflate(R.layout.third, null);
			thirdTitle = (TextView) v.findViewById(R.id.thirdtitle);
			mDayList = new ArrayList<DayInfo>();
			mGvCalendar = (GridView) v.findViewById(R.id.calendar);
			mThisMonthCalendar = Calendar.getInstance();
			mThisMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);
			getCalendar(mThisMonthCalendar);
			
			break;
		}
		((ViewPager)view).addView(v, 0);
		return v;
	}
	@Override
	public boolean isViewFromObject(View view, Object obj) {	//instantiateItem메소드에서 생성한 객체를 이용할 것인지 여부를 반환 하는 메소드
		return view == obj;
	}
	
	AdapterView.OnItemLongClickListener mOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {		//longclicklistener 지정

		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			//Toast.makeText(main, SwipeTestActivity.today+" "+dailyarray.get(position).mTime, Toast.LENGTH_SHORT).show();
			array = dailyarray.get(position);	//longclick을 받은 array의 포지션을 저장
			
			LinearLayout linear = (LinearLayout) View.inflate(main, R.layout.updatedelete, null); 	//dialog의 화면을 갖고옴
			Button update = (Button) linear.findViewById(R.id.update);		//버튼 id 지정
			Button delete = (Button) linear.findViewById(R.id.delete);
			update.setOnClickListener(mOnClickListener);
			delete.setOnClickListener(mOnClickListener);		//리스너 연결

			editdialog = new AlertDialog.Builder(main)
			.setTitle("수정 또는 삭제")
			.setView(linear)
			.setNegativeButton("취소",null)
			.show();
			return false;
		}
	};
	
	View.OnClickListener mOnClickListener = new View.OnClickListener() {
		
		public void onClick(View v) {
			switch(v.getId()) {
			case R.id.update :
				String[] tt = new String[2];
				Intent intent = new Intent(main, AddSchedule.class);
				tt[0] = ScheduleActivity.today+" "+array.mTime;
				tt[1] = array.mSchedule;
				editdialog.dismiss();
				intent.putExtra("data",tt);
				main.startActivity(intent);
				break;
			case R.id.delete :
				try {
					DailyScheduleDAO ddao = new DailyScheduleDAO(main);		//db준비
					ddao.delete(ScheduleActivity.today+" "+array.mTime, array.mSchedule);		//데이터 삭제
					editdialog.dismiss();		//dialog를 닫는다
					ScheduleActivity.refresh();		//원래화면 새로고침
					Toast.makeText(main, "삭제되었습니다", Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(main, "에러가 발생하였습니다", Toast.LENGTH_SHORT).show();
				}
				break;
			}
		}
	};
	
	public void changeTitle(int num, boolean arrow) {		//arrow : 누른 화살표 방향
		switch(num) {
		case 0 : 
			firstTitle.setText(ScheduleActivity.today);
			break;
		case 1 :
			if(arrow) {
				mThisWeekCalendar = getNextWeek(mThisWeekCalendar);
			} else {
				mThisWeekCalendar = getLastWeek(mThisWeekCalendar);
			}
			getWeek(mThisWeekCalendar);
			break;
		case 2 :
			if(arrow) {		//오른쪽 화살표 누를 경우
				mThisMonthCalendar = getNextMonth(mThisMonthCalendar);
			} else {		//왼쪽 화살표 누를 경우
				mThisMonthCalendar = getLastMonth(mThisMonthCalendar);	
			}
			getCalendar(mThisMonthCalendar);
			break;
		}
	}
	
	private void getWeek(Calendar calendar){
		Calendar[] weekDate = new Calendar[7];
		int dayOfMonth=calendar.get(Calendar.DAY_OF_WEEK);
		
		mWeekList.clear();
		calendar.add( Calendar.DATE, -dayOfMonth); // today를 일요일로 설정 
		
		for( int i = 0 ; i < Calendar.DAY_OF_WEEK ; i++) { 
			calendar.add(Calendar.DATE, 1); 
			weekDate[i] = (Calendar)calendar.clone(); 
		}
		WeekInfo weekInfo;
		DailyScheduleDAO ddao = new DailyScheduleDAO(main);
		ArrayList<DailyScheduleVO> show = ddao.weekSelect(weekDate);
		for(int i=0 ; i < show.size() ; i++) {
			weekInfo = new WeekInfo();
			weekInfo.setWeekAndDate(show.get(i).getDate());
			weekInfo.setmSchedule(show.get(i).getSchedule());
			mWeekList.add(weekInfo);
		}
		
		
		
		initWeekAdapter();
	}
	
	private void getCalendar(Calendar calendar)
	{
		int lastMonthStartDay;
		int dayOfMonth;
		int thisMonthLastDay;
		
		mDayList.clear();
		
		// 이번달 시작일의 요일을 구한다. 시작일이 일요일인 경우 인덱스를 1(일요일)에서 8(다음주 일요일)로 바꾼다
		dayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
		thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		calendar.add(Calendar.MONTH, -1);

		// 지난달의 마지막 일자를 구한다.
		lastMonthStartDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		calendar.add(Calendar.MONTH, 1);
		
		/*if(dayOfMonth == SUNDAY)		//시작일이 일요일인 경우 한주 뒤로 미룬다
		{
			dayOfMonth += 7;
		}*/
		
		lastMonthStartDay -= (dayOfMonth-1)-1;
		

		// 캘린더 타이틀(년월 표시)을 세팅한다.
		String year = mThisMonthCalendar.get(Calendar.YEAR)+"";
		String month = "";
		if((mThisMonthCalendar.get(Calendar.MONTH) + 1) < 10) {
			month = "0"+(mThisMonthCalendar.get(Calendar.MONTH) + 1);
		} else {
			month =(mThisMonthCalendar.get(Calendar.MONTH) + 1)+"";
		}
		
		setThirdTitle(year+ "년 " + month + "월");
		
		DailyScheduleDAO ddao = new DailyScheduleDAO(main);
		ArrayList<String> show = ddao.monthSelect(year+"-"+month);
		
		
		DayInfo day;
		
		for(int i=0; i<dayOfMonth-1; i++)
		{
			int date = lastMonthStartDay+i;
			day = new DayInfo();
			day.setDay(Integer.toString(date));
			day.setInMonth(false);
			day.setEvent(false);
			
			mDayList.add(day);
		}
		for(int i=1; i <= thisMonthLastDay; i++)
		{
			day = new DayInfo();
			day.setDay(Integer.toString(i));
			day.setInMonth(true);
			String m="";
			if(i < 10 ) {
				m = "0"+i;
			} else {
				m = i+"";
			}
			
			for(int j=0 ; j < show.size() ; j++) {
				if(m.equals(show.get(j))) {
					day.setEvent(true);
					break;
				} else {
					day.setEvent(false);
				}
			}
			mDayList.add(day);
		}
		for(int i=1; i<42-(thisMonthLastDay+dayOfMonth-1)+1; i++)
		{
			day = new DayInfo();
			day.setDay(Integer.toString(i));
			day.setInMonth(false);
			day.setEvent(false);
			mDayList.add(day);
		}
		
		initCalendarAdapter();
	}
	
	private void initWeekAdapter() {
		DailyMemoDAO dm2dao = new DailyMemoDAO(main);
		String wm = "";
		for(int i=0 ; i<mWeekList.size();i++) {
			if(dm2dao.select(mWeekList.get(i).getWeekAndDate())!=null)
				wm += dm2dao.select(mWeekList.get(i).getWeekAndDate())+"\n";
		}
		weekMemo.setText(wm);
		mWeekAdapter = new WeekAdapter(main, wb.ml.R.layout.weekcustomlist, mWeekList);
		weekListView.setAdapter(mWeekAdapter);
		weekListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				goToFirst(mWeekList.get(position).getWeekAndDate());
			}
		});
	}
	
	private void initCalendarAdapter() {
		mCalendarAdapter = new CalendarAdapter(main, wb.ml.R.layout.day, mDayList);
		mGvCalendar.setAdapter(mCalendarAdapter);
		mGvCalendar.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(position > 6) {
					if( mDayList.get(position-7).isInMonth() ) {
						String year = mThisMonthCalendar.get(Calendar.YEAR)+"";
						String month = "";
						if((mThisMonthCalendar.get(Calendar.MONTH) + 1)<10){
							month="0"+(mThisMonthCalendar.get(Calendar.MONTH) + 1);
						} else {
							month=(mThisMonthCalendar.get(Calendar.MONTH) + 1)+"";
						}
						String day="";
						if(Integer.parseInt(mDayList.get(position-7).getDay())<10) {
							day = "0"+mDayList.get(position-7).getDay();
						} else {
							day = mDayList.get(position-7).getDay();
						}
						goToFirst(year+"-"+month+"-"+day);
					}
				}
			}
		});
	}
	
	private void goToFirst(String day1) {
		ScheduleActivity.today = day1;
		ScheduleActivity.position=0;
		refresh(ScheduleActivity.position);
		setFirstTitle(ScheduleActivity.today);
		ScheduleActivity.refresh();
	}
	
	/**
	 * 지난달의 Calendar 객체를 반환합니다.
	 * 
	 * @param calendar
	 * @return LastMonthCalendar
	 */
	private Calendar getLastMonth(Calendar calendar)
	{
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
		calendar.add(Calendar.MONTH, -1);
		return calendar;
	}

	/**
	 * 다음달의 Calendar 객체를 반환합니다.
	 * 
	 * @param calendar
	 * @return NextMonthCalendar
	 */
	private Calendar getNextMonth(Calendar calendar)
	{
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
		calendar.add(Calendar.MONTH, +1);
		return calendar;
	}
	
	private Calendar getLastWeek(Calendar calendar) {
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
		calendar.add(Calendar.DATE, -7);
		return calendar;
	}
	
	private Calendar getNextWeek(Calendar calendar) {
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
		calendar.add(Calendar.DATE, +7);
		return calendar;
	}
	
	void refresh(int position) {
		mViewPager.setCurrentItem(position);
	}
}
