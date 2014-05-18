package ro.pub.cs.brtalk;

import java.util.Arrays;
import java.util.List;

import ro.pub.cs.brtalk.services.Database;
import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Parcel; 

public class Friends extends Activity {
	private ListView listviewFriends;
	private Button addFriend;
	private Database database;
	private ArrayAdapter<Friend> friendArrayAdapter;
	final Context context = this; 
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends);
		database = Database.getInstance(this);
		
		final TextView no_friends = (TextView)findViewById(R.id.no_friends);
		if(database.selectFriends(-1).isEmpty())
			no_friends.setVisibility(View.VISIBLE);
		else no_friends.setVisibility(View.INVISIBLE);
		
		listviewFriends = (ListView)findViewById(R.id.listviewFriends);
		
		friendArrayAdapter = new ArrayAdapter<Friend>(this, android.R.layout.simple_spinner_item, database.selectFriends(-1));
		listviewFriends.setAdapter(friendArrayAdapter);
		
	
		
		//if("CHOOSE".equals(getIntent().getStringExtra("MUST"))){
		if("CHOOSE".equals(getIntent().getStringExtra("MUST"))){

			listviewFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				    //Toast.makeText(Friends.this, "You have clicked on "+friendArrayAdapter.getItem(position)+" item", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(Friends.this,Chat.class);
					intent.putExtra("CHATS<->FRIENDS<->CHAT","TRUE");
//					// 2. create person object
//					Friend passFriend = friendArrayAdapter.getItem(position);
//					// 3. put person in intent data	
//					intent.putExtra("passed",passFriend);
//					// 4. start the activity
					startActivity(intent);
				}
			});
			setTitle("Choose a friend");
		}
		else{
			listviewFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				    //Toast.makeText(Friends.this, "You have clicked on "+friendArrayAdapter.getItem(position)+" item", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(Friends.this,Friend.class);
					// 2. create person object
					Friend passFriend = friendArrayAdapter.getItem(position);
					// 3. put person in intent data	
					intent.putExtra("passed",passFriend);
					// 4. start the activity
					startActivity(intent);
				}
			});
		
		}
			listviewFriends.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			    @Override
			    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
			    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			    	alertDialogBuilder.setTitle("Delete this friend?");
			    	 
					// set dialog message
					alertDialogBuilder
						//.setMessage("Click yes to exit!")
						.setCancelable(false)
						.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								database.removeFriend(friendArrayAdapter.getItem(position));
								friendArrayAdapter.remove(friendArrayAdapter.getItem(position));
								if(database.selectFriends(-1).isEmpty())
									no_friends.setVisibility(View.VISIBLE);
							}
						  })
						.setNegativeButton("No",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
							}
						});
		 
						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();
		 
						// show it
						alertDialog.show();
			        Toast.makeText(Friends.this, "You have long clicked on "+friendArrayAdapter.getItem(position)+" item", Toast.LENGTH_SHORT).show();
			        return true;
			    }
			});
		
		
		
		
		
		addFriend = (Button) findViewById(R.id.addFriend);
		addFriend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Friend friend = new Friend(database.generateNextID(Database.FRIENDS_TABLE_NAME, Database.FRIEND_ID),"Alex");
				database.addFriend(friend);
				friendArrayAdapter.add(friend);
				no_friends.setVisibility(View.INVISIBLE);
			}
		});

	}

	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friends, menu);
		return true;
	}

}
