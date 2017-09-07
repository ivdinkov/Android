package ivandinkov.github.com.taxiclerk;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JobProviderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JobProviderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JobProviderFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	// Error message
	private static String error_msg = "Invalid character!";
	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	
	private OnFragmentInteractionListener mListener;
	private static Context mContext;
	
	public JobProviderFragment() {
		// Required empty public constructor
	}
	
	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment JobProviderFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static JobProviderFragment newInstance(String param1, String param2) {
		JobProviderFragment fragment = new JobProviderFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v=inflater.inflate(R.layout.fragment_job_provider, container, false);
		
		// Initialise button
		Button btnAddProvider = (Button) v.findViewById(R.id.btnAddProvider);
		btnAddProvider.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog();
			}
		});
		return v;
	}
	
	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}
	
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnFragmentInteractionListener) {
			mListener = (OnFragmentInteractionListener) context;
		} else {
			throw new RuntimeException(context.toString()
							+ " must implement OnFragmentInteractionListener");
		}
		this.mContext = context;
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}
	
	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		void onFragmentInteraction(Uri uri);
	}
	
	/**
	 * Show Terms Dialog.
	 */
	private void showDialog() {
		FragmentManager fm = getFragmentManager();
		MyDialog alertDialog = new MyDialog();
		alertDialog.show(fm, "fragment_job_provider");
	}
	
	public static class MyDialog extends DialogFragment {
		
		/* (non-Javadoc)
		 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
		 */
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			Rect displayRectangle = new Rect();
			Window window = getActivity().getWindow();
			window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
			
			LayoutInflater inflater = getActivity().getLayoutInflater();
			final Dialog alertDialogBuilder = new Dialog(getActivity());
			
			View customView = inflater.inflate(R.layout.add_job_provider_dialog, null);
			
			customView.setMinimumWidth((int) (displayRectangle.width() * 0.6f));
			customView.setMinimumHeight((int) (displayRectangle.height() * 0.2f));
			
			TextView btnYes = (TextView) customView.findViewById(R.id.txtSaveJobProvider);
			TextView btnCancel = (TextView) customView.findViewById(R.id.txtCancelJobProvider);
			final TextView txtName = (TextView) customView.findViewById(R.id.txtProvider);
			
			alertDialogBuilder.setContentView(customView);
			
			btnYes.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Save provider to DB
					// TODO validate input
					Provider p = new Provider(txtName.toString(),"yes");
					saveProvider(p);
					alertDialogBuilder.cancel();
				}
			});
			btnCancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					alertDialogBuilder.cancel();
				}
			});
			return alertDialogBuilder;
		}
	}
	// TODO Delete when ready
	public static void showToast(String msg){
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(mContext, msg, duration);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();
	}
	
	private static void saveProvider(Provider p){
		// TODO Save provider
		DB db = new DB(mContext,null);
		if(db.insertProvider(p)){
			showToast("Provider save success");
		}else{
			showToast("Unable to save provider");
			// TODO Save to log file
		}
	}
}
