package uk.ac.edina.floorplan;

import java.io.Serializable;

/**
 * Created by murrayking on 22/07/2015.
 */
public class Area implements Serializable{


    private String title;
    private int imageId;
    private String description;
    private ImagePixelLocation point;

    public int getImageId() {
        return imageId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ImagePixelLocation getPoint() {
        return point;
    }

    Area(String title, int imageId, String description, String locations) {
        this.title = title;
        this.imageId = imageId;
        this.description = description;
        String[] p = locations.split(",");
        int x = Integer.valueOf(p[0]);
        int y = Integer.valueOf(p[1]);
        point = new ImagePixelLocation(x, y);
    }


}
