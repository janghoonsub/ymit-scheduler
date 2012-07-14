package wb.ml.schedule.day;

import java.util.ArrayList;

import wb.ml.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

//커스텀 리스트뷰를 만들기 위한 클래스
public class ListAdapter extends BaseAdapter {
	Context context;
	LayoutInflater inflater;
	ArrayList<Arrays> list;
	int layout;
	
	public ListAdapter(Context c,int l, ArrayList<Arrays> ll) {
		context = c;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = l;
		list = ll;	
	}
	
	public int getCount() {
		return list.size();
	}

	public String getItem(int position) {
		return list.get(position).mSchedule;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		//final int pos = position;
		if(convertView == null) {
			convertView = inflater.inflate(layout , parent, false);
		}
		
		TextView time = (TextView) convertView.findViewById(R.id.time);
		TextView schedule = (TextView) convertView.findViewById(R.id.schedule);
		
		time.setText(list.get(position).mTime);
		schedule.setText(list.get(position).mSchedule);
		
		return convertView;
	}
	
}
