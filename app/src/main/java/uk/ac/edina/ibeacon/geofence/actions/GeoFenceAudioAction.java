package uk.ac.edina.ibeacon.geofence.actions;

import android.app.Activity;
import android.media.MediaPlayer;

import java.io.File;

import uk.ac.edina.ibeacon.geofence.Utils;

/**
 * Created by murrayking on 20/10/2014.
 */
public class GeoFenceAudioAction implements GeoFenceAction {


    private final Activity activity;
    private final String audioFile;

    public GeoFenceAudioAction(Activity activity, String audioFile){
        this.activity = activity;
        this.audioFile = audioFile;

    }
    @Override
    public void onEnter() {

        File audioToPlay = Utils.getInstance().copyFileFromAssets(audioFile, activity.getAssets(), activity.getPackageName());
        String filePath = audioToPlay.getAbsolutePath();
        MediaPlayer mp = new MediaPlayer();


        try {
            mp.setDataSource(filePath);
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public void onLeave() {

    }
}
