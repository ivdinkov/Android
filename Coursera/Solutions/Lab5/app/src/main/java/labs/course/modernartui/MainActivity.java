package labs.course.modernartui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
	TextView tv1;
	TextView tv2;
	TextView tv3;
	TextView tv5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tv1 = (TextView) findViewById(R.id.tv1);
		tv2 = (TextView) findViewById(R.id.tv2);
		tv3 = (TextView) findViewById(R.id.tv3);
		tv5 = (TextView) findViewById(R.id.tv5);
		
		SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
		
		seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
	}
	
	private SeekBar.OnSeekBarChangeListener seekBarChangeListener
					= new SeekBar.OnSeekBarChangeListener() {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			updateBackground(progress);
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}
		
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		}
	};
	
	private void updateBackground(int progress) {
		// TODO rgb color change
		/**
		 * tv1
		 * min -> max
		 * rgb(106,119,183) -> rgb(255,255,183)
		 * 1.49,1.36,0
		 */
		int r1 = (int)(106 +((double)progress*1.49));
		int g1 = (int)(119 +((double)progress*1.36));
		tv1.setBackgroundColor(Color.rgb(r1,g1,183));
		/**
		 * tv2
		 * min -> max
		 * rgb(214,79,151) -> rgb(255,229,151)
		 * 0.41,1.5.,0
		 */
		int r2 = (int)(214 +((double)progress*0.41));
		int g2 = (int)(79 +((double)progress*1.50));
		tv2.setBackgroundColor(Color.rgb(r2,g2,151));
		/**
		 * tv3
		 * min -> max
		 * rgb(163,29,33) -> rgb(255,179,33)
		 * 0.92,1.5,0
		 */
		int r3 = (int)(163 +((double)progress*0.92));
		int g3 = (int)(29 +((double)progress*1.5));
		tv3.setBackgroundColor(Color.rgb(r3,g3,33));
		/**
		 * tv5
		 * min -> max
		 * rgb(39,58,157) -> rgb(189,208,157)
		 * 1.5,1.5,0
		 */
		int r5 = (int)(39 +((double)progress*1.5));
		int g5 = (int)(58 +((double)progress*1.5));
		tv5.setBackgroundColor(Color.rgb(r5,g5,157));
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
			// Display Dialog
			showAlertDialog();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Show alert dialog.
	 */
	private void showAlertDialog() {
		FragmentManager fm = getSupportFragmentManager();
		MyDialog alertDialog = new MyDialog();
		alertDialog.show(fm, "fragment_alert");
	}
	
	/**
	 * Dialog class
	 */
	public static class MyDialog extends DialogFragment {
		View customView;
		
		/* (non-Javadoc)
		 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
		 */
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			Rect displayRectangle = new Rect();
			Window window = getActivity().getWindow();
			window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
			
			LayoutInflater inflater = getActivity().getLayoutInflater();
			final Dialog alertDialogBuilder = new Dialog(getActivity(), R.style.DialogTheme);
			
			customView = inflater.inflate(R.layout.dialog, null);
			
			customView.setMinimumWidth((int) (displayRectangle.width() * 0.9f));
			customView.setMinimumHeight((int) (displayRectangle.height() * 0.2f));
			
			TextView btnYes = (TextView) customView.findViewById(R.id.btnDialogYes);
			TextView btnNo = (TextView) customView.findViewById(R.id.btnDialogNo);
			
			alertDialogBuilder.setContentView(customView);
			
			btnYes.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String url = "https://www.moma.org";
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(url));
					startActivity(i);
					alertDialogBuilder.cancel();
				}
			});
			btnNo.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					alertDialogBuilder.cancel();
				}
			});
			return alertDialogBuilder;
		}
	}
}
