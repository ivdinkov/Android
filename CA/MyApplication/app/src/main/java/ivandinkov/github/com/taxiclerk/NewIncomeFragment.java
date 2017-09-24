package ivandinkov.github.com.taxiclerk;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewIncomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewIncomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewIncomeFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	
	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	
	private OnFragmentInteractionListener mListener;
	private DisplayMetrics dm;
	private EditText txtNewIncomeAmount;
	private String incomeAmountToBeSaved;
	private Button btnNewIncomeNote;
	private Button btnSelectProvider;
	private Button btnSaveNewIncome;
	private RadioButton radioAccount;
	private RadioButton radioCash;
	private static String noteToBeSaved = "";
	private String providerToBeSaved;
	private String incomeTypeToBeSaved = "Account";
	private Button btnCancelNewIncome;
	
	public NewIncomeFragment() {
		// Required empty public constructor
	}
	
	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment NewIncomeFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static NewIncomeFragment newInstance(String param1, String param2) {
		NewIncomeFragment fragment = new NewIncomeFragment();
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
		View view = inflater.inflate(R.layout.fragment_new_income, container, false);
		
		// Initialize view elements
		txtNewIncomeAmount = (EditText) view.findViewById(R.id.editTextNewFareAmount);
		btnNewIncomeNote = (Button) view.findViewById(R.id.btnNoteNewIncome);
		btnSelectProvider = (Button) view.findViewById(R.id.btnProviderPicker);
		btnSaveNewIncome = (Button) view.findViewById(R.id.btnSaveNewIncome);
		radioCash = (RadioButton) view.findViewById(R.id.cashNewIncome);
		radioAccount = (RadioButton) view.findViewById(R.id.accNewIncome);
		btnCancelNewIncome = (Button) view.findViewById(R.id.btnCancelNewIncome);
		
		LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.newIncomeLayoutWraper);
		// Get device dimensions
		dm = getWidthAndHeightPx();
		// Set register layout holder to 80% width
		FrameLayout.LayoutParams lpWrapper = (FrameLayout.LayoutParams) linearLayout.getLayoutParams();
		lpWrapper.leftMargin = (dm.widthPixels - (int) (dm.widthPixels * 0.8)) / 2;
		lpWrapper.rightMargin = (dm.widthPixels - (int) (dm.widthPixels * 0.8)) / 2;
		
		
		btnCancelNewIncome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LinearLayout buttonHolder = (LinearLayout) getActivity().findViewById(R.id.income_button_holder);
				buttonHolder.setVisibility(View.VISIBLE);
				Fragment fragment = null;
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				fragment = new HomeFragment();
				ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
				ft.replace(R.id.main_fragment_container, fragment, "home").commit();
			}
		});
		btnSaveNewIncome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// validate input
				if (providerToBeSaved == null || incomeAmountToBeSaved == null) {
					Toast toast = Toast.makeText(getActivity(), "Incomplete details", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.TOP, 0, 0);
					toast.show();
				} else {
					// save the new income to db
					DB db = new DB(getActivity(), null);
					db.saveNewIncome(new Income(getDate(), incomeTypeToBeSaved, incomeAmountToBeSaved, noteToBeSaved, providerToBeSaved));
					
					LinearLayout buttonHolder = (LinearLayout) getActivity().findViewById(R.id.income_button_holder);
					buttonHolder.setVisibility(View.VISIBLE);
					
					Fragment fragment = null;
					FragmentTransaction ft = getFragmentManager().beginTransaction();
					fragment = new HomeFragment();
					ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
					ft.replace(R.id.main_fragment_container, fragment, "home").commit();
				}
			}
		});
		/*
		 * Get provider
		 */
		btnSelectProvider.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final ArrayList<String> providers = getProviders();
				
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, providers);
				new AlertDialog.Builder(getActivity()).setTitle("Select provider").setAdapter(adapter, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// btnSpinner.setBackgroundResource(R.drawable.edittext_login_box_border);
						btnSelectProvider.setText(providers.get(which).toString());
						providerToBeSaved = providers.get(which).toString();
						dialog.dismiss();
					}
				}).create().show();
			}
		});

		/*
		 * Get selected Radio
		 */
		radioCash.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				incomeTypeToBeSaved = (String) radioCash.getText();
			}
		});
		radioAccount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				incomeTypeToBeSaved = (String) radioAccount.getText();
			}
		});

		/*
		 * Get Income amount
		 */
		txtNewIncomeAmount.setRawInputType(Configuration.KEYBOARD_12KEY);
		txtNewIncomeAmount.setText("0.00");
		txtNewIncomeAmount.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				txtNewIncomeAmount.post(new Runnable() {
					@Override
					public void run() {
						txtNewIncomeAmount.setSelection(txtNewIncomeAmount.getText().length());
					}
				});
				return false;
			}
		});
		txtNewIncomeAmount.addTextChangedListener(new TextWatcher() {
			DecimalFormat dec = new DecimalFormat("0.00");
			
			@Override
			public void afterTextChanged(Editable s) {
				if (!s.toString().matches("^(\\d{1,3}(,\\d{3})*|(\\d+))(\\.\\d{2})?$")) {
					String userInput = "" + s.toString().replaceAll("[^\\d]", "");
					if (userInput.length() > 0) {
						Float in = Float.parseFloat(userInput);
						float percent = in / 100;
						
						txtNewIncomeAmount.setText(dec.format(percent));
						incomeAmountToBeSaved = dec.format(percent);
						txtNewIncomeAmount.setSelection(txtNewIncomeAmount.getText().length());
					}
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//txtNewIncomeAmount.setBackgroundResource(R.drawable.edittext_login_box_border);
			}
		});
		
		/*
		 * Get Note
		 */
		btnNewIncomeNote.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showNoteDialog();
			}
		});
		return view;
	}
	
	private String getDate() {
		DateFormat df = new SimpleDateFormat("dd MM yyyy, HH:mm");
		String date = df.format(Calendar.getInstance().getTime());
		return date;
	}
	
	private ArrayList<String> getProviders() {
		DB db = new DB(getActivity(), null);
		ArrayList<String> providers = db.getProviderNames();
		db.close();
		return providers;
	}
	
	private DisplayMetrics getWidthAndHeightPx() {
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm;
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
	 * Show Note Dialog.
	 */
	private void showNoteDialog() {
		FragmentManager fm = getFragmentManager();
		NewIncomeFragment.NoteDialog alertDialog = new NewIncomeFragment.NoteDialog();
		alertDialog.show(fm, "note");
	}
	
	public static class NoteDialog extends DialogFragment {
		
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
			
			View customView = inflater.inflate(R.layout.new_income_note, null);
			
			customView.setMinimumWidth((int) (displayRectangle.width() * 0.6f));
			customView.setMinimumHeight((int) (displayRectangle.height() * 0.2f));
			
			TextView btnYes = (TextView) customView.findViewById(R.id.txtSaveNewNote);
			TextView btnCancel = (TextView) customView.findViewById(R.id.txtCancelNewNote);
			final EditText txtNote = (EditText) customView.findViewById(R.id.txtNote);
			
			alertDialogBuilder.setContentView(customView);
			
			btnYes.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO validate input
					noteToBeSaved = txtNote.getText().toString();
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
	
	
}
