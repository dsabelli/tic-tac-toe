package com.example.tic_tac_toe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.tic_tac_toe.other.Player;
import com.example.tic_tac_toe.other.SharedPrefsUtil;
import com.example.tic_tac_toe.viewModels.EnterNamesViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;

public class EnterNamesActivity extends AppCompatActivity {
    EditText nameInput;
    String playerName;


    Button save_name;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_names);
        save_name = (Button) findViewById(R.id.btn_save_names);
        nameInput = (EditText) findViewById(R.id.player_name);

//         Retrieve names from ViewModel
        EnterNamesViewModel viewModel = new ViewModelProvider(this).get(EnterNamesViewModel.class);
        if (viewModel.getName() != null) {
            nameInput.setText(viewModel.getName());
        }

        save_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerName = nameInput.getText().toString();
                if (!playerName.isEmpty()&&playerName.length()<13) {
                    if (SharedPrefsUtil.savePlayer(EnterNamesActivity.this, playerName)) {

                        Toast.makeText(EnterNamesActivity.this,
                                "Player " + playerName + " saved.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EnterNamesActivity.this,
                                "Player " + playerName + " already exists.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(EnterNamesActivity.this,
                            "Enter a name between 1-12 characters.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

