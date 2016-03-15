package uk.ac.edina.ibeacon.geofence.states;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import uk.ac.edina.ibeacon.geofence.BeaconGeoFence;

/**
 * Created by murrayking on 20/10/2014.
 */
public class GeoFenceOutsideState implements BeaconGeoFenceState {
    private BeaconGeoFence beaconGeoFence;
    private Context context ;


    public GeoFenceOutsideState(BeaconGeoFence beaconGeoFence) {
        this.beaconGeoFence = beaconGeoFence;

    }

    @Override
    public void evaluateGeofence(String id, double distance) {


        if( !beaconGeoFence.getMinorId().equals(id)){
            return;
        }

        Log.d("Ranging1", "Distance Outside" + distance + " radius for geofence:" + beaconGeoFence.getRadius());
        boolean enteringGeofence = distance < beaconGeoFence.getRadius();

        Log.d("BeaconGeoFenceState", "Distance Outside" + distance);
        if(enteringGeofence) {
            beaconGeoFence.setCurrentState(beaconGeoFence.getInsideGeoFence());

            // broadcast ENTER state change to other geofences (including this one)
            Intent beaconGeofenceEnterIntent = new Intent() ;
            beaconGeofenceEnterIntent.setAction("ENTER") ;
            beaconGeofenceEnterIntent.putExtra("BEACON_ID",beaconGeoFence.getMinorId()) ;
            beaconGeofenceEnterIntent.putExtra("DISTANCE",distance) ;
            LocalBroadcastManager.getInstance(beaconGeoFence.getAppContext()).sendBroadcast(beaconGeofenceEnterIntent) ;

            beaconGeoFence.getGeoFenceAction().onEnter();
        }
        else
        {
            // broadcast no state change to other geofences
            Intent beaconGeofenceNoStateChangeIntent = new Intent() ;
            beaconGeofenceNoStateChangeIntent.setAction("STAY_OUTSIDE") ;
            beaconGeofenceNoStateChangeIntent.putExtra("BEACON_ID",beaconGeoFence.getMinorId()) ;
            beaconGeofenceNoStateChangeIntent.putExtra("DISTANCE",distance) ;
            LocalBroadcastManager.getInstance(beaconGeoFence.getAppContext()).sendBroadcast(beaconGeofenceNoStateChangeIntent) ;

        }

    }

}
