package com.hockeypool.bc;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

public class Team {
	private long id = -1;
	private String name;
	private List<Player> players;
	
	public Team(String name){
		this.name = name;
		this.players = new ArrayList<Player>();
	}
	
	public Team(long id, String name){
		setId(id);
		this.name = name;
	}
	
	public void setPlayers(List<Player> players){
		this.players = players;
	}
	
	public void addPlayer(Player player){
		this.players.add(player);
	}
	
	public String getName(){
		return this.name;
	}
	
	public List<Player> getPlayers(){
		return this.players;
	}
	
	public String getPlayersJSON(){
		String sPlayers = "";
		JSONArray jPlayers = new JSONArray();
		for (Player p : this.players){
			jPlayers.put(p.getId());
		}
		
		sPlayers = jPlayers.toString();
		return sPlayers;
	}
	
	public void setId(long id){
		if (this.id == -1)
			this.id = id;
	}
	
	public long getId(){
		return id;
	}
}
