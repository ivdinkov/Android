package labs.course.q2;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class GotFamilyViewerActivity extends AppCompatActivity implements HouseFragment.ListSelectionListener {
	
	public static String[] mTitleArray;
	public static String[] mQuoteArray;
	
	
	private final HouseFragment mQuoteFragment = new HouseFragment();
	private FragmentManager mFragmentManager;
	private FrameLayout mTitleFrameLayout, mQuotesFrameLayout;
	
	private static final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;
	private static final String TAG = "QuoteViewerActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		Log.i(TAG, getClass().getSimpleName() + ":entered onCreate()");
		
		super.onCreate(savedInstanceState);
		
		// Get the string arrays with the titles and qutoes
		mTitleArray = getResources().getStringArray(R.array.Titles);
		mQuoteArray = getResources().getStringArray(R.array.Quotes);
		
		setContentView(R.layout.activity_main);
		
		// Get references to the TitleFragment and to the QuotesFragment
		mTitleFrameLayout = (FrameLayout) findViewById(R.id.house_fragment_container);
		mQuotesFrameLayout = (FrameLayout) findViewById(R.id.details_fragment_container);
		
		
		// Get a reference to the FragmentManager
		mFragmentManager = getFragmentManager();
		
		// Start a new FragmentTransaction
		FragmentTransaction fragmentTransaction = mFragmentManager
						.beginTransaction();
		
		// Add the TitleFragment to the layout
		fragmentTransaction.add(R.id.house_fragment_container,
						new HouseFragment();
		
		// Commit the FragmentTransaction
		fragmentTransaction.commit();
		
		// Add a OnBackStackChangedListener to reset the layout when the back stack changes
		mFragmentManager
						.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
							public void onBackStackChanged() {
								setLayout();
							}
						});
	}
	
	private void setLayout() {
		
		// Determine whether the QuoteFragment has been added
		if (!mQuoteFragment.isAdded()) {
			
			// Make the TitleFragment occupy the entire layout
			mTitleFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(
							MATCH_PARENT, MATCH_PARENT));
			mQuotesFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
							MATCH_PARENT));
		} else {
			
			// Make the TitleLayout take 1/3 of the layout's width
			mTitleFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
							MATCH_PARENT, 1f));
			
			// Make the QuoteLayout take 2/3's of the layout's width
			mQuotesFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
							MATCH_PARENT, 2f));
		}
	}
	
	// Called when the user selects an item in the TitlesFragment
	@Override
	public void onListSelection(int index) {
		
		// If the QuoteFragment has not been added, add it now
		if (!mQuoteFragment.isAdded()) {
			
			// Start a new FragmentTransaction
			FragmentTransaction fragmentTransaction = mFragmentManager
							.beginTransaction();
			
			// Add the QuoteFragment to the layout
			fragmentTransaction.add(R.id.house_fragment_container, mQuoteFragment);
			
			// Add this FragmentTransaction to the backstack
			fragmentTransaction.addToBackStack(null);
			
			// Commit the FragmentTransaction
			fragmentTransaction.commit();
			
			// Force Android to execute the committed FragmentTransaction
			mFragmentManager.executePendingTransactions();
		}
		
		if (mQuoteFragment.getShownIndex() != index) {
			
			// Tell the QuoteFragment to show the quote string at position index
			mQuoteFragment.showQuoteAtIndex(index);
			
		}
	}
	

	
}
