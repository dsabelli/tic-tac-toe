package com.example.tic_tac_toe.other;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tic_tac_toe.R;

import java.util.ArrayList;

// Adapter for displaying timestamps in a RecyclerView.
public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {
    private final ArrayList<Player> players; // List of timestamps to display.

    // Constructor to initialize the adapter with a list of timestamps.
    public PlayerAdapter(ArrayList<Player> players) {
        this.players = players;
    }

    // Creates a new ViewHolder and inflates the view for each item.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player, parent, false);
        return new ViewHolder(view);
    }

    // Binds the data to the ViewHolder.
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name = players.get(position).getName() + ": ";
        String wins = players.get(position).getWins() == 1 ? players.get(position).getWins() + " win" : players.get(position).getWins() + " wins";
        String date = "Last played: " + players.get(position).getLastPlayed();
        holder.playerNameTextView.setText(name);
        holder.playerWinsTextView.setText(wins);
        if (players.get(position).getLastPlayed() != null) {
            holder.playerDateTextView.setText(date);
        }
    }

    // Returns the total number of items in the data set held by the adapter.
    @Override
    public int getItemCount() {
        return players.size();
    }

    // ViewHolder class for the RecyclerView.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView playerNameTextView;
        TextView playerWinsTextView;
        TextView playerDateTextView;

        // Constructor to initialize the ViewHolder.
        public ViewHolder(View itemView) {
            super(itemView);
            playerNameTextView = (TextView) itemView.findViewById(R.id.player_name_item);
            playerWinsTextView = (TextView) itemView.findViewById(R.id.player_wins_item);
            playerDateTextView = (TextView) itemView.findViewById(R.id.player_date_item);
        }
    }
}
