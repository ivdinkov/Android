package ivandinkov.github.com.taxiclerk;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.text.DecimalFormat;


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
	private String incomeAmountToBeSaved = null;
	
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
		View view =  inflater.inflate(R.layout.fragment_new_income, container, false);
		
		// Initialize view elements
		txtNewIncomeAmount = (EditText) view.findViewById(R.id.editTextNewFareAmount);
		LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.newIncomeLayoutWraper);
		// Get device dimensions
		dm = getWidthAndHeightPx();
		// Set register layout holder to 80% width
		FrameLayout.LayoutParams lpWrapper = (FrameLayout.LayoutParams) linearLayout.getLayoutParams();
		lpWrapper.leftMargin = (dm.widthPixels - (int) (dm.widthPixels * 0.8)) / 2;
		lpWrapper.rightMargin = (dm.widthPixels - (int) (dm.widthPixels * 0.8)) / 2;
		
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
		
	return view;
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
}
