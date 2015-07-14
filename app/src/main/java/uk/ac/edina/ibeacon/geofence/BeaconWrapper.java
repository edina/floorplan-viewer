package uk.ac.edina.ibeacon.geofence;

import org.altbeacon.beacon.Beacon;

/**
 * Created by murrayking on 22/10/2014.
 */
public class BeaconWrapper implements IBeacon {
    Beacon beacon;

    public BeaconWrapper(Beacon beacon) {
        this.beacon = beacon;
    }


    @Override
    public double getDistance() {
        return beacon.getDistance();
    }

    @Override
    public int getRssi() {
        return beacon.getRssi();
    }

    @Override
    public int getTxPower() {
        return beacon.getTxPower();
    }

    @Override
    public String getMinorId() {
        return beacon.getId3().toString();
    }
}
