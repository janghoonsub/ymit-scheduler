package wb.ml.schedule.calendar;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CalendarAdapter extends BaseAdapter {
	private ArrayList<DayInfo> mDayList;
	private Context mContext;
	private int mResource;
	private LayoutInflater mLiInflater;
	private String[] dayOfWeek = {"일","월","화","수","목","금","토"};
	
	public CalendarAdapter(Context context, int textResource, ArrayList<DayInfo> dayList)
	{
		this.mContext = context;
		this.mDayList = dayList;
		this.mResource = textResource;
		this.mLiInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public int getCount() {
		return mDayList.size()+7;
	}

	public Object getItem(int position) {
		return mDayList.get(position);
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{

		DayViewHolde dayViewHolder;

		if(convertView == null)
		{
			convertView = mLiInflater.inflate(mResource, null);

			if(position % 7 == 6)
			{
				convertView.setLayoutParams(new GridView.LayoutParams(getCellWidthDP()+getRestCellWidthDP(), getCellHeightDP()));
			}
			else
			{
				convertView.setLayoutParams(new GridView.LayoutParams(getCellWidthDP(), getCellHeightDP()));	
			}
			
			
			dayViewHolder = new DayViewHolde();

			dayViewHolder.llBackground = (LinearLayout)convertView.findViewById(wb.ml.R.id.day_background);
			dayViewHolder.tvDay = (TextView) convertView.findViewById(wb.ml.R.id.day);
			
			convertView.setTag(dayViewHolder);
		}
		else
		{
			dayViewHolder = (DayViewHolde) convertView.getTag();
		}
		
		
		if(position < 7) {
			dayViewHolder.tvDay.setText(dayOfWeek[position]);
			if(position % 7 ==0) {
				dayViewHolder.tvDay.setTextColor(Color.RED);
			} else if(position % 7 == 6) {
				dayViewHolder.tvDay.setTextColor(Color.BLUE);
			}
		} else {
			DayInfo day = mDayList.get(position - 7);
			
			if(day != null) {
				
				dayViewHolder.tvDay.setText(day.getDay());

				if(day.isInMonth()) {
					if(day.isEvent()) {
						dayViewHolder.tvDay.setBackgroundColor(Color.YELLOW);
					}
					if(position % 7 == 0) {
						dayViewHolder.tvDay.setTextColor(Color.RED);
					}
					else if(position % 7 == 6) {
						dayViewHolder.tvDay.setTextColor(Color.BLUE);
					}
					else {
						dayViewHolder.tvDay.setTextColor(Color.BLACK);
					}
				}
				else {
					dayViewHolder.tvDay.setTextColor(Color.GRAY);
				}
			}
		}

		return convertView;
	}

	public class DayViewHolde
	{
		public LinearLayout llBackground;
		public TextView tvDay;
		
	}

	private int getCellWidthDP()
	{
//		int width = mContext.getResources().getDisplayMetrics().widthPixels;
		int cellWidth = 480/7;
		
		return cellWidth;
	}
	
	private int getRestCellWidthDP()
	{
//		int width = mContext.getResources().getDisplayMetrics().widthPixels;
		int cellWidth = 480%7;
		
		return cellWidth;
	}
	
	private int getCellHeightDP()
	{
//		int height = mContext.getResources().getDisplayMetrics().widthPixels;
		int cellHeight = 480/6;
		
		return cellHeight;
	}

}
