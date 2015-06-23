package uk.ac.edina.floorplan;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qozix.tileview.TileView;


public class MainActivity extends Fragment {
    private TileView tileView;
    private Utils utils = new Utils();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PlacesFragment.SingleRow routeRow = getRow();
        // Create our TileView
        tileView = new TileView(this.getActivity());
        //2000 × 2829 pixels
        // Set the minimum parameters
        int zoomF = 1;
        tileView.setSize(5657*zoomF, 4000*zoomF);
        tileView.addDetailLevel(1f /zoomF, "tiles/groundfloor/1000/_%col%_%row%.png", "tiles/groundfloor/groundfloor.jpg");

        tileView.addDetailLevel(0.5f /zoomF, "tiles/groundfloor/500/_%col%_%row%.png", "tiles/groundfloor/groundfloor.jpg", 256, 256);

        // bottom left of university 55.942584, -3.188343
        Utils.LatLon latLonM = utils.latLonToMeters(55.942584, -3.188343);

        Utils.LatLon latLonImagePixels = utils.latLonToImagePixels(latLonM.getLat(), latLonM.getLon());
        Log.d(this.tileView.getClass().getName(), "lon " + latLonImagePixels.getLon());
        Log.d(this.tileView.getClass().getName(), "lat " + latLonImagePixels.getLat());
        // Add the view to display it
        tileView.setCacheEnabled(true);
        tileView.setTransitionsEnabled(false);
        //tileView.setScale(0.5);
        int x = routeRow.getPoint().getX();
        int y = routeRow.getPoint().getY();
        tileView.slideToAndCenter(x * zoomF, y * zoomF);

        tileView.setMarkerAnchorPoints(-0.5f, -0.5f);
        Log.d("tag", "x = " + x + "  y = " + y);
        addPin(x, y);

        return  tileView;



        //setContentView(R.layout.activity_main);
    }


    private void addPin( double x, double y ) {
        ImageView imageView = new ImageView( this.getActivity() );
        imageView.setImageResource( R.drawable.push_pin );
        tileView.addMarker(imageView, x, y );
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


    public static MainActivity newInstance(PlacesFragment.SingleRow row) {
        if(row == null){
            throw new NullPointerException("Need a map route row to display");
        }
        MainActivity f = new MainActivity();
        Bundle args = new Bundle();
        args.putSerializable(PlacesFragment.ROUTE_CHOSEN_KEY, row);

        f.setArguments(args);

        return f;
    }

    public PlacesFragment.SingleRow getRow() {
        return (PlacesFragment.SingleRow) getArguments().getSerializable(PlacesFragment.ROUTE_CHOSEN_KEY);
    }
}
