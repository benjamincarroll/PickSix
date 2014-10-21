package com.hockeypool.picksix;

import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hockeypool.bc.BiddingPlayer;
import com.hockeypool.bc.Player;

public class MainActivity extends FragmentActivity {

	// Constants

	// An account type, in the form of a domain name
	public static final String ACCOUNT_TYPE = "picksix.com"; // this
																// could
																// be
																// where
	static final int num_tabs = 3;
	
	static Fragment[] fragments = {new Frag_SearchPlayers(), new Frag_MyTeam(), new Frag_CurrentBid()};
	static String[] tabNames = { "Search", "Teams", "Bids" };
	
	GlobalState state; // the
	ContentResolver mResolver;
	Uri mUri;
	
	MyAdapter myAdapter;
	ViewPager mPager;

	// The account name
	public static final String ACCOUNT = "dummyaccount";
	// Instance fields
	Account mAccount;
	Menu menu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final ActionBar actionBar = getActionBar();
		// Specify that tabs should be displayed in the action bar.
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    
	    myAdapter = new MyAdapter(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
                        getActionBar().setSelectedNavigationItem(position);
                    }
                });
        mPager.setAdapter(myAdapter);
        
        mPager.setCurrentItem(0);
        String[] teamNames = new String[] { "Ben's Team", "Dave's Team", "Mike's Team" };
		//actionBar.removeAllTabs();
		//actionBar.setDisplayHomeAsUpEnabled(true);
		//actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#a5a5a5")));
		//actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>( getBaseContext(), android.R.layout.simple_spinner_dropdown_item, teamNames);
		actionBar.setListNavigationCallbacks(adapter, new OnNavigationListener(){

			@Override
			public boolean onNavigationItemSelected(int arg0, long arg1) {
				// TODO Auto-generated method stub
				return false;
			}
			
		});
        
        state = (GlobalState) getApplication();
		state.viewPlayersList = (ListView) findViewById(R.id.listView1);
		
		// Create a tab listener that is called when the user changes tabs.
	    ActionBar.TabListener tabListener = new ActionBar.TabListener() {

			@Override
			public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				// When the tab is selected, switch to the
	            // corresponding page in the ViewPager.
	            mPager.setCurrentItem(tab.getPosition());
				
			}

			@Override
			public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				
			}
	    };
		
	 // Add 3 tabs, specifying the tab's text and TabListener
	    for (int i = 0; i < 3; i++) {
	        actionBar.addTab(
	                actionBar.newTab()
	                        .setText(tabNames[i])
	                        .setTabListener(tabListener));
	    }
		
		List<Player> allPlayers = state.dWrap.getAllPlayers();
		if (allPlayers != null) {
			state.viewPlayer = allPlayers;
		}
		
		List<Player> myTeam = state.dWrap.getTeam(Constants.MY_NAME);
		if (myTeam != null) {
			state.myTeam.setPlayers(myTeam); 
		}
		
		List<BiddingPlayer> bidders = state.dWrap.getAllBids();
		if (bidders != null){
			state.bidPlayers = bidders;
			if (state.bidAdapter != null)
				state.bidAdapter.notifyDataSetChanged();
		}
		
		mResolver = getContentResolver();
		// Initializes onChange
		TableObserver observer = new TableObserver(new Handler());
		mUri = new Uri.Builder().scheme(Constants.SCHEME)
				.authority(Constants.AUTHORITY).build();
		mResolver.registerContentObserver(mUri, true, observer);

		AccountManager accountManager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
		Account[] accountList = accountManager
				.getAccountsByType(Constants.ACCOUNT_TYPE);

		if (accountList.length == 0) {
			// user has no accounts
			Log.d("State", "User has no accounts");
			mAccount = CreateSyncAccount(this, accountManager);
		} else {
			mAccount = accountList[0];
			Log.d("State", "An account exists");
		}
		
		/*
		 * turn on periodic sync
		 */

		if (mAccount != null) {
			Log.d("State", "Setting sync period");
			ContentResolver.addPeriodicSync(mAccount, Constants.AUTHORITY,
					new Bundle(), 30 * 60);
		}

		if (state.viewPlayersAdapter != null) {
			state.viewPlayersAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * Create a new dummy account for the sync adapter
	 * 
	 * @param context
	 *            The application context
	 */
	public static Account CreateSyncAccount(Context context,
			AccountManager accountManager) {
		// Create the account type and default account
		Account newAccount = new Account(Constants.ACCOUNT_TYPE,
				Constants.ACCOUNT_TYPE);


		/*
		 * Add the account and account type, no password or user data If
		 * successful, return the Account object, otherwise report an error.
		 */
		if (accountManager.addAccountExplicitly(newAccount, null, null)) {
			/*
			 * If you don't set android:syncable="true" in in your <provider>
			 * element in the manifest, then call context.setIsSyncable(account,
			 * AUTHORITY, 1) here.
			 */

			ContentResolver.setIsSyncable(newAccount, Constants.AUTHORITY, 1);
            ContentResolver.cancelSync();


			// this could be where the problem is, I am suppose to be
			// returning the account object here
		} else {
			/*
			 * The account exists or some other error occurred. Log this, report
			 * it, or handle it internally.
			 */
		}
		return newAccount;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		this.menu = menu;
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		} else if (id == R.id.action_sync){
			Log.d("Testing", "sync has been requested");
			
			 // Pass the settings flags by inserting them in a bundle
	        Bundle settingsBundle = new Bundle();
	        settingsBundle.putBoolean(
	                ContentResolver.SYNC_EXTRAS_MANUAL, true);
	        settingsBundle.putBoolean(
	                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
			
			// this isnt working at the moment
			ContentResolver.requestSync(mAccount, Constants.AUTHORITY, settingsBundle);

		}
		return super.onOptionsItemSelected(item);
	}

	public class TableObserver extends ContentObserver {

		public TableObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			this.onChange(selfChange, null);
		}

		@Override
		public void onChange(boolean selfChange, Uri uri) {
			List<Player> allPlayers = state.dWrap.getAllPlayers();
			if (allPlayers != null) {
				state.viewPlayer = allPlayers;
				state.viewPlayersAdapter.notifyDataSetChanged();
			}
			
			List<BiddingPlayer> bidders = state.dWrap.getAllBids();
			if (bidders != null){
				state.bidPlayers = bidders;
				if (state.bidAdapter != null)
					state.bidAdapter.notifyDataSetChanged();
			}
		}
	}

	public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return num_tabs;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }
    }

}
