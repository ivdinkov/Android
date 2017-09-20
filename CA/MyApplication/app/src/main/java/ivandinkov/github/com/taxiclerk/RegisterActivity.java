package ivandinkov.github.com.taxiclerk;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
	
	DisplayMetrics dm;
	private static Boolean terms = false;
	private static CheckBox chkBoxTerms;
	TextView txtSign;
	TextView txtTerms;
	EditText txtEmail;
	EditText txtPassword;
	Button btnRegister;
	private ProgressDialog progressDialog;
	private FirebaseAuth firebaseAuth;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		// initialize progress bar
		progressDialog = new ProgressDialog(this);
		// initialize firebase object
		firebaseAuth = FirebaseAuth.getInstance();
		txtTerms = (TextView) findViewById(R.id.terms);
		chkBoxTerms = (CheckBox) findViewById(R.id.checkbox_terms);
		LinearLayout registerLayoutWrapper = (LinearLayout) findViewById(R.id.registerLayout);
		txtEmail = (EditText) findViewById(R.id.txtEmail);
		txtPassword = (EditText) findViewById(R.id.txtPassword);
		txtSign = (TextView) findViewById(R.id.txtViewSign);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		
		
		dm = getWidthAndHeightPx();
		// Set register layout holder to 80% width
		LinearLayout.LayoutParams lpWrapper = (LinearLayout.LayoutParams) registerLayoutWrapper.getLayoutParams();
		lpWrapper.leftMargin = (dm.widthPixels - (int) (dm.widthPixels * 0.8)) / 2;
		lpWrapper.rightMargin = (dm.widthPixels - (int) (dm.widthPixels * 0.8)) / 2;
		
		txtTerms.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showAlertDialog();
			}
		});
		chkBoxTerms.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				terms = chkBoxTerms.isChecked();
			}
		});
		btnRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!checkConnection()) {
					// TODO display fragment message
					Toast toast = Toast.makeText(RegisterActivity.this, "No connection!", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.TOP, 0, 0);
					toast.show();
				} else {
					registerUser();
				}
			}
		});
	}
	
	/**
	 * Check for internet connection.
	 *
	 * @return the boolean
	 */
	private Boolean checkConnection() {
		ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
		if (activeNetwork != null) {
			return true;
		}
		return false;
	}
	
	/**
	 * Method for getting device screen metrics
	 *
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
	private float convertDpToPixel(float dp) {
		Resources resources = getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
		return px;
	}
	
	/**
	 * This method converts device specific pixels to density independent pixels.
	 *
	 * @param px A value in px (pixels) unit. Which we need to convert into db
	 * @return A float value to represent dp equivalent to px value
	 */
	private float convertPixelsToDp(float px) {
		Resources resources = getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
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
	
	private void registerUser() {
		
		
		// Reset errors.
		txtEmail.setError(null);
		txtPassword.setError(null);
		
		// Store values at the time of the login attempt.
		String email = txtEmail.getText().toString();
		String password = txtPassword.getText().toString();
		
		boolean cancel = false;
		View focusView = null;
		
		
		// Check for a valid password, if the user entered one.
		if (TextUtils.isEmpty(password)) {
			txtPassword.setError(getString(R.string.error_field_required));
			focusView = txtPassword;
			cancel = true;
		} else if (!isPasswordValid(password)) {
			txtPassword.setError(getString(R.string.error_invalid_password));
			focusView = txtPassword;
			cancel = true;
			
		}
		
		// Check for a valid email address.
		if (TextUtils.isEmpty(email)) {
			txtEmail.setError(getString(R.string.error_field_required));
			focusView = txtEmail;
			cancel = true;
		} else if (!isEmailValid(email)) {
			txtEmail.setError(getString(R.string.error_invalid_email));
			focusView = txtEmail;
			cancel = true;
		}
		// Check for terms checked
		if (!terms) {
			Toast toast = Toast.makeText(RegisterActivity.this, "You have to accept the terms!", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.show();
			focusView = chkBoxTerms;
			cancel = true;
		}
		
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
			
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			progressDialog.setMessage("Registering User..");
			progressDialog.show();
			
			firebaseAuth.createUserWithEmailAndPassword(email, password)
							.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
								@Override
								public void onComplete(@NonNull Task<AuthResult> task) {
									if (task.isSuccessful()) {
										progressDialog.dismiss();
										Toast toast = Toast.makeText(RegisterActivity.this, "Registration success!", Toast.LENGTH_LONG);
										toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
										toast.show();
										startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
										finish();
									} else {
										progressDialog.dismiss();
										Toast toast = Toast.makeText(RegisterActivity.this, "Registration fail!", Toast.LENGTH_LONG);
										toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
										toast.show();
									}
								}
							});
			
		}
	}
	
	
	private boolean isEmailValid(String email) {
		return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}
	
	private boolean isPasswordValid(String password) {
		return password.length() > 6;
	}
	
	/**
	 * Dialog class used to display terms fragment
	 */
	public static class MyDialog extends DialogFragment {
		View customView;
		TextView btnYes;
		TextView txtTerms;
		
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
			btnYes = (TextView) customView.findViewById(R.id.btnDialogYes);
			txtTerms = (TextView) customView.findViewById(R.id.txtTemrs);
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
