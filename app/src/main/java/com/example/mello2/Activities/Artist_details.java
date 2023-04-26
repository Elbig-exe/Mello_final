package com.example.mello2.Activities;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mello2.Adapters.Artist_D_Adapter;
import com.example.mello2.Files.Music_files;
import com.example.mello2.R;
import com.example.mello2.SongEng;

import java.util.ArrayList;

import static com.example.mello2.Activities.MainActivity.db;
import static com.example.mello2.Activities.MainActivity.music_files;
import static com.example.mello2.Activities.MainActivity.player;
import static com.example.mello2.Activities.MainActivity.relativeLayout;

import static com.example.mello2.Adapters.Artist_D_Adapter.context;
import static com.example.mello2.Adapters.Artist_D_Adapter.initartist_shortcut;
import static com.example.mello2.SongEng.mediaPlayer;

public class Artist_details extends AppCompatActivity {
    ImageView artist_in_artist;
    TextView artist_name;
    RecyclerView recycler_View;
    String artistname;
    public static ImageView art_album_inar,playbtn_inar,skipbtn_inar;
    public static TextView name_song_inar,name_artist_inar;
    public static RelativeLayout short_hold,toodbar;

    ArrayList<Music_files> artist_song_list;
    Artist_D_Adapter artist_d_adapter;
    SongEng player_artists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_details);
        initviews();
        getsonglist();
        initVisuals();
        if (relativeLayout!=null){
            if (relativeLayout.getVisibility()==View.VISIBLE){
                initartist_shortcut(this,player.getArt(),player.getArtistNameToInitVisuals(music_files),player.getArtistNameToInitVisuals(music_files));
            }else {
                short_hold.setVisibility(View.GONE);
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (!(artist_song_list.size() <1)){
            artist_d_adapter= new Artist_D_Adapter(this,artist_song_list);
            recycler_View.setAdapter(artist_d_adapter);
            recycler_View.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        }
        if (player.isPlaying())
            initartist_shortcut(context,player.getArt(),player.getSongNameToInitVisuals(music_files),player.getArtistNameToInitVisuals(music_files));
    }
    public void initVisuals(){
        artist_name.setText(artistname);
        byte[] image=getAlbumArt(artist_song_list.get(0).getPath());
        if (image!=null){
            Glide.with(this).asBitmap().load(image).into(artist_in_artist);
        } else {
            Glide.with(this).asBitmap().load(R.drawable.ic_baseline_person_24).into(artist_in_artist);
        }
    }
    public void getsonglist(){
        artistname=getIntent().getStringExtra("artist_name");
        artist_song_list=db.getSongsofArtist(music_files,artistname);
        player.setSongslist(artist_song_list);
    }
    public void initviews(){
        artist_in_artist=findViewById(R.id.artist_img_artist);
        artist_name=findViewById(R.id.artist_name_artists);
        recycler_View=findViewById(R.id.song_list_ar);
        art_album_inar= findViewById(R.id.album_art_short);
        playbtn_inar= findViewById(R.id.play_button_short);
        skipbtn_inar=findViewById(R.id.skip_button_short);
        name_artist_inar= findViewById(R.id.artist_short);
        name_song_inar= findViewById(R.id.Song_name_short);
        short_hold=findViewById(R.id.shortcut_holder_ar);
        toodbar=findViewById(R.id.toolbar_Artist);
    }
    private byte[] getAlbumArt(String uri){
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art=retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
    public void playbtn(View view){
        if(mediaPlayer.isPlaying()){
            player.pause();
            playbtn_inar.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
        }else {
            player.resume();
            playbtn_inar.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
        }
    }
    public void skip(View view){
        player.playNext();
        byte[] bytes= player.getArt();
        Glide.with(this).asBitmap().load(bytes).into(art_album_inar);
        name_artist_inar.setText(player.getArtistName());
        name_song_inar.setText(player.getSongName());
        playbtn_inar.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
    }
    public void song_details(View view){
        Intent intent= new Intent(this, PlayerActivity.class);
        intent.putExtra("position",player.getCurrentSong());
        this.startActivity(intent);
    }
    public void backPressed(View view){
        super.onBackPressed();
    }
}