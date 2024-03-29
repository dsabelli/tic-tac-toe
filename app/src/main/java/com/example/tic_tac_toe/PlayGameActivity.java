package com.example.tic_tac_toe;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

import com.example.tic_tac_toe.other.Player;
import com.example.tic_tac_toe.other.SharedPrefsUtil;
import com.example.tic_tac_toe.other.TicTacToeAI;
import com.example.tic_tac_toe.viewModels.PlayGameViewModel;

import java.util.ArrayList;
import java.util.Random;

public class PlayGameActivity extends AppCompatActivity {
    private final String TIME_LIMIT = "TimeLimit";
    private final String AI_MODE = "AIMode";
    private final String PLAYER_ONE_SYMBOL = "X";
    private final String PLAYER_TWO_SYMBOL = "O";
    private static final int DRAWABLE_X_WIN = R.drawable.x_win;
    private static final int DRAWABLE_O_WIN = R.drawable.o_win;
    private static final int DRAWABLE_X = R.drawable.x;
    private static final int DRAWABLE_O = R.drawable.o;
    String playerOne;
    String playerTwo;
    Spinner spinnerPlayerOne;
    Spinner spinnerPlayerTwo;
    TextView playerOneSymbol;
    TextView playerTwoSymbol;
    TextView playerOneWins;
    TextView playerTwoWins;
    TextView winMessage;
    Button playGameBtn;
    private ImageView[][] imageViews = new ImageView[3][3];
    private boolean playerOneTurn = true;
    private boolean gameOver = true;
    private String aiMode;
    private int roundCount = 0;
    private int timeLimit;
    private Handler timeLimitHandler = new Handler();
    private Runnable timeLimitRunnable;
    private PlayGameViewModel viewModel;
    private boolean gameStateRetrieved = false;
    int[][] gameState = new int[3][3];
    Vibrator vibrator;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        SavedStateViewModelFactory factory = new SavedStateViewModelFactory(getApplication(), this);
        initializeUI();
        viewModel = new ViewModelProvider(this).get(PlayGameViewModel.class);

