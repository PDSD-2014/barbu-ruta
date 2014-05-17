package ro.pub.cs.brtalk;

import ro.pub.cs.brtalk.interfaces.IManagerApp;
import ro.pub.cs.brtalk.services.IMService;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp extends Activity {
	
	private EditText username;
	private EditText password1;
	private EditText password2;
	private EditText email;
	private Button submit;
	private Button cancel;
	
	private IManagerApp imService;
	private Handler handler = new Handler();
	
	private ServiceConnection mConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onServiceConnected(ComponentName arg0, IBinder arg1) {
			// TODO Auto-generated method stub
			imService = ((IMService.IMBinder)arg1).getService();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//start service
		startService(new Intent(SignUp.this, IMService.class));
				
		setContentView(R.layout.sign_up_layout);
		
		setTitle("Sign Up");
		
		username = (EditText) findViewById(R.id.username_sign_up);
		password1 = (EditText) findViewById(R.id.password_1);
		password2 = (EditText) findViewById(R.id.password_2);
		email = (EditText) findViewById(R.id.email);
		submit = (Button) findViewById(R.id.sign_up_button);
		cancel = (Button) findViewById(R.id.cancel_button);
		
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (imService == null){
					Toast.makeText(SignUp.this, R.string.not_connected_service, Toast.LENGTH_LONG).show();
					return;
				}else if (imService.isNetworkConnected() == false){
					Toast.makeText(SignUp.this, R.string.not_connected_internet, Toast.LENGTH_LONG).show();
					return;
				}else if (username.length() == 0 || password1.length() == 0 || password2.length() == 0 || email.length() == 0 ){
					Toast.makeText(SignUp.this, R.string.all_fields, Toast.LENGTH_LONG).show();
					return;
				}else{	
					if (password1.getText().toString().equals(password2.getText().toString())){
						Thread threadSignUp = new Thread(){
							String result = new String();

							@Override
							public void run() {
								// TODO Auto-generated method stub
								result = imService.signUpUser(username.getText().toString(), password1.getText().toString(), email.getText().toString());
								handler.post(new Runnable() {
									public void run() {
										if (result.equals("1") || result.equals("COULD NOT ADD USER")){
											Toast.makeText(SignUp.this, R.string.signup_error, Toast.LENGTH_LONG).show();
										}else if (result.equals("USER EXISTENT")){
											Toast.makeText(SignUp.this, R.string.already_user, Toast.LENGTH_LONG).show();
										}else if (result.equals("SUCCESS")){
											Toast.makeText(SignUp.this, R.string.success_signup, Toast.LENGTH_LONG).show();
											finish();
										}
									}
								});
								
								
							}
							
						};
						threadSignUp.start();
					}else{
						password1.setText("");
						password2.setText("");
						Toast.makeText(SignUp.this, R.string.password_mismatch, Toast.LENGTH_LONG).show();
						return;
					}
				}
			}
		});
		
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
	}

	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		unbindService(mConnection);
		super.onPause();
	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		bindService(new Intent(SignUp.this, IMService.class), mConnection, Context.BIND_AUTO_CREATE);
		super.onResume();
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		return true;
	}

}
