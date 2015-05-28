package fplan.edina.ac.uk.fplan;

/**
 * Created by murrayking on 22/05/2015.
 */
public class Utils {

    double originShift = 2 * Math.PI * 6378137 / 2.0;

    //todo
    //process image world file to get values
    //Line 1: A: pixel size in the x-direction in map units/pixel
    //Line 2: D: rotation about y-axis
    //Line 3: B: rotation about x-axis
    //Line 4: E: pixel size in the y-direction in map units, almost always negative[3]
    //Line 5: C: x-coordinate of the center of the upper left pixel
    //Line 6: F: y-coordinate of the center of the upper left pixel
    // formula from http://en.wikipedia.org/wiki/World_file
    final double A = 0.02476749115,
            D = 0.007008493559,
            B = 0.006617771404,
            E = -0.02338670833,
            C = -355077.5328,
            F = 7547044.7;


    /**
        Converts given lat/lon in WGS84 Datum to XY in Spherical Mercator EPSG:900913
     */
    public  LatLon latLonToMeters (double lat,double lon){
        double mx = lon * this.originShift / 180.0;
        double my = Math.log( Math.tan((90 + lat) * Math.PI / 360.0 )) / (Math.PI / 180.0);

        my = my * this.originShift / 180.0;
        return new LatLon( my, mx);

    }


    public LatLon latLonToImagePixels(double y1, double x1){

        //console.log("p " + p);

        //x = (Ex' - By' +BF -EC)/(AE -DB)
        double x = (E*x1 - B*y1 + B*F - E*C )/(A*E - D*B);
        //y = (-Dx' + Ay' + DC - AF)/(AE - DB)
        double y = (-D*x1 + A*y1 + D*C - A*F)/(A*E - D*B);
        //move Y 2000 m

        //System.out.println( "Y_proj " + Y_proj);
        return new LatLon(y,x);
    }



    class LatLon {
        private double lat;
        private double lon;


        LatLon(double lat,double lon){
            this.lat = lat;
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public double getLon() {
            return lon;
        }
    }
}
