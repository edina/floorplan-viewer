package uk.ac.edina.floorplan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.qozix.tileview.TileView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import uk.ac.edina.ibeacon.geofence.BeaconGeoFence;
import uk.ac.edina.ibeacon.geofence.BeaconWrapper;
import uk.ac.edina.ibeacon.geofence.actions.GeoFenceAction;
import uk.ac.edina.ibeacon.geofence.actions.GeoFenceAlertDialogAction;
import uk.ac.edina.ibeacon.geofence.actions.GeoFenceShowOnPlan;


public class MainActivity extends Activity implements BeaconConsumer {

    protected static final String TAG = "RangingActivity";
    private static  Boolean addBeaconParserDone = new Boolean(false);
    public static final String BEACON_LAYOUT_FOR_ESTIMOTE = "m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24";
    private BeaconManager beaconManager;
    private TileView tileView;
    private Utils utils = new Utils();

    List<BeaconGeoFence> beaconGeoFences = new ArrayList<>();

    @Override
     public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //beaconManager = BeaconManager.getInstanceForApplication(this.getApplicationContext());
        Intent previousIntent = getIntent();

        Area area = (Area)previousIntent.getSerializableExtra(AreasList.AREA_KEY);

        /*synchronized (addBeaconParserDone) {
            if (addBeaconParserDone == false) {

                BeaconParser beaconParser = new BeaconParser();
                beaconParser.setBeaconLayout(BEACON_LAYOUT_FOR_ESTIMOTE);
                beaconManager.getBeaconParsers().add(beaconParser);
                addBeaconParserDone = true;
            }
        }*/


        //beaconManager.bind(this);

        // Create our TileView
        tileView = new TileView(this);

