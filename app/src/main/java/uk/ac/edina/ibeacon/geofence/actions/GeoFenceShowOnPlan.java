package uk.ac.edina.ibeacon.geofence.actions;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import com.qozix.tileview.TileView;

import uk.ac.edina.floorplan.Area;
import uk.ac.edina.floorplan.R;

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

                //final Dialog dialog = new Dialog(activity, R.style.NewDialog);

                View view  = LayoutInflater.from(activity).inflate(R.layout.callout_layout, null);
                // Include dialog.xml file
                //dialog.setContentView(R.layout.callout_layout);
                // Set dialog title
               // dialog.setTitle("Grid Reference");

                //TextView gridRefText = (TextView) dialog.findViewById(R.id.grid_ref);
                //TextView eastingsNorthings = (TextView) dialog.findViewById(R.id.eastingsNorthings);
                //TextView latLong =  (TextView) dialog.findViewById(R.id.latLong);


                floorPlan.addCallout( view, x, y, -0.5f, -1.0f );
                /*
                eastingsNorthings.setText("easting  northing");

                gridRefText.setText("gridRef");

                latLong.setText("latt  lont");

                Window window = dialog.getWindow();
                window.setGravity(Gravity.TOP);

                dialog.show();

                Button dismissGridDialog = (Button) dialog.findViewById(R.id.dismissGridDialog);
                dismissGridDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });*/
                floorPlan.slideToAndCenter(x,y);
            }
        });




    }

    @Override
    public void onLeave() {

    }
}
