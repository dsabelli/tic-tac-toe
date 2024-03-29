package com.example.tic_tac_toe.viewModels;

import android.os.Handler;
import android.widget.ImageView;

import androidx.lifecycle.ViewModel;

public class PlayGameViewModel extends ViewModel {

    private boolean playerOneTurn;
    private String playerOne;
    private String playerTwo;
    private boolean gameOver;

    private int roundCount;

    int[][] gameState;

    public String getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(String playerOne) {
        this.playerOne = playerOne;
    }

    public String getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(String playerTwo) {
        this.playerTwo = playerTwo;
    }

    public boolean isPlayerOneTurn() {
        return playerOneTurn;
    }

    public void setPlayerOneTurn(boolean playerOneTurn) {
        this.playerOneTurn = playerOneTurn;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public int getRoundCount() {
        return roundCount;
    }

    public void setRoundCount(int roundCount) {
        this.roundCount = roundCount;
    }

    public int[][] getGameState() {
        return gameState;
    }

    public void setGameState(int[][] gameState) {
        this.gameState = gameState;
    }


}