package wb.ml.plan;

import wb.ml.R;
import wb.ml.domain.ExamPlannerDateDAO;
import wb.ml.domain.ExamPlannerVO;
import wb.ml.domain.ExamPlannerDAO;
import wb.ml.timetable.TimeTableAdapter.ViewHolde;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ExamPlanAdapter extends BaseAdapter {
	private Context context;
	private int mResource;
	private LayoutInflater mLayoutInflater;

	public ExamPlanAdapter(Context context, int textResource) {
		this.context = context;
		mResource = textResource;
		mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public int getCount() {
		return 30;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolde viewHolde;
		if(convertView == null) {
			convertView = mLayoutInflater.inflate(mResource, null);
			
			if(position < 5) {
				convertView.setLayoutParams(new GridView.LayoutParams(96, 40));
			} else {
				convertView.setLayoutParams(new GridView.LayoutParams(96, 65));
			}
		
			viewHolde = new ViewHolde();
			
			viewHolde.background = (LinearLayout) convertView.findViewById(R.id.cell_ll_background);
			viewHolde.textView = (TextView) convertView.findViewById(R.id.time_cell);
			
			convertView.setTag(viewHolde);
		} else {
			viewHolde = (ViewHolde) convertView.getTag();
		}
		
		if(position>0 && position<5) {
			ExamPlannerDateDAO epddao = new ExamPlannerDateDAO(context);
			if(epddao.select() != null) {
				String[] cal = epddao.select().split("-");
				String month, date;
				if(Integer.parseInt(cal[1])<10) {
					month = "0"+(Integer.parseInt(cal[1])+1);
				} else {
					month = (Integer.parseInt(cal[1])+1)+"";
				}
				int d = Integer.parseInt(cal[2]);
				switch(position) {
				case 2 : 
					d = d+1;
					break;
				case 3 : 
					d = d+2;
					break;
				case 4 : 
					d = d+3;
					break;
				}
				if(d<10) {
					date = "0"+d;
				} else {
					date = d+"";
				}
				viewHolde.textView.setText(month+"/"+date);
				viewHolde.textView.setTextSize(12);
			}
		} else if(position == 5) {
			viewHolde.textView.setText("Set");
		} else if(5<position && position<10) {
			viewHolde.textView.setText((position-5)+"일");
			viewHolde.textView.setTextColor(Color.LTGRAY);
		} else if(position%5==0 && position>9) {
			viewHolde.textView.setText(((position/5)-1)+"교시");
			viewHolde.textView.setTextColor(Color.LTGRAY);
		}
		
		ExamPlannerDAO edao = new ExamPlannerDAO(context);
		if(edao.exist(position)) {
			ExamPlannerVO evo = new ExamPlannerVO();
			evo = edao.select(position);
			if(evo.getSuject().length()<2) {
				viewHolde.textView.setText(evo.getSuject().substring(0, 1));
			} else {
				viewHolde.textView.setText(evo.getSuject().substring(0, 2));
			}
		}
		
		
		return convertView;
	}
	
	public class ViewHolde {
		public LinearLayout background;
		public TextView textView;
	}
}
