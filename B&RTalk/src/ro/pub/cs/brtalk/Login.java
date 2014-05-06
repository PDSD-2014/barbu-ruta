package ro.pub.cs.brtalk;

import ro.pub.cs.brtalk.interfaces.IManagerApp;
import ro.pub.cs.brtalk.services.IMService;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	
	public static final int SIGN_UP_ID = Menu.FIRST;
	public static final int EXIT_ID = Menu.FIRST + 1;

	private EditText username;
	private EditText password;
	private Button cancelBut;
	private Button loginBut;
	
	private IManagerApp iMService;
	
	private ServiceConnection mConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			
			
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			iMService = ((IMService.IMBinder)service).getService();
			if (iMService.isUserAuthenticated() == true){
				Toast.makeText(Login.this, "User autentificat!", Toast.LENGTH_LONG).show();
				Intent i = new Intent(Login.this, HelloWorkd.class);
				startActivity(i);
				Login.this.finish();
			}
			//this will be removed in final version
			else{
				Toast.makeText(Login.this, "User neautentificat!", Toast.LENGTH_LONG).show();
			}
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//start service
		startService(new Intent(Login.this, IMService.class));
		
		
		setContentView(R.layout.login_screen);
		setTitle(R.string.login_title);
		
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		cancelBut = (Button) findViewById(R.id.cancel);
		loginBut = (Button) findViewById(R.id.login);
		
		loginBut.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (iMService == null){
					Toast.makeText(Login.this, R.string.not_connected_service, Toast.LENGTH_LONG).show();
					return;
				}else if (iMService.isNetworkConnected() == false){
					Toast.makeText(Login.this, R.string.not_connected_internet, Toast.LENGTH_LONG).show();
					return;
				}else if (username.length() > 0 && password.length() > 0){
					Thread loginThread = new Thread(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							super.run();
						}
						
					};
				}else{
					//Username or/and password were not inserted
					Toast.makeText(Login.this, R.string.not_inserted_user_pass, Toast.LENGTH_LONG).show();
					return;
				}
				
			}
		});
		
		cancelBut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				iMService.exit();
				finish();
			}
		});
	}
	
	

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		bindService(new Intent(this,IMService.class), mConnection, Context.BIND_AUTO_CREATE);
		super.onStart();
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
		bindService(new Intent(this,IMService.class), mConnection, Context.BIND_AUTO_CREATE);
		super.onResume();
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Boolean retM = super.onCreateOptionsMenu(menu);
		menu.add(0, SIGN_UP_ID, 0, R.string.sign_up);
		menu.add(0, EXIT_ID, 0, R.string.exit);
		return retM;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		
		switch (item.getItemId()) {
		case SIGN_UP_ID:
			Intent i = new Intent(Login.this, SignUp.class);
			startActivity(i);
			break;

		case EXIT_ID:
			cancelBut.performClick();
			break;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}

	
	
}
