package uk.ac.edina.ibeacon.geofence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import uk.ac.edina.floorplan.FloorPlanApplication;
import uk.ac.edina.ibeacon.geofence.actions.GeoFenceAction;
import uk.ac.edina.ibeacon.geofence.distancecalc.DefaultDistanceCalculator;
import uk.ac.edina.ibeacon.geofence.distancecalc.DistanceCalculator;
import uk.ac.edina.ibeacon.geofence.states.BeaconGeoFenceState;
import uk.ac.edina.ibeacon.geofence.states.GeoFenceInsideState;
import uk.ac.edina.ibeacon.geofence.states.GeoFenceOutsideState;

/**
 * Created by murray on 14/10/14.
 */
public class BeaconGeoFence extends BroadcastReceiver {

    private final GeoFenceAction geoFenceAction;
    private double radius;
    private String minorId;

    private DistanceCalculator distanceCalculator;


    private BeaconGeoFenceState currentState;

    private BeaconGeoFenceState outsideGeoFence;
    private BeaconGeoFenceState insideGeoFence;
    private Context appContext ;


    public BeaconGeoFence(double radius, String minorId, GeoFenceAction geoFenceAction, Context appContext) {

        this(radius,minorId,geoFenceAction, new DefaultDistanceCalculator());
        this.appContext = appContext ;
        IntentFilter beaconIntentFilter = new IntentFilter() ;
        beaconIntentFilter.addAction("ENTER");
        beaconIntentFilter.addAction("STAY_OUTSIDE");
        beaconIntentFilter.addAction("STAY_INSIDE");
        beaconIntentFilter.addAction("RESET");
        beaconIntentFilter.addAction("EXIT");
        LocalBroadcastManager.getInstance(appContext).registerReceiver(this, beaconIntentFilter);

    }

    public BeaconGeoFence(double radius, String minorId, GeoFenceAction geoFenceAction, DistanceCalculator distanceCalculator){
        if(minorId == null || minorId.isEmpty() ){
            throw new IllegalArgumentException("Beacon minorId required");
        }
        if(radius < 0 ){
            throw new IllegalArgumentException("Radius must be > 0");
        }
        this.radius = radius;
        this.minorId = minorId;
        this.geoFenceAction = geoFenceAction;
        this.outsideGeoFence = new GeoFenceOutsideState(this);
        this.insideGeoFence = new GeoFenceInsideState(this);
        this.currentState = outsideGeoFence;
        this.distanceCalculator = distanceCalculator;
    }


    public void evaluateGeofence(IBeacon beacon) {

      //  Log.d("Ranging1", "evaluating geofence " + this.getMinorId() + " against beacon " + beacon.getMinorId()) ;
        double distance = distanceCalculator.getDistance(beacon);


        currentState.evaluateGeofence(beacon.getMinorId(), distance);
    }

    public void setCurrentState(BeaconGeoFenceState currentState) {
        this.currentState = currentState;
    }

    public BeaconGeoFenceState getCurrentState() {
        return currentState;
    }

    public double getRadius() {
        return radius;
    }

    public String getMinorId() {
        return minorId;
    }

    public GeoFenceAction getGeoFenceAction() {
        return geoFenceAction;
    }

    public BeaconGeoFenceState getOutsideGeoFence() {
        return outsideGeoFence;
    }

    public BeaconGeoFenceState getInsideGeoFence() {
        return insideGeoFence;
    }

    @Override
    public String toString() {
        return "BeaconGeoFence{" +
                "radius=" + radius +
                ", minorId='" + minorId + '\'' +
                '}';
    }

    public Context getAppContext()
    {
        return this.appContext ;
    }
    /*
     * receive message from a geofence which has just changed itself to InsideState
     * and unless this is the same geofence set current state to OutsideState to
     * ensure only the newly registered inside state geofence is active
     *
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction() ;
        if("EXIT".equals(action)) {

            String beaconId = intent.getStringExtra("BEACON_ID") ;
            double exitDistance = intent.getDoubleExtra("DISTANCE", -1) ;


            if( ! this.getMinorId().equals(beaconId) )
            {
                Log.d("Ranging1", "BeaconGeoFence " + this.getMinorId() +
                        " received EXIT intent from beacon " + beaconId +
                        " at exit distance:" + exitDistance);


            }
        }
        if("ENTER".equals(action))
        {

            String beaconId = intent.getStringExtra("BEACON_ID") ;
            double enterDistance = intent.getDoubleExtra("DISTANCE", -1) ;

            // the nearest beacon in the last batch of beacon readings was nearer than previous INSIDE beacon
            // so broadcast an enter event. All other beacons set to the enter distance. the new threshold to beat.

            if( ! this.getMinorId().equals(beaconId) )
            {
                Log.d("Ranging1", "BeaconGeoFence " + this.getMinorId() +
                        " received ENTER intent from beacon " + beaconId +
                        " at enter distance:" + enterDistance) ;

                this.setCurrentState(this.getOutsideGeoFence());
                this.radius = enterDistance  ;
            }
        }
        else if("STAY_OUTSIDE".equals(action))
        {
            String beaconId = intent.getStringExtra("BEACON_ID") ;
            Log.d("Ranging1", "BeaconGeoFence " + this.getMinorId() +
                    " received STAY_OUTSIDE intent from beacon " + beaconId ) ;

            // increase entry radius as the closet beacon in last abtch of readings was not near enough to trigger an entry
            // increasing the entry radius makes it easier next time for this or another outside beacon to push out the current INside bacon

            if(this.radius < (FloorPlanApplication.MAX_BEACON_RANGE - (FloorPlanApplication.MAX_BEACON_RANGE* 0.1 )) ){
                // increase radius by a tenth of max range
                this.radius = this.radius + (FloorPlanApplication.MAX_BEACON_RANGE*0.1);
            }


            Log.d("Ranging1", "BeaconGeoFence " + this.getMinorId() +
                    "STAY_OUTSIDE intent changed radius to " + this.radius ) ;

        // the current INSIDE geofence was the closest in the batch
        // all other beacons change their radius to the latest reading from the INSIDE beacon
        // the INSIDE beacon still keeps its orginal ENTER distance radius
        }else if("STAY_INSIDE".equals(action)) {
            String beaconId = intent.getStringExtra("BEACON_ID");
            double insideDistance = intent.getDoubleExtra("DISTANCE", -1) ;
            Log.d("Ranging1", "BeaconGeoFence " + this.getMinorId() +
                    " received STAY_INSIDE intent from beacon " + beaconId + " with inside distance:" + insideDistance);

            if(! this.getMinorId().equals(beaconId) ) {
                // TODO only if insideDistnace < this.radius?
                this.radius = insideDistance;
            }
        }else if("RESET".equals(action)) {

            Log.d("Ranging1", "BeaconGeoFence " + this.getMinorId() +
                    " received RESET intent current raduius  " + getRadius())  ;

            this.radius = FloorPlanApplication.MAX_BEACON_RANGE ;
            this.currentState = this.getOutsideGeoFence() ;
        }




        }
}
