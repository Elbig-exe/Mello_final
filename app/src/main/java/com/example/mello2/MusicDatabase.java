package com.example.mello2;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.mello2.Song;
import com.example.mello2.SongDao;
import com.example.mello2.Weights;
import com.example.mello2.WeightsDao;


@Database(entities = {Song.class, Weights.class}, version = 2)
public abstract class MusicDatabase extends RoomDatabase {
    public abstract SongDao songDao();
    public abstract WeightsDao weightsDao();
}


