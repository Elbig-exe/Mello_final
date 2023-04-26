package com.example.mello2;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;


@Entity(foreignKeys = {@ForeignKey(entity = Song.class,
        parentColumns = "path",
        childColumns = "topath",
        onDelete = ForeignKey.CASCADE)
},primaryKeys = {"path","topath"})

public class Weights{
    @NonNull
    public String path;
    public float weight;
    @NonNull
    public String topath;
}
