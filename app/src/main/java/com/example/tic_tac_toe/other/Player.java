package com.example.tic_tac_toe.other;

import java.util.Date;

public class Player implements Comparable<Player> {
    private int wins;
    private String name;
    private String lastPlayed;

    public String getLastPlayed() {
        return lastPlayed;
    }

    public void setLastPlayed(String lastPlayed) {
        this.lastPlayed = lastPlayed;
    }


    // Constructor, getters, and setters
    public Player(String name, int wins) {
        this.name = name;
        this.wins = wins;
    }

    public String getName() {
        return name;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }


    @Override
    public int compareTo(Player o) {
        if (getWins() < o.getWins()) return 1;
        else if (getWins() > o.getWins()) return -1;
        return 0;
    }
}
