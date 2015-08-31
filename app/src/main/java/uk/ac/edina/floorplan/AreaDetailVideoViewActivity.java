package uk.ac.edina.floorplan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class AreaDetailVideoViewActivity extends FloorPlanBaseActivity {

    public static final String POSITION = "Position";
    private VideoView myVideoView;
    private int position = 0;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the layout from video_main.xml
        setContentView(R.layout.activity_area_detail_video_view);

        if (mediaControls == null) {
            mediaControls = new MediaController(AreaDetailVideoViewActivity.this);
        }

        // Find your VideoView in your video_main.xml layout
        myVideoView = (VideoView) findViewById(R.id.video_view);

        if(savedInstanceState != null){
            position = savedInstanceState.getInt(POSITION);
        }

        Intent previousIntent = getIntent();

        Area area = (Area)previousIntent.getSerializableExtra(AreasListActivity.AREA_KEY);

        TextView areaDetailsTitle = (TextView) findViewById(R.id.areaDetailVideoTitle);

        // Create a progressbar
        progressDialog = new ProgressDialog(AreaDetailVideoViewActivity.this);
        // Set progressbar title
        progressDialog.setTitle("Loading Video Resource");
        // Set progressbar message
        progressDialog.setMessage("Loading...");

        progressDialog.setCancelable(false);
        // Show progressbar
        progressDialog.show();

        try {
            myVideoView.setMediaController(mediaControls);
            myVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + area.getVideoId()));

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        myVideoView.requestFocus();

        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                progressDialog.dismiss();
                myVideoView.seekTo(position);
                myVideoView.start();
            }
        });

    }

    @Override
    public boolean acceptBeaconNotifications() {
        return false;
    }

    @Override
    public boolean removeNavigationHistory() {
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(POSITION, myVideoView.getCurrentPosition());
        myVideoView.pause();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position = savedInstanceState.getInt(POSITION);
        myVideoView.seekTo(position);
    }
}