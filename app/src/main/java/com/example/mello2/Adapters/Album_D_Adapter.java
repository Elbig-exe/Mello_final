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

import static com.example.mello2.Activities.Album_details.short_cut_holder;
import static com.example.mello2.Activities.Album_details.shortcut_ablum_art;
import static com.example.mello2.Activities.Album_details.name_artist;
import static com.example.mello2.Activities.Album_details.name_song;
import static com.example.mello2.Activities.Album_details.playbtn;
import static com.example.mello2.Activities.MainActivity.player;
import static com.example.mello2.Adapters.Artist_D_Adapter.initartist_shortcut;
import static com.example.mello2.Adapters.Music_Adapter.init_shortcut;

public class Album_D_Adapter extends RecyclerView.Adapter<Album_D_Adapter.MyViewHolder> {
    public static Context context;
    private ArrayList<Music_files> music_files;

    public Album_D_Adapter(Context context, ArrayList<Music_files> music_files){
        Album_D_Adapter.context =context;
        this.music_files=music_files;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.music_items,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.file_name.setText(music_files.get(position).getTitel());
        holder.artist_name.setText(music_files.get(position).getArtist());
        byte []image =getAlbumArt(music_files.get(position).getPath());
        if (image!=null){
            Glide.with(context).load(image).into(holder.album_art);
        }else {
            Glide.with(context).load(R.drawable.cov).into(holder.album_art);
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
        return music_files.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView file_name,artist_name;
        ImageView album_art;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            file_name= itemView.findViewById(R.id.title);
            artist_name= itemView.findViewById(R.id.artist);
            album_art= itemView.findViewById(R.id.image);
        }
    }
    private byte[] getAlbumArt(String uri){
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art=retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
    public static void initalbum_shortcut(Context context,byte[] bytes,String s,String s2){
        if (short_cut_holder!=null){
            short_cut_holder.setVisibility(View.VISIBLE);
            if (bytes!=null){
                Glide.with(context).load(bytes).into(shortcut_ablum_art);
            }else {
                Glide.with(context).load(R.drawable.ic_baseline_album_24).into(shortcut_ablum_art);
            }
            name_song.setText(s);
            name_artist.setText(s2);
            if (player.isPlaying()){
                playbtn.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
            }else {
                playbtn.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
            }
        }
    }


}