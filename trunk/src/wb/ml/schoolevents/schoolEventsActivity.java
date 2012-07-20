package wb.ml.schoolevents;

import wb.ml.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

public class schoolEventsActivity extends Activity {
	ViewFlipper mFlip;
	GestureDetector mDetector;
	final int DISTANCE = 100;
	final int VELOCITY = 200; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schoolevents);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		mFlip = (ViewFlipper) findViewById(R.id.schooleventsflip);
		mDetector = new GestureDetector(this, mGestureListener);
		mDetector.setIsLongpressEnabled(false);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home : finish(); break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		return mDetector.onTouchEvent(event);
	}
	
	OnGestureListener mGestureListener = new OnGestureListener() {

		public boolean onDown(MotionEvent e) {return false;}
		public void onLongPress(MotionEvent e) {}
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {return false;}
		public void onShowPress(MotionEvent e) {}
		public boolean onSingleTapUp(MotionEvent e) {return false;}
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			if(Math.abs(velocityX) > VELOCITY) {
				if(e1.getX() - e2.getX() > DISTANCE) {
					mFlip.setInAnimation(AnimationUtils.loadAnimation(schoolEventsActivity.this, R.anim.push_left_in));
					mFlip.setOutAnimation(AnimationUtils.loadAnimation(schoolEventsActivity.this, R.anim.push_left_out));
					mFlip.showPrevious();
				}
				if(e2.getX() - e1.getX() > DISTANCE) {
					mFlip.setInAnimation(AnimationUtils.loadAnimation(schoolEventsActivity.this, R.anim.push_right_in));
					mFlip.setOutAnimation(AnimationUtils.loadAnimation(schoolEventsActivity.this, R.anim.push_right_out));
					mFlip.showNext();
				}
			}
			return true;
		}
	};
}
