package ro.pub.cs.brtalk.services;

import java.util.ArrayList;
import java.util.List;

import ro.pub.cs.brtalk.Chat;
import ro.pub.cs.brtalk.Friend;
import ro.pub.cs.brtalk.tools.FriendInfo;
import ro.pub.cs.brtalk.tools.Message;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "B&R Talk";
	public static final int DATABASE_VERSION = 1;
	private static Database instance;
	
	public static final String ASSOCIATIONS_CHAT_FRIEND_TABLE_NAME = "associations_chats_friends";
	public static final String ASSOCIATION_CHAT_FRIEND_ID = "association_chat_friend_id";
	public static final String CHATS_TABLE_NAME = "chats";
	public static final String CHAT_ID = "chat_id";
	public static final String CHAT_TEXT = "name";
	public static final String FROM = "fromm";
	public static final String WHEN = "whenn";
	public static final String FRIENDS_TABLE_NAME = "friends";
	public static final String FRIEND_ID = "friend_id";
	public static final String FRIEND_NAME = "name";
	public static final String FRIEND_EMAIL = "email";
	public static final String DIR = "dir";

	public static Database getInstance(Context context) {
		if (instance == null)
			instance = new Database(context.getApplicationContext());

		return instance;
	}

	private Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String CREATE_CHATS_TABLE = "CREATE TABLE " + CHATS_TABLE_NAME + " ( "
				+ CHAT_ID + " INTEGER PRIMARY KEY, "
				+ CHAT_TEXT + " TEXT, " + FROM  + " TEXT, " + WHEN + " TEXT, " + DIR + " TEXT)" ; 
		String CREATE_FRIENDS_TABLE = "CREATE TABLE " + FRIENDS_TABLE_NAME + "("
				+ FRIEND_ID + " INTEGER PRIMARY KEY, "
				+ FRIEND_NAME + " TEXT, " + FRIEND_EMAIL + "TEXT)";	
		/*String CREATE_ASSOCIATIONS_CHATS_FRIENDS_TABLE = "CREATE TABLE " + ASSOCIATIONS_CHAT_FRIEND_TABLE_NAME + "("
				+ ASSOCIATION_CHAT_FRIEND_ID + " INTEGER PRIMARY KEY, "
				+ CHAT_ID + " INTEGER, " 
				+ FRIEND_ID + " INTEGER, "
				+ "FOREIGN KEY (" + CHAT_ID + ") REFERENCES " + CHATS_TABLE_NAME + " (" + CHAT_ID + "), "
				+ "FOREIGN KEY (" + FRIEND_ID + ") REFERENCES " + FRIENDS_TABLE_NAME + " (" + FRIEND_ID + "))";*/
		db.execSQL(CREATE_CHATS_TABLE);
		db.execSQL(CREATE_FRIENDS_TABLE);
		//db.execSQL(CREATE_ASSOCIATIONS_CHATS_FRIENDS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + CHATS_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + FRIENDS_TABLE_NAME);
		//db.execSQL("DROP TABLE IF EXISTS " + ASSOCIATIONS_CHAT_FRIEND_TABLE_NAME);
		onCreate(db);
	}
	
	public void addChat(Message mess) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues contentValues = new ContentValues();
		contentValues.put(CHAT_ID, mess.getId());
		contentValues.put(CHAT_TEXT, mess.getText());
		contentValues.put(FROM, mess.getFrom());
		contentValues.put(WHEN, mess.getWhen());
		contentValues.put(DIR, mess.getDir());
		
		db.insert(CHATS_TABLE_NAME, null, contentValues);
		
		db.close();
	}
	
	
	public void removeChat(Message mess) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(CHATS_TABLE_NAME, CHAT_ID, null);
		db.close();
	}
	
	public void addFriend(FriendInfo friend) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues contentValues = new ContentValues();
		contentValues.put(FRIEND_ID, friend.getId());
		contentValues.put(FRIEND_NAME, friend.getUsername());
//		contentValues.put(FRIEND_EMAIL, friend.getEmail());
		
		db.insert(FRIENDS_TABLE_NAME, null, contentValues);
		
		db.close();
	}
	public void removeFriend(Friend friend) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(FRIENDS_TABLE_NAME, FRIEND_ID + "=?", new String[]{ Integer.toString(friend.getFriendID()) });
		
		db.close();
	}
	
	public void deleteAllFriends(Friend friend) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(FRIENDS_TABLE_NAME, null, null);
		
		db.close();
	}

	
	public FriendInfo[] selectFriends() {
		SQLiteDatabase db = this.getReadableDatabase();
		String SELECT_FRIENDS_QUERY;
		SELECT_FRIENDS_QUERY = "SELECT " + FRIEND_NAME + " FROM " + FRIENDS_TABLE_NAME;
		
		Log.d("MY_TAG", SELECT_FRIENDS_QUERY);
		Cursor cursor = db.rawQuery(SELECT_FRIENDS_QUERY, null);
		ArrayList<FriendInfo> result = new ArrayList<FriendInfo>();
		if (cursor.moveToFirst()) {			
			do {
				FriendInfo friend = new FriendInfo(cursor.getString(0));
				result.add(friend);
			} while (cursor.moveToNext());
		}
		
		FriendInfo[] myres = new FriendInfo[result.size()];
		
		for(int k = 0; k < result.size(); k++){
			myres[k] = result.get(k);
		}
		
		return myres;
	}
	
	public Message[] selectChats(String username) {
		SQLiteDatabase db = this.getReadableDatabase();
		String SELECT_CHATS_QUERY;
		SELECT_CHATS_QUERY = "SELECT * FROM " + CHATS_TABLE_NAME + " WHERE " + FROM + " = '" + username + "'"; // "' ORDER BY " + WHEN + " DESC LIMIT 10" ; 
		Cursor cursor = db.rawQuery(SELECT_CHATS_QUERY, null);
		ArrayList<Message> result = new ArrayList<Message>();
		if (cursor.moveToFirst()) {
			do {
				Message chat = new Message();
				chat.setId(Integer.parseInt(cursor.getString(0)));
				chat.setText(cursor.getString(1));
				chat.setDir(cursor.getString(4));
				result.add(chat);
			} while (cursor.moveToNext());
		}
		
		Message[] mymes = new Message[result.size()];
		for(int k = 0; k < result.size(); k++){
			mymes[k] = result.get(k);
		}
		return mymes;
	}	
	
	public int generateNextID(String tableName, String primaryKeyName) {
		SQLiteDatabase db = this.getReadableDatabase();
		String SELECT_MAX_ID_FROM_TABLE = "SELECT MAX(" + primaryKeyName + ") FROM " + tableName;
		Cursor cursor = db.rawQuery(SELECT_MAX_ID_FROM_TABLE, null);
		if (cursor != null && cursor.moveToFirst() && cursor.getString(0) != null)
			return Integer.parseInt(cursor.getString(0)) + 1;
		
		return 0;
	}
}
