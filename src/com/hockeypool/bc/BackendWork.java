package com.hockeypool.bc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.util.Log;

public class BackendWork {

	// Start working here!! Yeah nut
	public void updatebids(String url, Context con, List<BiddingPlayer> bids){
		AndroidHttpClient http = AndroidHttpClient.newInstance("myApp");
		HttpPost post = new HttpPost("http://" + url + "/team");

	}

	public static Team[] getAllTeams(String url, Context con){
		Team[] teams = null;
		ArrayList<Team> teamList = new ArrayList<Team>();
		String data = null;
		BufferedReader in = null;
		AndroidHttpClient http = AndroidHttpClient.newInstance("myApp");
		HttpGet request = new HttpGet("http://" + url + "/teams");
		try {
			HttpResponse response = http.execute(request);
			if (response != null) {
				Log.d("Downlaod", "Connected to PickSix server");
			} else {
				Log.d("Download", "Could not connect to PickSix server");
			}

			HttpEntity resEntityGet = response.getEntity();
			data = new String(EntityUtils.toString(resEntityGet));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		http.close();
		try {
			JSONArray jArray = new JSONArray(data);
			int length = jArray.length();
			for (int i = 0; i < length; i++){
				Log.d("Testing", "Name: " + jArray.getJSONObject(i).getString("name"));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return teams;
	}
	public static Player[] getAllPlayers(String url, Context con) {
		Player[] players = null;
		ArrayList<Player> playerList = new ArrayList<Player>();
		BufferedReader in = null;
		String data = null;
		// http get call, retrieves response from PickSix
		AndroidHttpClient http = AndroidHttpClient.newInstance("myApp");
		Log.d("Testing", "This is the url: " + "http://" + url + "/players");
		HttpGet request = new HttpGet("http://" + url + "/players");

		try {
			HttpResponse response = http.execute(request);
			if (response != null) {
				Log.d("Downlaod", "Connected to PickSix server");
			} else {
				Log.d("Download", "Could not connect to PickSix server");
			}

			HttpEntity resEntityGet = response.getEntity();
			data = new String(EntityUtils.toString(resEntityGet));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		http.close();

		// parsing the JSON data
		try {
			JSONArray jArray = new JSONArray(data);
			int length = jArray.length();
			for (int i = 0; i < length; i++) {
				String name = jArray.getJSONObject(i).getString("name");
				int points = jArray.getJSONObject(i).getInt("points");
				int goals = jArray.getJSONObject(i).getInt("goals");
				int assists = jArray.getJSONObject(i).getInt("assists");
				String position = jArray.getJSONObject(i).getString("position");
				Player player = new Player(name, points, assists, goals,
						position);
				// Log.d("Testing", "Position: " + player.getPlayerPosition());
				playerList.add(player);

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		int length = playerList.size();
		players = new Player[length];

		for (int i = 0; i < length; i++) {
			players[i] = playerList.get(i);
		}

		return players;
	}

	public static void updateTeam(Team team, String url, Context con) {
		AndroidHttpClient http = AndroidHttpClient.newInstance("myApp");
		HttpPost post = new HttpPost("http://" + url + "/team");
		JSONObject allPlayer = new JSONObject();

		List<Player> playerList = team.getPlayers();
			JSONArray positionArray = new JSONArray();
			for (Player somePlayer : playerList) {
				JSONObject player = new JSONObject();
				try {
					player.put("name", somePlayer.getPlayerName());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				positionArray.put(player);
			}
			try {
				allPlayer.put("Players", positionArray);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		try {
			post.setHeader("name", team.getName());
			post.setHeader("size", team.getPlayers().size() + "");
			post.setEntity(createStringEntity(allPlayer));
			HttpResponse resp = http.execute(post);
			if (resp != null){
				if (resp.getStatusLine().getStatusCode() == 204){
					Log.d("UPDATE", "Update team successful");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		http.close();
	}

	private static HttpEntity createStringEntity(JSONObject params) {
	    StringEntity se = null;
	    try {
	        se = new StringEntity(params.toString(), "UTF-8");
	        se.setContentType("application/json; charset=UTF-8");
	    } catch (UnsupportedEncodingException e) {
	    	e.printStackTrace();
	    }
	    return se;
	}

}
