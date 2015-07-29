package uk.ac.edina.floorplan;

import java.io.Serializable;

/**
 * Created by murrayking on 22/07/2015.
 */
public class ImagePixelLocation implements Serializable {
    private int x;
    private int y;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImagePixelLocation that = (ImagePixelLocation) o;

        if (x != that.x) return false;
        return y == that.y;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

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
