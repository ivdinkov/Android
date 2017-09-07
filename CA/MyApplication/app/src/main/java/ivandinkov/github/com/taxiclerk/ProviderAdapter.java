package ivandinkov.github.com.taxiclerk;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by iv on 07/09/2017.
 */

public class ProviderAdapter extends ArrayAdapter<Provider> {
	
	private final Activity mContext;
	private final List<Provider> mList;
	private final UpdateRecord mUpdateCallback;
	
	/**
	 * Instantiates a new income custom adapter.
	 *
	 * @param context the context
	 * @param list the list
	 * @param updateCallback the update callback
	 */
	public ProviderAdapter(Activity context, List<Provider> list, UpdateRecord updateCallback) {
		super(context, R.layout.list_view_provider, list);
		this.mContext = context;
		this.mList = list;
		this.mUpdateCallback = updateCallback;
	}
	
	
	
	/**
	 * The Interface UpdateRecord.
	 */
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
		protected TextView provider;
		public TextView provider_id;
		public TextView provider_name;
		public ImageButton provider_delete;
		public ImageButton provider_edit;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = null;
		if (convertView == null) {
				view = mContext.getLayoutInflater().inflate(R.layout.list_view_provider, null);
				final ViewHolder viewHolder = new ViewHolder();
				viewHolder.provider_id = (TextView) view.findViewById(R.id.provider_id);
				viewHolder.provider_name = (TextView) view.findViewById(R.id.provider_name);
				
				view.setTag(viewHolder);
		} else {
			view = convertView;
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.provider_id.setText(String.valueOf(mList.get(position).getID()));
		holder.provider_name.setText(mList.get(position).getName());
		return view;
	}
}
