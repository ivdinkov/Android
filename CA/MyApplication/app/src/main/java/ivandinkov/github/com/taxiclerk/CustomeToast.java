package ivandinkov.github.com.taxiclerk;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by iv on 30/10/2017.
 */

public class CustomeToast {
	public static void ShowToast(Activity activity, View view, LayoutInflater inflater, String message) {
		// inflate the new view
		View toastLayout = inflater.inflate(R.layout.custom_toast, (ViewGroup) view.findViewById(R.id.CustomToastLayout));
		// set custom message
		TextView messageView = (TextView) toastLayout.findViewById(R.id.toastText);
		messageView.setText(message);
		// build the toast
		Toast toast = new Toast(activity);
		toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL | Gravity.CENTER_HORIZONTAL, 0,0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(toastLayout);
		toast.show();
	}
}
