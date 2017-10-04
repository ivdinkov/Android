package ivandinkov.github.com.taxiclerk;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.R.attr.type;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DayFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	private static final String TAG = "TC";
	private OnFragmentInteractionListener mListener;
	private static SimpleDateFormat sdfDB;
	private TextView txtAmountDayJobs;
	private TextView txtAmountExpenses;
	private TextView txtAmountAccJobs;
	private TextView txtAmountCashJobs;
	DecimalFormat dec = new DecimalFormat("0.00");
	private int allJobsForDay;
	
	public DayFragment() {
		// Required empty public constructor
	}
	
	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment DayFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static DayFragment newInstance(String param1, String param2) {
		DayFragment fragment = new DayFragment();
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
			String mParam1 = getArguments().getString(ARG_PARAM1);
			String mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_day, container, false);
		
		LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.dayLayoutWraper);
		// Get device dimensions
		DisplayMetrics dm = getWidthAndHeightPx();
		// Set register layout holder to 80% width
		FrameLayout.LayoutParams lpWrapper = (FrameLayout.LayoutParams) linearLayout.getLayoutParams();
		lpWrapper.leftMargin = (dm.widthPixels - (int) (dm.widthPixels * 0.8)) / 2;
		lpWrapper.rightMargin = (dm.widthPixels - (int) (dm.widthPixels * 0.8)) / 2;
		
		txtAmountDayJobs = (TextView) view.findViewById(R.id.txtDayJobs);
		txtAmountCashJobs = (TextView) view.findViewById(R.id.txtCash);
		txtAmountAccJobs = (TextView) view.findViewById(R.id.txtAccount);
		txtAmountExpenses = (TextView) view.findViewById(R.id.txtExpenses);
		
		// Extract DB data
		DB db = new DB(getContext(), null);
		// day cash
		String c = db.getDayAmount(getDate(), "Cash");
		if (!c.isEmpty()) {
			float in = Float.parseFloat(c);
			txtAmountCashJobs.setText(dec.format(in));
		}
		// dat account
		String a = db.getDayAmount(getDate(), "Account");
		if (!a.isEmpty()) {
			float in = Float.parseFloat(a);
			txtAmountAccJobs.setText(dec.format(in));
		}
		// day expenses
		String exp = db.getDailyExp(getDate());
		if (!exp.isEmpty()) {
			float in = Float.parseFloat(exp);
			txtAmountExpenses.setText(dec.format(in));
		}
		// day total jobs
		allJobsForDay = db.allJobsForDate(getDate());
		if (allJobsForDay != 0) {
			String jobs = String.valueOf(allJobsForDay);
			txtAmountDayJobs.setText(jobs);
		}
		db.close();
	
		
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
	 * Get the current date.
	 */
	private String getDate() {
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		c.set(year, month, day);
		
		sdfDB = new SimpleDateFormat("ddMMyyyy", Locale.ENGLISH);
		
		return sdfDB.format(c.getTime());
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
		void onFragmentInteraction(Uri uri);
	}
}
