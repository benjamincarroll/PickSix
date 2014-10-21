package com.hockeypool.picksix;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.hockeypool.bc.BiddingPlayer;
import com.hockeypool.bc.DataWrapper;
import com.hockeypool.bc.Player;
import com.hockeypool.bc.Team;

public class GlobalState extends Application{
	public List<Player> viewPlayer = new ArrayList<Player>();
	public Team myTeam = new Team(Constants.MY_NAME);
	public List<BiddingPlayer> bidPlayers = new ArrayList<BiddingPlayer>();
	
	public BaseAdapter viewPlayersAdapter;
	public BaseAdapter myPlayersAdapter;
	public BaseAdapter bidAdapter;
	
	public DataWrapper dWrap;
	
	public ListView viewPlayersList; 	// List of players in search
	public ListView myPlayersList;		// List of players on my team
	public ListView currentBids;		// List of current bids taking place
	
	public void onCreate(){
		super.onCreate();
		dWrap = new DataWrapper(this);
	}
}
