package ro.pub.cs.brtalk;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ro.pub.cs.brtalk.interfaces.IManagerApp;
import ro.pub.cs.brtalk.services.Database;
import ro.pub.cs.brtalk.services.IMService;
import ro.pub.cs.brtalk.tools.Message;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
	public int lastID = 0;
	String message;
	
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
//				int ir = 0;
//				while(ir < 40){
				comm.setText(comm.getText() + "\n" + commWith + ":" + history[k].getText());
//					ir++;
//				}
			}else{
				comm.setText(comm.getText() + "\nME:" + history[k].getText());
			}
		}
		
		send.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				message = whereIWrite.getText().toString();
				whereIWrite.setText("");
				Message m = new Message(commWith,message,getDateTime(),lastID,"iesit");
				
				db.addChat(m);
				comm.setText(comm.getText() + "\nME:" + message); 
				Thread sendMessage = new Thread(){
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						imService.sendMessage(commWith, message);
						
						
					}
					
				};
				sendMessage.start();
				
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
		unregisterReceiver(mr);
		super.onPause();
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		bindService(new Intent(Communicating.this, IMService.class),mConnection, Context.BIND_AUTO_CREATE);
		IntentFilter i = new IntentFilter();
		i.addAction("MESAJENOI");
		registerReceiver(mr, i);
	}

	
	public class MessageReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			Bundle extra = arg1.getExtras();
			if (extra != null){
				String action = arg1.getAction();
				if (action.equals("MESAJENOI")){
					comm.setText("");
					Message[] history = db.selectChats(commWith);
					
					for (int k = history.length-1; k >=0; k--){
						if (history[k].getDir().equals("venit")){
//							int ir = 0;
//							while(ir < 40){
							comm.setText(comm.getText() + "\n" + commWith + ":" + history[k].getText());
//								ir++;
//							}
						}else{
							comm.setText(comm.getText() + "\nME:" + history[k].getText());
						}
					}
				}
			}
			
		}
		
	};
	
	private MessageReceiver mr = new MessageReceiver();
	
	private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
}
	

}
