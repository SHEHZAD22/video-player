package com.shehzad.gifsvideo;

import android.annotation.SuppressLint;
import android.app.PictureInPictureParams;

import android.content.pm.ActivityInfo;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Rational;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.Objects;

public class VidActivity extends AppCompatActivity {

    PlayerView playerView;
    ProgressBar progressBar;
    ImageView fullscreen, pip, mute;
    String url;
    Uri uri;
    boolean flag = false;
    ExoPlayer exoPlayer;
    boolean muteFlag = false;

    //picture in picture mode
    PictureInPictureParams.Builder pictureInPicture;
    boolean isCrossChecked;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vid);
        Objects.requireNonNull(getSupportActionBar()).hide();

        //assging variable
        playerView = findViewById(R.id.exoplayer);
        progressBar = findViewById(R.id.progressBarVid);
        fullscreen = findViewById(R.id.fullscreen);
        pip = findViewById(R.id.pip);
        mute = findViewById(R.id.mute);

        //make activity fulll screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //picture in picture mode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            pictureInPicture = new PictureInPictureParams.Builder();
        }

        //video url
        Bundle bundle = getIntent().getExtras();
        url = bundle.getString("url");
        if (url != null) {
            uri = Uri.parse(url);
        }

        exoPlayer = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(exoPlayer);
        MediaItem mediaItem = MediaItem.fromUri(uri);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();

        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_BUFFERING)
                    progressBar.setVisibility(View.VISIBLE);
                else if (playbackState == Player.STATE_READY)
                    progressBar.setVisibility(View.GONE);
            }
        });

        fullscreen.setOnClickListener(view -> rotateScreen());

        pip.setOnClickListener(view -> setPictureInPicture());

        mute.setOnClickListener(view ->  setMuteUnmute());

    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private void rotateScreen() {
        if (flag) {
            //when flag is false, set enter full scn img
            fullscreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_round_fullscreen));

            //set portrait orinentain
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            //set flag value to false'
            flag = false;
        } else {

            //when flag is true, exit full scn img
            fullscreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen_exit));
            //set landscape mode
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            flag = true;
        }
    }

    private void setPictureInPicture() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Rational aspectRatio = new Rational(16, 9);
            pictureInPicture.setAspectRatio(aspectRatio);
            enterPictureInPictureMode(pictureInPicture.build());
        } else Log.wtf("not oreo", "yes");
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        isCrossChecked = isInPictureInPictureMode;
        if (isInPictureInPictureMode) {
            playerView.hideController();
            fullscreen.setVisibility(View.INVISIBLE);
            pip.setVisibility(View.INVISIBLE);
        } else {
            playerView.showController();
            fullscreen.setVisibility(View.VISIBLE);
            pip.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setMuteUnmute() {
        if(muteFlag){
            exoPlayer.setVolume(1);
            mute.setImageDrawable(getResources().getDrawable(R.drawable.ic_unmute));
            muteFlag=false;
        }else{
            exoPlayer.setVolume(0);
            mute.setImageDrawable(getResources().getDrawable(R.drawable.ic_mute));
            muteFlag=true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onPause() {
        super.onPause();
        playWhenReady(false);
        if (isInPictureInPictureMode()) exoPlayer.setPlayWhenReady(true);
        else playWhenReady(false);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //play video when ready
        playWhenReady(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        playWhenReady(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isCrossChecked) {
            exoPlayer.release();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exoPlayer.release();
    }

    private void playWhenReady(boolean b) {
        exoPlayer.setPlayWhenReady(b);
        exoPlayer.getPlaybackState();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (exoPlayer.isPlaying()) exoPlayer.stop();
    }
}


