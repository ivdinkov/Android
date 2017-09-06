package ivandinkov.github.com.taxiclerk;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by iv on 06/09/2017.
 */

public class HomeAdapter extends FragmentStatePagerAdapter {
	
	private String fragmentNames[] = {"DAY","WEEK","YEAR"};
	
	public HomeAdapter(FragmentManager fm) {
		super(fm);
	}
	
	@Override
	public Fragment getItem(int arg0) {
		Fragment fragment = null;
		if (arg0 == 0) {
			fragment = new DayFragment();
		}
		if (arg0 == 1) {
			fragment = new MonthFragment();
		}
		if (arg0 == 2) {
			fragment = new YearFragment();
		}
		
		return fragment;
	}
	
	@Override
	public int getCount() {
		return 3;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		String n = new String();
		if(position == 0)
		{
			n = fragmentNames[0];
		}
		if(position == 1)
		{
			n = fragmentNames[1];
		}
		if(position == 2)
		{
			n = fragmentNames[2];
		}
		return n;
	}
}