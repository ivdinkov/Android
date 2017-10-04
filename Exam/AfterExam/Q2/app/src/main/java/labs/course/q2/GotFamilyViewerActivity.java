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
	public static String[] mDetailsArray;
	
	
	private final DetailsFragment mDetailsFragment = new DetailsFragment();
	private FragmentManager mFragmentManager;
	private FrameLayout mTitleFrameLayout, mQuotesFrameLayout;
	
	private static final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;
	private static final String TAG = "QuoteViewerActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		Log.i(TAG, getClass().getSimpleName() + ":entered onCreate()");
		
		super.onCreate(savedInstanceState);
		
		mTitleArray = getResources().getStringArray(R.array.HouseNames);
		mDetailsArray = getResources().getStringArray(R.array.Details);
		
		setContentView(R.layout.activity_main);
		
		mTitleFrameLayout = (FrameLayout) findViewById(R.id.house_fragment_container);
		mQuotesFrameLayout = (FrameLayout) findViewById(R.id.details_fragment_container);
		
		
		mFragmentManager = getFragmentManager();
		
		FragmentTransaction fragmentTransaction = mFragmentManager
						.beginTransaction();
		
		fragmentTransaction.add(R.id.house_fragment_container,
						new HouseFragment());
		
		fragmentTransaction.commit();
		
		mFragmentManager
						.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
							public void onBackStackChanged() {
								setLayout();
							}
						});
	}
	
	private void setLayout() {
		
		if (!mDetailsFragment.isAdded()) {
			
			mTitleFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(
							MATCH_PARENT, MATCH_PARENT));
			mQuotesFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
							MATCH_PARENT));
		} else {
			
			mTitleFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
							MATCH_PARENT, 1f));
			
			mQuotesFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
							MATCH_PARENT, 2f));
		}
	}
	
	// Called when the user selects an item in the TitlesFragment
	@Override
	public void onListSelection(int index) {
		
		if (!mDetailsFragment.isAdded()) {
			
		FragmentTransaction fragmentTransaction = mFragmentManager
							.beginTransaction();
			
			fragmentTransaction.add(R.id.details_fragment_container,
							mDetailsFragment);
			
			fragmentTransaction.addToBackStack(null);
			
			fragmentTransaction.commit();
			
			mFragmentManager.executePendingTransactions();
		}
		
		if (mDetailsFragment.getShownIndex() != index) {
			
			mDetailsFragment.showQuoteAtIndex(index);
			
		}
	}
	
}