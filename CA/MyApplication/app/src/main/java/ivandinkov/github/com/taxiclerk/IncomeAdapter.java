package ivandinkov.github.com.taxiclerk;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by iv on 24/09/2017.
 */

public class IncomeAdapter extends ArrayAdapter<Income> {
	
	private final Activity mContext;
	private final ArrayList<Income> list;
	//private final UpdateRecord updateCallback;
	Context mContex;
	private DisplayMetrics dm;
	
	public interface UpdateRecord {
		void onRecordSelectUpdate(int recordID, int flag);
	}
	
//	public IncomeAdapter(Activity context, ArrayList<Income> list, UpdateRecord updateCallback) {
//		super(context, R.layout.single_income_record, list);
//		this.mContext = context;
//		this.list = list;
//		this.updateCallback = updateCallback;
//	}
	public IncomeAdapter(Activity context, ArrayList<Income> list) {
		super(context, R.layout.single_income_record, list);
		this.mContext = context;
		this.list = list;
		//this.updateCallback = updateCallback;
	}
	
	
	/**
	 * The Class ViewHolder.
	 */
	static class ViewHolder {
		/** The provider. */
		protected TextView income;
		public TextView income_id;
		public TextView income_date;
		public TextView income_type;
		public TextView income_amount;
		public TextView income_provider;
		public TextView income_note;
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = null;
		if (convertView == null) {
			LayoutInflater inflator = mContext.getLayoutInflater();
			
			if (list.isEmpty()) {
				view = inflator.inflate(R.layout.no_income, null);
			} else {
				
				view = inflator.inflate(R.layout.single_income_record, null);
				final IncomeAdapter.ViewHolder viewHolder = new IncomeAdapter.ViewHolder();
				viewHolder.income_id = (TextView) view.findViewById(R.id.income_id);
				viewHolder.income_date = (TextView) view.findViewById(R.id.textViewDate);
				viewHolder.income_type = (TextView) view.findViewById(R.id.textViewPaymentType);
				viewHolder.income_amount = (TextView) view.findViewById(R.id.textViewAmount);
				viewHolder.income_provider = (TextView) view.findViewById(R.id.textViewProviderName);
				viewHolder.income_note = (TextView) view.findViewById(R.id.textViewNote);
//				viewHolder.btnEdit = (ImageButton) view.findViewById(R.id.btnIncomeEdit);
//				viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
////
//					@Override
//					public void onClick(View v) {
//						String msg = (String) viewHolder.income_id.getText();
//						int flag = 2;// 2 EDIT RECORD
//						try {
//							updateCallback.onRecordSelectUpdate(Integer.valueOf(msg), flag);
//						} catch (ClassCastException e) {
//							throw new ClassCastException(mContext.toString() + " must implement DeleteRecord");
//						}
//
//					}
//				});
//				viewHolder.btnDelete = (ImageButton) view.findViewById(R.id.btnIncomeDelete);
//				viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						String msg = (String) viewHolder.income_id.getText();
//						int flag = 1;// 1 DELETE RECORD
//						try {
//							updateCallback.onRecordSelectUpdate(Integer.valueOf(msg), flag);
//						} catch (ClassCastException e) {
//							throw new ClassCastException(context.toString() + " must implement DeleteRecord");
//						}
//
//						// db.updateIncome(msg);
//					}
//
//				});
//
				view.setTag(viewHolder);
			}
		} else {
			view = convertView;
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.income_id.setText(String.valueOf(list.get(position).getID()));
		holder.income_date.setText(list.get(position).getDate());
		holder.income_type.setText(list.get(position).getIncType());
		holder.income_amount.setText(list.get(position).getAmount());
		holder.income_provider.setText(list.get(position).getNote());
		holder.income_note.setText(list.get(position).getProvider());
		
		return view;
	}
	
	
	
}
