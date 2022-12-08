package com.example.events_live.presentation.sharedPreferences;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class TeamsOrPlayers {

    private static final String FOOTBALL_BASKETBALL = "TEAMS_PLAYERS";
    static SharedPreferences SharedPreferences;
    static SharedPreferences.Editor Editor;



    public static void saveTeamsOrPlayersInSP(Context context, String text) {
        SharedPreferences = context.getSharedPreferences(FOOTBALL_BASKETBALL, MODE_PRIVATE);
        Editor = SharedPreferences.edit();

        Editor.putString("teams_players",text);

        Editor.commit();
    }



    public static String getTeamsOrPlayersInSP(Context context) {
        String text;
        SharedPreferences shared = context.getSharedPreferences(FOOTBALL_BASKETBALL, MODE_PRIVATE);

        text = (shared.getString("teams_players", ""));
        if (text == null || text.isEmpty())
        {
            text = "empty";
        }
        return text;
    }




    public static void cleanTeamsOrPlayers(Context context) {
        SharedPreferences = context.getSharedPreferences(FOOTBALL_BASKETBALL, MODE_PRIVATE);
        Editor = SharedPreferences.edit();
        Editor.putString("teams_players","");

        Editor.commit();
    }

}
