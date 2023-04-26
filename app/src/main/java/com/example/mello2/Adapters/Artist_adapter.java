package com.example.mello2.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mello2.Activities.Artist_details;
import com.example.mello2.Files.Artists_files;
import com.example.mello2.R;

import java.util.ArrayList;

public class Artist_adapter extends RecyclerView.Adapter<Artist_adapter.MyHolder> {
    private Context context;
    private ArrayList<Artists_files> artist_files;


    public Artist_adapter(Context context, ArrayList<Artists_files> album_files) {
        this.context = context;
        this.artist_files = album_files;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.artist_item,parent,false);
        return new MyHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.artist_name.setText(artist_files.get(position).getArtist());
        byte []image =artist_files.get(position).getArt();
        if (image!=null){
            Glide.with(context).load(image).into(holder.artist_img);
        }else {
            Glide.with(context).load(R.drawable.ic_baseline_person_24).into(holder.artist_img);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, Artist_details.class);
                intent.putExtra("position",position);
                intent.putExtra("artist_name", artist_files.get(position).getArtist());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return artist_files.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        ImageView artist_img;
        TextView artist_name;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            artist_img=itemView.findViewById(R.id.artist_photo);
            artist_name=itemView.findViewById(R.id.Artist_name);
        }
    }
    private byte[] getAlbumArt(String uri){
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art=retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }

    public Context getContext() {
        return context;
    }
}