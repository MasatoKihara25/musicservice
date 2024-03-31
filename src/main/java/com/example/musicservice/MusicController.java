package com.example.musicservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MusicController {

    private MusicMapper musicMapper;

    public MusicController(MusicMapper musicMapper) {
        this.musicMapper = musicMapper;
    }

    @GetMapping("/music")
    public List<Music> getMusic(){
        List<Music> music = musicMapper.findAll();
        return music;

   }
}
