package wb.ml.schoolevents;

import wb.ml.R;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class SampleAdapter extends PagerAdapter {
	private Context mContext;
	private int[] picture = {R.drawable.m0304,
			R.drawable.m0506, R.drawable.m0708,
			R.drawable.m0910, R.drawable.m1112,
			R.drawable.m0102};
	
	public SampleAdapter(Context context) {
		mContext = context;
	}
	
	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager)arg0).removeView((View) arg2);
	}

	@Override
	public void finishUpdate(View arg0) {
	}

	@Override
	public int getCount() {
		return 6;
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
		
		ImageView child = new ImageView(mContext);
		child.setImageResource(picture[arg1]);
		child.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		child.setScaleType(ScaleType.FIT_XY);
		((ViewPager)arg0).addView(child);
		
		
		return child;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View)arg1);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
	}

}
