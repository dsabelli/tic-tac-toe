package com.example.tic_tac_toe.viewModels;

import androidx.lifecycle.ViewModel;

public class EnterNamesViewModel extends ViewModel {

    private String name;

    public String getName() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }


}
