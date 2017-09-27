package ivandinkov.github.com.taxiclerk;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by iv on 24/09/2017.
 */

public class ExpenseTypeAdapter extends ArrayAdapter<Expense> {
	private final Activity mContext;
	private final List<Expense> mList;
	
	/**
	 * Instantiates a new income custom adapter.
	 *
	 * @param context the context
	 * @param list the list
	 * @param updateCallback the update callback
	 */
	public ExpenseTypeAdapter(Activity context, List<Expense> list, ExpenseTypeAdapter.UpdateRecord updateCallback) {
		super(context, R.layout.list_view_provider, list);
		this.mContext = context;
		this.mList = list;
		UpdateRecord mUpdateCallback = updateCallback;
	}
	
	public interface UpdateRecord {
		/**
		 * On record select update.
		 *
		 * @param recordID the record id
		 * @param flag the flag
		 */
		public void onRecordSelectUpdate(int recordID, int flag);
	}
	/**
	 * The Class ViewHolder.
	 */
	static class ViewHolder {
		/** The provider. */
		public TextView expense_type_id;
		public TextView expense_type_name;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = null;
		if (convertView == null) {
			view = mContext.getLayoutInflater().inflate(R.layout.list_view_expense_type, null);
			final ExpenseTypeAdapter.ViewHolder viewHolder = new ExpenseTypeAdapter.ViewHolder();
			viewHolder.expense_type_id = (TextView) view.findViewById(R.id.expense_type_id);
			viewHolder.expense_type_name = (TextView) view.findViewById(R.id.expense_type_name);
			
			view.setTag(viewHolder);
		} else {
			view = convertView;
		}
		ExpenseTypeAdapter.ViewHolder holder = (ExpenseTypeAdapter.ViewHolder) view.getTag();
		holder.expense_type_id.setText(String.valueOf(mList.get(position).getID()));
		holder.expense_type_name.setText(mList.get(position).getName());
		return view;
	}
}
