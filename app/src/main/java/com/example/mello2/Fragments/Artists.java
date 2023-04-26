package com.example.mello2.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mello2.Adapters.Artist_adapter;
import com.example.mello2.R;

import static com.example.mello2.Activities.MainActivity.db;
import static com.example.mello2.Activities.MainActivity.music_files;

public class Artists extends Fragment {
    RecyclerView recyclerView;
    Artist_adapter artist_adapter;
    public Artists() {
        // Required empty public constructor
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_artists, container, false);
        recyclerView= view.findViewById(R.id.recycler_view_artist);
        recyclerView.setHasFixedSize(true);
        if (!(music_files.size()<1)){
            artist_adapter=new Artist_adapter(getContext(),db.getArtists(music_files));
            recyclerView.setAdapter(artist_adapter);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        }
        return view ;
    }
}