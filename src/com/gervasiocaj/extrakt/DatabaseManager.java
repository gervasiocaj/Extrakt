package com.gervasiocaj.extrakt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseManager {
	
	public static final String 
		SCHEMA = "extrakt",
		STORED_CALLS = "stored_calls",
		STORED_CALLS_CALL = "call",
		STORED_CALLS_RESPONSE = "response",
		STORED_CALLS_CREATE = "CREATE TABLE " + STORED_CALLS + " ( " +
							STORED_CALLS_CALL + " text primary key not null, " +
							STORED_CALLS_RESPONSE + " text );";
	public static final int VERSION = 1;
	private final Context context;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	
	public DatabaseManager(Context ctx) {
		this.context = ctx;
		this.dbHelper = new DatabaseHelper(context); 
		this.db = dbHelper.getWritableDatabase();
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, SCHEMA, null, VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(STORED_CALLS_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
		}
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public String getStoredCall(String call) {
		Log.d(getClass().getSimpleName(), "get call " + call);
		Cursor response = db.query(STORED_CALLS, new String[] {STORED_CALLS_RESPONSE}, STORED_CALLS_CALL + " like ?", new String[] {call}, null, null, null);
		if (!response.moveToFirst())
			return null;
		return response.getString(0);
	}
	
	public void insertOrUpdateStoredCall(String call, String response) {
		Log.d(getClass().getSimpleName(), "inserting/updating call " + call);
		ContentValues values = new ContentValues();
		values.put(STORED_CALLS_RESPONSE, response);
		
		if (getStoredCall(call) == null) { // will insert
			values.put(STORED_CALLS_CALL, call);
			db.insert(STORED_CALLS, null, values);
		} else { //will update
			db.update(STORED_CALLS, values, STORED_CALLS_CALL + " like ?", new String[] {call});
		}
	}
	
	/*
	public void insert(String imdb_id, String img) {
		ContentValues values = new ContentValues();
		values.put(IMAGES_ID, imdb_id);
		values.put(IMAGES_IMG, img);
		db.insert(IMAGES_TABLE, null, values);
	} */

}
