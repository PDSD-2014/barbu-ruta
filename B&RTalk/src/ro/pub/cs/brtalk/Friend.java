package ro.pub.cs.brtalk;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Friend extends Activity {
	private int friendID;
	private String friendName;
	public Friend (){
		this.friendID = -1;
		this.friendName = null;
	}
	public Friend (int ID, String name){
		this.friendID = ID;
		this.friendName = name;
	}
	public int getFriendID() {
		return friendID;
	}

	public void setFriendID(int friendID) {
		this.friendID = friendID;
	}

	public String getFriendName() {
		return friendName;
	}

	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}
	public String toString() {
		return friendID + " - " + friendName;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friend, menu);
		return true;
	}

}
