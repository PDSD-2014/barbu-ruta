package ro.pub.cs.brtalk;

import ro.pub.cs.brtalk.services.Database;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class Chats extends Activity {
	
	private ListView listviewChats;
	
	private Button friendsBut;
	private Button newChatBut;

	Activity context;
	
	public Database database;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chats);
		
		context = this;
		database = Database.getInstance(this);
		
		friendsBut = (Button) findViewById(R.id.friends_button);
		friendsBut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Chats.this, Friends.class);
				startActivity(i);
			}
		});
		
		
		newChatBut = (Button) findViewById(R.id.new_chat_button);
		newChatBut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub	
			}
		});
		
		listviewChats = (ListView)findViewById(R.id.listviewChats);
		
		final ArrayAdapter<Chat> chatsAdapter = new ArrayAdapter<Chat>(this, android.R.layout.simple_list_item_1, database.selectChats(-1));
		if (chatsAdapter.isEmpty()){
			
		}
		listviewChats.setAdapter(chatsAdapter);
		listviewChats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			 @Override
			    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			        Toast.makeText(Chats.this, "You have clicked on "+chatsAdapter.getItem(position)+" item", Toast.LENGTH_SHORT).show();
			    }
			});
		listviewChats.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
		    @Override
		    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		        Toast.makeText(Chats.this, "You have long clicked on "+chatsAdapter.getItem(position)+" item", Toast.LENGTH_SHORT).show();
			return true;
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
