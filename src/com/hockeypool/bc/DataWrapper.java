package com.hockeypool.bc;

import java.util.ArrayList;
import java.util.List;

import com.hockeypool.picksix.Constants;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class DataWrapper {

	private ContentResolver resolver;
	public static final Uri TABLE_URI = Uri.withAppendedPath(
			DataContentProvider.CONTENT_URI, "players");
	public static final Uri BIDS_URI = Uri.withAppendedPath(
			DataContentProvider.CONTENT_URI, "bids");
	public static final Uri TEAM_URI = Uri.withAppendedPath(
			DataContentProvider.CONTENT_URI, "teams");

	public DataWrapper(Context context) {
		this.resolver = context.getContentResolver();
	}

	public long addPlayer(Player player) {
		Cursor cursor = resolver.query(TABLE_URI, null, "name =?",
				new String[] { player.getPlayerName() }, null);
		if (cursor.getCount() != 0) {
			Log.d("DBL", "A player with that name has already been added");
			cursor.close();
			return -1;
		}

		ContentValues values = new ContentValues();
		values.put("name", player.getPlayerName());
		values.put("points", player.getPlayerPoints());
		values.put("assists", player.getPlayerAssists());
		values.put("goals", player.getPlayerGoals());
		values.put("position", player.getPlayerPosition());

		Uri id_uri = resolver.insert(TABLE_URI, values);

		int id = Integer.parseInt(id_uri.getPath().replace("/", ""));
		player.setId(id);
		cursor.close();

		return id;
	}
	
	public long addTeam(Team team) {
		Cursor cursor = resolver.query(TEAM_URI, null, "name =?",
				new String[] { team.getName() }, null);
		if (cursor.getCount() != 0) {
			Log.d("DBL", "A team with that name has already been added");
			cursor.close();
			return -1;
		}

		ContentValues values = new ContentValues();
		values.put("name", team.getName());
		values.put("players", team.getPlayersJSON());
		Uri id_uri = resolver.insert(TABLE_URI, values);

		int id = Integer.parseInt(id_uri.getPath().replace("/", ""));
		team.setId(id);
		cursor.close();

		return id;
	}

	public long addBid(BiddingPlayer bid) {
		Player player = bid.getPlayer();
		Cursor cursor = resolver.query(BIDS_URI, null, "name =?",
				new String[] { player.getPlayerName() }, null);
		if (cursor.getCount() != 0) {
			Log.d("DBL", "A player with that name has already been added");
			cursor.close();
			return -1;
		}

		ContentValues values = new ContentValues();
		values.put("name", player.getPlayerName());
		values.put("amount", bid.getAmount());

		Uri id_uri = resolver.insert(BIDS_URI, values);

		int id = Integer.parseInt(id_uri.getPath().replace("/", ""));
		player.setId(id);
		cursor.close();

		return id;
	}

	public List<Player> getAllPlayers() {
		List<Player> allPlayers = new ArrayList<Player>();
		Cursor cursor = resolver.query(TABLE_URI, null, null, null,
				"points DESC");
		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}
		if (cursor.moveToFirst()) {
			do {
				Player players = new Player(Integer.parseInt(cursor
						.getString(0)), cursor.getString(1),
						Integer.parseInt(cursor.getString(2)),
						Integer.parseInt(cursor.getString(3)),
						Integer.parseInt(cursor.getString(4)),
						cursor.getString(5));
				allPlayers.add(players);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return allPlayers;
	}

	public List<BiddingPlayer> getAllBids() {
		List<BiddingPlayer> allPlayers = new ArrayList<BiddingPlayer>();
		Cursor cursor = resolver.query(BIDS_URI, null, null, null,
				"amount DESC");
		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}
		if (cursor.moveToFirst()) {
			do {
				String playerName = cursor.getString(1);
				Player player = getPlayer(playerName);
				BiddingPlayer players = new BiddingPlayer(
						Integer.parseInt(cursor.getString(0)),
						Integer.parseInt(cursor.getString(2)), 
						player);
				allPlayers.add(players);
			} while (cursor.moveToNext());
		}
		
		cursor.close();
		return allPlayers;
	}

	public Player getPlayer(String playerName) {
		Player player = null;
		Cursor cursor = resolver.query(TABLE_URI, null, "name =?",
				new String[] { playerName }, null);
		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		if (cursor.moveToFirst()) {
			player = new Player(
					Integer.parseInt(cursor.getString(0)),
					cursor.getString(1), 
					Integer.parseInt(cursor.getString(2)),
					Integer.parseInt(cursor.getString(3)),
					Integer.parseInt(cursor.getString(4)), 
					cursor.getString(5));
		}
		
		cursor.close();
		return player;
	}

	public List<Player> getTeam(String teamName) {
		List<Player> myPlayers = new ArrayList<Player>();
		String[] arguments = { teamName };
		Cursor cursor = resolver.query(TABLE_URI, null, "pool_team =?",
				arguments, "points DESC");
		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}
		if (cursor.moveToFirst()) {
			do {
				Player players = new Player(Integer.parseInt(cursor
						.getString(0)), cursor.getString(1),
						Integer.parseInt(cursor.getString(2)),
						Integer.parseInt(cursor.getString(3)),
						Integer.parseInt(cursor.getString(4)),
						cursor.getString(5));
				myPlayers.add(players);
			} while (cursor.moveToNext());
		}
		
		cursor.close();
		return myPlayers;
	}

	public long updatePlayer(Player player) {

		ContentValues values = new ContentValues();
		// values.put("id", player.getId());
		values.put("name", player.getPlayerName());
		values.put("points", player.getPlayerPoints());
		values.put("assists", player.getPlayerAssists());
		values.put("goals", player.getPlayerGoals());
		values.put("position", player.getPlayerPosition());

		String arguments[] = { player.getPlayerName() };

		int id_uri = resolver.update(TABLE_URI, values, "id = ?",
				new String[] { "" + player.getId() });

		return id_uri;
	}
}
