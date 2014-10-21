package com.hockeypool.picksix;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.hockeypool.bc.BackendWork;
import com.hockeypool.bc.Player;
import com.hockeypool.bc.Team;
	/**
	 * Handle the transfer of data between a server and an
	 * app, using the Android sync adapter framework.
	 */
	public class SyncAdapter extends AbstractThreadedSyncAdapter {
	    
		GlobalState state;
	    // Global variables
	    // Define a variable to contain a content resolver instance
	    ContentResolver mContentResolver;
	    Context con;
	    /**
	     * Set up the sync adapter
	     */
	    public SyncAdapter(Context context, boolean autoInitialize) {
	        super(context, autoInitialize);
	        /*
	         * If your app uses a content resolver, get an instance of it
	         * from the incoming Context
	         */
	        Log.d("Sync Adapter", "Sync Adapter has started");
	        mContentResolver = context.getContentResolver();
	        con = context.getApplicationContext();
	        state = (GlobalState) context.getApplicationContext();
	    }
	   
	    /**
	     * Set up the sync adapter. This form of the
	     * constructor maintains compatibility with Android 3.0
	     * and later platform versions
	     */
	    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
		public SyncAdapter(
	            Context context,
	            boolean autoInitialize,
	            boolean allowParallelSyncs) {
	        super(context, autoInitialize, allowParallelSyncs);
	        /*
	         * If your app uses a content resolver, get an instance of it
	         * from the incoming Context
	         */
	        mContentResolver = context.getContentResolver();
	     
	    }

		@Override
		public void onPerformSync(
				Account account, 
				Bundle extras, 
				String authority,
				ContentProviderClient provider, 
				SyncResult syncResult) {
			
			Log.d("Testing", "Inside the sync adapter");
			Player[] newPlayers = BackendWork.getAllPlayers(Constants.URL, con);
				
			for (int i = 0; i < newPlayers.length; i++){
				state.dWrap.addPlayer(newPlayers[i]);
			}
			
			BackendWork.updateTeam(state.myTeam, Constants.URL, con);
			Team[] teams = BackendWork.getAllTeams(Constants.URL, con);
			
			for (int i = 0; i < teams.length; i++){
				state.dWrap.addTeam(teams[i]);
			}
		}
}
