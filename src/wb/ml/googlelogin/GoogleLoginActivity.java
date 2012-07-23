package wb.ml.googlelogin;

import wb.ml.R;
import wb.ml.domain.AccessTokenDAO;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;

public class GoogleLoginActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		WebView webView = (WebView) findViewById(R.id.login);
		webView.setWebViewClient(new GoogleWebViewClient(this));
		AccessTokenDAO accessTokenDAO = new AccessTokenDAO(this);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("구글 로그인");
		// DB에 이미 토큰이 있다면  --> 차후에 토큰이 유효한가에 대한 확인 필요. refresh와 연관됨
		if(accessTokenDAO.exist()){
			Log.d("accessTokenDAO.exist()", "true");
			webView.loadUrl("file:///android_asset/www/login.html");
		} else{
			Log.d("accessTokenDAO.exist()", "false");
			webView.loadUrl(GoogleCalendarAdapter.getInstance().getAuthorizationUrl());
		}	
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home : finish(); break;
		}
		return super.onOptionsItemSelected(item);
	}
}
