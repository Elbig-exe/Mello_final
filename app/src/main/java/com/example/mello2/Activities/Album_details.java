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
import com.example.mello2.Adapters.Album_D_Adapter;
import com.example.mello2.Files.Music_files;
import com.example.mello2.R;

import java.util.ArrayList;

import static com.example.mello2.Activities.MainActivity.db;
import static com.example.mello2.Activities.MainActivity.music_files;
import static com.example.mello2.Activities.MainActivity.player;
import static com.example.mello2.Activities.MainActivity.relativeLayout;

import static com.example.mello2.Adapters.Album_D_Adapter.initalbum_shortcut;
import static com.example.mello2.Adapters.Album_D_Adapter.context;
import static com.example.mello2.SongEng.mediaPlayer;

public class Album_details extends AppCompatActivity {
    RecyclerView Album_song_list;
    ImageView its_album_album;
    TextView it_album_name;
    String albumnname;
    ArrayList<Music_files> song_list= new ArrayList<Music_files>();
    Album_D_Adapter album_adapter;
    public static ImageView shortcut_ablum_art,playbtn,skipbtn;
    public static TextView name_song,name_artist;
    public static RelativeLayout  short_cut_holder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);
        initviews();
        getAlbum_Songs();
        initVisuals();
        if (relativeLayout!=null){
            if (relativeLayout.getVisibility()==View.VISIBLE){
                initalbum_shortcut(this,player.getArt(),player.getSongNameToInitVisuals(music_files),player.getArtistNameToInitVisuals(music_files));
            }else {
                short_cut_holder.setVisibility(View.GONE);
            }
        }


    }
    @Override
    protected void onResume() {
        super.onResume();
        if (!(song_list.size()<1)){
            album_adapter=new Album_D_Adapter(this,song_list);
            Album_song_list.setAdapter(album_adapter);
            Album_song_list.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        }
        if (player.isPlaying())
            initalbum_shortcut(context,player.getArt(),player.getSongNameToInitVisuals(music_files),player.getArtistNameToInitVisuals(music_files));
    }

    public void initviews(){
        Album_song_list=findViewById(R.id.song_list);
        its_album_album=findViewById(R.id.album_art);
        it_album_name=findViewById(R.id.album_name);
        shortcut_ablum_art = findViewById(R.id.album_art_short);
        playbtn = findViewById(R.id.play_button_short);
        skipbtn = findViewById(R.id.skip_button_short);
        name_artist = findViewById(R.id.artist_short);
        name_song = findViewById(R.id.Song_name_short);
        short_cut_holder = findViewById(R.id.shortcut_holder);

    }
    public void initVisuals(){
        it_album_name.setText(albumnname);
        byte[] image=getAlbumArt(song_list.get(0).getPath());
        if (image!=null){
            Glide.with(this).asBitmap().load(image).into(its_album_album);
        } else {
            Glide.with(this).asBitmap().load(R.drawable.cov).into(its_album_album);
        }
    }
    public void getAlbum_Songs(){
        albumnname= getIntent().getStringExtra("Albumname");
        song_list=db.getSongsInAlbum(music_files,albumnname);
        player.setSongslist(song_list);
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
            playbtn.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
        }else {
            player.resume();
            playbtn.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
        }
    }
    public void skip(View view){
        player.playNext();
        byte[] bytes= player.getArt();
        Glide.with(this).asBitmap().load(bytes).into(shortcut_ablum_art);
        name_song.setText(player.getSongName());
        name_artist.setText(player.getArtistName());
        playbtn.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
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