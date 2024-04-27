package com.example.musicservice;

import jakarta.validation.constraints.NotBlank;

/**
 * ミュージック情報登録時に使うリクエストパラメータ
 */
public class MusicRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String artist;

    public MusicRequest(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }
}
