package ivandinkov.github.com.taxiclerk;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.DecimalFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by iv on 25/09/2017.
 */

class ExpenseAdapter extends ArrayAdapter<MExpense> {
	
	private static final String TAG = "TC";
	private final Activity mContext;
	private final ArrayList<MExpense> list;
	private SimpleDateFormat sdf;
	
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
		sdf = new SimpleDateFormat();
	}
	
	
	/**
	 * The Class ViewHolder.
	 */
	static class ViewHolder {
		/**
		 * The provider.
		 */
		protected TextView expense;
		public TextView expense_id;
		public TextView expense_date;
		public TextView expense_type;
		public TextView expense_amount;
		public TextView expense_provider;
		public TextView expense_note;
		public ImageView expense_image;
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
				viewHolder.expense_image = (ImageView) view.findViewById(R.id.imageExp);
				
				view.setTag(viewHolder);
			}
		} else {
			view = convertView;
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.expense_id.setText(String.valueOf(list.get(position).getID()));
		holder.expense_date.setText(convertToFullDate(list.get(position).getDate()));
		holder.expense_type.setText(list.get(position).getExpPayType());
		holder.expense_amount.setText(list.get(position).getAmount());
		holder.expense_provider.setText(list.get(position).getNote());
		holder.expense_note.setText(list.get(position).getExpenseType());
		holder.expense_image.setImageBitmap(decodeImage(list.get(position).getImage()));
		return view;
	}
	
	private String convertToFullDate(String string) {
		SimpleDateFormat format = new SimpleDateFormat("ddMMyyyyHHmm");
		Date date = null;
		try {
			Log.i(TAG, "db string: " + string);
			date = format.parse(string);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		sdf.applyPattern("dd-MMM-yyyy hh:mm");
		return sdf.format(date);
	}
	
	
	private static Bitmap decodeImage(String pathString) {
		
		if (pathString != null) {
			
			BitmapFactory.Options bmOptions = new BitmapFactory.Options();
			bmOptions. inJustDecodeBounds = false;
			bmOptions. inSampleSize = 4;
			Bitmap bitmap = BitmapFactory.decodeFile(pathString, bmOptions);
			return bitmap;
		}
		Bitmap b = Bitmap.createBitmap(40, 40, Bitmap.Config.ARGB_8888);
		b.eraseColor(Color.GRAY);
		return b;
	}
}