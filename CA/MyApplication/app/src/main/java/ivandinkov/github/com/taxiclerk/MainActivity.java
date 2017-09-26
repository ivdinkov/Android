package ivandinkov.github.com.taxiclerk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
				implements NavigationView.OnNavigationItemSelectedListener,
				HomeFragment.OnFragmentInteractionListener,
				DayFragment.OnFragmentInteractionListener,
				MonthFragment.OnFragmentInteractionListener,
				YearFragment.OnFragmentInteractionListener,
				IncomeFragment.OnFragmentInteractionListener,
				ExpenseFragment.OnFragmentInteractionListener,
				ReportFragment.OnFragmentInteractionListener,
				SettingsFragment.OnFragmentInteractionListener,
				NewIncomeFragment.OnFragmentInteractionListener,
				NewExpenseFragment.OnFragmentInteractionListener,
				JobProviderFragment.OnFragmentInteractionListener,
				ExpenseAddFragment.OnFragmentInteractionListener,
				TrainFragment.OnFragmentInteractionListener
			

{
	private static final String TAG = "TC";
	LinearLayout btnHolder;
	boolean isFirstTime = true;
	private FirebaseAuth firebaseAuth;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		firebaseAuth = FirebaseAuth.getInstance();
		//		FirebaseUser user = firebaseAuth.getCurrentUser();
//		if(user != null){
//			Log.i(TAG,"User signed in");
//			startActivity(new Intent(LoginActivity.this,MainActivity.class));
//		}else{
//			Log.i(TAG,"User signed out");
//		}
		
		
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
						this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();
		
		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
		
		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(1);
			isFirstTime = false;
		}
		// get linear layout reference
		btnHolder = (LinearLayout) findViewById(R.id.income_button_holder);
		
		Button btnAddFare = (Button) findViewById(R.id.new_fare);
		btnAddFare.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment fragment = null;
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				// Hide buttons to make space for new income fragment
				btnHolder.setVisibility(View.GONE);
				// Show Settings
				fragment = new NewIncomeFragment();
				ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
				ft.replace(R.id.main_fragment_container, fragment, "new fare").commit();
			}
		});
		Button btnAddExpense = (Button) findViewById(R.id.new_expense);
		btnAddExpense.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment fragment = null;
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				// Hide buttons to make space for new expense fragment
				btnHolder.setVisibility(View.GONE);
				// Show Settings
				fragment = new NewExpenseFragment();
				ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
				ft.replace(R.id.main_fragment_container, fragment, "new expense").commit();
			}
		});
		
	}
	
	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		switch (position) {
			case 1:
				// Show buttons
					if (!isFirstTime) {
						btnHolder.setVisibility(View.VISIBLE);
					}
				fragment = new HomeFragment();
				ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
				ft.replace(R.id.main_fragment_container, fragment, "home").commit();
				break;
			case 2:
				// Hide buttons to make space for settings fragment
				btnHolder.setVisibility(View.GONE);
				// Show all Jobs
				fragment = new IncomeFragment();
				ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
				ft.replace(R.id.main_fragment_container, fragment, "income").commit();
				break;
			case 3:
				// Hide buttons to make space for settings fragment
				btnHolder.setVisibility(View.GONE);
				// Show All expenses
				fragment = new ExpenseFragment();
				ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
				ft.replace(R.id.main_fragment_container, fragment, "expense").commit();
				break;
			case 4:
				// Hide buttons to make space for settings fragment
				btnHolder.setVisibility(View.GONE);
				// Show Reports
				fragment = new ReportFragment();
				ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
				ft.replace(R.id.main_fragment_container, fragment, "reports").commit();
				break;
			case 5:
				// Hide buttons to make space for settings fragment
				btnHolder.setVisibility(View.GONE);
				// Show Settings
				fragment = new SettingsFragment();
				ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
				ft.replace(R.id.main_fragment_container, fragment, "settings").commit();
				break;
			case 6:
				btnHolder.setVisibility(View.GONE);
				// Show train fragment
				fragment = new TrainFragment();
				ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
				ft.replace(R.id.main_fragment_container, fragment, "settings").commit();
				break;
			case 7:
				// Sign Out App
				finish();
				firebaseAuth.signOut();
 				startActivity(new Intent(MainActivity.this,LoginActivity.class));
				
				break;
			
			default:
				break;
		}
		
//		if (fragment != null) {
//			FragmentManager fragmentManager = getSupportFragmentManager();
//			fragmentManager.beginTransaction().replace(R.id.main_fragment_container,
//							fragment).commit();
//
//		} else {
//			finish();
//			startActivity(new Intent(MainActivity.this,LoginActivity.class));
//		}
	}
	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			// Disable Android back button
			displayView(1);
			// Show buttons
			btnHolder.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();
		
		if (id == R.id.nav_home) {
			displayView(1);
		} else if (id == R.id.nav_jobs) {
			displayView(2);
		} else if (id == R.id.nav_expenses) {
			displayView(3);
		} else if (id == R.id.nav_reports) {
			displayView(4);
		} else if (id == R.id.nav_settings) {
			displayView(5);
		} else if (id == R.id.nav_trains) {
			displayView(6);
		} else if (id == R.id.nav_logout) {
			displayView(7);
		} else if (id == R.id.nav_send) {
			
		}
		
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		
		return true;
	}
	
	@Override
	public void onFragmentInteraction(Uri uri) {
		
	}
	
}
