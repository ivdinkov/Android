package ivandinkov.github.com.taxiclerk;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link } interface
 * to handle interaction events.
 * Use the {@link ExpenseAddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpenseAddFragment extends ListFragment implements UpdateRecord {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	
	private static Context mContext;
	
	private OnFragmentInteractionListener mListener;
	
	public ExpenseAddFragment() {
		// Required empty public constructor
	}
	
	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment ExpenseAddFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static ExpenseAddFragment newInstance(String param1, String param2) {
		ExpenseAddFragment fragment = new ExpenseAddFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof ExpenseAddFragment.OnFragmentInteractionListener) {
			mListener = (ExpenseAddFragment.OnFragmentInteractionListener) context;
		} else {
			throw new RuntimeException(context.toString()
							+ " must implement OnFragmentInteractionListener");
		}
		this.mContext = context;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			String mParam1 = getArguments().getString(ARG_PARAM1);
			String mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}
	
	protected void setList() {
		setListAdapter(new ExpenseTypeAdapter(getActivity(), GetExpenseList(), null));
	}
	
	private List<Expense> GetExpenseList() {
		DB db = new DB(getActivity(), null);
		List<Expense> expenseNamesList = db.GetExpenseTypeList();
		db.close();
		return expenseNamesList;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_expense_provider, container, false);
		
		// Initialise button
		Button btnAddExpense = (Button) v.findViewById(R.id.btnAddExpenseProvider);
		btnAddExpense.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog();
			}
		});
		// Populate providers list from DB
		setList();
		return v;
		
	}
	
	/**
	 * Show proviser Dialog.
	 */
	private void showDialog() {
		FragmentManager fm = getFragmentManager();
		ExpenseAddFragment.MyDialog alertDialog = new ExpenseAddFragment.MyDialog();
		alertDialog.show(fm, "expenses");
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
			
			View customView = inflater.inflate(R.layout.add_expense_type_dialog, null);
			
			customView.setMinimumWidth((int) (displayRectangle.width() * 0.6f));
			customView.setMinimumHeight((int) (displayRectangle.height() * 0.2f));
			
			TextView btnYes = (TextView) customView.findViewById(R.id.btnSaveExpenseType);
			TextView btnCancel = (TextView) customView.findViewById(R.id.btnCancelExpenseType);
			final TextView txtExpenseName = (TextView) customView.findViewById(R.id.txtExpenseType);
			
			alertDialogBuilder.setContentView(customView);
			
			btnYes.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO validate input
					// Save provider to DB
					saveExpenseType(new Expense(txtExpenseName.getText().toString(), "yes"));
					FragmentTransaction ft = getFragmentManager().beginTransaction();
					ft.setCustomAnimations(R.anim.slide_in_top, R.anim.slide_out_bottom);
					ft.replace(R.id.main_fragment_container, new SettingsFragment()).commit();
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
	
	
	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	
	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}
	
	@Override
	public void onRecordSelectUpdate(int recordID, int flag) {
		
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
	
	public static void showToast(String msg) {
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(mContext, msg, duration);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();
	}
	
	public static void saveExpenseType(Expense e) {
		// Save provider
		DB db = new DB(mContext, null);
		if (db.insertExpenseType(e)) {
			showToast("Expense type save success");
		} else {
			showToast("Unable to save expense");
		}
	}
	
}
