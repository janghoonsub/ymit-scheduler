package wb.ml.timetable;

import java.util.ArrayList;

import wb.ml.R;
import wb.ml.domain.SchoolTableDAO;
import wb.ml.domain.SchoolTableVO;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TimeTableAdapter extends BaseAdapter {
	private Context mContext;
	private int count=56;
	private int mResource;
	private LayoutInflater mLayoutInflater;
	String[] mWeekTitleIds= { "시간", " 월", " 화", " 수", " 목", " 금", " 토" };
	private SchoolTableDAO dao;
	//private SchoolTableVO vo;
	private ArrayList<SchoolTableVO> sub;
	
	public TimeTableAdapter(Context context, int textResource) {		//생성자 함수
		mContext = context;
		mResource = textResource;
		mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		dao = new SchoolTableDAO(mContext);
		sub = dao.select();
	}
	
	
	public int getCount() {
		return count;
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {		//그리드뷰 화면 구성
		
		ViewHolde viewHolde;	//아래 따로 만들어논 클래스에요. 각각 하나의 셀을 표시하기 위해서 만든 것.
		
		if(convertView == null) {
			convertView = mLayoutInflater.inflate(mResource, null);
			
			if(position % 7 == 6) {			//아래 설정한 각 하나의 셀의 크기를 정하기 위한 부분
				convertView.setLayoutParams(new GridView.LayoutParams(getCellWidthDP()+getRestCellWidthDP(), getCellHeightDP()));
			} else {
				convertView.setLayoutParams(new GridView.LayoutParams(getCellWidthDP(), getCellHeightDP()));
			}
			
			viewHolde = new ViewHolde();
			
			viewHolde.background = (LinearLayout) convertView.findViewById(R.id.cell_ll_background);
			viewHolde.textView = (TextView) convertView.findViewById(R.id.time_cell);
			
			convertView.setTag(viewHolde);		//여기까지가 기본적인 화면 처리
		} else {
			viewHolde = (ViewHolde) convertView.getTag();
		}
		
		
		if(position <= 6) {		//첫줄에 요일표시하기 위한 코드
			if(position == 0)
				viewHolde.textView.setTextSize((float)12.0);
			viewHolde.textView.setText(mWeekTitleIds[position]);
			viewHolde.textView.setTextColor(Color.BLACK);
			
		} else if(position %7 == 0) {	//	세로 첫줄에 몇교시인지 표시하는 코드
			viewHolde.textView.setText((position/7)+"교시");
			viewHolde.textView.setTextColor(Color.BLUE);
			viewHolde.textView.setTextSize((float)11.0);
		}
		
		for(int i=0 ; i<sub.size() ; i++) {		//시간표 저장한 것이 있다면 표시해주는 코드
			if(sub.get(i).getPosition() == position) {
				viewHolde.textView.setText(sub.get(i).getSubject());
				viewHolde.background.setBackgroundColor(sub.get(i).getColor());
			}
		}
		
		
		return convertView;
	}
	
	
	public class ViewHolde {
		public LinearLayout background;
		public TextView textView;
	}
	
	
	private int getCellWidthDP()
	{
		int cellWidth = 480/7;
		return cellWidth;
	}
	
	private int getRestCellWidthDP()
	{
		int cellWidth = 480%7;
		return cellWidth;
	}
	
	private int getCellHeightDP()
	{
		int cellHeight = 480/6;
		return cellHeight;
	}
	
}
