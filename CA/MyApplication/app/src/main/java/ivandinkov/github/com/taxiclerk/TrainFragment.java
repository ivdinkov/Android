package ivandinkov.github.com.taxiclerk;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TrainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TrainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrainFragment extends Fragment implements ResultCallBack{
	private static String ARG_PARAM1;
	private static String ARG_PARAM2;
	private String selectedStation = "CNLLY";
	private String selectedTime = "15";
	private String server_url = "";
	private static final String TAG = "TC";
	
	public static final String SERVER_URL = "http://api.irishrail.ie/realtime/realtime.asmx/getStationDataByCodeXML_WithNumMins";
	public static final String QUERY_OPTIONS_STATION = "?StationCode=";
	public static final String QUERY_OPTIONS_TIME = "&NumMins=";
	public static final String TEXT = "";
	private OnFragmentInteractionListener mListener;
	private String queryUrl;
	private HashMap<Object, Object> currentMap;
	private ResultCallBack callback;
	private ListView listT;
	
	
	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment TrainFragment.
	 */
	public static TrainFragment newInstance(String param1, String param2){
		TrainFragment fragment = new TrainFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}
	
	public TrainFragment() {
		// Required empty public constructor
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
		View view = inflater.inflate(R.layout.fragment_train, container, false);
		
		
		final String[] stationValues = getResources().getStringArray(R.array.stations);
		final String[] minuteValues = getResources().getStringArray(R.array.minutes);
		final String[] stationCodes = getResources().getStringArray(R.array.stations_codes);
		listT = (ListView) view.findViewById(R.id.listT);
		
		// handle the button showTrains
		Button btnShowTrain = (Button) view.findViewById(R.id.btnShowTrain);
		btnShowTrain.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// check first for internet connection
				if (!checkConnection()) {
					Toast toast = Toast.makeText(getActivity(), "No connection!", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
					toast.show();
				} else {
					// initialise xml params
					queryUrl = buildUrl(SERVER_URL, QUERY_OPTIONS_STATION, QUERY_OPTIONS_TIME, selectedStation, selectedTime);
					Log.i(TAG, "Build URL: " + queryUrl);
					
					// fire xml
					IrishRail showTrains = new IrishRail(callback);
					showTrains.execute();
				}
			}
		});
		
		
		// shrink width with wraper
		LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.trainWraper);
		// Get device dimensions
		DisplayMetrics dm = getWidthAndHeightPx();
		// Set register layout holder to 80% width
		LinearLayout.LayoutParams lpWrapper = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
		lpWrapper.leftMargin = (dm.widthPixels - (int) (dm.widthPixels * 0.9)) / 2;
		lpWrapper.rightMargin = (dm.widthPixels - (int) (dm.widthPixels * 0.9)) / 2;
		
		// Spinners
		Spinner spinnerStation = (Spinner) view.findViewById(R.id.spinnerStation);
		ArrayAdapter<CharSequence> adapterStations = ArrayAdapter.createFromResource(getActivity(),
						R.array.stations, android.R.layout.simple_spinner_item);
		adapterStations.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerStation.setAdapter(adapterStations);
		
		Spinner spinnerTime = (Spinner) view.findViewById(R.id.spinnerTime);
		ArrayAdapter<CharSequence> adapterTime = ArrayAdapter.createFromResource(getActivity(),
						R.array.minutes, android.R.layout.simple_spinner_item);
		adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerTime.setAdapter(adapterTime);
		
		spinnerStation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				selectedStation = stationCodes[position];
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		
		spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				selectedTime = minuteValues[position];
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		
		
		return view;
	}
	
	private String buildUrl(String serverUrl, String queryOptionsStation, String queryOptionsTime, String selectedStation, String selectedTime) {
		
		return serverUrl + queryOptionsStation + selectedStation + queryOptionsTime + selectedTime;
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
		ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
		if (activeNetwork != null) {
			return true;
		}
		return false;
	}
	
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}
	
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		//callback = (ResultCallBack) getActivity();
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
	
	@Override
	public void onPreExecute() {
		
	}
	
	@Override
	public void onPostExecute(ArrayList<HashMap<String, String>> result) {
		listT.setAdapter(new TainDisplayAdapater(getContext(),result));
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
	
	public class IrishRail extends AsyncTask<Void, Void, ArrayList<HashMap<String, String>>> {
		
		private ProgressDialog pDialog;
		ResultCallBack callBack = null;
		
		public IrishRail(ResultCallBack call){
			
			this.callBack = call;
		}
		public void onAttach(){
			this.callBack = callBack;
		}
		public void onDeAttach(ResultCallBack callBack){
			callBack = null;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setTitle("Get IrishRail Info");
			pDialog.setMessage("Loading...");
			pDialog.show();
			if(callBack != null){
				callBack.onPreExecute();
			}
		}
		
		protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
			
			ArrayList<HashMap<String, String>> result = new ArrayList<>();
			try {
				URL url = new URL(queryUrl);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				InputStream inputStream = connection.getInputStream();
				result = processXML(inputStream);
			} catch (Exception e) {
				Log.i(TAG, e.getMessage());
			}
			return result;
		}
		protected void onPostExecute(ArrayList<HashMap<String, String>> result){
			pDialog.dismiss();
			listT.setAdapter(new TainDisplayAdapater(getContext(),result));
			if(callBack != null){
				callBack.onPreExecute();
			}
			
		}
		
		public ArrayList<HashMap<String, String>> processXML(InputStream inputStream) throws Exception {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document xmlDocument = documentBuilder.parse(inputStream);
			Element rootElement = xmlDocument.getDocumentElement();
			NodeList itemsList = rootElement.getElementsByTagName("objStationData");
			
			Node currentItem = null;
			NodeList itemChildren = null;
			Node currentChild = null;
			
			// Number of trains
			ArrayList<HashMap<String, String>> result = new ArrayList<>();
			HashMap<String, String> currentMap;
			
			for (int i = 0; i < itemsList.getLength(); i++) {
				
				Log.i(TAG, "Number of trains: " + String.valueOf(itemsList.getLength()));
				currentItem = itemsList.item(i);
				itemChildren = currentItem.getChildNodes();
				currentMap = new HashMap<>();
				Log.i(TAG, "Size of child: " + String.valueOf(itemChildren.getLength()));
				
				for (int j = 0; j < itemChildren.getLength(); j++) {
					
					currentChild = itemChildren.item(j);
					if (currentChild.getNodeName().equalsIgnoreCase("Origin")) {
						Log.i(TAG, currentChild.getTextContent());
						currentMap.put("origin", currentChild.getTextContent());
					}
					if (currentChild.getNodeName().equalsIgnoreCase("Destination")) {
						Log.i(TAG, currentChild.getTextContent());
						currentMap.put("destination", currentChild.getTextContent());
					}
					if (currentChild.getNodeName().equalsIgnoreCase("Late")) {
						Log.i(TAG, currentChild.getTextContent());
						currentMap.put("late", currentChild.getTextContent());
					}
					if (currentChild.getNodeName().equalsIgnoreCase("Exparrival")) {
						Log.i(TAG, currentChild.getTextContent());
						currentMap.put("arrival", currentChild.getTextContent());
					}
					
				}
				if (currentMap != null && !currentMap.isEmpty()) {
					result.add(currentMap);
				}
				
			}
			return result;
		}
	}
} // End Fragment
interface ResultCallBack{
	void onPreExecute();
	void onPostExecute(ArrayList<HashMap<String, String>> result);
}

