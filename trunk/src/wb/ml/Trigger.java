package wb.ml;

import wb.ml.googlelogin.EventImportor;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class Trigger extends Service {
	public static NotificationManager mNotiManager;
	EventImportor importor;
	public static boolean run;
	
	private static Trigger trigger = null;
	public static Trigger getInstance() {
		if(trigger == null)
			trigger = new Trigger();
		return trigger;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		importor = new EventImportor(this);
		run=true;
		importor.start();
		mNotiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		run=false;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
