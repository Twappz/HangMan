package twinapps.com.hangman;



import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class AppMemorys {
    private static final String USER_PREFS = "USER_PREFS";
    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;
    private String game_won = "user_games_won";
    private String game_lost = "user_games_lost";

    public AppMemorys(Context context) {
        // TODO Auto-generated constructor stub
        this.appSharedPrefs = context.getSharedPreferences(USER_PREFS, Activity.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }
    public int getGamesWon(){
        return appSharedPrefs.getInt(game_won, 0);
    }
    public void setGamesWon(int gamesWon){
        prefsEditor.putInt(game_won,gamesWon).commit();
    }

    public int getGamesLost(){
        return appSharedPrefs.getInt(game_lost, 0);
    }
    public void setGamesLost(int gamesLost){
        prefsEditor.putInt(game_lost, gamesLost).commit();
    }
}
