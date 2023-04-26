package com.example.mello2.Files;

public class Artists_files {
        byte[] art;
        String artist;

    public void setArt(byte[] art) {
        this.art = art;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtist() {
            return artist;
        }
        public byte[] getArt() {
            return art;
        }
        @Override
        public boolean equals(Object o) {
            Artists_files al = (Artists_files) o;
            return artist.equals(al.artist);
        }

}
