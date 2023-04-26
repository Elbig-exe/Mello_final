package com.example.mello2.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mello2.Files.Music_files;
import com.example.mello2.R;

import java.util.ArrayList;

import static com.example.mello2.Activities.Artist_details.art_album_inar;
import static com.example.mello2.Activities.Artist_details.name_artist_inar;
import static com.example.mello2.Activities.Artist_details.name_song_inar;
import static com.example.mello2.Activities.Artist_details.playbtn_inar;
import static com.example.mello2.Activities.Artist_details.short_hold;
import static com.example.mello2.Activities.MainActivity.music_files;
import static com.example.mello2.Activities.MainActivity.player;
import static com.example.mello2.Adapters.Album_D_Adapter.initalbum_shortcut;
import static com.example.mello2.Adapters.Music_Adapter.init_shortcut;

public class Artist_D_Adapter extends RecyclerView.Adapter<Artist_D_Adapter.MyHolder> {
    public static Context context;
    private ArrayList<Music_files> Artist_files;
    View view;

    public Artist_D_Adapter(Context context, ArrayList<Music_files> music_files) {
        Artist_D_Adapter.context = context;
        this.Artist_files = music_files;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from(context).inflate(R.layout.music_items,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.song_name.setText(Artist_files.get(position).getTitel());
        holder.song_artist.setText(Artist_files.get(position).getArtist());
        byte []image =getAlbumArt(Artist_files.get(position).getPath());
        if (image!=null){
            Glide.with(context).load(image).into(holder.album_art);
        }else {
            Glide.with(context).load(R.drawable.ic_baseline_person_24).into(holder.album_art);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.setCurrentSong(position);
                player.startSong();
                init_shortcut(image,player.getSongNameToInitVisuals(music_files),player.getArtistNameToInitVisuals(music_files));
                initalbum_shortcut(context,image,player.getSongNameToInitVisuals(music_files),player.getArtistNameToInitVisuals(music_files));
                initartist_shortcut(context,image,player.getSongNameToInitVisuals(music_files),player.getArtistNameToInitVisuals(music_files));
            }
        });
    }

    @Override
    public int getItemCount() {
        return Artist_files.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView album_art;
        TextView song_name,song_artist;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            album_art=itemView.findViewById(R.id.image);
            song_name=itemView.findViewById(R.id.title);
            song_artist=itemView.findViewById(R.id.artist);

        }
    }
    private byte[] getAlbumArt(String uri){
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art=retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }

    public static Context getContext() {
        return context;
    }
    public static void initartist_shortcut(Context context,byte[] bytes,String s,String s2){
        if (short_hold!=null){
            short_hold.setVisibility(View.VISIBLE);
            if (bytes!=null){
                Glide.with(context).load(bytes).into(art_album_inar);
            }else {
                Glide.with(context).load(R.drawable.ic_baseline_music_note_24).into(art_album_inar);
            }
            name_song_inar.setText(player.getSongName());
            name_artist_inar.setText(player.getSongName());
            if (player.isPlaying()){
                playbtn_inar.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
            }else {
                playbtn_inar.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
            }
        }

    }

}
