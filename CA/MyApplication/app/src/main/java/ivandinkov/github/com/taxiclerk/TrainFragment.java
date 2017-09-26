package ivandinkov.github.com.taxiclerk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.ObbInfo;
import android.content.res.XmlResourceParser;
import android.icu.util.RangeValueIterator;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TrainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TrainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrainFragment extends Fragment {
	private static String ARG_PARAM1;
	private static String ARG_PARAM2;
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private DisplayMetrics dm;
	private Spinner spinnerStation;
	private Spinner spinnerTime;
	private String selectedStation = "CNLLY";
	private String selectedTime = "15";
	private String server_url = "";
	private static final String TAG = "TC";
	
	private String mParam1;
	private String mParam2;
	
	public static final String SERVER_URL = "http://api.irishrail.ie/realtime/realtime.asmx/getStationDataByCodeXML_WithNumMins";
	public static final String QUERY_OPTIONS_STATION = "?StationCode=";
	public static final String QUERY_OPTIONS_TIME = "&NumMins=";
	public static final String TEXT = "";
	private OnFragmentInteractionListener mListener;
	private Button btnShowTrain;
	private String queryUrl;
	private HashMap<Object, Object> currentMap;
	private ResultCallBack callback;
	private ListView listVieTrain;
	
	
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
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
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

		
		// handle the button showTrains
		btnShowTrain = (Button) view.findViewById(R.id.btnShowTrain);
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
		dm = getWidthAndHeightPx();
		// Set register layout holder to 80% width
		LinearLayout.LayoutParams lpWrapper = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
		lpWrapper.leftMargin = (dm.widthPixels - (int) (dm.widthPixels * 0.9)) / 2;
		lpWrapper.rightMargin = (dm.widthPixels - (int) (dm.widthPixels * 0.9)) / 2;
		
		// Spinners
		spinnerStation = (Spinner) view.findViewById(R.id.spinnerStation);
		ArrayAdapter<CharSequence> adapterStations = ArrayAdapter.createFromResource(getActivity(),
						R.array.stations, android.R.layout.simple_spinner_item);
		adapterStations.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerStation.setAdapter(adapterStations);
		
		spinnerTime = (Spinner) view.findViewById(R.id.spinnerTime);
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
		
		public IrishRail(ResultCallBack callBack){
			this.callBack = callBack;
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
				for (int j = 0; j < itemChildren.getLength(); j++) {
					currentChild = itemChildren.item(j);
					if (currentChild.getNodeName().equalsIgnoreCase("Origin")) {
						//Log.i(TAG, currentChild.getTextContent());
						currentMap.put("origin", currentChild.getTextContent());
					}
					if (currentChild.getNodeName().equalsIgnoreCase("Destination")) {
						//Log.i(TAG, currentChild.getTextContent());
						currentMap.put("destination", currentChild.getTextContent());
					}
					if (currentChild.getNodeName().equalsIgnoreCase("Late")) {
						//Log.i(TAG, currentChild.getTextContent());
						currentMap.put("late", currentChild.getTextContent());
					}
					if (currentChild.getNodeName().equalsIgnoreCase("Exparrival")) {
						//Log.i(TAG, currentChild.getTextContent());
						currentMap.put("arrival", currentChild.getTextContent());
					}
					
					if (currentMap != null && !currentMap.isEmpty()) {
						result.add(currentMap);
					}
				}
			}
			return result;
		}
	}
} // End Fragment
interface ResultCallBack{
	void onPreExecute();
	void onPostExecute();
}
class MyAdapter extends BaseAdapter{
	ArrayList<HashMap<String,String>> dataSource = new ArrayList<>();
	Context context;
	LayoutInflater layoutInflater;
	
	
	public MyAdapter(Context context,ArrayList<HashMap<String,String>> dataSource){
		this.dataSource = dataSource;
		context = context;
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		return dataSource.size();
	}
	
	@Override
	public Object getItem(int position) {
		return dataSource.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		MyHolder myHolder = null;
		if(row == null){
			row = layoutInflater.inflate(R.layout.single_train,parent,false);
			myHolder = new MyHolder(row);
			row.setTag(myHolder);
		}else{
			myHolder = (MyHolder) row.getTag();
		}
		HashMap<String,String> currentItem = dataSource.get(position);
		myHolder.txtFrom.setText(currentItem.get("origin"));
		myHolder.txtTo.setText(currentItem.get("destination"));
		myHolder.txtDelay.setText(currentItem.get("late"));
		myHolder.txtTime.setText(currentItem.get("arrival"));
		return null;
	}
}
class MyHolder{
	TextView txtFrom;
	TextView txtTo;
	TextView txtTime;
	TextView txtDelay;
	public MyHolder(View view){
		txtFrom = (TextView) view.findViewById(R.id.txtFrom);
		txtTo = (TextView) view.findViewById(R.id.txtTo);
		txtTime = (TextView) view.findViewById(R.id.txtTime);
		txtDelay = (TextView) view.findViewById(R.id.txtDelay);
	}
}


