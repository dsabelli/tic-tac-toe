package com.example.tic_tac_toe.other;

import android.os.Handler;
import android.widget.ImageView;

import java.util.Random;

public class TicTacToeAI {
    private static final int EMPTY = -1;
    private static final int AI = 0;
    private static final int HUMAN = 1;

    // Evaluates the game state
    private int evaluate(int[][] gameState) {
        for (int i = 0; i < 3; i++) {
            if (gameState[i][0] == gameState[i][1] && gameState[i][1] == gameState[i][2]) {
                if (gameState[i][0] == AI) return +10;
                else if (gameState[i][0] == HUMAN) return -10;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (gameState[0][i] == gameState[1][i] && gameState[1][i] == gameState[2][i]) {
                if (gameState[0][i] == AI) return +10;
                else if (gameState[0][i] == HUMAN) return -10;
            }
        }

        if (gameState[0][0] == gameState[1][1] && gameState[1][1] == gameState[2][2]) {
            if (gameState[0][0] == AI) return +10;
            else if (gameState[0][0] == HUMAN) return -10;
        }

        if (gameState[0][2] == gameState[1][1] && gameState[1][1] == gameState[2][0]) {
            if (gameState[0][2] == AI) return +10;
            else if (gameState[0][2] == HUMAN) return -10;
        }

        return 0;
    }

    // Checks if there are any empty cells
    private boolean isMovesLeft(int[][] gameState) {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (gameState[i][j] == EMPTY)
                    return true;
        return false;
    }

    // Minimax algorithm
    private int minimax(int[][] gameState, int depth, boolean isMax) {
        int score = evaluate(gameState);

        // If Maximizer has won the game return his/her evaluated score
        if (score == 10)
            return score;

        // If Minimizer has won the game return his/her evaluated score
        if (score == -10)
            return score;

        // If there are no more moves and no winner then it is a tie
        if (isMovesLeft(gameState) == false)
            return 0;

        if (isMax) {
            int best = -1000;

            // Traverse all cells
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    // Check if cell is empty
                    if (gameState[i][j] == EMPTY) {
                        // Make the move
                        gameState[i][j] = AI;

                        // Call minimax recursively and choose the maximum value
                        best = Math.max(best, minimax(gameState, depth + 1, !isMax));

                        // Undo the move
                        gameState[i][j] = EMPTY;
                    }
                }
            }
            return best;
        } else {
            int best = 1000;

            // Traverse all cells
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    // Check if cell is empty
                    if (gameState[i][j] == EMPTY) {
                        // Make the move
                        gameState[i][j] = HUMAN;

                        // Call minimax recursively and choose the minimum value
                        best = Math.min(best, minimax(gameState, depth + 1, !isMax));

                        // Undo the move
                        gameState[i][j] = EMPTY;
                    }
                }
            }
            return best;
        }
    }

    // Finds the best move for the AI
    public int[] findBestMove(int[][] gameState) {
        int bestVal = -1000;
        int bestRow = -1;
        int bestCol = -1;

        // Traverse all cells, evaluate minimax function for all empty cells.
        // And return the cell with optimal value.
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // Check if cell is empty
                if (gameState[i][j] == EMPTY) {
                    // Make the move
                    gameState[i][j] = AI;

                    // Compute evaluation function for this move.
                    int moveVal = minimax(gameState, 0, false);

                    // Undo the move
                    gameState[i][j] = EMPTY;

                    // If the value of the current move is more than the best value, then update best/
                    if (moveVal > bestVal) {
                        bestRow = i;
                        bestCol = j;
                        bestVal = moveVal;
                    }
                }
            }
        }
        return new int[]{bestRow, bestCol};
    }


    public void easyAITurn(int[][] gameState, ImageView[][] imageViews) {

        Random rand = new Random();
        int delay = rand.nextInt(800) + 100; // Generate random delay between 0.1s and 0.8s

        // Use Handler to post a delayed Runnable
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                performRandomClick(gameState,imageViews);
            }
        }, delay);
    }

    public void hardAITurn(int[][] gameState, ImageView[][] imageViews) {
        
        Random rand = new Random();
        int delay = rand.nextInt(800) + 100; // Generate random delay between 0.1s and 0.8s

        // Use Handler to post a delayed Runnable
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int[] bestMove = findBestMove(gameState);
                gameState[bestMove[0]][bestMove[1]] = 0;
                imageViews[bestMove[0]][bestMove[1]].performClick();
            }
        }, delay);
    }

    public void performRandomClick(int[][] gameState, ImageView[][] imageViews) {
        Random rand = new Random();
        boolean turnMade = false;
        while (!turnMade) {
            int i = rand.nextInt(3);
            int j = rand.nextInt(3);
            if (gameState[i][j] == -1) {
                turnMade = true;
                imageViews[i][j].performClick();
            }
        }
    }
    
}