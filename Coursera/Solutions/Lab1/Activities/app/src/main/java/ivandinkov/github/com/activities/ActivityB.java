package ivandinkov.github.com.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivityB extends AppCompatActivity {
    
    private static final String TAG = "Activity";
    
    protected void onStart(){
        super.onStart();
        Log.i(TAG, "onStart: Activity B is visible and is about to start");
    }
    
    protected void onRestart(){
        super.onRestart();
        Log.i(TAG, "onRestart: Activity B is visible and about to restart");
    }
    
    protected void onResume(){
        super.onResume();
        Log.i(TAG, "onResume: Activity B is and has focus (it is resumed)");
    }
    
    protected void onPause(){
        super.onPause();
        Log.i(TAG, "onPause: Activity B is taking focus, and is about to be paused");
    }
    
    protected void onStop(){
        super.onStop();
        Log.i(TAG, "onStop: Activity B is no longer visible and it is now (stopped)");
    }
    
    protected void onDestroy(){
        super.onDestroy();
        Log.i(TAG, "onDestroy: Activity B is about to be destroyed");
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        
        //Load any saved instances
        super.onCreate(savedInstanceState);
        
        //Set content view
        setContentView(R.layout.activity_b);
        
        Log.i(TAG, "onCreate: ActivityB is created");
        
        //Initialize TextView
        final TextView activityA = (TextView) findViewById(R.id.textViewActivityB);
        
        //Initialize button
        final Button button = (Button) findViewById(R.id.btnStartActivityA);
        
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openActivityA = new Intent(ActivityB.this,MainActivity.class);
                startActivity(openActivityA);
            }
        });
    }
}
