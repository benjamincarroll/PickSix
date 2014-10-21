package com.hockeypool.picksix;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.hockeypool.bc.BiddingPlayer;
import com.hockeypool.bc.Player;

public class Frag_SearchPlayers extends Fragment {
	public static class ViewHolder {
		public TextView text;
	}
	private GlobalState state;
	ListView playerList;
	View rootView;
	private Context context;
	
	class ItemAdapter extends BaseAdapter {
		
		@Override
		public int getCount(){
			return state.viewPlayer.size();
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
				view = getActivity().getLayoutInflater().inflate(R.layout.manage_view_players_fragment, parent, false);
				
			} 	
			TextView playerName = (TextView) view.findViewById(R.id.textView1);
			playerName.setText(state.viewPlayer.get(position).getPlayerName());
			
			TextView goals = (TextView) view.findViewById(R.id.goals);
			goals.setText(state.viewPlayer.get(position).getPlayerGoals() + "");
			
			TextView assists = (TextView) view.findViewById(R.id.assists);
			assists.setText(state.viewPlayer.get(position).getPlayerAssists() + "");
			
			TextView points = (TextView) view.findViewById(R.id.points);
			points.setText(state.viewPlayer.get(position).getPlayerPoints() + "");
			
			view.setClickable(true);
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
                    // create dialog box that will ask the user how much they would like to bid
					createExampleDialog(v.getContext(), state.viewPlayer.get(position));

				}
			});
			
			return view;
		}
		
	}
	
	private void createExampleDialog(Context context, final Player player) {
		 
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Would you like to bid?");
        builder.setMessage("Please enter bid amount:");
 
         // Use an EditText view to get user input.
         final EditText input = new EditText(context);
         input.setId(1);
         builder.setView(input);
 
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
 
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                BiddingPlayer newBid = new BiddingPlayer(Integer.valueOf(value), player);
                state.dWrap.addBid(newBid);
                state.bidPlayers.add(newBid);
                if (state.bidAdapter != null){
                	state.bidAdapter.notifyDataSetChanged();
                } 
                return;
            }
        });
 
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
 
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
 
        builder.show();
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		rootView = inflater.inflate(R.layout.manage_view_players, container, false);
		state = (GlobalState) getActivity().getApplication();
		
		playerList = (ListView) rootView.findViewById(R.id.listView1);
		state.viewPlayersAdapter = new ItemAdapter();
		
		playerList.setAdapter(state.viewPlayersAdapter);
		return rootView;
	}
}
