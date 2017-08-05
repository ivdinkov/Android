package ivandinkov.github.com.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Activity";

    protected void onStart(){
        super.onStart();
        Log.i(TAG, "onStart: Main Activity is visible and is about to start");
    }

    protected void onRestart(){
        super.onRestart();
        Log.i(TAG, "onRestart: Main Activity is visible and about to restart");
    }

    protected void onResume(){
        super.onResume();
        Log.i(TAG, "onResume: Main Activity is and has focus (it is resumed)");
    }

    protected void onPause(){
        super.onPause();
        Log.i(TAG, "onPause: Main Activity is taking focus, and is about to be paused");
    }

    protected void onStop(){
        super.onStop();
        Log.i(TAG, "onStop: Main Activity is no longer visible and it is now (stopped)");
    }

    protected void onDestroy(){
        super.onDestroy();
        Log.i(TAG, "onDestroy: Main Activity is about to be destroyed");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Display any saved instance
        super.onCreate(savedInstanceState);

        //Set view
        setContentView(R.layout.activity_main);

        //Initialize TextView
        final TextView activityA = (TextView) findViewById(R.id.textViewActivityA);

        //Initialize button
        final Button button = (Button) findViewById(R.id.btnStartActivityB);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    Intent openActivityB = new Intent(MainActivity.this,ActivityB.class);
                    startActivity(openActivityB);
            } catch (Exception e){
                Log.e(TAG,"Unable to start " +
                        "Activity B");
            }
            }
        });

        }

    }




