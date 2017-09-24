package ivandinkov.github.com.taxiclerk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
	
	private static final String TAG = "TC";
	DisplayMetrics dm;
	EditText txtLoginEmail;
	EditText txtLoginPassword;
	Button btnLogin;
	private ProgressDialog progressDialog;
	private FirebaseAuth firebaseAuth;
	private LinearLayout lnrViewSlide;
	private TextView notHaveAcc;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		// initialize progress bar
		progressDialog = new ProgressDialog(this);
		
		// initialize Firebase object
		firebaseAuth = FirebaseAuth.getInstance();
		
		
		LinearLayout loginLayoutWrapper = (LinearLayout) findViewById(R.id.loginLayout);
		txtLoginEmail = (EditText) findViewById(R.id.txtLoginEmail);
		txtLoginPassword = (EditText) findViewById(R.id.txtLoginPassword);
		lnrViewSlide = (LinearLayout) findViewById(R.id.login_slide);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		notHaveAcc = (TextView) findViewById(R.id.txtViewRegister);
		
		// Get device dimensions
		dm = getWidthAndHeightPx();
		// Set register layout holder to 80% width
		LinearLayout.LayoutParams lpWrapper = (LinearLayout.LayoutParams) loginLayoutWrapper.getLayoutParams();
		lpWrapper.leftMargin = (dm.widthPixels - (int) (dm.widthPixels * 0.8)) / 2;
		lpWrapper.rightMargin = (dm.widthPixels - (int) (dm.widthPixels * 0.8)) / 2;
		
		
		
		notHaveAcc.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
				finish();
			}
		});
		txtLoginPassword.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 2f);
				lnrViewSlide.setLayoutParams(layoutParams1);
				return false;
			}
		});
		txtLoginEmail.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 2f);
				lnrViewSlide.setLayoutParams(layoutParams1);
				return false;
			}
		});
		
		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!checkConnection()) {
					// TODO display fragment message
					Toast toast = Toast.makeText(LoginActivity.this, "No connection!", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.TOP, 0, 0);
					toast.show();
				} else {
					loginUser();
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
	
	private void loginUser() {
		
		// Reset errors.
		txtLoginEmail.setError(null);
		txtLoginEmail.setError(null);
		
		// Store values at the time of the login attempt.
		String email = txtLoginEmail.getText().toString();
		String password = txtLoginPassword.getText().toString();
		
		boolean cancel = false;
		View focusView = null;
		
		
		// Check for a valid password, if the user entered one.
		if (TextUtils.isEmpty(password)) {
			txtLoginEmail.setError(getString(R.string.error_field_required));
			focusView = txtLoginEmail;
			cancel = true;
		}
		
		// Check for a valid email address.
		if (TextUtils.isEmpty(email)) {
			txtLoginEmail.setError(getString(R.string.error_field_required));
			focusView = txtLoginEmail;
			cancel = true;
		}
		
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
			
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			progressDialog.setMessage("Login User..");
			progressDialog.show();
			
			// Firebase login
			firebaseAuth.signInWithEmailAndPassword(email,password)
			.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
				@Override
				public void onComplete(@NonNull Task<AuthResult> task) {
					if (task.isSuccessful()) {
						progressDialog.dismiss();
						startActivity(new Intent(LoginActivity.this,MainActivity.class));
						finish();
					} else {
						progressDialog.dismiss();
						Toast toast = Toast.makeText(LoginActivity.this, "Login fail!", Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
						toast.show();
					}
				}
			});
		}
	}
	
}
