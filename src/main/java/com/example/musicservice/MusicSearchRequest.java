package com.example.musicservice;

public class MusicSearchRequest {
   
    private String startsWith;
    private String endsWith;

    public MusicSearchRequest(String startsWith, String endsWith) {
        this.startsWith = startsWith;
        this.endsWith = endsWith;
    }

    public String getStartsWith() {
        return startsWith;
    }

    public String getEndsWith() {
        return endsWith;
    }
}
