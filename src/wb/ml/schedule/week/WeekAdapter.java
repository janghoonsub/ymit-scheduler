package wb.ml.schedule.week;

import java.util.ArrayList;
import wb.ml.R;
import wb.ml.schedule.TabAdapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class WeekAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<WeekInfo> weekList;
	private int mResource;
	private LayoutInflater inflater;
	private String[] weekday = {"일", "월", "화", "수", "목", "금", "토"};
	
	public WeekAdapter(Context context, int mResource, ArrayList<WeekInfo> weekList) {
		this.context = context;
		this.weekList = weekList;
		this.mResource = mResource;
		this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return weekList.size();
	}

	public Object getItem(int position) {
		return weekList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		WeekViewHolde weekViewHolde;
		
		if(convertView == null) {
			convertView = inflater.inflate(mResource, parent, false);
			
			weekViewHolde = new WeekViewHolde();
			
			weekViewHolde.weekDate = (TextView) convertView.findViewById(R.id.weekdate);
			weekViewHolde.weekSchedule = (TextView) convertView.findViewById(R.id.weekschedule);
			
			convertView.setTag(weekViewHolde);
			
		} else {
			weekViewHolde = (WeekViewHolde) convertView.getTag();
		}
		
		String[] t = weekList.get(position).getWeekAndDate().split("-");
		
		
		weekViewHolde.weekDate.setText(weekday[position] +" "+t[1]+"-"+t[2]);
		weekViewHolde.weekSchedule.setText(weekList.get(position).getmSchedule());
		
		return convertView;
	}
	
	public class WeekViewHolde {
		public TextView weekDate;
		public TextView weekSchedule;
	}
}
