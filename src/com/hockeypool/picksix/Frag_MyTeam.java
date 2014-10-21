package com.hockeypool.picksix;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Frag_MyTeam extends Fragment {
	private GlobalState state;
	View rootView;
	ListView myPlayerList;
	Context context;
	
	
class ItemAdapter extends BaseAdapter {
		
		@Override
		public int getCount(){
			return state.myTeam.getPlayers().size();
		}
		
		@Override
		public Object getItem(int position){
			return position;
		}
		
		@Override
		public long getItemId(int position){
			return position;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent){
			
			View view = convertView;
			if (convertView == null){
				view = getActivity().getLayoutInflater().inflate(R.layout.my_team_item, parent, false);
				
			} 	
			TextView playerName = (TextView) view.findViewById(R.id.playerName);
			playerName.setText(state.myTeam.getPlayers().get(position).getPlayerName());
			
			TextView playerPosition = (TextView) view.findViewById(R.id.teamPosition);
			playerPosition.setText(state.myTeam.getPlayers().get(position).getPlayerPosition());
			
			view.setClickable(true);
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.d("Testing", "holy fuck");
				}
			});
			
			return view;
		}
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedI FnstanceState){
		rootView = inflater.inflate(R.layout.my_team, container, false);
		state = (GlobalState) getActivity().getApplication();
		context = getActivity().getApplicationContext();
		myPlayerList = (ListView) rootView.findViewById(R.id.myPlayerList);
		state.myPlayersAdapter = new ItemAdapter();
		
		myPlayerList.setAdapter(state.myPlayersAdapter);
		TextView teamName = new TextView(context);
		teamName.setTextSize(26);
		teamName.setTextColor(Color.BLACK);
		teamName.setPadding(6, 6, 6, 6);
		teamName.setText("Ben");
		myPlayerList.addHeaderView(teamName);
		
		return rootView;
	}

}
