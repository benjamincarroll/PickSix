package com.hockeypool.bc;

public class BiddingPlayer {
	private long id = -1;
	private int amount;
	private Player player;
	
	public BiddingPlayer(int id,int amount, Player player){
		this.amount = amount;
		this.player = player;
		this.id = id;
	}
	
	public BiddingPlayer(int amount, Player player){
		this.amount = amount;
		this.player = player;
	}
	
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
}
