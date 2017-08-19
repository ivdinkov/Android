package ivandinkov.github.com.taxiclerk;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		TextView email = (TextView) findViewById(R.id.txtEmail);
		TextView password = (TextView) findViewById(R.id.txtPass1);
		TextView button = (TextView) findViewById(R.id.btnRegister);
		
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO validate user input
				startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
			}
		});
	}
	
}
