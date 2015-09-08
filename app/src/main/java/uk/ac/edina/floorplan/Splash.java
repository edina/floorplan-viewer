package uk.ac.edina.floorplan;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import java.util.Locale;

public class Splash extends Activity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 10000;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);
        AssetManager am = this.getApplicationContext().getAssets();
        Typeface typefaceBold = Typeface.createFromAsset(am,
                String.format(Locale.ENGLISH, "fonts/%s", "SourceSansPro-Bold.ttf"));
        TextView splashTitle = (TextView)this.findViewById(R.id.splashTitle);
        splashTitle.setTypeface(typefaceBold);

        Typeface typefaceRegular = Typeface.createFromAsset(am,
                String.format(Locale.ENGLISH, "fonts/%s", "SourceSansPro-Regular.ttf"));
        TextView splashSubtitle = (TextView)this.findViewById(R.id.splashSubtitle);
        splashSubtitle.setTypeface(typefaceRegular);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(Splash.this,AreasListActivity.class);
                Splash.this.startActivity(mainIntent);
                Splash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
