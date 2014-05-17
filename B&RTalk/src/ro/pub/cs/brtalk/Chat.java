package ro.pub.cs.brtalk;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Chat extends Activity {

	private int chatID;
	private String chatName;
	
	public Chat(){
		this.chatID = -1;
		this.chatName = null;
	}

	public Chat(int chatID, String chatName) {
		this.chatID = chatID;
		this.chatName = chatName;
	}
	
	public int getChatID(){
		return this.chatID;
	}
	
	public String getChatName(){
		return this.chatName;
	}
	
	public void setChatID(int id){
		this.chatID = id;
	}
	
	public void setChatName(String name){
		this.chatName = name;
	}
	public String toString() {
		return chatID + " - " + chatName;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat, menu);
		return true;
	}

}
