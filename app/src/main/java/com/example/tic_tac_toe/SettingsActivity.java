package com.example.tic_tac_toe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tic_tac_toe.other.Player;
import com.example.tic_tac_toe.other.SharedPrefsUtil;

public class SettingsActivity  extends AppCompatActivity {
    private final String TIME_LIMIT = "TimeLimit";
    private final String TIME_LIMIT_POSITION = "TimeLimitPosition";
    private final String AI_MODE = "AIMode";
    Button resetBtn;
    Button deleteBtn;
    Spinner spinnerTimeLimit;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch switchAIMode;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPrefsUtil.savePlayer(this,"AI");

        resetBtn = (Button) findViewById(R.id.btn_reset_scores);
        deleteBtn = (Button) findViewById(R.id.btn_delete_users);
        spinnerTimeLimit = (Spinner) findViewById(R.id.time_limit_spinner);
        switchAIMode = (Switch)findViewById(R.id.ai_mode_switch);

        switchAIMode.setChecked(!SharedPrefsUtil.getAIMode(this, AI_MODE).equals("EASY"));

        spinnerTimeLimit.setSelection(Integer.parseInt((SharedPrefsUtil.getTimeLimitPosition(this,TIME_LIMIT))));

        switchAIMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefsUtil.setAIMode(SettingsActivity.this,AI_MODE,switchAIMode.isChecked());
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefsUtil.resetScores(SettingsActivity.this);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefsUtil.deletePlayers(SettingsActivity.this);
                SharedPrefsUtil.savePlayer(SettingsActivity.this,"AI");
            }
        });

        spinnerTimeLimit.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ((TextView) spinnerTimeLimit.getSelectedView()).setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            }
        });

        spinnerTimeLimit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               String timeLimit = parent.getItemAtPosition(position).toString();
                String timeLimitPosition = String.valueOf(position);
                SharedPrefsUtil.setTimeLimit(SettingsActivity.this,TIME_LIMIT, timeLimit);
                SharedPrefsUtil.setTimeLimitPosition(SettingsActivity.this,TIME_LIMIT_POSITION, timeLimitPosition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }


}



