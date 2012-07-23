package wb.ml.googlelogin;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import wb.ml.R;
import wb.ml.Trigger;
import wb.ml.domain.AccessTokenDAO;
import wb.ml.domain.AccessTokenVO;
import wb.ml.domain.AddEmailDAO;
import wb.ml.domain.DailyScheduleDAO;
import wb.ml.domain.DailyScheduleVO;
import wb.ml.domain.GoogleEventListDAO;
import wb.ml.domain.NoticeListDAO;
import wb.ml.domain.NoticeListVO;
import wb.ml.notice.NoticeListActivity;

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
				
				String r = regularExpression(event.getSummary() + " " + event.getLocation());
				if(r != null) {
					//2-1 만약 제목이나 장소에 @태그 값이 있으면 공지에 추가한다.
					insertNotice(r, event.getStart().toString());
				} else {
					//2-2. 일정에 추가한다.
					insertList(event.getSummary(), event.getStart().toString());
				}
				
			}
			
		}
	}
	
	@SuppressWarnings("deprecation")
	private void insertNotice(String summary, String time) {
		NoticeListDAO ndao = new NoticeListDAO(context);
		NoticeListVO nvo = new NoticeListVO();
		
		if(time.length() == 21) {	//일정이 하루종일인 경우
			nvo.setDate(time.substring(9, 19)+" 00:00");
		} else {					//일정 시간이 정해져 있는 경우
			String[] ab = time.substring(13, 29).split("T");				//ex)	{"dateTime":"2012-07-30T08:00:00.800+09:00"}
																			//		String ab[0] = 2012-07-30 ab[1] = 08:00
			nvo.setDate(ab[0]+" "+ab[1]);
		}
		
		nvo.setDesc(summary);
		ndao.insert(nvo);
		
		
		
		//통지
		Notification noti = new Notification(R.drawable.main_img, "공지가 도착했습니다.", System.currentTimeMillis());
		noti.defaults |= Notification.DEFAULT_SOUND;
		// 진동 사용
		noti.defaults |= (Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
		Intent intent = new Intent(context, NoticeListActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent content = PendingIntent.getActivity(context, 0, intent, 0);
		noti.setLatestEventInfo(context, "공지가 도착했습니다", summary, content);
		Trigger.mNotiManager.notify(1, noti);
		
	}
	
	
	private void insertList(String summary, String event) {
		DailyScheduleDAO ddao = new DailyScheduleDAO(context);
		DailyScheduleVO dvo = new DailyScheduleVO();
		
		if(event.length() == 21) {	//일정이 하루종일인 경우
			dvo.setDate(event.substring(9, 19)+" 00:00");
		} else {					//일정 시간이 정해져 있는 경우
			String[] ab = event.substring(13, 29).split("T");				//ex)	{"dateTime":"2012-07-30T08:00:00.800+09:00"}
																			//		String ab[0] = 2012-07-30 ab[1] = 08:00
			dvo.setDate(ab[0]+" "+ab[1]);
		}
		
		
		dvo.setSchedule(summary);
		ddao.insert(dvo);
	}
	
	private String regularExpression(String search) {
		Pattern p =Pattern.compile("[^\uAC00-\uD7A3xfe@0-9a-zA-Z\\s]");
		Pattern p2=Pattern.compile("@[a-z|A-Z|ㄱ-ㅎㅏ-ㅣ가-힣|0-9]*");
		Matcher m =p.matcher(search);
		Matcher m2=p2.matcher(search);
		if(m.find())
			return null;
		else if(m2.find()) {
			String op1= m2.group().substring(m2.group().indexOf('@')+1);
			return op1;
		}
		return null;
	}
}
