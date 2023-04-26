package com.example.mello2;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import androidx.room.Room;

import com.example.mello2.Song;
import com.example.mello2.SongDao;
import com.example.mello2.Weights;
import com.example.mello2.WeightsDao;
import com.example.mello2.MusicDatabase;
import com.example.mello2.Files.Album_files;
import com.example.mello2.Files.Artists_files;
import com.example.mello2.Files.Music_files;

import java.util.ArrayList;
import java.util.List;

public class DatabaseEng{
    private MusicDatabase db;
    private SongDao songDao ;
    private WeightsDao wdaw;
    public  DatabaseEng(Context context){
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                db = Room.databaseBuilder(context, MusicDatabase.class, "Music_Adapter").build();

            }
        };
        Thread thread=new Thread(runnable);
        thread.run();
        songDao = db.songDao();
        wdaw=db.weightsDao();
    }

    int  songInListPosition(List<Song>  songs,String path){
        for(int i=0;i<songs.size();i++){
            if(songs.get(i).path.equals(path)){
                return i;
            }
        }
        return -1;
    }

    public List<Song> get_new(ArrayList<Music_files> mf,List<Song> songs){
        for(int i=0;i<mf.size();i++){
            int pos=songInListPosition(songs,mf.get(i).getPath());
            if(pos!=-1){
                if (i<songs.size())
                    songs.remove(i);
            }
        }
        return songs;
    }



    private void insertSongWeights(Song song,List<Song>  existing){
        for(int i=0;i<existing.size();i++){
            try {
                if(!song.path.equals(existing.get(i).path)) {
                    Weights w = new Weights();
                    w.path = song.path;
                    w.topath = existing.get(i).path;
                    w.weight = (float) 0.5;
                    wdaw.insert(w);
                    w = new Weights();
                    w.path = existing.get(i).path;
                    w.topath = song.path;
                    w.weight = (float) 0.5;
                    wdaw.insert(w);
                }
            }catch (Exception e){

            }
        }
    }

    private void insertSongs(List<Song> songs ){
        for(int i=0;i<songs.size();i++){
            try {
                songDao.insert(songs.get(i));
            }catch (Exception e){

            }
        }
    }

    private void insertSongsWeights(List<Song> songs,List<Song>  existing){
        for(int i=0;i<songs.size();i++){
            insertSongWeights(songs.get(i),existing);
        }
    }

    private void musicFilesToSongs(ArrayList<Music_files> mf, List<Song> songs){
        for(int i=0;i<mf.size();i++){
            Song song=new Song();
            song.path=mf.get(i).getPath();
            songs.add(song);
        }
    }

    public void fillDatabase(ArrayList<Music_files> mf){
        new Thread(()->{
            List<Song> oldList=songDao.getAll();
            if(oldList.size()==0){
                Log.e("fill","old is empty");
            }
            List<Song> newList=get_new(mf,oldList);
            if(newList.size()==0){
                musicFilesToSongs(mf, newList);
                Log.e("fill","new is empty");
            }
            insertSongs(newList);
            insertSongsWeights(newList,oldList);
        }).start();
    }

    List<Weights> weights;
    boolean gotWeight;
    public List<Weights> getWeightsForSong(String song){
       // new Thread(()->{
            weights=wdaw.getWeightsFor(song);
        //}).start();
        return weights;
    }

    public void printWeightsForSong(String song){
        new Thread(()->{
            List<Weights> weights=wdaw.getWeightsFor(song);
            for(int i=0;i<weights.size();i++){
                Log.e("weights_db",i+"from: "+weights.get(i).path+", to: "+weights.get(i).topath+", with: "+weights.get(i).weight);
            }
        }).start();
    }

    public void printSongsTable(){
        new Thread(()->{
            Log.e("songs_db","**************");
            List<Song> songs=songDao.getAll();
            for(int i=0;i<songs.size();i++){
                Log.e("songs_db",i+songs.get(i).path);
            }
        }).start();

    }

    public void printWeightsTable(){
        new Thread(()->{
            List<Weights> weights=wdaw.getAll();
            for(int i=0;i<weights.size();i++){
                Log.e("weights_db",i+"from: "+weights.get(i).path+", to: "+weights.get(i).topath+", with: "+weights.get(i).weight);
            }
        }).start();

    }
    void setWeight(Weights w){
        new Thread(()->{
            wdaw.updateWeights(w);

        }).start();
    }

    private String getName(ArrayList<Music_files> mf,String path){
        for(int i=0;i<mf.size();i++){
            if(mf.get(i).getPath().equals(path)){
                return mf.get(i).getAlbum();
            }
        }
        return null;
    }
    private String getartist(ArrayList<Music_files> mf,String path){
        for(int i=0;i<mf.size();i++){
            if(mf.get(i).getPath().equals(path)){
                return mf.get(i).getArtist();
            }
        }
        return null;
    }
    private byte[] getArtFor(String path){
        MediaMetadataRetriever retriever= new MediaMetadataRetriever();
        retriever.setDataSource(path);
        return retriever.getEmbeddedPicture();
    }
    public ArrayList<Album_files>getAlbums(ArrayList<Music_files> mf){
        ArrayList<Album_files> albumFiles =new ArrayList<>();
        ArrayList<String> existingAlbums= new ArrayList<>();
        for(int i = 0; i< mf.size(); i++){
            Album_files al=new Album_files();
            if (!existingAlbums.contains(mf.get(i).getAlbum())){
                al.setName(getName(mf,mf.get(i).getPath()));
                al.setArt(getArtFor(mf.get(i).getPath()));
                albumFiles.add(al);
                existingAlbums.add(mf.get(i).getAlbum());
            }
        }
        return albumFiles;
    }

    public ArrayList<Artists_files> getArtists(ArrayList<Music_files> mf){
        ArrayList<Artists_files> artistsFiles =new ArrayList<>();
        ArrayList<String> existingArtists= new ArrayList<>();
        for(int i = 0; i< mf.size(); i++) {
            Artists_files al = new Artists_files();
            if (!existingArtists.contains(mf.get(i).getArtist())){
                al.setArtist(getartist(mf, mf.get(i).getPath()));
                al.setArt(getArtFor(mf.get(i).getPath()));
                artistsFiles.add(al);
                existingArtists.add(mf.get(i).getArtist());
            }
        }
        return artistsFiles;
    }
    public ArrayList<Music_files> getSongsofArtist(ArrayList<Music_files> mf,String name){
        ArrayList<Music_files> result=new ArrayList<>();
        for(int i=0;i<mf.size();i++){
            if(mf.get(i).getArtist().equals(name)){
                result.add(mf.get(i));
            }
        }
        return result;
    }
    public ArrayList<Music_files> getSongsInAlbum(ArrayList<Music_files> mf,String name){
        ArrayList<Music_files> result=new ArrayList<>();
        for(int i=0;i<mf.size();i++){
            if(mf.get(i).getAlbum().equals(name)){
                result.add(mf.get(i));
            }
        }
        return result;
    }
}


