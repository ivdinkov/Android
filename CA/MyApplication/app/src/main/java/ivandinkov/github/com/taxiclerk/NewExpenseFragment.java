package ivandinkov.github.com.taxiclerk;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.zip.Inflater;

import static android.app.Activity.RESULT_OK;
//import static ivandinkov.github.com.taxiclerk.R.id.imageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewExpenseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewExpenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewExpenseFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	
	private OnFragmentInteractionListener mListener;
	private static final String TAG = "TC";
	private String expenseAmountToBeSaved;
	private Button btnSelectExpense;
	private RadioButton radioCashExpense;
	private RadioButton radioAccountExpense;
	private RadioButton radioBankExpense;
	private String expensePaymentTypeToBeSaved = "Cash";
	static private String noteToBeSaved;
	private String expenseTypeToBeSaved;
	private EditText txtAmountToBeSaved;
	private static SimpleDateFormat sdf;
	private static SimpleDateFormat sdfDB;
	private static String dateToDisplay;
	private static String dateToBeSaved;
	private static TextView txtCurDate;
	private Uri tempImageUri = null;
	private String endImage;
	private boolean isPhotoTaken = false;
	static final int REQUEST_TAKE_PHOTO = 1;
	private Spinner spinnerExpType;
	private DisplayMetrics dm;
	private LayoutInflater _inflater;
	
	
	public NewExpenseFragment() {
		// Required empty public constructor
	}
	
	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment NewExpenseFragment.
	 */
	public static NewExpenseFragment newInstance(String param1, String param2) {
		NewExpenseFragment fragment = new NewExpenseFragment();
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
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {
		this._inflater = inflater;
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_new_expense, container, false);
		
		// Initialize elements
		txtAmountToBeSaved = (EditText) view.findViewById(R.id.editTextNewExpenseAmount);
		Button btnNewExpenseNote = (Button) view.findViewById(R.id.btnNoteNewExpense);
		btnSelectExpense = (Button) view.findViewById(R.id.btnExpenseTypePicker);
		final Button btnSaveNewExpense = (Button) view.findViewById(R.id.btnSaveNewExpense);
		radioCashExpense = (RadioButton) view.findViewById(R.id.cashNewExpense);
		radioAccountExpense = (RadioButton) view.findViewById(R.id.accNewExpense);
		radioBankExpense = (RadioButton) view.findViewById(R.id.bankNewExpense);
		Button btnCancelNewExpense = (Button) view.findViewById(R.id.btnCancelNewExpense);
		ImageView imgCam = (ImageView) view.findViewById(R.id.imageCam);
		ImageView imgExpCalendar = (ImageView) view.findViewById(R.id.imageExpCalendar);
		txtCurDate = (TextView) view.findViewById(R.id.textViewCurDateExp);
//		To be done
//		spinnerExpType= (Spinner) view.findViewById(R.id.spinnerExpType);
//		ArrayAdapter<CharSequence> adapterStations = ArrayAdapter.createFromResource(getActivity(),R.array.stations, android.R.layout.simple_spinner_item);
//
		//		adapterStations.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		spinnerExpType.setAdapter(adapterStations);
		
		LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.newExpenseLayoutWraper);
		
		
		// Get device dimensions
		dm = getWidthAndHeightPx();
		// Set register layout holder to 80% width
		FrameLayout.LayoutParams lpWrapper = (FrameLayout.LayoutParams) linearLayout.getLayoutParams();
		lpWrapper.leftMargin = (dm.widthPixels - (int) (dm.widthPixels * 0.8)) / 2;
		lpWrapper.rightMargin = (dm.widthPixels - (int) (dm.widthPixels * 0.8)) / 2;
		
		// Get current date
		try {
			setCurrentDate();
		} catch (Exception e) {
			Log.e(TAG, "ERROR Unable to pick date: ", e);
		}
		
		imgCam.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				takePicture();
			}
		});
		
		imgExpCalendar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment picker = new DatePickerFragment();
				picker.show(getFragmentManager(), "datePicker");
			}
		});
		
		btnCancelNewExpense.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				LinearLayout buttonHolder = (LinearLayout) getActivity().findViewById(R.id.income_button_holder);
