package uk.ac.edina.floorplan;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class AreaDetailView extends Activity {

    private MediaPlayer mediaPlayer;
    public TextView areaDetailsTitle, duration;
    private ImageView areaDetailsImage;
    private double timeElapsed = 0, finalTime = 0;
    private int forwardTime = 2000, backwardTime = 2000;
    private Handler durationHandler = new Handler();
    private SeekBar seekBar;
    private TextView areaDetailsDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //set the layout of the Activity
        setContentView(R.layout.activity_area_detail_view);

        //initialize views
        initializeViews();
    }

    public void initializeViews(){
        Intent previousIntent = getIntent();

        Area area = (Area)previousIntent.getSerializableExtra(PlacesFragment.AREA_KEY);

        areaDetailsTitle = (TextView) findViewById(R.id.areaDetailTitle);
        areaDetailsImage = (ImageView)findViewById(R.id.areaDetailsImage);
        areaDetailsDescription = (TextView) findViewById(R.id.areaDetailsDescription);
        mediaPlayer = MediaPlayer.create(this, R.raw.sample_song);
        finalTime = mediaPlayer.getDuration();
        duration = (TextView) findViewById(R.id.duration);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        areaDetailsTitle.setText(area.getTitle());
        areaDetailsImage.setImageResource(area.getImageId());
        areaDetailsDescription.setText(area.getDescription());
        seekBar.setMax((int) finalTime);
        seekBar.setClickable(true);

        seekBar.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                seekChange(v);
                return false;
            }
        });
    }


    private void seekChange(View v){
        if(mediaPlayer.isPlaying()){
            SeekBar sb = (SeekBar)v;
            mediaPlayer.seekTo(sb.getProgress());
        }
    }

    // play mp3 song
    public void play(View view) {
        mediaPlayer.start();
        timeElapsed = mediaPlayer.getCurrentPosition();
        seekBar.setProgress((int) timeElapsed);
        durationHandler.postDelayed(updateSeekBarTime, 100);
    }

    //handler to change seekBarTime
    private Runnable updateSeekBarTime = new Runnable() {
        public void run() {
            //get current position
            timeElapsed = mediaPlayer.getCurrentPosition();
            //set seekBar progress
            seekBar.setProgress((int) timeElapsed);
            //set time remaing

            duration.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed), TimeUnit.MILLISECONDS.toSeconds((long) timeElapsed) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed))));

            //repeat yourself that again in 100 miliseconds
            durationHandler.postDelayed(this, 100);
        }
    };

    // pause mp3 song
    public void pause(View view) {
        mediaPlayer.pause();
    }

    // go forward at forwardTime seconds
    public void forward(View view) {
        //check if we can go forward at forwardTime seconds before song endes
        if ((timeElapsed + forwardTime) <= finalTime) {
            timeElapsed = timeElapsed + forwardTime;

            //seek to the exact second of the track
            mediaPlayer.seekTo((int) timeElapsed);
        }
    }

    // go backwards at backwardTime seconds
    public void rewind(View view) {
        //check if we can go back at backwardTime seconds after song starts
        if ((timeElapsed - backwardTime) > 0) {
            timeElapsed = timeElapsed - backwardTime;

            //seek to the exact second of the track
            mediaPlayer.seekTo((int) timeElapsed);
        }
    }

}
