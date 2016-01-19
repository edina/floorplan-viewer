package uk.ac.edina.ibeacon.geofence.actions;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import uk.ac.edina.floorplan.Area;
import uk.ac.edina.floorplan.AreasListActivity;
import uk.ac.edina.floorplan.FloorPlanApplication;
import uk.ac.edina.floorplan.FloorPlanBaseActivity;
import uk.ac.edina.floorplan.FloorPlanViewActivity;

/**
 * Created by murrayking on 06/08/2015.
 */
public class GeoFenceCardViewAction implements GeoFenceAction {
    private static final String TAG = "Ranging";
    private String enterMessage;
    private String leaveMessage;
    private FloorPlanApplication context;
    private Area area;
    private final String LOG_TAG = "GeoFenceDebugAction" ;

    public GeoFenceCardViewAction(FloorPlanApplication context, Area area, String enterMessage, String leaveMessage) {

        this.enterMessage = enterMessage;
        this.leaveMessage = leaveMessage;
        this.context = context;
        this.area = area;
        //goto floorplan


    }

    @Override
    public void onEnter() {
        showLog(enterMessage);

        final FloorPlanBaseActivity currentActivity = context.getCurrentActivity();
        //&& currentActivity.acceptBeaconNotifications()

        if(currentActivity != null ) {

            //add an alert here
            currentActivity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
                    builder.setMessage(enterMessage);
                    builder.setCancelable(true);
                    builder.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    Intent detailIntent = new Intent(currentActivity, FloorPlanViewActivity.class);
                                    if (currentActivity.removeNavigationHistory()) {
                                        detailIntent.setFlags(detailIntent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    }

                                    detailIntent.putExtra(AreasListActivity.AREA_KEY, area);

                                    currentActivity.startActivity(detailIntent);
                                }
                            });


                    AlertDialog alert11 = builder.create();
                    alert11.show();
                }
            });


        }

    }

    private void showLog(final String message) {
        Log.d(TAG, message);
    }

    @Override
    public void onLeave() {
        final FloorPlanBaseActivity currentActivity = context.getCurrentActivity();
        //&& currentActivity.acceptBeaconNotifications()

        if(currentActivity != null ) {

            //add an alert here
            currentActivity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
                    builder.setMessage(leaveMessage);
                    builder.setCancelable(true);
                    builder.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                }
                            });

                    Log.d(LOG_TAG,leaveMessage) ;

                   // AlertDialog alert11 = builder.create();
                  //  alert11.show();
                }
            });


        }
    }
}