        addGeoFences();


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
            TileView tileView = MainActivity.this.tileView;
            // we saved the coordinate in the marker's tag
            final Area area = (Area) view.getTag();
            // lets center the screen to that coordinate
            CalloutFactory calloutFactory = new CalloutFactory();
            View callout = calloutFactory.createCallout(MainActivity.this, area);

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



    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {

                    for (Beacon beacon : beacons) {

                        for (final BeaconGeoFence geoFence : beaconGeoFences) {

                            geoFence.evaluateGeofence(new BeaconWrapper(beacon));

                            Log.d(TAG, beacon.toString());


                        }
                    }

                    /**************************** debug to display *******************/

                    final StringBuilder debug = new StringBuilder();

                    List<Beacon> sortedBeaconsByDistance = new ArrayList<>(beacons);

                    Collections.sort(sortedBeaconsByDistance, new Comparator<Beacon>() {
                        @Override
                        public int compare(Beacon beacon, Beacon beacon2) {
                            double test = beacon.getDistance() - beacon2.getDistance();
                            return (int) Math.round(test * 100);
                        }
                    });


                    for (Beacon b : sortedBeaconsByDistance) {
                        debug.append(b.getId3()).append(": dis : ").append(b.getDistance()).append("\n");
                    }
                    /*MainMapView.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            distanceFromBeacon.setText(debug.toString());
                        }
                    });*/


                    /**************************** end debug to display *******************/
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("all beacons", null, null, null));
        } catch (RemoteException e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public Context getApplicationContext() {
        return this.getApplicationContext();
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        this.unbindService(serviceConnection);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return this.bindService(intent, serviceConnection, i);
    }

    private void addGeoFences() {


        GeoFenceAction alertDialogAction = new GeoFenceAlertDialogAction(this, "Enter Message", "Leave Message");
        String printerHelpUrl = "http://www8.hp.com/uk/en/home.html";
        //GeoFenceAction showPrinterPage = new GeoFenceWebAction(MainMapView.this, printerHelpUrl);

        String lightBlueBeaconMinorId = "59317";
        String purpleBeaconMinorId = "24489";

        List<Area> areas = FloorPlanAreas.getAreas(this.getResources());

        GeoFenceAction exhibitionRoom = new GeoFenceShowOnPlan(areas.get(0), this.tileView, this);

        BeaconGeoFence blueBeaconShowSampleAlert = new BeaconGeoFence(5, purpleBeaconMinorId, exhibitionRoom);

        GeoFenceAction debug27600 = new GeoFenceAlertDialogAction(this, "Enter Icy Marshmallow 27600", "Leave Icy Marshmallow 27600" );

        GeoFenceAction debug59317 = new GeoFenceAlertDialogAction(this, "Enter Icy Marshmallow 59317", "Leave Icy Marshmallow 59317" );
        GeoFenceAction debug43808 = new GeoFenceAlertDialogAction(this, "Enter Mint Cocktail 43808", "Leave Mint Cocktail 43808");


        BeaconGeoFence icyMarshmallow27600 = new BeaconGeoFence(5, "27600", debug27600);


        BeaconGeoFence icyMarshmallow59317 = new BeaconGeoFence(5, "59317", debug59317);


        BeaconGeoFence mintCocktail43808 = new BeaconGeoFence(5, "43808", debug43808);

        beaconGeoFences.add(blueBeaconShowSampleAlert);


        beaconGeoFences.add(icyMarshmallow27600);
        beaconGeoFences.add(mintCocktail43808);
        beaconGeoFences.add(icyMarshmallow59317);


        /*GeoFenceAction highlightEdinaMeetingRoom = new GeoFenceHighLightRegionAction(MainMapView.this, mapView);
        GeoFenceAction geoFenceAudioAction = new GeoFenceAudioAction(MainMapView.this, "chime.mp3");
        GeoFenceAction geoFenceAudioAction2 = new GeoFenceAudioAction(MainMapView.this, "notificationdetect.mp3");
        GeoFenceAction alertDialogWelcome = new GeoFenceAlertDialogAction(MainMapView.this, "Welcome to EDINA", "Don't forget to leave FOB at reception!");
        GeoFenceAction alertDialogPrinter = new GeoFenceAlertDialogAction(MainMapView.this, "Printer CSCH2a", "Bye bye Printer");
        String printerHelpUrl = "http://www.okidata.com/printers/color/c830";
        GeoFenceAction showPrinterPage = new GeoFenceWebAction(MainMapView.this, printerHelpUrl);*/
        String blueberryBeaconMinorId = "24489";
        String mintBeaconMinorId = "11097";

        /*BeaconGeoFence blueBeaconHighlightMeetingRoom = new BeaconGeoFence(1.5,lightBlueBeaconMinorId, highlightEdinaMeetingRoom);
        BeaconGeoFence blueberryBeaconPrinter = new BeaconGeoFence(1.5,blueberryBeaconMinorId, alertDialogPrinter);
        BeaconGeoFence mintBeaconAlert = new BeaconGeoFence(1.5,mintBeaconMinorId, alertDialogWelcome);
        BeaconGeoFence blueBeaconAudioAction = new BeaconGeoFence(1.5,lightBlueBeaconMinorId, geoFenceAudioAction2);
        BeaconGeoFence mintBeaconAudioAction = new BeaconGeoFence(1.5,mintBeaconMinorId, geoFenceAudioAction);
        BeaconGeoFence blueberryBeaconAudioAction = new BeaconGeoFence(1.5,blueberryBeaconMinorId, geoFenceAudioAction2);
        beaconGeoFences.add(blueberryBeaconPrinter) ;
        beaconGeoFences.add(blueBeaconHighlightMeetingRoom) ;
        beaconGeoFences.add(blueBeaconAudioAction) ;
        beaconGeoFences.add(blueberryBeaconAudioAction) ;
        beaconGeoFences.add(mintBeaconAudioAction) ;
        beaconGeoFences.add(mintBeaconAlert) ;*/

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            beaconManager.unbind(this);
        } catch (Exception e) {
            Log.e(this.getClass().getName(), e.getMessage());
        }
    }


}
