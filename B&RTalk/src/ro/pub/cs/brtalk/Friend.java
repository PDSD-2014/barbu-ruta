package ro.pub.cs.brtalk;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class Friend extends Activity implements Parcelable{
	
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
		// 1. get intent
		Intent intent = getIntent(); 
        // 2. get person object from intent
		Friend passedFriend = intent.getExtras().getParcelable("passed");
        // 3. get reference to person textView 
        final TextView friend = (TextView)findViewById(R.id.friend_details);
        // 4. display details on textView 
        friend.setText(passedFriend.toString());
		setTitle(passedFriend.friendName);

	}
 
		     
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friend, menu);
		return true;
	}
	
	// Parceling part
    public Friend(Parcel in){
  	  readFromParcel(in);
    }
    
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(this.friendName);
		dest.writeInt(this.friendID);
	}

	private void readFromParcel(Parcel in) {   
		// We just need to read back each 
		// field in the order that it was 
// written to the parcel 
		this.friendName = in.readString(); 
		this.friendID = in.readInt(); 
		}
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { 
		public Friend createFromParcel(Parcel in) { 
			return new Friend(in); }   
			public Friend[] newArray(int size) { 
				return new Friend[size]; 
				} 
			};

}
