package course.labs.activitylab;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ActivityTwo extends Activity {

	// Use these as keys when you're saving state between reconfigurations
	private static final String RESTART_KEY = "restart";
	private static final String RESUME_KEY = "resume";
	private static final String START_KEY = "start";
	private static final String CREATE_KEY = "create";

	// String for LogCat documentation
	private final static String TAG = "Lab-ActivityTwo";
	
	// Lifecycle counters
	private int mCreate = 0;
	private int mRestart = 0;
	private int mStart = 0;
	private int mResume = 0;
	
	// TextView variables
	private TextView mTvCreate;
	private TextView mTvRestart;
	private TextView mTvStart;
	private TextView mTvResume;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_two);
		
		// Initialize UI elements
		mTvCreate = (TextView) findViewById(R.id.create);
		mTvRestart = (TextView) findViewById(R.id.restart);
		mTvStart  = (TextView) findViewById(R.id.start);
		mTvResume  = (TextView) findViewById(R.id.resume);
		Button closeButton = (Button) findViewById(R.id.bClose);
		
		// When clicked will close ActivityTwo
		closeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				// Close ActivityTwo
				finish();
			}
		});

		// Has previous state been saved?
		if (savedInstanceState != null) {
			
			// Extract saved Lifecycle counters
			mCreate = savedInstanceState.getInt(CREATE_KEY);
			mRestart = savedInstanceState.getInt(RESTART_KEY);
			mStart = savedInstanceState.getInt(START_KEY);
			mResume = savedInstanceState.getInt(RESUME_KEY);
		}

		// Emit LogCat message
		Log.i(TAG, "Entered the onCreate() method");
		
		// This is first time so increment only mCreate
		mCreate += 1;
		
		// Display counters
		displayCounts();
	}

	// Lifecycle callback methods overrides

	@Override
	public void onStart() {
		super.onStart();

		// Emit LogCat message
		Log.i(TAG, "Entered the onStart() method");
		
		// Increment counters
		mStart += 1;
		
		// Display counters
		displayCounts();
	}

	@Override
	public void onResume() {
		super.onResume();

		// Emit LogCat message
		Log.i(TAG, "Entered the onResume() method");
		
		// Update mResume counter
		mResume += 1;
		
		// Display counters
		displayCounts();
	}

	@Override
	public void onPause() {
		super.onPause();

		// Emit LogCat message
		Log.i(TAG, "Entered the onPause() method");
	}

	@Override
	public void onStop() {
		super.onStop();

		// Emit LogCat message
		Log.i(TAG, "Entered the onStop() method");
	}

	@Override
	public void onRestart() {
		super.onRestart();

		// Emit LogCat message
		Log.i(TAG, "Entered the onRestart() method");
		
		// Update mRestart counter
		mRestart += 1;
		
		// Update the user interface
		displayCounts();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// Emit LogCat message
		Log.i(TAG, "Entered the onDestroy() method");
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		
		// Saving Lifecycle counters
		savedInstanceState.putInt(CREATE_KEY,mCreate);
		savedInstanceState.putInt(RESTART_KEY,mRestart);
		savedInstanceState.putInt(START_KEY,mStart);
		savedInstanceState.putInt(RESUME_KEY,mResume);
	}

	// Display counters
	public void displayCounts() {
		
		// Display counters
		mTvCreate.setText("onCreate() calls: " + mCreate);
		mTvStart.setText("onStart() calls: " + mStart);
		mTvResume.setText("onResume() calls: " + mResume);
		mTvRestart.setText("onRestart() calls: " + mRestart);
	}
}
