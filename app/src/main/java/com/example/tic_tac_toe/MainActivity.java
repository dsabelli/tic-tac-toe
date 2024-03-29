package com.example.tic_tac_toe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tic_tac_toe.other.Player;
import com.example.tic_tac_toe.other.SharedPrefsUtil;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
Button names;
Button game;
Button standings;
Button settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        names = (Button) findViewById(R.id.btn_names);
        game = (Button) findViewById(R.id.btn_game);
        standings = (Button) findViewById(R.id.btn_standings);
        settings = (Button) findViewById(R.id.btn_settings);

    SharedPrefsUtil.savePlayer(this,"AI");

        names.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EnterNamesActivity.class);
                startActivity(intent);
            }
        });

        game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlayGameActivity.class);
                startActivity(intent);
            }
        });

        standings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowStandingsActivity.class);
                startActivity(intent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

    }
}