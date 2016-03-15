package uk.ac.edina.ibeacon.geofence.states;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import uk.ac.edina.ibeacon.geofence.BeaconGeoFence;

/**
 * Created by murrayking on 20/10/2014.
 */
public class GeoFenceInsideState implements  BeaconGeoFenceState {

    private BeaconGeoFence beaconGeoFence;


    public GeoFenceInsideState(BeaconGeoFence beaconGeoFence) {
        this.beaconGeoFence = beaconGeoFence;
    }

    @Override
    public void evaluateGeofence(String id, double distance) {


        if( !beaconGeoFence.getMinorId().equals(id)){
            return;
        }

        Log.d("BeaconGeoFenceState", "Distance Inside " + distance);
        Log.d("Ranging1", "Distance Inside" + distance + " radius for geofence:" + beaconGeoFence.getRadius());

        // radius is value set by enter event - so first value read that detected within original radius
        boolean leavingGeofence = distance > beaconGeoFence.getRadius();
        if(leavingGeofence) {
            beaconGeoFence.setCurrentState(beaconGeoFence.getOutsideGeoFence());
            beaconGeoFence.getGeoFenceAction().onLeave();
            Intent beaconGeofenceExitIntent = new Intent() ;
            beaconGeofenceExitIntent.setAction("EXIT") ;
            beaconGeofenceExitIntent.putExtra("BEACON_ID",beaconGeoFence.getMinorId()) ;
            beaconGeofenceExitIntent.putExtra("DISTANCE",distance) ;
            LocalBroadcastManager.getInstance(beaconGeoFence.getAppContext()).sendBroadcast(beaconGeofenceExitIntent) ;
        }
        else
        {

            //staying inside - braodcast latest inside distance all geofences
            Intent beaconGeofenceNoStateChangeIntent = new Intent() ;
            beaconGeofenceNoStateChangeIntent.setAction("STAY_INSIDE") ;
            beaconGeofenceNoStateChangeIntent.putExtra("BEACON_ID",beaconGeoFence.getMinorId()) ;
            beaconGeofenceNoStateChangeIntent.putExtra("DISTANCE",distance) ;
            LocalBroadcastManager.getInstance(beaconGeoFence.getAppContext()).sendBroadcast(beaconGeofenceNoStateChangeIntent) ;
        }
    }
}
