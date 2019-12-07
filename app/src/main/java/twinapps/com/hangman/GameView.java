package twinapps.com.hangman;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends Activity {
    private String[] words;
    private ArrayList<String> wordsGuessed;
    private String currentWord;
    private String curretnGuess;
    private String currentLetterGuessed;
    private int triesLeft;
    private ImageView image;
    private int gamesWon;
    private int gamesLost;
    private AppMemorys appPrefs;
    private InterstitialAd interstitial;
    private AdRequest adRequestFull ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game);

        words = getResources().getStringArray(R.array.words);
        wordsGuessed = new ArrayList<String>();
        image = (ImageView) findViewById(R.id.hangmanImage);
        Context context = getApplicationContext();
        appPrefs = new AppMemorys(context);
        loadWinsAndLosses();
        init();

        // ads
        AdView adView = (AdView) this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        /*AdRequest request = new AdRequest.Builder()
                .addTestDevice("E80B0238B23C463C595999207F2288DA")  // device ID
                .build();*/

        // full screen ads
        // Create the interstitial.
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId("ca-app-pub-6711318348099993/5984622252");
        // interstitial.setAdUnitId("ca-app-pub-3940256099942544/1033173712"); //Test ADS
        // Create ad request.
        adRequestFull = new AdRequest.Builder().build();

        // Begin loading your interstitial.
        interstitial.loadAd(adRequestFull);
        interstitial.setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {
                //load next ads
                interstitial.loadAd(adRequestFull);
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                //load next ads
                interstitial.loadAd(adRequestFull);
                super.onAdFailedToLoad(errorCode);
            }

            @Override
            public void onAdLeftApplication() {
                // TODO Auto-generated method stub
                super.onAdLeftApplication();
            }

            @Override
            public void onAdLoaded() {
                // TODO Auto-generated method stub
                super.onAdLoaded();
            }

            @Override
            public void onAdOpened() {
                // TODO Auto-generated method stub
                super.onAdOpened();
            }

        });
    }

    // Invoke displayInterstitial() when you are ready to display an
    // interstitial.
    public void displayInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();

        }
    }

    private void init() {
        currentWord = getRandomWord();
        wordsGuessed.add(currentWord);

        triesLeft = 6;
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < currentWord.length(); i++) {
            builder.append("-");
        }

        curretnGuess = builder.toString();
        updateGuess(curretnGuess);

        image.setImageResource(R.drawable.p1);
        updateCounter(triesLeft);

    }

    private void loadWinsAndLosses() {
        gamesWon = appPrefs.getGamesWon();
        gamesLost = appPrefs.getGamesLost();
    }

    private void saveWinsAndLosses() {
        appPrefs.setGamesWon(gamesWon);
        appPrefs.setGamesLost(gamesLost);
    }

    public void onLetterClick(View view) {
        Button button = (Button) view;
        currentLetterGuessed = (String) button.getText();

        if (triesLeft > 0) {
            if (isLegalLetter(currentLetterGuessed)) {
                onLetterSuccess(button);
            } else {
                onLetterFailure(button);
            }
        } else {
            if (curretnGuess.equals(currentWord)) {
                onWordSuccess();
            } else {
                onWordFailure();
            }
        }
    }

    public void onWordSuccess() {
        gamesWon++;
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

        dlgAlert.setTitle("WIN!");
        dlgAlert.setIcon(R.drawable.big_smile);
        dlgAlert.setCancelable(true);
        dlgAlert.setPositiveButton(getResources().getString(R.string.success),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        reset();
                    }
                });

        dlgAlert.create().show();
        //displayInterstitial();
    }

    public void onWordFailure() {
        gamesLost++;
        image.setImageResource(R.drawable.p7);

        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage(currentWord);
        dlgAlert.setTitle("FAIL!");
        dlgAlert.setIcon(R.drawable.pudency);
        dlgAlert.setMessage(getResources().getString(R.string.wordWas) + " "
                + currentWord);
        dlgAlert.setCancelable(true);
        dlgAlert.setPositiveButton(getResources().getString(R.string.failure),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        reset();
                    }
                });
        dlgAlert.show();
        // displayInterstitial();
    }

    public void onLetterSuccess(Button button) {
        StringBuilder builder = new StringBuilder();
        builder.append(curretnGuess);

        for (int i = 0; i < currentWord.length(); i++) {
            if (currentWord.toLowerCase().charAt(i) == currentLetterGuessed
                    .toLowerCase().charAt(0)) {
                builder.setCharAt(i, currentLetterGuessed.charAt(0));
            }
        }

        button.setVisibility(View.INVISIBLE);
        button.setClickable(false);

        curretnGuess = builder.toString();
        updateGuess(curretnGuess);
        if (curretnGuess.toLowerCase().equals(currentWord.toLowerCase())) {
            onWordSuccess();
        }
    }

    public void onLetterFailure(Button button) {
        triesLeft--;
        updateCounter(triesLeft);

        button.setVisibility(View.INVISIBLE);
        button.setClickable(false);

        int tries = 6 - triesLeft;
        switch (tries) {
            case 1:
                image.setImageResource(R.drawable.p2);
                break;
            case 2:
                image.setImageResource(R.drawable.p3);
                break;
            case 3:
                image.setImageResource(R.drawable.p4);
                break;
            case 4:
                image.setImageResource(R.drawable.p5);
                break;
            case 5:
                image.setImageResource(R.drawable.p6);
                break;
            case 6:
                onWordFailure();
                break;

            default:
                image.setImageResource(R.drawable.p1);
                break;
        }
    }

    private boolean isLegalLetter(String c) {
        return currentWord.toLowerCase().contains(c.toLowerCase());
    }

    private void reset() {
        init();

        Resources res = getResources();
        int id = res.getIdentifier("titleText", "id", getBaseContext()
                .getPackageName());

        TableLayout tl = (TableLayout) findViewById(R.id.tableLayout1);
        for (int i = 0; i < tl.getChildCount(); i++) {
            TableRow row = (TableRow) tl.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                Button btn = (Button) row.getChildAt(j);
                btn.setVisibility(View.VISIBLE);
                btn.setClickable(true);
            }
        }
    }

    private String getRandomWord() {
        Random rand = new Random();

        String word;

        do {
            word = words[rand.nextInt(words.length)];
        } while (wordsGuessed.contains(word));
        return word;
    }

    private void updateGuess(String newWord) {
        TextView view = (TextView) findViewById(R.id.word);
        view.setText(newWord);
    }

    private void updateCounter(int newCounter) {
        TextView counter = (TextView) findViewById(R.id.counter);
        counter.setText(newCounter + "");
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        saveWinsAndLosses();
        displayInterstitial();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        saveWinsAndLosses();
    }

}

