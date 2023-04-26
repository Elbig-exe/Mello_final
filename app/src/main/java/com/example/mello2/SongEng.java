package com.example.mello2;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.mello2.Files.Music_files;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.mello2.Activities.MainActivity.db;
import static com.example.mello2.Activities.MainActivity.music_files;

public class SongEng {
    private ArrayList<Music_files> songslist;
    public static MediaPlayer mediaPlayer=null;
    private Uri currentUri;
    private Context context;
    private float learningRate;
    private boolean paused=false;
    Weights chosenWeight;
    List<Weights> weights;
    float randomLevel;
    int songPosition=-1;
    boolean shuffle;
    boolean replay;
    public boolean isReplaing(){
        return replay;
    }
    public boolean isPlaying(){ return songPosition!=-1; }
    public void setReplay(boolean b){
        this.replay=b;
    }
    public boolean isShuffling(){
        return shuffle;
    }
    public void setShuffle(boolean shuffle){
        this.shuffle=shuffle;
    }

    private static float sigmoid(float x) {
        return ((float)(1 / (1 + Math.exp(-x))));
    }

    public void setRandomLevel(float rand){
        this.randomLevel=rand;
    }

    public SongEng(Context context){
        songslist=music_files;
        chosenWeight=null;
        learningRate=0.3f;
        randomLevel=0.1f;
        shuffle=false;
        this.context=context;
    }
    public SongEng(Context context,ArrayList<Music_files> list){
        songslist=list;
        chosenWeight=null;
        learningRate=0.3f;
        randomLevel=0.1f;
        shuffle=false;
        this.context=context;
    }

    void setContext(Context context){
        this.context=context;

    }
    public int getCurrentSong(){
        return songPosition;
    }

    public boolean setCurrentSong(int position){
        new Thread(()-> {
            weights=db.getWeightsForSong(songslist.get(songPosition).getPath());
        }).start();

        //db.printWeightsForSong(songslist.get(position).getPath());
        songPosition=position;
        if (songslist!=null) {//setting the current song
            currentUri = Uri.parse(songslist.get(position).getPath());
            return true;
        }
        return false;
    }

    int getPositionOfSong(String path){
        new Thread(()-> {
            weights=db.getWeightsForSong(songslist.get(songPosition).getPath());
        }).start();
        for(int i=0;i<songslist.size();i++){
            if(songslist.get(i).getPath().equals(path)){
                return i;
            }
        }
        return 0;
    }

    boolean setCurrentSong(String path){

        songPosition=getPositionOfSong(path);
        if (songslist!=null) {//setting the current song
            currentUri = Uri.parse(path);
            return true;
        }
        return false;
    }

    public void startSong(){
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(context, currentUri);
        mediaPlayer.start();
        paused=false;
    }
    public void seekTo(int sec){
        mediaPlayer.seekTo(sec*1000);
    }
    public int getProgress(){
        if(mediaPlayer!=null){
            return mediaPlayer.getCurrentPosition()/1000;
        }
        return 0;
    }
    public int getSongDuration(){
        if(mediaPlayer!=null) {
            return Integer.parseInt(songslist.get(songPosition).getDuration()) / 1000;
        }
        return 0;
    }
    public String getSongName(){
        if (songPosition>=0)
            return songslist.get(songPosition).getTitel();
        else return null;
    }
    public String getSongNameToInitVisuals(ArrayList<Music_files> previousList){
        if (songPosition>=0)
            return previousList.get(songPosition).getTitel();
        else return null;
    }
    public String getArtistName(){
        if (songPosition>=0)
            return songslist.get(songPosition).getArtist();
        else return null;
    }
    public String getArtistNameToInitVisuals(ArrayList<Music_files> previousList){
        if (songPosition>=0)
            return previousList.get(songPosition).getArtist();
        else return null;
    }


    void learnFromChosenWeight(){
        if(chosenWeight!=null){
            chosenWeight.topath=songslist.get(songPosition).getPath();

            if(goodDecision()){
                chosenWeight.weight=sigmoid((chosenWeight.weight+learningRate));
            }else{
                chosenWeight.weight=sigmoid((chosenWeight.weight-learningRate));
            }
            db.setWeight(chosenWeight);

        }
    }

    boolean goodDecision(){
        return getProgress()>getSongDuration()/2;
    }

    void playNextRand(){
        learnFromChosenWeight();
        //List<Weights> weights=db.getWeightsForSong(songslist.get(songPosition).getPath());
        Random rand=new Random(System.currentTimeMillis());
        int maxPos=0;
        float w;
        float max=0f;
        for(int i=0;i<weights.size();i++){
            w=sigmoid((weights.get(i).weight+(rand.nextFloat()*randomLevel))/2);
            if(w>max){
                max=w;
                maxPos=i;
            }
        }
        setCurrentSong(weights.get(maxPos).topath);
        if(goodDecision()){
            chosenWeight=weights.get(maxPos);
        }
    }

    public void playNext(){
            if(shuffle){
                playNextRand();
            }else{
                songPosition= (songPosition+1) % songslist.size();
                setCurrentSong(songPosition);
            }
            startSong();
    }

    public void playPrevious(){
        songPosition= (songPosition-1) <0? (songslist.size()-1):(songPosition-1);
        setCurrentSong(songPosition);
        startSong();
    }

    public void pause(){
        mediaPlayer.pause();
        paused=true;
    }

    public void resume(){
        mediaPlayer.start();
        paused=false;
    }

    public byte[] getArt(){
        MediaMetadataRetriever retriever= new MediaMetadataRetriever();
        if (currentUri!=null){
            retriever.setDataSource(currentUri.toString());
            byte[] art=retriever.getEmbeddedPicture();
            if (art!=null){
                return art;
            }
            else {
                return null;
            }
        }else return null;

    }
    public boolean isPaused(){
        return paused;
    }
    public void imageAnimation(Context context, ImageView imageView, byte[] bytes){
        Animation aniout = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        Animation aniin = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        aniout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (bytes!=null){
                    Glide.with(context).asBitmap().load(bytes).into(imageView);}
                else {
                    Glide.with(context).asBitmap().load(R.drawable.ic_baseline_person_24).into(imageView);
                }
                aniin.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        imageView.startAnimation(aniout);


    }

    public void setSongslist(ArrayList<Music_files> songslist) {
        chosenWeight=null;
        this.songslist = songslist;
    }

    public void stop() {
        mediaPlayer.release();
    }
}
