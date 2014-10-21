package com.hockeypool.bc;

import com.hockeypool.picksix.Constants;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.content.ContentUris;

public class DataContentProvider extends ContentProvider {

	public static final Uri CONTENT_URI = Uri.parse("content://" + Constants.AUTHORITY);
	private DatabaseHandler database;

	@Override
	public boolean onCreate() {
		database = new DatabaseHandler(getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int ret = database.delete(getTableName(uri), selection, selectionArgs);

		getContext().getContentResolver().notifyChange(uri, null);

		return ret;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long retval = database.insert(getTableName(uri), values);

		Uri u = ContentUris.withAppendedId(CONTENT_URI, retval);
		getContext().getContentResolver().notifyChange(u, null);

		return u;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor c = database.query(getTableName(uri), projection, selection,
				selectionArgs, null, null, sortOrder);

		c.setNotificationUri(getContext().getContentResolver(), uri);

		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int ret = database.update(getTableName(uri), values, selection,
				selectionArgs);

		Uri u = ContentUris.withAppendedId(CONTENT_URI, Integer.parseInt(selectionArgs[0]));
		getContext().getContentResolver().notifyChange(u, null);

		return ret;
	}

	// helpers
	public static String getTableName(Uri uri) {
		String value = uri.getPath();
		value = value.replace("/", "");// we need to remove '/'
		return value;
	}

}