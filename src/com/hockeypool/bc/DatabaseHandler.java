package com.hockeypool.bc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "playerManager";
	private static final String TABLE_PLAYERS = "players";
	private static final String TABLE_TEAMS = "teams";
	private static final String TABLE_BIDS = "bids";
	private static DatabaseHandler instance = null;
	private static SQLiteDatabase db;

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		DatabaseHandler.instance = this;
	}

	public static DatabaseHandler getInstance() {
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_PLAYERS_TABLE = "CREATE TABLE " + TABLE_PLAYERS +
				"(id INTEGER PRIMARY KEY," +
				" name TEXT," +
				" points INTEGER," +
				" assists INTEGER," +
				" goals INTEGER," +
				" position TEXT," +
				" pool_team TEXT" +
				")";
		
		String CREATE_BIDS_TABLE = "CREATE TABLE " + TABLE_BIDS + 
				"(id INTEGER PRIMARY KEY," +
				" name TEXT," +
				" amount INTEGER" +
				")";
		
		String CREATE_TEAMS_TABLE = "CREATE TABLE " + TABLE_TEAMS + 
				"(id INTEGER PRIMARY KEY," +
				" name TEXT," +
				" players TEXT" +
				")";

		db.execSQL(CREATE_BIDS_TABLE);
		db.execSQL(CREATE_PLAYERS_TABLE);
		db.execSQL(CREATE_TEAMS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
		onCreate(db);
	}

	public int delete(String table, String whereClause, String[] whereArgs) {
		if (db == null)
			db = this.getWritableDatabase();

		return db.delete(table, whereClause, whereArgs);
	}

	public long insert(String table, ContentValues values) {
		if (db == null)
			db = this.getWritableDatabase();

		return db.insert(table, null, values);
	}

	public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
		if (db == null)
			db = this.getWritableDatabase();

		return db.update(table, values, whereClause, whereArgs);
	}

	public Cursor query(String table, String[] columns, String select, String[] selectionArgs, String groupBy, String having, String orderBy) {
		if (db == null)
			db = this.getReadableDatabase();

		return db.query(table, columns, select, selectionArgs, groupBy, having, orderBy);
	}
}