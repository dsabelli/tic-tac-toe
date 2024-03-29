package com.example.tic_tac_toe;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tic_tac_toe.other.Player;
import com.example.tic_tac_toe.other.PlayerAdapter;
import com.example.tic_tac_toe.other.SharedPrefsUtil;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ShowStandingsActivity  extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standings);

        List<Player> players = SharedPrefsUtil.getPlayerList(this);
        Collections.sort(players);
        ArrayList<Player> sortedPlayers = new ArrayList<>(players);

        RecyclerView recyclerView = findViewById(R.id.player_standings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        PlayerAdapter adapter = new PlayerAdapter(sortedPlayers);

        recyclerView.setAdapter(adapter);

    }
}
