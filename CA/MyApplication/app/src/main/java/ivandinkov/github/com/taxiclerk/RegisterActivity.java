package ivandinkov.github.com.taxiclerk;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {
	
	DisplayMetrics dm;
	private static Boolean terms = false;
	private static CheckBox chkBoxTerms;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		TextView email = (TextView) findViewById(R.id.txtEmail);
		TextView password = (TextView) findViewById(R.id.txtPass1);
		TextView button = (TextView) findViewById(R.id.btnRegister);
		TextView terms = (TextView) findViewById(R.id.terms);
		chkBoxTerms = (CheckBox) findViewById(R.id.checkbox_terms);
		LinearLayout registerLayoutWrapper = (LinearLayout) findViewById(R.id.registerLayout);
		
		
		dm = getWidthAndHeightPx();
		// Set register layout holder to 80% width
		LinearLayout.LayoutParams lpWrapper = (LinearLayout.LayoutParams)registerLayoutWrapper.getLayoutParams();
		lpWrapper.leftMargin = (dm.widthPixels - (int)(dm.widthPixels*0.8))/2;
		lpWrapper.rightMargin = (dm.widthPixels - (int)(dm.widthPixels*0.8))/2;
		
		terms.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showAlertDialog();
			}
		});
		
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO validate user input
				startActivity(new Intent(getApplicationContext(),LoginActivity.class));
				finish();
			}
		});
	}
	
	/**
	 * Method for getting device screen metrics
	 * @return DisplayMetrics
	 */
	public DisplayMetrics getWidthAndHeightPx() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm;
	}
	/**
	 * This method converts dp unit to equivalent pixels, depending on device density.
	 *
	 * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
	 * @return A float value to represent px equivalent to dp depending on device density
	 */
	private  float convertDpToPixel(float dp){
		Resources resources = getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
		return px;
	}
	/**
	 * This method converts device specific pixels to density independent pixels.
	 *
	 * @param px A value in px (pixels) unit. Which we need to convert into db
	 * @return A float value to represent dp equivalent to px value
	 */
	private  float convertPixelsToDp(float px){
		Resources resources = getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
		return dp;
	}
	/**
	 * Call to Terms Dialog Fragment.
	 */
	private void showAlertDialog() {
		FragmentManager fm = getSupportFragmentManager();
		MyDialog alertDialog = new MyDialog();
		alertDialog.show(fm, "fragment_alert");
	}
	
	/**
	 * Dialog class used to display terms fragment
	 */
	public static class MyDialog extends DialogFragment {
		private View customView;
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			Rect displayRectangle = new Rect();
			Window window = getActivity().getWindow();
			window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
			LayoutInflater inflater = getActivity().getLayoutInflater();
			final Dialog alertDialogBuilder = new Dialog(getActivity(), R.style.DialogTheme);
			customView = inflater.inflate(R.layout.terms_dialog, null);
			customView.setMinimumWidth((int) (displayRectangle.width() * 0.9f));
			customView.setMinimumHeight((int) (displayRectangle.height() * 0.9f));
			TextView btnYes = (TextView) customView.findViewById(R.id.btnDialogYes);
			TextView txtTerms = (TextView) customView.findViewById(R.id.txtTemrs);
			txtTerms.setMovementMethod(new ScrollingMovementMethod());
			alertDialogBuilder.setContentView(customView);
			
			btnYes.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					chkBoxTerms.setChecked(true);
					terms = true;
					alertDialogBuilder.cancel();
				}
			});
			return alertDialogBuilder;
		}
	}
	
}
