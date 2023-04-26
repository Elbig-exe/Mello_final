package com.example.mello2.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mello2.Adapters.Music_Adapter;
import com.example.mello2.R;

import static com.example.mello2.Activities.MainActivity.music_files;

public class Tracks extends Fragment {
    RecyclerView recyclerView;
    Music_Adapter music_adapter;

    public Tracks() {

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_tracks, container, false);
        recyclerView= view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        if (!(music_files.size()<1)){
            music_adapter=new Music_Adapter(getContext(),music_files);
            recyclerView.setAdapter(music_adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,
                    false));
        }
        return view ;
    }

}