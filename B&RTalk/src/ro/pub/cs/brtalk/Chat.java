package ro.pub.cs.brtalk;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class Chat extends Activity implements Parcelable {

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
		setTitle(R.string.chats);

		final TextView chat = (TextView)findViewById(R.id.chat_details);
		if("TRUE".equals(getIntent().getStringExtra("CHATS<->FRIENDS<->CHAT"))){
	       chat.setText("a venit de la fiends");
        }
		else {
		       chat.setText("a venit de la chats");

			Chat passedChat = getIntent().getExtras().getParcelable("passed");		
	        chat.setText(passedChat.toString());
		}
	}
 
		     
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friend, menu);
		return true;
	}
	
	// Parceling part
    public Chat(Parcel in){
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
		dest.writeString(this.chatName);
		dest.writeInt(this.chatID);
	}

	private void readFromParcel(Parcel in) {   
		// We just need to read back each 
		// field in the order that it was 
// written to the parcel 
		this.chatName = in.readString(); 
		this.chatID = in.readInt(); 
		}
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { 
		public Chat createFromParcel(Parcel in) { 
			return new Chat(in); }   
			public Chat[] newArray(int size) { 
				return new Chat[size]; 
				} 
			};


}
