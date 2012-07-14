package wb.ml.googlelogin;

import java.io.IOException;

import wb.ml.domain.AccessTokenDAO;
import wb.ml.domain.AccessTokenVO;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class GoogleWebViewClient extends WebViewClient {
	String code;
	Context context = null;
	
	public GoogleWebViewClient(Context context) {
		super();
		this.context = context;
	}
	
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		if(Uri.parse(url).getHost().equals("localhost")) {
			view.loadUrl("file:///android_asset/www/login.html");
			return true;
		}
		view.loadUrl(url);
		return true;
	}
	
	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
		if(url.indexOf("code=")!=-1) {
			code = url.substring(url.indexOf("code=")+5);
			new RequestToken().execute(null,null,null);
		}
	}
	
	private class RequestToken extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				GoogleCalendarAdapter instance = GoogleCalendarAdapter.getInstance();
				AccessTokenDAO accessTokenDAO = new AccessTokenDAO(context);
				AccessTokenVO atvo = AccessTokenVO.getInstance();
				instance.setCode(code);
				instance.setToken();
				accessTokenDAO.insert(atvo);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
