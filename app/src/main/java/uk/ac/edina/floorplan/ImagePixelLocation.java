package uk.ac.edina.floorplan;

import java.io.Serializable;

/**
 * Created by murrayking on 22/07/2015.
 */
public class ImagePixelLocation implements Serializable {
    private int x;
    private int y;

    public ImagePixelLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
