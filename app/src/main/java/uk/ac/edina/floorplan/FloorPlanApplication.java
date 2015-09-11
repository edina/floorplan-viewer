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
import uk.ac.edina.ibeacon.geofence.IBeacon;
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

    public FloorPlanBaseActivity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(FloorPlanBaseActivity currentActivity) {
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
        final List<Area> areas = FloorPlanAreas.getAreas(this.getResources());
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

                List<Beacon> sortedBeaconsByDistance = new ArrayList<>(beacons);

                Collections.sort(sortedBeaconsByDistance, new Comparator<Beacon>() {
                    @Override
                    public int compare(Beacon beacon, Beacon beacon2) {
                        double test = beacon.getDistance() - beacon2.getDistance();
                        return (int) Math.round(test * 1000);
                    }
                });

                Beacon beaconToTest = null;

                for (int i = 0; i < sortedBeaconsByDistance.size(); i++) {
                    Beacon b = sortedBeaconsByDistance.get(i);
                    IBeacon bWrapper = new BeaconWrapper(b);
                    String title = "Not Edina";
                    for (Area a : areas) {
                        if (a.getBeaconId().equals(bWrapper.getMinorId())) {
                            title = a.getTitle();
                        }
                    }
                    //
                    if (i == 0) {
                        beaconToTest = b;
                        Log.d("Ranging1", "--------------------- Testing For--------------- ");
                        Log.d("Ranging1", "XXXXXXX  Beacon XXXXXX " + title + " Id " + bWrapper.getMinorId() + " Distance " + bWrapper.getDistance() +
                                " Signal Strength " + bWrapper.getRssi() + " Tx Power " + bWrapper.getTxPower());
                    } else {
                        Log.d("Ranging1", "--------------------- Ignoring --------------- ");
                        Log.d("Ranging1", "XXXXXXX  Beacon XXXXXX " + title + " Id " + bWrapper.getMinorId() + " Distance " + bWrapper.getDistance() +
                                " Signal Strength " + bWrapper.getRssi() + " Tx Power " + bWrapper.getTxPower());
                    }
                }

                if (beaconToTest != null) {


                    // Log.d(TAG, beacon.toString());
                    IBeacon bWrapper = new BeaconWrapper(beaconToTest);
                    boolean coloredBeaconsDebug = false;

                    //find area details
                    String title = "Not Edina";
                    for (Area a : areas) {
                        if (a.getBeaconId().equals(bWrapper.getMinorId())) {
                            title = a.getTitle();
                        }
                    }
                    //Log.d("Ranging1", "XXXXXXX Beacon XXXXXX " + title +  " Id " + bWrapper.getMinorId() +" Distance " + bWrapper.getDistance() +
                    //    " Signal Strength " + bWrapper.getRssi() + " Tx Power " + bWrapper.getTxPower() );

                    for (final BeaconGeoFence geoFence : beaconGeoFences) {

                        geoFence.evaluateGeofence(bWrapper);

                    }
                }


                /**************************** debug to display *******************/


                    /*MainMapView.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            distanceFromBeacon.setText(debug.toString());
                        }
                    });*/


                /**************************** end debug to display *******************/

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
        beaconGeoFences.clear();
        for (Area a : areas) {
            String enterRegionNotificationText = String.format("Entering region %s", a.getTitle());
            String leavingRegionNotificationText = String.format("Leaving region %s", a.getTitle());
            GeoFenceAction action = new GeoFenceDebugAction(this, a, enterRegionNotificationText, leavingRegionNotificationText);
            BeaconGeoFence beaconGeoFence = new BeaconGeoFence(3.0, a.getBeaconId(), action);
            beaconGeoFences.add(beaconGeoFence);

        }

    }


}
