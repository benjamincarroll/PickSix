package com.hockeypool.bc;

public class Player {

	private long id = -1;
	private String name;
	private int goals;
	private int assists;
	private int points;
	private String position;
	
	public Player(String name, int points, int assists, int goals, String position){
		this.name = name;
		this.goals = goals;
		this.assists = assists;
		this.points = points;
		this.position = position;
	}
	
	public Player(long id, String name, int points, int assists, int goals, String position){
		setId(id);
		this.name = name;
		this.goals = goals;
		this.assists = assists;
		this.points = points;
		this.position = position;
	}
	
	public void setId(long id){
		if (this.id == -1)
			this.id = id;
	}
	
	public long getId(){
		return id;
	}
	
	public String getPlayerName(){
		return name;
	}
	
	public String getPlayerPosition(){
		return position;
	}
	
	public int getPlayerGoals(){
		return goals;
	}
	
	public int getPlayerAssists(){
		return assists;
	}
	
	public int getPlayerPoints(){
		return points;
	}
}
