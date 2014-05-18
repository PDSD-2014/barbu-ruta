package ro.pub.cs.brtalk.services;

import java.util.ArrayList;
import java.util.List;

import ro.pub.cs.brtalk.Chat;
import ro.pub.cs.brtalk.Friend;
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
	public static final String CHAT_NAME = "name";
	public static final String FRIENDS_TABLE_NAME = "friends";
	public static final String FRIEND_ID = "friend_id";
	public static final String FRIEND_NAME = "name";

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

		String CREATE_CHATS_TABLE = "CREATE TABLE " + CHATS_TABLE_NAME + "("
				+ CHAT_ID + " INTEGER PRIMARY KEY, "
				+ CHAT_NAME + " TEXT)";
		String CREATE_FRIENDS_TABLE = "CREATE TABLE " + FRIENDS_TABLE_NAME + "("
				+ FRIEND_ID + " INTEGER PRIMARY KEY, "
				+ FRIEND_NAME + " TEXT)";	
		String CREATE_ASSOCIATIONS_CHATS_FRIENDS_TABLE = "CREATE TABLE " + ASSOCIATIONS_CHAT_FRIEND_TABLE_NAME + "("
				+ ASSOCIATION_CHAT_FRIEND_ID + " INTEGER PRIMARY KEY, "
				+ CHAT_ID + " INTEGER, " 
				+ FRIEND_ID + " INTEGER, "
				+ "FOREIGN KEY (" + CHAT_ID + ") REFERENCES " + CHATS_TABLE_NAME + " (" + CHAT_ID + "), "
				+ "FOREIGN KEY (" + FRIEND_ID + ") REFERENCES " + FRIENDS_TABLE_NAME + " (" + FRIEND_ID + "))";
		db.execSQL(CREATE_CHATS_TABLE);
		db.execSQL(CREATE_FRIENDS_TABLE);
		db.execSQL(CREATE_ASSOCIATIONS_CHATS_FRIENDS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + CHATS_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + FRIENDS_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + ASSOCIATIONS_CHAT_FRIEND_TABLE_NAME);
		onCreate(db);
	}
	
	public void addChat(Chat chat) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues contentValues = new ContentValues();
		contentValues.put(CHAT_ID, chat.getChatID());
		contentValues.put(CHAT_NAME, chat.getChatName());
		
		db.insert(CHATS_TABLE_NAME, null, contentValues);
		
		db.close();
	}
	public void addFriend(Friend friend) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues contentValues = new ContentValues();
		contentValues.put(FRIEND_ID, friend.getFriendID());
		contentValues.put(FRIEND_NAME, friend.getFriendName());
		
		db.insert(FRIENDS_TABLE_NAME, null, contentValues);
		
		db.close();
	}
	
	
	public void removeChat(Chat chat) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues contentValues = new ContentValues();
		contentValues.remove(CHAT_ID);
		contentValues.remove(CHAT_NAME);
		
		db.delete(CHATS_TABLE_NAME, CHAT_ID + "=?", new String[]{ Integer.toString(chat.getChatID()) });
		
		db.close();
	}
	
	
	public void removeFriend(Friend friend) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues contentValues = new ContentValues();
		contentValues.remove(FRIEND_ID);
		contentValues.remove(FRIEND_NAME);
		
		db.delete(FRIENDS_TABLE_NAME, FRIEND_ID + "=?", new String[]{ Integer.toString(friend.getFriendID()) });
		
		db.close();
	}
	
	public void deleteAllFriends() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(FRIENDS_TABLE_NAME, null, null);
	}
	
	public void deleteAllChats() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(CHATS_TABLE_NAME, null, null);
	}

	
	public List<Friend> selectFriends(int ID) {
		SQLiteDatabase db = this.getReadableDatabase();
		String SELECT_FRIENDS_QUERY;
		if (ID != -1)
			SELECT_FRIENDS_QUERY = "SELECT t." + FRIEND_ID + ", " + FRIEND_NAME + " FROM " + FRIENDS_TABLE_NAME + " t, " + ASSOCIATIONS_CHAT_FRIEND_TABLE_NAME + " act"
				+ " WHERE act." + CHAT_ID + "=" + ID + " AND t." + FRIEND_ID + " = act." + FRIEND_ID;
		else
			SELECT_FRIENDS_QUERY = "SELECT * FROM " + FRIENDS_TABLE_NAME;
		
		Log.d("MY_TAG", SELECT_FRIENDS_QUERY);
		Cursor cursor = db.rawQuery(SELECT_FRIENDS_QUERY, null);
		ArrayList<Friend> result = new ArrayList<Friend>();
		if (cursor.moveToFirst()) {			
			do {
				Friend friend = new Friend();
				friend.setFriendID(Integer.parseInt(cursor.getString(0)));
				friend.setFriendName(cursor.getString(1));
				result.add(friend);
			} while (cursor.moveToNext());
		}
		return result;
	}
	
	public List<Chat> selectChats(int ID) {
		SQLiteDatabase db = this.getReadableDatabase();
		String SELECT_CHATS_QUERY;
		if (ID != -1)
			SELECT_CHATS_QUERY = "SELECT c." + CHAT_ID + ", "+ CHAT_NAME + " FROM " + CHATS_TABLE_NAME + " c, " + ASSOCIATIONS_CHAT_FRIEND_TABLE_NAME + " act"
				+ " WHERE act." + CHAT_ID + "=" + ID + " AND c." + CHAT_ID + " = act." + CHAT_ID;
		else
			SELECT_CHATS_QUERY = "SELECT * FROM " + CHATS_TABLE_NAME; 
		Cursor cursor = db.rawQuery(SELECT_CHATS_QUERY, null);
		ArrayList<Chat> result = new ArrayList<Chat>();
		if (cursor.moveToFirst()) {
			do {
				Chat chat = new Chat();
				chat.setChatID(Integer.parseInt(cursor.getString(0)));
				chat.setChatName(cursor.getString(1));
				result.add(chat);
			} while (cursor.moveToNext());
		}
		return result;
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
