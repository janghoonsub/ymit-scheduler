package wb.ml.googlelogin;

import java.io.IOException;
import java.util.GregorianCalendar;

import wb.ml.domain.AccessTokenVO;
import android.util.Log;
import com.google.api.client.auth.oauth2.draft10.AccessTokenResponse;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessProtectedResource;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAuthorizationRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessTokenRequest.GoogleAuthorizationCodeGrant;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Events;

public class GoogleCalendarAdapter {

	private static GoogleCalendarAdapter instance = null;
	private HttpTransport httpTransport = null;
	private JacksonFactory jsonFactory = null;
	private String clientId = null;
	private String clientSecret = null;
	private String redirectUrl = null;
	private String scope = null;  
	private String code = null;
	private String authorizationUrl = null;
	private AccessTokenVO accessTokenVO = AccessTokenVO.getInstance();
	private AccessTokenResponse accessTokenResponse = null;
	private GoogleAccessProtectedResource accessProtectedResource = null;
	private Calendar service = null;


	private GoogleCalendarAdapter(){
		httpTransport = new NetHttpTransport();
		jsonFactory = new JacksonFactory();
		clientId = "922857426869-6selh6031gjo48f8nlmpnm2kei9b8hh0.apps.googleusercontent.com";
		clientSecret = "pEcShH36RmGte3pHCd94Z5-V";
		redirectUrl = "http://localhost";
		scope = "https://www.googleapis.com/auth/calendar";  
	}

