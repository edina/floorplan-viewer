package uk.ac.edina.ibeacon.geofence.states;

import android.util.Log;

import uk.ac.edina.ibeacon.geofence.BeaconGeoFence;

/**
 * Created by murrayking on 20/10/2014.
 */
public class GeoFenceOutsideState implements BeaconGeoFenceState {
    private BeaconGeoFence beaconGeoFence;

    public GeoFenceOutsideState(BeaconGeoFence beaconGeoFence) {
        this.beaconGeoFence = beaconGeoFence;
    }

    @Override
    public void evaluateGeofence(String id, double distance) {


        if( !beaconGeoFence.getMinorId().equals(id)){
            return;
        }

        boolean enteringGeofence = distance < beaconGeoFence.getRadius();

        Log.d("BeaconGeoFenceState", "Distance Outside" + distance);
        if(enteringGeofence) {
            beaconGeoFence.setCurrentState(beaconGeoFence.getInsideGeoFence());
            beaconGeoFence.getGeoFenceAction().onEnter();
        }

    }
}
