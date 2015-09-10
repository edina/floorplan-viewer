package uk.ac.edina.floorplan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by murrayking on 22/07/2015.
 */
public class Area implements Serializable{


    List<double[]> bboxPoints;
    private String title;
    private int imageId;
    private String description;
    private ImagePixelLocation point;
    private String beaconId;
    private int videoId;

    Area(String title, int imageId, String description, String locations, int videoId, String bbox, String beaconId) {
        this.title = title;
        this.imageId = imageId;
        this.description = description;
        String[] p = locations.split(",");
        int x = Integer.valueOf(p[0]);
        int y = Integer.valueOf(p[1]);
        point = new ImagePixelLocation(x, y);
        this.videoId = videoId;
        this.bboxPoints = parseBBox(bbox);
        this.beaconId = beaconId;
    }

    public String getBeaconId() {
        return beaconId;
    }

    public List<double[]> getBboxPoints() {
        return bboxPoints;
    }

    public int getVideoId() {
        return videoId;
    }

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

private List<double[]>  parseBBox(String bbox){
        ArrayList<double[]> bboxPoints = new ArrayList<>();

        String[] points = bbox.split(";");
        for(String coordinates : points){

           String[] coords =  coordinates.split(",");

            bboxPoints.add(new double[]{ Double.valueOf(coords[0]), Double.valueOf(coords[1])});

        }

        return bboxPoints;

    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Area area = (Area) o;

        if (imageId != area.imageId) return false;
        if (title != null ? !title.equals(area.title) : area.title != null) return false;
        if (description != null ? !description.equals(area.description) : area.description != null)
            return false;
        return !(point != null ? !point.equals(area.point) : area.point != null);

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (point != null ? point.hashCode() : 0);
        return result;
    }
}
