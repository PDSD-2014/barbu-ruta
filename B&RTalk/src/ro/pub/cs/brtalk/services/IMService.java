package ro.pub.cs.brtalk.services;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import ro.pub.cs.brtalk.communication.Socket;
import ro.pub.cs.brtalk.interfaces.IManagerApp;
import ro.pub.cs.brtalk.interfaces.ISocket;
import ro.pub.cs.brtalk.tools.FriendInfo;
import ro.pub.cs.brtalk.tools.Message;
import ro.pub.cs.brtalk.tools.ParseXMLFile;
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
	private Database db; 
	private Timer timer;
	
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
		db = Database.getInstance(this);
		
		timer = new Timer();
		
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
		timer.cancel();
		socket.exit();
		socket = null;
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
		}else{
			return result;
		}
		
		/*
		 * Now that the user is authenticated let's get some updates
		 */
		
		String params1 = params + "&messages=yes";
		String newMessages = socket.sendHttpRequest(params1);
		ParseXMLFile pXMLMe = new ParseXMLFile(newMessages, true, false);
		ArrayList<Message> messageList = pXMLMe.getMessages();
		for(int cnt = 0; cnt < messageList.size(); cnt++){
			db.addChat(messageList.get(cnt));
		}
		
		Intent i = new Intent("MESAJENOI");
		i.putExtra("MESAJE", newMessages);
		sendBroadcast(i);
		
		String params2 = params + "&friends=yes"; 
		String newFriends = socket.sendHttpRequest(params2);
		ParseXMLFile pXMLFr = new ParseXMLFile(newFriends, false, true);
		ArrayList<FriendInfo> friendList = pXMLFr.getFriends();
		for(int cnt = 0; cnt < friendList.size(); cnt++){
			db.addFriend(friendList.get(cnt));
		}
		Intent j = new Intent("NEWFRIENDS");
		j.putExtra("FRIENDS", newFriends);
		sendBroadcast(j);
		
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				String newMessages = IMService.this.getMessages();
				String newFriends = IMService.this.getFriends();
				ParseXMLFile pXMLMe = new ParseXMLFile(newMessages, true, false);
				ArrayList<Message> messageList = pXMLMe.getMessages();
				for(int cnt = 0; cnt < messageList.size(); cnt++){
					db.addChat(messageList.get(cnt));
				}
				Intent i = new Intent("MESAJENOI");
				i.putExtra("MESAJE", newMessages);
				sendBroadcast(i);
				
				ParseXMLFile pXMLFr = new ParseXMLFile(newFriends, false, true);
				ArrayList<FriendInfo> friendList = pXMLFr.getFriends();
				for(int cnt = 0; cnt < friendList.size(); cnt++){
					db.addFriend(friendList.get(cnt));
				}
				Intent j = new Intent("NEWFRIENDS");
				j.putExtra("FRIENDS", newFriends);
				sendBroadcast(j);
				
			}
		}, 15000, 15000);
		
		return result;
	}
	
	
	public String getFriends(){
		String result = null;
		String params = "type=signin&username=" + username + "&password=" + password + "&friends=yes";
		result = socket.sendHttpRequest(params);
		
		return result;
	}
	
	public String getMessages(){
		String result = null;
		String params = "type=signin&username=" + username + "&password=" + password + "&messages=yes";
		result = socket.sendHttpRequest(params);
		
		return result;
	}


	@Override
	public String friendAdd(String user) {
		// TODO Auto-generated method stub
		String result = null;
		String params = "type=addfriend&username=" + user + "&fromwho=" + this.username;
		result = socket.sendHttpRequest(params);
		
		String[] newTemp = result.split(" ");
		FriendInfo ala = new FriendInfo(newTemp[0], newTemp[1], Integer.parseInt(newTemp[2]));
		db.addFriend(ala);
		Intent j = new Intent("NEWFRIENDS");
		j.putExtra("FRIENDS", result);
		sendBroadcast(j);
		
		return result;
	}
	

}
