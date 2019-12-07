package twinapps.com.hangman;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity extends Activity {

    private AppMemorys appMemorys;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setWinsAndLossesText();
        //AppRater.app_launched(this);

		/*AdRequest request = new AdRequest.Builder()
				.addTestDevice("E80B0238B23C463C595999207F2288DA")  // device ID
				.build();*/

        // Look up the AdView as a resource and load a request.
        AdView adView = (AdView)this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();

        // Start loading the ad in the background.
        adView.loadAd(adRequest);

        //Interstitial Ads
        InterstitialAd mInterstitialAd;
        // MobileAds.initialize(this, "ca-app-pub-6711318348099993~4259449371");

        //Interstitial Ads//
        mInterstitialAd = new InterstitialAd(this);
        // mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712"); //Test ADS
        mInterstitialAd.setAdUnitId("ca-app-pub-6711318348099993/5984622252");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        TextView tv2 = findViewById(R.id.text2);



        tv2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                final Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("market://search?q=pub:TwinApps"));
                //i.setData(Uri.parse("http://play.google.com/store/apps/developer?id=TwinApps"));
                startActivity(i);
            }

        });



    }

    private void setWinsAndLossesText() {
        Context context = getApplicationContext();
        appMemorys = new AppMemorys(context);

        int wins = 0;
        int losses = 0;
        try{
            wins = appMemorys.getGamesWon();
            losses = appMemorys.getGamesLost();
        }catch(Exception e){

        }

        TextView textViewWin = (TextView)findViewById(R.id.winsAndLossesCount);
        //textViewWin.setText(getResources().getString(R.string.wins)+" "+wins+" "+getResources().getString(R.string.and)+" "+losses+" "+getResources().getString(R.string.losses));

    }

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        setWinsAndLossesText();
    }

    public void startGame(View v){
        try{
            Intent i = new Intent(this,GameView.class);
            startActivity(i);
        }catch(Exception e){
            Log.e("exception","Could not start game - error : "+e.getMessage());
        }
    }

    public void showAbout(View view){
        try{
            Intent i = new Intent(this,AboutView.class);
            startActivity(i);
        }catch(Exception e){
            Log.e("exception","Could not load about page,error :" +e.getMessage());
        }
    }
    public void rateApp() {
        // Uri uri=Uri.parse("market://search?q=pub:TwinApps");
        Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (Exception ex) {
            Toast.makeText(this, "Couldn't launch the market",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.rate:
                rateApp();
                break;
            case R.id.publish:
                publishApp("Iyu Tan");
                break;
            default:
                break;
        }
        return true;
    }

    private void publishApp(String devName) {
        Uri uri = Uri.parse("market://search?q=pub:" + devName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (Exception ex) {
            Toast.makeText(this, "Couldn't launch the market",
                    Toast.LENGTH_LONG).show();
        }
    }



}
