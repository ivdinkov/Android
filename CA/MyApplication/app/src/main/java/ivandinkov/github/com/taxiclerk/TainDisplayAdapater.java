package ivandinkov.github.com.taxiclerk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by iv on 27/09/2017.
 */

class TainDisplayAdapater extends BaseAdapter{
	private ArrayList<HashMap<String,String>> dataSource = new ArrayList<>();
	private Context mContext;
	private LayoutInflater layoutInflater;
	
	public TainDisplayAdapater(Context context, ArrayList<HashMap<String,String>> dataSource){
		this.dataSource = dataSource;
		mContext = context;
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		return dataSource.size();
	}
	
	@Override
	public Object getItem(int position) {
		return dataSource.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View row = convertView;
		MyHolder myHolder = null;
		if(convertView == null){
			row = layoutInflater.inflate(R.layout.single_train,parent,false);
			myHolder = new MyHolder(row);
			row.setTag(myHolder);
		}else{
			row = convertView;
			myHolder = (MyHolder) row.getTag();
		}
		HashMap<String,String> currentItem = dataSource.get(position);
		myHolder.txtFrom.setText(currentItem.get("origin"));
		myHolder.txtTo.setText(currentItem.get("destination"));
		myHolder.txtDelay.setText(currentItem.get("late"));
		myHolder.txtTime.setText(currentItem.get("arrival"));
		return row;
	}
	
	static class MyHolder{
		TextView txtFrom;
		TextView txtTo;
		TextView txtTime;
		TextView txtDelay;
		public MyHolder(View view){
			txtFrom = (TextView) view.findViewById(R.id.txtFrom);
			txtTo = (TextView) view.findViewById(R.id.txtTo);
			txtTime = (TextView) view.findViewById(R.id.txtTime);
			txtDelay = (TextView) view.findViewById(R.id.txtDelay);
		}
	}
}
