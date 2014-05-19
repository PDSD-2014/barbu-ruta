package ro.pub.cs.brtalk;

import ro.pub.cs.brtalk.interfaces.IManagerApp;
import ro.pub.cs.brtalk.services.Database;
import ro.pub.cs.brtalk.services.IMService;
import ro.pub.cs.brtalk.tools.Message;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Communicating extends Activity {

	public String commWith = null;
	private TextView comm;
	private EditText whereIWrite;
	private Button send;
	private IManagerApp imService ;
	private Database db;
	
	private ServiceConnection mConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			imService = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			imService = ((IMService.IMBinder)service).getService();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.communicating);

		comm = (TextView) findViewById(R.id.messageHistory);
		comm.setMovementMethod(new ScrollingMovementMethod());
		whereIWrite = (EditText) findViewById(R.id.message);
		send = (Button) findViewById(R.id.sends);
		db = Database.getInstance(this);
		Bundle extras = this.getIntent().getExtras();
		commWith = extras.getString("username");
		
		Message[] history = db.selectChats(commWith);
		
		for (int k = history.length-1; k >=0; k--){
			if (history[k].getDir().equals("venit")){
				int ir = 0;
				while(ir < 40){
					comm.setText(comm.getText() + "\n" + commWith + ":" + history[k].getText() + " "+ir);
					ir++;
				}
			}else{
				comm.setText(comm.getText() + "\nME:" + history[k].getText());
			}
		}
		
		send.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//comm.setText(commWith);
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.communicating, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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
		super.onResume();
		bindService(new Intent(Communicating.this, IMService.class),mConnection, Context.BIND_AUTO_CREATE);
	}

	

}