	public static GoogleCalendarAdapter getInstance() {
		if(instance ==null )
		{
			instance = new GoogleCalendarAdapter();
		}
		return instance;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	// 작성자 : 송태영
	// 내용 : DB에 토큰이 없는 경우 구글 서버로 부터 authorization code를 얻기 위한, auth url을 얻어 오는 메소드
	// 마지막 업데이트 날짜 : 2012-2-19
	public String getAuthorizationUrl() {
		return new GoogleAuthorizationRequestUrl(clientId, redirectUrl, scope).build();
	}	

	// 작성자 : 송태영, 박상희
	// 내용 : DB에 토큰이 없는 경우 구글 서버로 부터 토큰을 받아오는 메소드, 넣는 시간도 찍도록 함
	// 마지막 업데이트 날짜 : 2012-3-5
	public void setToken() throws IOException {
		Log.d("code", code);
		accessTokenResponse = new GoogleAuthorizationCodeGrant(httpTransport, jsonFactory,clientId, clientSecret, code, redirectUrl).execute();
		if(accessTokenResponse.accessToken != null){
			accessTokenVO.setAccessToken(accessTokenResponse.accessToken);
			//현재시간 넣는 부분.
			long lTime = System.currentTimeMillis();
			accessTokenVO.setCurrentTime(lTime);
		}
		if(accessTokenResponse.refreshToken != null){
			accessTokenVO.setRefreshToken(accessTokenResponse.refreshToken);
		}
		if(accessTokenResponse.expiresIn  != null){
//			Long time = new Long(accessTokenResponse.expiresIn + 3600);
			Long time = new Long(accessTokenResponse.expiresIn);
			accessTokenVO.setExpiresIn(time);	
			//token시간 찍어보기 위해서 생성, 3600이었음, +3600을 해도 반영됨, 임의로 값을 줘도 변경됨
			Log.d("expirese", time.toString());
		}
		accessProtectedResource = new GoogleAccessProtectedResource(accessTokenVO.getAccessToken(), httpTransport, jsonFactory, clientId, clientSecret, accessTokenVO.getRefreshToken());		
	}
	
	// 작성자 : 송태영
	// 내용 : DB에 토큰이 있는 경우 DB로 부터 토큰을 받아온 것을 전제로 (이 메소드가 호출되는 시점에는 
	// 이미 DB로 부터 accessTokenVO에 토큰이 복사 되어 있음) 구글로 부터 AccessProtectedResource를 얻어옴
	// 마지막 업데이트 날짜 : 2012-2-19
	public void getToken() throws IOException {
		accessProtectedResource = new GoogleAccessProtectedResource(accessTokenVO.getAccessToken(), httpTransport, jsonFactory, clientId, clientSecret, accessTokenVO.getRefreshToken());
	}	

	// 작성자 : 이현우, 박상희
	// 내용 : 구글로 부터 토큰을 리프레시 하는 메소드, 리플레쉬를 실행하면서 VO에 값을 넣도록 함
	// 마지막 업데이트 날짜 : 2012-3-05
	public AccessTokenVO refresh(){
	
		try {
			accessProtectedResource.refreshToken();
			Log.d(" New Token", accessProtectedResource.getAccessToken());
			
			String a = Long.toString(System.currentTimeMillis());
			Log.d(" New Token", a);
			accessTokenVO.setAccessToken(accessTokenResponse.accessToken);
			accessTokenVO.setCurrentTime(System.currentTimeMillis());
			accessTokenVO.setRefreshToken(accessTokenResponse.refreshToken);	
			accessTokenVO.setExpiresIn(accessTokenResponse.expiresIn);	
			return accessTokenVO;
			
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		Log.d(" New Token", "실패");
		return null;
	}
		
	// 작성자 : 송태영
	// 내용 : 구글로 부터 Calendar 클레스의 객체인 service를 얻어옴
	// 마지막 업데이트 날짜 : 2012-2-19
	public boolean getService(){
		service = Calendar.builder(httpTransport, jsonFactory).setApplicationName("Webcal_Automation").setHttpRequestInitializer(accessProtectedResource).build();
		if(service !=null)
			return true;
		return false;
	}

	// 작성자 : 송태영
	// 내용 : 구글로 부터 Events 객체를 받아오는 메소드들을 테스트 하기 위한 메소드
	// 마지막 업데이트 날짜 : 2012-2-18	
	public Events getEventsTest() throws IOException {
		Events events = service.events().list("primary").execute();
		return events;
	}
	
	// 작성자 : 송태영, 정민영
	// 내용 : 구글로 부터 Events 객체를 받아오는 메소드
	// 마지막 업데이트 날짜 : 2012-2-24	
	public Events getEvents() throws IOException {
		
        java.util.Calendar today = java.util.Calendar.getInstance() ;    
        String month;
        String year = Integer.toString(today.get(today.YEAR));
        if((today.get(today.MONTH) + 1)>10){
        	month = Integer.toString(today.get(today.MONTH) + 1);
        }else{
        	month = "0" + Integer.toString(today.get(today.MONTH) + 1);	
        }
        String date = Integer.toString(today.getActualMaximum(java.util.Calendar.DATE));
        if(Integer.parseInt(date) < 10) {
        	date = "0"+date;
        }
        
        Log.d("min", year + "-" + month + "-" + date + "T00:00:00Z");
		
        Events events = service
	                .events()
	                .list("primary")
//	                .setTimeMin("2011-01-01T00:00:00Z")
	                .setTimeMin(year + "-" + month + "-01T00:00:00Z")//오늘 0시부터 
	                .setTimeMax(year + "-" + month + "-" + date + "T23:59:59Z")//~까지 일정 받아옴.
	                .setSingleEvents(true)//singleevents:Valid values are true (expand recurring events) or false (leave recurring events represented as single events). Default is false.
//	                .setMaxResults(7) //한 화면에 7개만 가져옴.
	                .setOrderBy("startTime")
	                .execute();
        
		return events;
	}	
	
	public Events getEventanother(String email) throws IOException {
		
        java.util.Calendar today = java.util.Calendar.getInstance() ;    
        String month;
        String year = Integer.toString(today.get(today.YEAR));
        if((today.get(today.MONTH) + 1)>10){
        	month = Integer.toString(today.get(today.MONTH) + 1);
        }else{
        	month = "0" + Integer.toString(today.get(today.MONTH) + 1);	
        }
        String date = Integer.toString(today.getActualMaximum(java.util.Calendar.DATE));
        if(Integer.parseInt(date) < 10) {
        	date = "0"+date;
        }
        
        Log.d("min", year + "-" + month + "-" + date + "T00:00:00Z");
		
        Events events = service
	                .events()
	                .list(email)
//	                .setTimeMin("2011-01-01T00:00:00Z")
	                .setTimeMin(year + "-" + month + "-01T00:00:00Z")//오늘 0시부터 
	                .setTimeMax(year + "-" + month + "-" + date + "T23:59:59Z")//~까지 일정 받아옴.
	                .setSingleEvents(true)//singleevents:Valid values are true (expand recurring events) or false (leave recurring events represented as single events). Default is false.
//	                .setMaxResults(7) //한 화면에 7개만 가져옴.
	                .setOrderBy("startTime")
	                .execute();
        
		return events;
	}		
}
