package uk.ac.edina.ibeacon.geofence.actions;

import android.app.Activity;
import android.view.View;

import com.qozix.tileview.TileView;

import uk.ac.edina.floorplan.Area;
import uk.ac.edina.floorplan.CalloutFactory;

/**
 * Created by murrayking on 22/07/2015.
 */
public class GeoFenceShowOnPlan implements GeoFenceAction {
    private final Activity activity;
    private Area area;
    private TileView floorPlan;

    public GeoFenceShowOnPlan(Area area, TileView floorPlan, Activity activity) {
        this.area = area;
        this.floorPlan = floorPlan;
        this.activity = activity;
    }

    @Override
    public void onEnter() {

        final int x = area.getPoint().getX();
        final int y = area.getPoint().getY();
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                CalloutFactory calloutFactory = new CalloutFactory();
                View callout = calloutFactory.createCallout(activity, area);

                // add it to the view tree at the same position and offset as the marker that invoked it
                floorPlan.addCallout( callout, x, y, -0.5f, -1.0f );

                floorPlan.slideToAndCenter(x,y);
            }
        });




    }

    @Override
    public void onLeave() {

    }
}
