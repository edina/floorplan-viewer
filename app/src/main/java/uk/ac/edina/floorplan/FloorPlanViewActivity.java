package uk.ac.edina.floorplan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.qozix.tileview.TileView;

import java.util.List;


public class FloorPlanViewActivity extends FloorPlanBaseActivity {


    private TileView tileView;
    private Utils utils = new Utils();



    @Override
     public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Intent previousIntent = getIntent();

        Area area = (Area)previousIntent.getSerializableExtra(AreasListActivity.AREA_KEY);

        /*synchronized (addBeaconParserDone) {
            if (addBeaconParserDone == false) {


                addBeaconParserDone = true;
            }
        }*/


        //

        // Create our TileView
        tileView = new TileView(this);




        // Set the minimum parameters
        int zoomF = 1;

        tileView.setSize(5657 * zoomF, 4000 * zoomF);
        tileView.addDetailLevel(1f / zoomF, "tiles/groundfloor/1000/_%col%_%row%.png", "tiles/groundfloor/groundfloor.jpg");

        tileView.addDetailLevel(0.5f / zoomF, "tiles/groundfloor/500/_%col%_%row%.png", "tiles/groundfloor/groundfloor.jpg", 256, 256);

        // bottom left of university 55.942584, -3.188343
        Utils.LatLon latLonM = utils.latLonToMeters(55.942584, -3.188343);

        Utils.LatLon latLonImagePixels = utils.latLonToImagePixels(latLonM.getLat(), latLonM.getLon());
        Log.d(this.tileView.getClass().getName(), "lon " + latLonImagePixels.getLon());
        Log.d(this.tileView.getClass().getName(), "lat " + latLonImagePixels.getLat());
        // Add the view to display it
        tileView.setCacheEnabled(true);
        tileView.setTransitionsEnabled(false);
        //tileView.setScale(2.0);
        final int x = area.getPoint().getX();
        final int y = area.getPoint().getY();


        tileView.setMarkerAnchorPoints(-0.5f, -0.5f);
        Log.d("tag", "x = " + x + "  y = " + y);
        List<Area> areas = FloorPlanAreas.getAreas(this.getResources());
        for(Area a: areas){
            addPin(a);
        }




        //setContentView(R.layout.activity_main);
        setContentView( tileView );




        tileView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tileView.slideToAndCenter(x, y);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    tileView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                } else {
                    tileView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });

    }

    @Override
    public boolean acceptBeaconNotifications() {
        return true;
    }

    @Override
    public boolean removeNavigationHistory() {
        return true;
    }


    private void addPin(Area area) {

        ImageView marker = new ImageView(this);
        marker.setImageResource(R.drawable.push_pin);
        int x = area.getPoint().getX();
        int y = area.getPoint().getY();
        marker.setTag(area);
        marker.setOnClickListener(markerClickListener);
        tileView.addMarker(marker, x, y);

    }
    private View.OnClickListener markerClickListener = new View.OnClickListener() {

        @Override
        public void onClick( View view ) {
            // get reference to the TileView
            TileView tileView = FloorPlanViewActivity.this.tileView;
            // we saved the coordinate in the marker's tag
            final Area area = (Area) view.getTag();
            // lets center the screen to that coordinate
            CalloutFactory calloutFactory = new CalloutFactory();
            View callout = calloutFactory.createCallout(FloorPlanViewActivity.this, area);

            // add it to the view tree at the same position and offset as the marker that invoked it
            int x = area.getPoint().getX(),y = area.getPoint().getY();
            tileView.addCallout( callout, x, y, -0.5f, -1.0f );
            tileView.setScale(3.0);
            tileView.slideToAndCenter(x,y);

        }
    };


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
