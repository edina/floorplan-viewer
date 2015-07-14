package uk.ac.edina.ibeacon.geofence.actions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by murray on 15/10/14.
 */
public class GeoFenceAlertDialogAction implements GeoFenceAction {

    private  String enterMessage;
    private String leaveMessage;
    private final Activity activity;

    public GeoFenceAlertDialogAction(Activity activity, String enterMessage, String leaveMessage){
        this.activity = activity;
        this.enterMessage = enterMessage;
        this.leaveMessage = leaveMessage;
    }
    @Override
    public void onEnter() {
        showDialog(enterMessage);

    }

    private void showDialog(final String message) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(GeoFenceAlertDialogAction.this.activity);
                builder1.setMessage(message);
                builder1.setCancelable(true);
                builder1.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });


                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
    }

        @Override
    public void onLeave() {
            showDialog(leaveMessage);
    }
}