        if (viewModel.getRoundCount() > 0) {
            getGameState();
        }


    }

    protected void onPause() {
        super.onPause();
        cancelTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private boolean checkForWin() {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (gameState[i][0] == gameState[i][1] && gameState[i][1] == gameState[i][2] && gameState[i][0] != -1) {
                showWin();
                return true;
            }
        }

        // Check columns
        for (int j = 0; j < 3; j++) {
            if (gameState[0][j] == gameState[1][j] && gameState[1][j] == gameState[2][j] && gameState[0][j] != -1) {
                showWin();
                return true;
            }
        }

        // Check diagonals
        if ((gameState[0][0] == gameState[1][1] && gameState[1][1] == gameState[2][2] && gameState[0][0] != -1) ||
                (gameState[0][2] == gameState[1][1] && gameState[1][1] == gameState[2][0] && gameState[0][2] != -1)) {
            showWin();
            return true;
        }

        return false;
    }

    private boolean showWin() {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (checkRow(i)) return true;
        }

        // Check columns
        for (int j = 0; j < 3; j++) {
            if (checkColumn(j)) return true;
        }

        // Check diagonals
        return checkDiagonal(true) || checkDiagonal(false);
    }

    private boolean checkRow(int row) {
        if (gameState[row][0] == gameState[row][1] && gameState[row][1] == gameState[row][2] && gameState[row][0] != -1) {
            setWinImageResources(row, 0, row, 1, row, 2);
            return true;
        }
        return false;
    }

    private boolean checkColumn(int col) {
        if (gameState[0][col] == gameState[1][col] && gameState[1][col] == gameState[2][col] && gameState[0][col] != -1) {
            setWinImageResources(0, col, 1, col, 2, col);
            return true;
        }
        return false;
    }

    private boolean checkDiagonal(boolean isMainDiagonal) {
        int[][] diagonals = isMainDiagonal ? new int[][]{{0, 0}, {1, 1}, {2, 2}} : new int[][]{{0, 2}, {1, 1}, {2, 0}};
        if (gameState[diagonals[0][0]][diagonals[0][1]] == gameState[diagonals[1][0]][diagonals[1][1]] &&
                gameState[diagonals[1][0]][diagonals[1][1]] == gameState[diagonals[2][0]][diagonals[2][1]] &&
                gameState[diagonals[0][0]][diagonals[0][1]] != -1) {
            setWinImageResources(diagonals[0][0], diagonals[0][1], diagonals[1][0], diagonals[1][1], diagonals[2][0], diagonals[2][1]);
            return true;
        }
        return false;
    }

    private void setWinImageResources(int row1, int col1, int row2, int col2, int row3, int col3) {
        int drawable = playerOneTurn ? DRAWABLE_X_WIN : DRAWABLE_O_WIN;
        imageViews[row1][col1].setImageResource(drawable);
        imageViews[row2][col2].setImageResource(drawable);
        imageViews[row3][col3].setImageResource(drawable);
    }

    private boolean checkPlayersSelected() {
        return !playerOne.equals(playerTwo);
    }

    private void restartGame() {
        gameOver = false;
        playerOneTurn = true;
        disableSpinners();
        winMessage.setText("");
        playGameBtn.setText(R.string.btn_game);
        roundCount = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gameState[i][j] = -1;
                String imageViewID = "imageView_" + i + j;
                int resID = getResources().getIdentifier(imageViewID, "id", getPackageName());
                imageViews[i][j] = findViewById(resID);
                imageViews[i][j].setImageResource(0);
            }
        }
    }

    private void disableSpinners() {
        spinnerPlayerOne.setEnabled(false);
        spinnerPlayerTwo.setEnabled(false);
    }

    private void easyAITurn() {

        Random rand = new Random();
        int delay = rand.nextInt(800) + 100; // Generate random delay between 0.1s and 0.8s

        // Use Handler to post a delayed Runnable
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                performRandomClick();
            }
        }, delay);
    }

    private void hardAITurn() {
        TicTacToeAI ai = new TicTacToeAI();
        Random rand = new Random();
        int delay = rand.nextInt(800) + 100; // Generate random delay between 0.1s and 0.8s

        // Use Handler to post a delayed Runnable
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int[] bestMove = ai.findBestMove(gameState);
                gameState[bestMove[0]][bestMove[1]] = 0;
                imageViews[bestMove[0]][bestMove[1]].performClick();
            }
        }, delay);

    }

    private void performRandomClick() {
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


    private void drawBoard(Vibrator vibrator) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String imageViewID = "imageView_" + i + j;
                int resID = getResources().getIdentifier(imageViewID, "id", getPackageName());
                int row = i;
                int col = j;
                imageViews[i][j] = findViewById(resID);
                if (gameStateRetrieved) {
                    if (gameState[i][j] == 0) {
                        imageViews[i][j].setImageResource(DRAWABLE_O);
                    } else if (gameState[i][j] == 1) {
                        imageViews[i][j].setImageResource(DRAWABLE_X);
                    }
                } else {
                    gameState[i][j] = -1;
                }
                imageViews[i][j].setOnClickListener(v -> handleImageClick(v, vibrator, row, col));
            }
        }
        if (gameStateRetrieved) {
            if (roundCount == 9) {
                handleDraw();
            }
            if (
                    checkForWin()) {
                handleWin();
            }
        }
    }

    private void handleImageClick(View v, Vibrator vibrator, int row, int col) {
        ImageView imageView = (ImageView) v;
        if (imageView.getDrawable() == null && !gameOver) {
            vibrator.vibrate(100);
            int drawable = playerOneTurn ? DRAWABLE_X : DRAWABLE_O;
            imageView.setImageResource(drawable);
            gameState[row][col] = playerOneTurn ? 1 : 0;
            roundCount++;
            cancelTimer();
            if (timeLimit != 0) handleTimer();
            checkGameStatus();
            setGameState();
        }
    }

    private void checkGameStatus() {
        if (checkForWin()) {
            handleWin();
            cancelTimer();
        } else if (roundCount == 3 * 3) {
            handleDraw();
            cancelTimer();
        } else {
            playerOneTurn = !playerOneTurn;
            if (playerOne.equals("AI") && playerOneTurn || playerTwo.equals("AI") && !playerOneTurn) {
                if (aiMode.equals("HARD")) {
                    hardAITurn();
                } else {
                    easyAITurn();
                }
            }
        }
    }

    private void setTimeStamps() {
        SharedPrefsUtil.updatePlayerTimestamp(PlayGameActivity.this, playerOne);
        SharedPrefsUtil.updatePlayerTimestamp(PlayGameActivity.this, playerTwo);
    }

    private void handleTimer() {
        timeLimitRunnable = new Runnable() {
            @Override
            public void run() {
                performRandomClick();
            }
        };
        timeLimitHandler.postDelayed(timeLimitRunnable, timeLimit * 1000);
    }

    private void cancelTimer() {
        if (timeLimitHandler != null && timeLimitRunnable != null) {
            timeLimitHandler.removeCallbacks(timeLimitRunnable);
        }
    }

    private void handleGameOver() {
        setTimeStamps();
        if (timeLimit != 0) handleTimer();
        if (roundCount > 0) {
            restartGame();
            if (playerOne.equals("AI") && playerOneTurn) {
                if (aiMode.equals("HARD")) {
                    hardAITurn();
                } else {
                    easyAITurn();
                }
            }
        } else {
            disableSpinners();
            gameOver = false;
            boolean aiSelected = playerOne.equals("AI") || playerTwo.equals("AI");
            if (playerOne.equals("AI") && playerOneTurn) {
                if (aiMode.equals("HARD")) {
                    hardAITurn();
                } else {
                    easyAITurn();
                }
            }
        }
    }

    private void handleWin() {
        gameOver = true;
        spinnerPlayerOne.setEnabled(true);
        spinnerPlayerTwo.setEnabled(true);
        String winner;
        if (playerOneTurn) {
            winner = playerOne;
            playerOneWins.setText(String.valueOf(SharedPrefsUtil.updatePlayerWins(PlayGameActivity.this, winner)));
        } else {
            winner = playerTwo;
            playerTwoWins.setText(String.valueOf(SharedPrefsUtil.updatePlayerWins(PlayGameActivity.this, winner)));
        }
        String displayWinner = getString(R.string.winner) + winner;
        winMessage.setText(displayWinner);
        playGameBtn.setText(R.string.reset);
    }

    private void handleDraw() {
        gameOver = true;
        spinnerPlayerOne.setEnabled(true);
        spinnerPlayerTwo.setEnabled(true);
        winMessage.setText(R.string.draw);
        playGameBtn.setText(R.string.reset);
    }


    private void getGameState() {
        playerOneTurn = viewModel.isPlayerOneTurn();
        playerOne = viewModel.getPlayerOne();
        playerTwo = viewModel.getPlayerTwo();
        gameOver = viewModel.isGameOver();
        roundCount = viewModel.getRoundCount();
        gameState = viewModel.getGameState();
        gameStateRetrieved = true;
    }

    private void setGameState() {
        viewModel.setPlayerOneTurn(playerOneTurn);
        viewModel.setPlayerOne(playerOne);
        viewModel.setPlayerTwo(playerTwo);
        viewModel.setGameOver(gameOver);
        viewModel.setRoundCount(roundCount);
        viewModel.setGameState(gameState);
    }

    private void setMessageAndFadeOut(int stringID) {
        winMessage.setText(stringID);
        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f); // Start from fully opaque, end at fully transparent
        fadeOut.setDuration(2000); // Set the duration of the animation to 2000 milliseconds (2 seconds)
        winMessage.startAnimation(fadeOut);

        // Use a Handler to delay the execution of the code
        long delayDuration = 1900; // 1.9 seconds
        new Handler().postDelayed(() -> winMessage.setText(""), delayDuration);
    }

    private void initializeUI() {
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        aiMode = SharedPrefsUtil.getAIMode(this, AI_MODE);

        timeLimit = SharedPrefsUtil.getTimeLimit(this, TIME_LIMIT).equals("None") ? 0 : Integer.parseInt(SharedPrefsUtil.getTimeLimit(this, TIME_LIMIT));

        spinnerPlayerOne = (Spinner) findViewById(R.id.select_player_one);
        spinnerPlayerTwo = (Spinner) findViewById(R.id.select_player_two);

        playerOneSymbol = (TextView) findViewById(R.id.player_one_symbol);
        playerTwoSymbol = (TextView) findViewById(R.id.player_two_symbol);
        playerOneWins = (TextView) findViewById(R.id.player_one_wins);
        playerTwoWins = (TextView) findViewById(R.id.player_two_wins);
        winMessage = (TextView) findViewById(R.id.win_message);
        playGameBtn = (Button) findViewById(R.id.btn_play_game);


        ArrayList<Player> players = SharedPrefsUtil.getPlayerList(this);
        ArrayList<String> playerNames = new ArrayList<>();
        for (Player player : players) {
            playerNames.add(player.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, playerNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlayerOne.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ((TextView) spinnerPlayerOne.getSelectedView()).setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                ((TextView) spinnerPlayerTwo.getSelectedView()).setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            }
        });
        spinnerPlayerOne.setAdapter(adapter);
        spinnerPlayerTwo.setAdapter(adapter);

        spinnerPlayerOne.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                playerOne = parent.getItemAtPosition(position).toString();
                Player player = SharedPrefsUtil.getPlayer(PlayGameActivity.this, playerOne);
                if (player != null) {
                    playerOneSymbol.setText(PLAYER_ONE_SYMBOL);
                    playerOneWins.setText(String.valueOf(player.getWins()));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
        spinnerPlayerTwo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                playerTwo = parent.getItemAtPosition(position).toString();
                Player player = SharedPrefsUtil.getPlayer(PlayGameActivity.this, playerTwo);
                if (player != null) {
                    playerTwoSymbol.setText(PLAYER_TWO_SYMBOL);
                    playerTwoWins.setText(String.valueOf(player.getWins()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        playGameBtn.setOnClickListener(v -> {
            if (!checkPlayersSelected()) {
                setMessageAndFadeOut(R.string.select_players);
                return;
            }
            if (gameOver) {
                handleGameOver();
                setMessageAndFadeOut(R.string.begin);

            }
        });

        drawBoard(vibrator);
    }
}

