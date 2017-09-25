package ivandinkov.github.com.taxiclerk;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TrainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TrainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrainFragment extends Fragment {
	
	
	private DisplayMetrics dm;
	private Spinner spinnerStation;
	private Spinner spinnerTime;
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	
	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	
	private OnFragmentInteractionListener mListener;
	
	public TrainFragment() {
		// Required empty public constructor
	}
	
	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment TrainFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static TrainFragment newInstance(String param1, String param2) {
		TrainFragment fragment = new TrainFragment();
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
		View view = inflater.inflate(R.layout.fragment_train, container, false);
		
		
		LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.trainWraper);
		// Get device dimensions
		dm = getWidthAndHeightPx();
		// Set register layout holder to 80% width
		LinearLayout.LayoutParams lpWrapper = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
		lpWrapper.leftMargin = (dm.widthPixels - (int) (dm.widthPixels * 0.8)) / 2;
		lpWrapper.rightMargin = (dm.widthPixels - (int) (dm.widthPixels * 0.8)) / 2;
		
		spinnerStation = (Spinner) view.findViewById(R.id.spinnerStation);
		spinnerTime = (Spinner) view.findViewById(R.id.spinnerTime);
		
		addItemsOnSpinnerStations();
		addItemsOnSpinnerMinutes();
		addListenerOnSpinnerStationItemSelection();
		addListenerOnSpinnerMinutesItemSelection();
		
		public void addItemsOnSpinnerStations() {
			
			List<String> list = new ArrayList<String>();
			list.add("list 1");
			list.add("list 2");
			list.add("list 3");
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
							android.R.layout.simple_spinner_item, list);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerStation.setAdapter(dataAdapter);
		}
		
		
		if (!checkConnection()) {
			Toast toast = Toast.makeText(getActivity(), "No connection!", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.TOP, 0, 0);
			toast.show();
		} else {
			
		}
		
		
		return view;
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
	
	private DisplayMetrics getWidthAndHeightPx() {
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm;
	}
	/**
	 * Check for internet connection.
	 *
	 * @return the boolean
	 */
	private Boolean checkConnection() {
		ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
		if (activeNetwork != null) {
			return true;
		}
		return false;
	}
	
	
	
}
