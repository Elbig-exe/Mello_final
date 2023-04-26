package com.example.mello2;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mello2.Weights;

import java.util.List;

@Dao
public interface WeightsDao {
    @Query("SELECT * FROM Weights")
    List<Weights> getAll();

    @Query("SELECT * FROM Weights WHERE path =:path")
    List<Weights> getWeightsFor(String path);

    @Update
    public void updateWeights(Weights w);

    @Insert
    void insert(Weights w);

    @Insert
    void insertAll(Weights... Weights);
}
