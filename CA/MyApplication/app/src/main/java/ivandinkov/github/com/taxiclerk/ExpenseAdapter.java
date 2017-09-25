package ivandinkov.github.com.taxiclerk;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by iv on 25/09/2017.
 */

public class ExpenseAdapter extends ArrayAdapter<MExpense> {
	
	private final Activity mContext;
	private final ArrayList<MExpense> list;
	//private final UpdateRecord updateCallback;
	
	public interface UpdateRecord {
		void onRecordSelectUpdate(int recordID, int flag);
	}
	
	//	public IncomeAdapter(Activity context, ArrayList<Income> list, UpdateRecord updateCallback) {
//		super(context, R.layout.single_income_record, list);
//		this.mContext = context;
//		this.list = list;
//		this.updateCallback = updateCallback;
//	}
	public ExpenseAdapter(Activity context, ArrayList<MExpense> list) {
		super(context, R.layout.single_expense_record, list);
		this.mContext = context;
		this.list = list;
	}
	
	
	/**
	 * The Class ViewHolder.
	 */
	static class ViewHolder {
		/** The provider. */
		protected TextView expense;
		public TextView expense_id;
		public TextView expense_date;
		public TextView expense_type;
		public TextView expense_amount;
		public TextView expense_provider;
		public TextView expense_note;
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = null;
		if (convertView == null) {
			LayoutInflater inflator = mContext.getLayoutInflater();
			
			if (list.isEmpty()) {
				view = inflator.inflate(R.layout.no_income, null);
			} else {
				
				view = inflator.inflate(R.layout.single_expense_record, null);
				final ExpenseAdapter.ViewHolder viewHolder = new ExpenseAdapter.ViewHolder();
				viewHolder.expense_id = (TextView) view.findViewById(R.id.expense_id);
				viewHolder.expense_date = (TextView) view.findViewById(R.id.textViewDate);
				viewHolder.expense_type = (TextView) view.findViewById(R.id.textViewPaymentType);
				viewHolder.expense_amount = (TextView) view.findViewById(R.id.textViewAmount);
				viewHolder.expense_provider = (TextView) view.findViewById(R.id.textViewProviderName);
				viewHolder.expense_note = (TextView) view.findViewById(R.id.textViewNote);
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
		holder.expense_id.setText(String.valueOf(list.get(position).getID()));
		holder.expense_date.setText(list.get(position).getDate());
		holder.expense_type.setText(list.get(position).getExpPayType());
		holder.expense_amount.setText(list.get(position).getAmount());
		holder.expense_provider.setText(list.get(position).getNote());
		holder.expense_note.setText(list.get(position).getExpenseType());
		
		return view;
	}
	
	
	
}
