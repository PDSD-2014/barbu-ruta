package ro.pub.cs.brtalk.services;


import ro.pub.cs.brtalk.communication.Socket;
import ro.pub.cs.brtalk.interfaces.IManagerApp;
import ro.pub.cs.brtalk.interfaces.ISocket;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.IBinder;

public class IMService extends Service implements IManagerApp {
	
	private final IBinder mBinder = new IMBinder();
	private NotificationManager mNM;
	private ConnectivityManager mConMan = null;
	private Boolean authenticatedUser = false;
	private String username = null;
	private String password = null;
	private ISocket socket = new Socket();
	
	public class IMBinder extends Binder{
		public IManagerApp getService(){
			return IMService.this;
		}
	}

	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		mConMan = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
		
	}

	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}


	@Override
	public boolean isNetworkConnected() {
		// TODO Auto-generated method stub
		if ( mConMan.getActiveNetworkInfo() == null ){
			return false;
		}else{
			return mConMan.getActiveNetworkInfo().isConnected();
		}
	}


	@Override
	public boolean isUserAuthenticated() {
		// TODO Auto-generated method stub
		return authenticatedUser;
	}


	@Override
	public void exit() {
		// TODO Auto-generated method stub
		this.stopSelf();
	}


	@Override
	public String signUpUser(String username, String password, String email) {
		// TODO Auto-generated method stub
		String params = "type=signup&username=" + username + "&password=" + password + 
				"&email=" + email;
		String result = socket.sendHttpRequest(params);
		return result;
	}


	@Override
	public String signInUser(String username, String password) {
		// TODO Auto-generated method stub
		this.authenticatedUser = false;
		String params = "type=signin&username=" + username + "&password=" + password;
		String result = socket.sendHttpRequest(params);
		if (!result.equals("1") && !(result.equals("INCORECT DATA"))){
			authenticatedUser = true;
			this.username = username;
			this.password = password;
		}
		return result;
	}
	
	
	
	

}
