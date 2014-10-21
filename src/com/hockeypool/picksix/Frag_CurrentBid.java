package com.hockeypool.picksix;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class Frag_CurrentBid extends Fragment {
	public static class ViewHolder {
		public TextView text;
	}
	private GlobalState state;
	
	class ItemAdapter extends BaseAdapter {
		
		@Override
		public int getCount(){
			return state.bidPlayers.size();
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
				view = getActivity().getLayoutInflater().inflate(R.layout.manage_bids_fragment_item, parent, false);
			} else {
				// TODO: What is going on here?
			}
			TextView playerName = (TextView) view.findViewById(R.id.bidPlayer);
			playerName.setText(state.bidPlayers.get(position).getPlayer().getPlayerName());
			
			TextView bidAmount = (TextView) view.findViewById(R.id.bidAmount);
			bidAmount.setText(state.bidPlayers.get(position).getAmount() + "");
			
			// Put a click listener on the button
			Button bidButton = (Button) view.findViewById(R.id.bidButton);
			bidButton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO: Create action here that will place a bid on a player
					//		 Then make it so every other player has to pass on bid
					//       before current player can purchase
					
					
				}
				
			});
			
			return view;
		}
		
	}
	
	ListView bidList;
	View rootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		rootView = inflater.inflate(R.layout.manage_bids_fragment, container, false);
		state = (GlobalState) getActivity().getApplication();
		
		bidList = (ListView) rootView.findViewById(R.id.bidList);
		state.bidAdapter = new ItemAdapter();
		
		bidList.setAdapter(state.bidAdapter);
		return rootView;
	}
}
