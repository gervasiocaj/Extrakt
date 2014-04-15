package com.gervasiocaj.extrakt;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager {
	
	public static final String 
		SCHEMA = "extrakt",
		IMAGES_TABLE = "images",
		IMAGES_ID = "imdbcode",
		IMAGES_IMG = "img",
		IMAGES_CREATE = "CREATE TABLE " + IMAGES_TABLE + " ( " +
							IMAGES_ID + " text primary key not null, " +
							IMAGES_IMG + " text );";
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
			db.execSQL(IMAGES_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
		}
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public void insert(String imdb_id, String img) {
		ContentValues values = new ContentValues();
		values.put(IMAGES_ID, imdb_id);
		values.put(IMAGES_IMG, img);
		db.insert(IMAGES_TABLE, null, values);
	}

}
