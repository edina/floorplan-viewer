package uk.ac.edina.ibeacon.geofence.actions;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by murray on 15/10/14.
 */
public class GeoFenceWebAction implements GeoFenceAction {
    private final String url;
    private Activity activity;

    public GeoFenceWebAction(Activity activity, String url){
        this.activity = activity;
        this.url = url;
    }
    @Override
    public void onEnter() {

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        activity.startActivity(i);
    }

    @Override
    public void onLeave() {

    }
}