//				buttonHolder.setVisibility(View.VISIBLE);
				
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				Fragment fragment = new HomeFragment();
				ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
				ft.replace(R.id.main_fragment_container, fragment, "home").commit();
			}
		});
		
		btnSaveNewExpense.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(btnSaveNewExpense.getWindowToken(), 0);
				// validate input
				if (expensePaymentTypeToBeSaved == null || expenseAmountToBeSaved == null || expenseTypeToBeSaved == null) {
					CustomeToast.ShowToast(getActivity(),getView(),inflater,"Incomplete details!");
				} else {
					// save the new income to db
					DB db = new DB(getActivity(), null);
					// user validated => check if taken photo
					if (isPhotoTaken && tempImageUri != null) {
						endImage = tempImageUri.toString();
						// helper to get image name from uri
						//Uri t = Uri.parse(tempImageUri.toString());
						//File f = new File("" + t);
						//endImage = f.getName();
					}
					
					db.saveNewExpense(new MExpense(dateToBeSaved, expensePaymentTypeToBeSaved, expenseAmountToBeSaved, noteToBeSaved, expenseTypeToBeSaved, endImage));
					Log.i(TAG,"new expanse:  pay type: " + expensePaymentTypeToBeSaved+ "note: " + noteToBeSaved + "exp type: " + expenseTypeToBeSaved + "img: " + endImage);
//					LinearLayout buttonHolder = (LinearLayout) getActivity().findViewById(R.id.income_button_holder);
//					buttonHolder.setVisibility(View.VISIBLE);
					
					FragmentTransaction ft = getFragmentManager().beginTransaction();
					Fragment fragment = new HomeFragment();
					ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
					ft.replace(R.id.main_fragment_container, fragment, "home").commit();
				}
			}
		});
		/*
		 * Get expense type
		 */
		btnSelectExpense.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final ArrayList<String> expenses = getExpenses();
				
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, expenses);
				new AlertDialog.Builder(getActivity()).setTitle("Select expense").setAdapter(adapter, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// btnSpinner.setBackgroundResource(R.drawable.edittext_login_box_border);
						btnSelectExpense.setText(expenses.get(which).toString());
						expenseTypeToBeSaved = expenses.get(which).toString();
						dialog.dismiss();
					}
				}).create().show();
			}
		});
		/*
		 * Get selected Radio
		 */
		radioCashExpense.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				expensePaymentTypeToBeSaved = (String) radioCashExpense.getText();
			}
		});
		radioAccountExpense.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				expensePaymentTypeToBeSaved = (String) radioAccountExpense.getText();
			}
		});
		radioBankExpense.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				expensePaymentTypeToBeSaved = (String) radioBankExpense.getText();
			}
		});
		/*
		 * Get Income amount
		 */
		txtAmountToBeSaved.setRawInputType(Configuration.KEYBOARD_12KEY);
		txtAmountToBeSaved.setText("0.00");
		txtAmountToBeSaved.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				txtAmountToBeSaved.post(new Runnable() {
					@Override
					public void run() {
						txtAmountToBeSaved.setSelection(txtAmountToBeSaved.getText().length());
					}
				});
				return false;
			}
		});
		txtAmountToBeSaved.addTextChangedListener(new TextWatcher() {
			DecimalFormat dec = new DecimalFormat("0.00");
			
			@Override
			public void afterTextChanged(Editable s) {
				if (!s.toString().matches("^(\\d{1,3}(,\\d{3})*|(\\d+))(\\.\\d{2})?$")) {
					String userInput = "" + s.toString().replaceAll("[^\\d]", "");
					if (userInput.length() > 0) {
						Float in = Float.parseFloat(userInput);
						float percent = in / 100;
						
						txtAmountToBeSaved.setText(dec.format(percent));
						expenseAmountToBeSaved = dec.format(percent);
						txtAmountToBeSaved.setSelection(txtAmountToBeSaved.getText().length());
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
		btnNewExpenseNote.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showNoteDialog();
			}
		});
		return view;
	}
	
	public static boolean copyFile(File sourceFile, File outputFile) {
		try {
			File sd = Environment.getExternalStorageDirectory();
			if (sd.canWrite()) {
				
				if (!outputFile.exists()) {
					outputFile.createNewFile();
				}
				if (sourceFile.exists()) {
					InputStream src = new FileInputStream(sourceFile);
					OutputStream dst = new FileOutputStream(outputFile);
					// Copy the bits from instream to outstream
					byte[] buf = new byte[1024];
					int len;
					while ((len = src.read(buf)) > 0) {
						dst.write(buf, 0, len);
					}
					src.close();
					dst.close();
				}
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	private void takePicture() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File f = new File(Environment.getExternalStorageDirectory(), createNewImageFile());
		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		tempImageUri = Uri.fromFile(f);
		startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
	}
	
	private String createNewImageFile() {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "TC_" + timeStamp + "_.jpg";
		return imageFileName;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
			isPhotoTaken = true;
			CustomeToast.ShowToast(getActivity(),getView(),_inflater,"Image saved success!");
		}
	}
	
	
	
	private void showNoteDialog() {
		FragmentManager fm = getFragmentManager();
		NewExpenseFragment.NoteDialog alertDialog = new NewExpenseFragment.NoteDialog();
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
			
			View customView = inflater.inflate(R.layout.new_expense_note, null);
			
			customView.setMinimumWidth((int) (displayRectangle.width() * 0.6f));
			customView.setMinimumHeight((int) (displayRectangle.height() * 0.2f));
			
			TextView btnYes = (TextView) customView.findViewById(R.id.txtSaveNewNote);
			TextView btnCancel = (TextView) customView.findViewById(R.id.txtCancelNewNote);
			final EditText txtNote = (EditText) customView.findViewById(R.id.txtNote);
			
			alertDialogBuilder.setContentView(customView);
			
			btnYes.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
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
	
	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}
	
	private ArrayList<String> getExpenses() {
		DB db = new DB(getActivity(), null);
		ArrayList<String> expenses = db.getExpenseTypeNames();
		db.close();
		return expenses;
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
	
	private DisplayMetrics getWidthAndHeightPx() {
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm;
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
	 * Sets the current date.
	 */
	private void setCurrentDate() {
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		c.set(year, month, day);
		
		sdf = new SimpleDateFormat("d MMM", Locale.ENGLISH);
		sdfDB = new SimpleDateFormat("ddMMyyyyHHmm", Locale.ENGLISH);
		
		dateToDisplay = sdf.format(c.getTime());
		dateToBeSaved = sdfDB.format(c.getTime());
		txtCurDate.setText(dateToDisplay);
	}
	
	/**
	 * The Class DatePickerFragment.
	 */
	public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
		
		
		/* (non-Javadoc)
				 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
				 */
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			
			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}
		
		/* (non-Javadoc)
		 * @see android.app.DatePickerDialog.OnDateSetListener#onDateSet(android.widget.DatePicker, int, int, int)
		 */
		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			Calendar c = Calendar.getInstance();
			int y = c.get(Calendar.YEAR);
			// setting current year, preventing the
			// user to pickup different year
			c.set(y, month, day);
			
			sdf = new SimpleDateFormat("d MMM", Locale.ENGLISH);
			sdfDB = new SimpleDateFormat("ddMMyyyyHHmm", Locale.ENGLISH);
			
			dateToDisplay = sdf.format(c.getTime());
			dateToBeSaved = sdfDB.format(c.getTime());
			txtCurDate.setText(dateToDisplay);
		}
	}
}
