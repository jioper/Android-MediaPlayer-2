package com.example.a10449.android_mediaplayer_finalversion;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;



public class SongAdapter extends ArrayAdapter<Song> {
    private static class ViewHolder {
        TextView name;
        TextView artist;
    }

    public SongAdapter(Context context, ArrayList<Song> songs) {
        super(context, 0, songs);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Song song = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {

            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.songlistview, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.filenametextview);
            viewHolder.artist = (TextView) convertView.findViewById(R.id.artistnametextview);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.artist.setText(song.artistName);
        viewHolder.name.setText(song.songName);
        return convertView;
    }
}