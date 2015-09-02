package uk.ac.edina.floorplan;

import android.app.Application;
import android.os.RemoteException;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
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
import uk.ac.edina.ibeacon.geofence.actions.GeoFenceDebugAction;

/**
 * Created by murrayking on 06/08/2015.
 */
public class FloorPlanApplication extends Application implements BeaconConsumer {

    private static final String TAG = "Ranging";
    private static final String BEACON_LAYOUT_FOR_ESTIMOTE = "m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24";
    private BeaconManager beaconManager;

    private List<BeaconGeoFence> beaconGeoFences = new ArrayList<>();

    private FloorPlanBaseActivity currentActivity;

    public FloorPlanBaseActivity getCurrentActivity(){
        return currentActivity;
    }
    public void setCurrentActivity(FloorPlanBaseActivity currentActivity){
        this.currentActivity = currentActivity;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        beaconManager = BeaconManager.getInstanceForApplication(this);
        BeaconParser beaconParser = new BeaconParser();
        beaconParser.setBeaconLayout(BEACON_LAYOUT_FOR_ESTIMOTE);
        beaconManager.getBeaconParsers().add(beaconParser);
        beaconManager.bind(this);

        addGeoFences();

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



    private void addGeoFences() {
        List<Area> areas = FloorPlanAreas.getAreas(this.getResources());

        GeoFenceAction alertDialogAction = new GeoFenceDebugAction(this, areas.get(0) , "Enter Message", "Leave Message");
        String printerHelpUrl = "http://www8.hp.com/uk/en/home.html";
        //GeoFenceAction showPrinterPage = new GeoFenceWebAction(MainMapView.this, printerHelpUrl);

        String lightBlueBeaconMinorId = "59317";
        String purpleBeaconMinorId = "24489";



        //GeoFenceAction exhibitionRoom = new GeoFenceShowOnPlan(areas.get(0), this.tileView, this);

        //BeaconGeoFence blueBeaconShowSampleAlert = new BeaconGeoFence(5, purpleBeaconMinorId, exhibitionRoom);

        //GeoFenceAction debug27600 = new GeoFenceDebugAction(this, areas.get(2) , "Enter Icy Marshmallow 27600", "Leave Icy Marshmallow 27600" );

        //GeoFenceAction debug59317 = new GeoFenceDebugAction( this, areas.get(0) ,"Enter Icy Marshmallow 59317", "Leave Icy Marshmallow 59317" );
        GeoFenceAction debug43808 = new GeoFenceDebugAction( this, areas.get(2) ,"Enter Exhibition Space 43808", "Leave Exhibition Space 43808");


       // BeaconGeoFence icyMarshmallow27600 = new BeaconGeoFence(5, "27600", debug27600);


        //BeaconGeoFence icyMarshmallow59317 = new BeaconGeoFence(5, "59317", debug59317);


        BeaconGeoFence mintCocktail43808 = new BeaconGeoFence(5, "43808", debug43808);

        //beaconGeoFences.add(blueBeaconShowSampleAlert);


        //beaconGeoFences.add(icyMarshmallow27600);
        beaconGeoFences.add(mintCocktail43808);
        //beaconGeoFences.add(icyMarshmallow59317);


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



}
