package com.example.a10449.android_mediaplayer_finalversion;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ArrayList<Song> arrayofSongs=new ArrayList<Song>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Intent intent=new Intent(this,PlayerMActivity.class);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                return;
            }}

        ContentResolver contentResolver = getContentResolver();
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if (cursor == null) {
            // query failed, handle error.
        } else if (!cursor.moveToFirst()) {
            // no media on the device
        } else {
            int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int dataColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            do {
                String thisartist = cursor.getString(artistColumn);
                String thisTitle = cursor.getString(titleColumn);
                String thisdata=cursor.getString(dataColumn);

                arrayofSongs.add(new Song(thisTitle,thisartist,thisdata));

            } while (cursor.moveToNext());
        }


        SongAdapter adapter=new SongAdapter(this,arrayofSongs);
        AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                Uri myUri=Uri.parse(arrayofSongs.get(position).dataPath);

                intent.putExtra("uri",myUri.toString());

                startActivity(intent);


            }
        };


        ListView listView=(ListView)findViewById(R.id.listofsongs);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(mMessageClickedHandler);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent serviceIntent = new Intent(MainActivity.this, MediaPlayerService.class);
        stopService(serviceIntent);
    }
}