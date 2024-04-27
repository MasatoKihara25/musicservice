package com.example.musicservice;

public class MusicDuplicatedException extends RuntimeException {

    public MusicDuplicatedException(String message) {
        super(message);
    }
}
