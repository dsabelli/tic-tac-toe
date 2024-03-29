package com.example.tic_tac_toe.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.tic_tac_toe.EnterNamesActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

public class SharedPrefsUtil {
    private static final String PREFS_NAME = "PlayerNames";
    private static final String PREFS_TIME_LIMIT = "TimeLimit";
    private static final String PREFS_TIME_LIMIT_POSITION = "TimeLimitPosition";
    private static final String PREFS_AI_MODE = "AIMode";

    public static void setTimeLimit(Context context, String key, String seconds) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_TIME_LIMIT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, seconds);
        editor.apply();
    }

    public static String getTimeLimit(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_TIME_LIMIT, Context.MODE_PRIVATE);
        return prefs.getString(key, "0");
    }

    public static void setTimeLimitPosition(Context context, String key, String position) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_TIME_LIMIT_POSITION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, position);
        editor.apply();
    }

    public static String getTimeLimitPosition(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_TIME_LIMIT_POSITION, Context.MODE_PRIVATE);
        return prefs.getString(key, "0");
    }

    public static void setAIMode(Context context, String key, boolean checked) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_AI_MODE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String mode;
        if (checked) {
            mode = "HARD";
        } else {
            mode = "EASY";
        }
        editor.putString(key, mode);
        editor.apply();
    }

    public static String getAIMode(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_AI_MODE, Context.MODE_PRIVATE);
        return prefs.getString(key, "EASY");
    }

    public static boolean savePlayer(Context context, String playerName) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        if (!containsName(context, playerName)) {
            Player player = new Player(playerName, 0);
            String playerJson = gson.toJson(player);
            editor.putString(playerName, playerJson);
            editor.apply();
            return true;
        }
        return false;
    }

    public static int updatePlayerWins(Context context, String playerName) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        Player player = getPlayer(context, playerName);
        player.setWins(player.getWins() + 1);
        String playerJson = gson.toJson(player);
        editor.putString(playerName, playerJson);
        editor.apply();
        return player.getWins();
    }

    public static void updatePlayerTimestamp(Context context, String playerName) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM/dd/yyyy - HH:mm:ss a", Locale.CANADA);
        String date = dateFormat.format(new Date());
        Gson gson = new Gson();
        Player player = getPlayer(context, playerName);
        player.setLastPlayed(date);
        String playerJson = gson.toJson(player);
        editor.putString(playerName, playerJson);
        editor.apply();
    }

    private static boolean containsName(Context context, String playerName) {
        ArrayList<Player> players = getPlayerList(context);
        ArrayList<String> names = new ArrayList<>();
        for (Player player : players) {
            names.add(player.getName());
        }
        return names.contains(playerName);
    }

    public static ArrayList<Player> getPlayerList(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Collection<?> jsonCollection = prefs.getAll().values();

        // Convert the collection to a JSON string
        StringBuilder jsonStringBuilder = new StringBuilder();
        jsonStringBuilder.append("[");
        for (Object jsonObject : jsonCollection) {
            jsonStringBuilder.append(jsonObject.toString()).append(",");
        }
        // Remove the last comma and close the JSON array
        if (jsonStringBuilder.length() > 1) {
            jsonStringBuilder.setLength(jsonStringBuilder.length() - 1);
        }
        jsonStringBuilder.append("]");

        String jsonString = jsonStringBuilder.toString();

        // Deserialize the JSON string into an ArrayList of Player objects
        Type playerListType = new TypeToken<ArrayList<Player>>() {
        }.getType();
        return gson.fromJson(jsonString, playerListType);
    }

    public static Player getPlayer(Context context, String playerName) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(playerName, "");
        return gson.fromJson(json, Player.class);
    }

    public static void resetScores(Context context) {
        ArrayList<Player> players = getPlayerList(context);
        for (Player player : players) {
            resetPlayerWins(context, player.getName());
        }

    }

    private static void resetPlayerWins(Context context, String playerName) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        Player player = getPlayer(context, playerName);
        player.setWins(0);
        String playerJson = gson.toJson(player);
        editor.putString(playerName, playerJson);
        editor.apply();
    }

    public static void deletePlayers(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }
}
