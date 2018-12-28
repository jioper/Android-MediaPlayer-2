package com.example.a10449.android_mediaplayer_finalversion;


import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Fouzia on 6/15/2017.
 */

public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener {


    public static MediaPlayer mediaPlayer = null;
    private boolean ServiceRunning = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {


        Uri ServiceUri = Uri.parse(intent.getStringExtra("ServiceUri"));

        if (!ServiceRunning) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), ServiceUri);

            mediaPlayer.start();
            ServiceRunning = true;

        } else {

            mediaPlayer.seekTo(0);
            mediaPlayer.pause();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), ServiceUri);
            mediaPlayer.start();

        }

        return START_STICKY;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(PlayerMActivity.MY_NOTIFICATION_ID);

    }
}


