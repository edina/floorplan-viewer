package fplan.edina.ac.uk.fplan;

import junit.framework.TestCase;

/**
 * Created by murrayking on 22/05/2015.
 */
public class UtilsTest extends TestCase {





    private static final String TAG = "UtilsTest";

    private static final double DELTA = 1e-1;

    public void setUp() throws Exception {
        super.setUp();

    }



    public void testLatLonToMeters() {

        Utils utils = new Utils();

        //edinburgh univerisity library
        //55.942710, -3.18915


        Utils.LatLon latLon = utils.latLonToMeters(55.942710, -3.18915);
        System.out.println("lat in meters " + latLon.getLat() + " lon in meters " + latLon.getLon());

        assertEquals("lat in meters ", 7547019.280554745, latLon.getLat(), DELTA);
        assertEquals("lon in meters ", -355014.5540633685, latLon.getLon(), DELTA);

    }

    public void testLatLonToImagePixels() {
        Utils utils = new Utils();
        //bottom right of image
        Utils.LatLon latLon = utils.latLonToImagePixels(7546992.67, -354927.1);

        System.out.println("yPixel " + latLon.getLat());
        System.out.println("xPixel " + latLon.getLon());

        assertEquals("x pixels in image ",  5073.129808, latLon.getLon(), DELTA);
        assertEquals("y pixels in image ",3745.075892, latLon.getLat(), DELTA);
    }
}