package fplan.edina.ac.uk.fplan;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.qozix.tileview.TileView;


public class MainActivity extends ActionBarActivity {
    private TileView tileView;
    private Utils utils = new Utils();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create our TileView
        tileView = new TileView(this);
        //2000 × 2829 pixels
        // Set the minimum parameters
        tileView.setSize(5657, 4000);
        tileView.addDetailLevel(1f, "groundfloor/1000_%col%_%row%.jpg", "groundfloor/groundfloor.jpg");

        tileView.addDetailLevel(0.5f, "groundfloor/500_%col%_%row%.jpg", "groundfloor/groundfloor.jpg");
        tileView.addDetailLevel(0.25f, "groundfloor/250_%col%_%row%.jpg", "groundfloor/groundfloor.jpg");
        tileView.addDetailLevel(0.125f, "groundfloor/125_%col%_%row%.jpg", "groundfloor/groundfloor.jpg");
        // bottom left of university 55.942584, -3.188343
        Utils.LatLon latLonM = utils.latLonToMeters(55.942584, -3.188343);
        Utils.LatLon latLonImagePixels = utils.latLonToImagePixels(latLonM.getLat(), latLonM.getLon());
        Log.d( this.tileView.getClass().getName(), "lon " + latLonImagePixels.getLon());
        Log.d( this.tileView.getClass().getName(), "lat " + latLonImagePixels.getLat());
        // Add the view to display it
        tileView.setCacheEnabled(true);
        tileView.moveToAndCenter(latLonImagePixels.getLon(), latLonImagePixels.getLat());
        setContentView(tileView);


        //setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
