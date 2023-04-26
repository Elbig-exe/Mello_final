package com.example.mello2;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SongDao {
    @Query("SELECT * FROM Song")
    List<Song> getAll();

    @Insert
    void insertAll(Song... songs);

    @Insert
    void insert(Song song);

    @Delete
    void delete(Song song);
}