package uk.ac.edina.ibeacon.geofence;

/**
 * Created by murrayking on 22/10/2014.
 */
public interface IBeacon {

    double getDistance();

    public int getRssi();

    public int getTxPower();

    public String getMinorId();
}
