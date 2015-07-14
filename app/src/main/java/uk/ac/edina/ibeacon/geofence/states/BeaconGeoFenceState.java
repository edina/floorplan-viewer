package uk.ac.edina.ibeacon.geofence.states;

/**
 * Created by murrayking on 20/10/2014.
 */
public interface BeaconGeoFenceState {

    void evaluateGeofence(String beaconId, double distanceFromBeacon);
}
