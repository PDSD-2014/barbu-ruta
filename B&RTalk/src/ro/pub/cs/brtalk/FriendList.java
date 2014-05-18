package ro.pub.cs.brtalk;

import java.util.ArrayList;

import ro.pub.cs.brtalk.interfaces.IManagerApp;
import ro.pub.cs.brtalk.services.IMService;
import ro.pub.cs.brtalk.tools.FriendInfo;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FriendList extends ListActivity {
	
	private IManagerApp imService = null;
	private FriendListAdapter friendAdapter;
	
	private class FriendListAdapter extends BaseAdapter{

		class ViewHolder{
			ImageView icon;
			TextView text;
		}
		
		private FriendInfo[] friends = null;
		private LayoutInflater mInflater;
		private Bitmap mIcon;
		
		public FriendListAdapter(Context context){
			super();
			mInflater = LayoutInflater.from(context);
			mIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return friends.length;
		}

		@Override
		public FriendInfo getItem(int arg0) {
			// TODO Auto-generated method stub
			return friends[arg0];
		}

		public void setFriendList(FriendInfo[] fi){
			this.friends = fi;
		}
		
		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView == null){
				convertView = mInflater.inflate(R.layout.friend_list, null);
				holder = new ViewHolder();
				holder.text = (TextView) convertView.findViewById(R.id.text);
				holder.icon = (ImageView) convertView.findViewById(R.id.icon);
				
				convertView.setTag(holder);	
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.text.setText(friends[position].getUsername());
			holder.icon.setImageBitmap(mIcon);
			
			return convertView;
		}
		
	}
	
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
		setContentView(R.layout.activity_friend_list);
		
		friendAdapter = new FriendListAdapter(this);
		
		FriendInfo fi = new FriendInfo("ionut", "ionut", 5);
		FriendInfo[] fr = new FriendInfo[1];
		fr[0] = fi;
		friendAdapter.setFriendList(fr);
		setListAdapter(friendAdapter);
		
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(this,Chats.class);
		FriendInfo friend = friendAdapter.getItem(position);
		intent.putExtra("username", friend.getUsername());
		startActivity(intent);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		unbindService(mConnection);
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		bindService(new Intent(FriendList.this, IMService.class),mConnection, Context.BIND_AUTO_CREATE);
	}

	
	

}
