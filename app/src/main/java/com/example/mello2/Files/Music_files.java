package com.example.mello2.Files;

import java.util.ArrayList;

public class Music_files extends ArrayList<Music_files> {
    private String titel;
    private String duration;
    private String artist;
    private String path;
    private String album;

    public Music_files(String titel, String duration, String artist, String path, String album) {
        this.titel = titel;
        this.duration = duration;
        if (artist.equals("<unknown>"))
            artist="Unknown Artist";
        this.artist = artist;
        this.path = path;
        this.album = album;
    }

    public Music_files() {
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
