package labs.course.q1;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.HashMap;

public class MapActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		try {
			
			// Create Intent object for starting Google Maps application
			Intent geoIntent = new Intent(
							android.content.Intent.ACTION_VIEW, Uri
							.parse("geo:0,0?q=" + address));
			
			// Use the Intent to start Google Maps application using Activity.startActivity()
			startActivity(geoIntent);
			
		} catch (Exception e) {
			// Log any error messages to LogCat using Log.e()
		}

	}
}
