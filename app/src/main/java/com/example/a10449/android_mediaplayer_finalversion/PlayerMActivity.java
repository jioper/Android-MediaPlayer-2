package com.example.a10449.android_mediaplayer_finalversion;


import android.animation.ObjectAnimator;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import java.util.concurrent.TimeUnit;
import android.os.Handler;

public class PlayerMActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;

    Button buttonPlay;
    Button buttonStop;
    Button buttonPause;
    TextView SongTitle;
    TextView ArtistTitle;
    TextView AlbumTitle;
    TextView TotalTime;
    TextView CurrentTime;
    SeekBar seekBar;
    Handler handler = new Handler();
    long totalTime, currentTime;
    Uri myUri;
    Uri uri;
    public static MediaMetadataRetriever retriever = new MediaMetadataRetriever();
    public static final int MY_NOTIFICATION_ID = 1234;


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_m);

        myUri = Uri.parse(getIntent().getExtras().getString("uri"));
        mediaPlayer = MediaPlayer.create(getApplicationContext(), myUri);
        uri = myUri;

        buttonPlay = (Button) findViewById(R.id.PlayButton);
        buttonStop = (Button) findViewById(R.id.StopButton);
        buttonPause = (Button) findViewById(R.id.PauseButton);
        SongTitle = (TextView) findViewById(R.id.TitleOfSong);
        ArtistTitle = (TextView) findViewById(R.id.ArtistOfSong);
        AlbumTitle = (TextView) findViewById(R.id.AlbumOfSong);
        TotalTime = (TextView) findViewById(R.id.TotalTime);
        CurrentTime = (TextView) findViewById(R.id.CurrentTime);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        totalTime = mediaPlayer.getDuration();
        seekBar.setMax((int) totalTime);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                int i = seekBar.getProgress();
                MediaPlayerService.mediaPlayer.seekTo(i);
                MediaPlayerService.mediaPlayer.start();
            }
        });

        retriever.setDataSource(getApplicationContext(), uri);
        ArtistTitle.setText(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        SongTitle.setText(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
        AlbumTitle.setText(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));

        StartPlay();

        TotalTime.setText(String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes(totalTime),
                TimeUnit.MILLISECONDS.toSeconds(totalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalTime))));


        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                MediaPlayerService.mediaPlayer.start();
            }

        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MediaPlayerService.mediaPlayer != null && MediaPlayerService.mediaPlayer.isPlaying()) {
                    MediaPlayerService.mediaPlayer.seekTo(0);
                    MediaPlayerService.mediaPlayer.pause();

                }


            }
        });

        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MediaPlayerService.mediaPlayer.isPlaying())
                    MediaPlayerService.mediaPlayer.pause();

            }
        });
    }


    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            currentTime = MediaPlayerService.mediaPlayer.getCurrentPosition();

            CurrentTime.setText(String.format("%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes(currentTime), TimeUnit.MILLISECONDS.toSeconds(currentTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes(currentTime))));

            seekBar.setProgress((int) currentTime);
            handler.postDelayed(this, 100);
        }
    };


    public void StartPlay() {

        Intent serviceIntent = new Intent(PlayerMActivity.this, MediaPlayerService.class);
        serviceIntent.putExtra("ServiceUri", myUri.toString());
        addNotification();
        startService(serviceIntent);
        handler.postDelayed(UpdateSongTime, 100);

    }

    private void addNotification() {
        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.holdplay)
                        .setOngoing(true)
                        .setContentTitle(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE))
                        .setContentText(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));

        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent contentIntent = PendingIntent.getActivity(this, MY_NOTIFICATION_ID, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(MY_NOTIFICATION_ID, builder.build());


    }
}