package ro.pub.cs.brtalk;

import java.util.List;

import ro.pub.cs.brtalk.services.Database;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Chats extends Activity {
	
	private ListView listviewChats;
	
	private Button friendsBut;
	private Button newChatBut;
	private ArrayAdapter<Chat> chatsAdapter;

	final Context context = this;
	public Database database;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chats);
		database = Database.getInstance(this);
		

		final TextView no_chats = (TextView)findViewById(R.id.no_chats);
		if(database.selectChats(-1).isEmpty())
			no_chats.setVisibility(View.VISIBLE);
		else no_chats.setVisibility(View.INVISIBLE);
		
		
		friendsBut = (Button) findViewById(R.id.friends_button);
		friendsBut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Chats.this, Friends.class);
				startActivity(i);
			}
		});
		
		listviewChats = (ListView)findViewById(R.id.listviewChats);
		
		chatsAdapter = new ArrayAdapter<Chat>(this, android.R.layout.simple_list_item_1, database.selectChats(-1));

		listviewChats.setAdapter(chatsAdapter);
		listviewChats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			 @Override
			    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			        //Toast.makeText(Chats.this, "You have clicked on "+chatsAdapter.getItem(position)+" item", Toast.LENGTH_SHORT).show();
				 	Intent i = new Intent(Chats.this,Chat.class);
				 	i.putExtra("passed",chatsAdapter.getItem(position));
					startActivity(i);
			 	}
			});
		listviewChats.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
							database.removeChat(chatsAdapter.getItem(position));
							chatsAdapter.remove(chatsAdapter.getItem(position));
							if(database.selectChats(-1).isEmpty())
								no_chats.setVisibility(View.VISIBLE);
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
		        Toast.makeText(Chats.this, "You have long clicked on "+chatsAdapter.getItem(position)+" item", Toast.LENGTH_SHORT).show();
		        return true;
		    }
		});
		
			
		
		newChatBut = (Button) findViewById(R.id.new_chat_button);
		newChatBut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub	
								
				Chat chat = new Chat(database.generateNextID(Database.CHATS_TABLE_NAME, Database.CHAT_ID),"newCHAT");
				database.addChat(chat);
				chatsAdapter.add(chat);
				no_chats.setVisibility(View.INVISIBLE);
				Intent i = new Intent(Chats.this, Friends.class);
				i.putExtra("MUST","CHOOSE");
				startActivity(i);
			}
		});
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chats, menu);
		return true;
	}

}
