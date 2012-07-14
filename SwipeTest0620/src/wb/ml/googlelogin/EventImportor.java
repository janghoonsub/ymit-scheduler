package wb.ml.googlelogin;

import java.util.ArrayList;

import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import android.content.Context;
import android.util.Log;
import wb.ml.Trigger;
import wb.ml.domain.AccessTokenDAO;
import wb.ml.domain.AccessTokenVO;
import wb.ml.domain.AddEmailDAO;
import wb.ml.domain.DailyScheduleDAO;
import wb.ml.domain.DailyScheduleVO;
import wb.ml.domain.GoogleEventListDAO;

public class EventImportor extends Thread {
	Context context;
	GoogleCalendarAdapter instance = GoogleCalendarAdapter.getInstance();
	static long a;
	
	public EventImportor(Context context) {
		this.context = context;
	}
	
	public void run() {
		while(Trigger.run) {
			try {
				callGoogleCalendarAdapter();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			try { Thread.sleep(10000);} catch (Exception e) { ; }
		}
	}
	
	private void callGoogleCalendarAdapter() throws Exception {
		AccessTokenDAO accessTokenDAO = new AccessTokenDAO(context);
		accessTokenDAO.select();
		AccessTokenVO accessTokenVO = AccessTokenVO.getInstance();
		
		a = (System.currentTimeMillis()-accessTokenVO.getCurrentTime())/(1000*60); 
		if(a<60) {
			eventList();
			accessTokenDAO.updateCurrentTime();
		} else {
			accessTokenDAO.delete1();
			Trigger.run=false;
		}
	}
	
	private void eventList() throws Exception {
		instance.getToken();
		instance.getService();
		Events events = instance.getEvents();
		parsingEvent(events);
		
		String email = null;
		AddEmailDAO adao = new AddEmailDAO(context);
		email = adao.select();
		if(email != null) {
			events = instance.getEventanother(email);
			parsingEvent(events);
		}
	}
	
	private void parsingEvent(Events events) {
		GoogleEventListDAO gdao = new GoogleEventListDAO(context);
		ArrayList<String> listgvo = gdao.search();
		
		for(Event event : events.getItems()) {
			
			boolean flag = false;
			//비교할 db가 비어 있지 않은 경우엔
			if(listgvo.size() != 0) {
				for(int i=0 ; i<listgvo.size() ; i++) {
					//구글에서 가져온 일정을 기존 db와 비교하여 이미 있다면 flag를 true로. 새로운 것이면 false.
					if(event.getId().equals(listgvo.get(i))) {
						flag = true;
						break;
					}
				}
			}
			//새로추가된 일정인 경우
			if(flag == false) {
				//1. 기존 비교할 db에 추가한다.
				gdao.insert(event.getId());
				//2. 일정에 추가한다.
				insertList(event.getSummary(), event.getStart().toString());
			}
			
		}
	}
	
	
	private void insertList(String summary, String event) {
		DailyScheduleDAO ddao = new DailyScheduleDAO(context);
		DailyScheduleVO dvo = new DailyScheduleVO();
		
		String[] ab = event.substring(13, 29).split("T");				//ex)	{"dateTime":"2012-07-30T08:00:00.800+09:00"}
																		//		String ab[0] = 2012-07-30 ab[1] = 08:00
		dvo.setDate(ab[0]+" "+ab[1]);
		dvo.setSchedule(summary);
		ddao.insert(dvo);
	}
}
