package com.example.mello2.Files;

public class Album_files {
    byte[] art;
    String name;
    public void setName(String name) {
        this.name = name;
    }

    public void setArt(byte[] art) {
        this.art = art;
    }

    public byte[] getArt(){
        return art;
    }
    public String getName(){
        return name;
    }

    @Override
    public boolean equals(Object o) {
        Album_files al=(Album_files)o;
        return name.equals(al.name);
    }
}
