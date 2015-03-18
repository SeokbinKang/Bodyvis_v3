package com.redbear.bodyvis;

/**
 * Created by kangpc on 3/18/2015.
 */
import java.util.ResourceBundle.Control;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayActivity extends Activity
{
    VideoView video_player_view;
    DisplayMetrics dm;
    SurfaceView sur_View;
    MediaController media_Controller;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_page);
        getInit();
    }
        public void getInit() {
            video_player_view = (VideoView) findViewById(R.id.video_player_view);
            media_Controller = new MediaController(this);
            dm = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(dm);
            int height = dm.heightPixels;
            int width = dm.widthPixels;

            video_player_view.setMediaController(media_Controller);
            //video_player_view.setVideoPath("/bodyvis/appledigestion2.3gp");
            video_player_view.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.appledigestion2);
            video_player_view.setMinimumWidth(width);
            video_player_view.setMinimumHeight(height);

            video_player_view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer vmp) {

                    playAudio();
                    finish();
                }
            });


            video_player_view.start();
        }
    public void playAudio()  {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.fart2);
        mediaPlayer.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
