package uk.ac.edina.ibeacon.geofence.distancecalc;

import uk.ac.edina.ibeacon.geofence.IBeacon;

/**
 * Created by murrayking on 22/10/2014.
 */
public class DefaultDistanceCalculator implements DistanceCalculator {
    @Override
    public double getDistance(IBeacon beacon) {
        return beacon.getDistance();
    }
}
