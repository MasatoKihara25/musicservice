package com.example.musicservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(MusicController.class)
class MusicControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MusicService musicService;

    @Test
    public void 引数に指定した文字列を含む楽曲の情報が返されること() throws Exception {

        List<Music> expectedMusic = List.of(new Music(1, "jaded", "Aero Smith"));

        doReturn(expectedMusic).when(musicService).findMusicStartingWith("j");

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(expectedMusic);
        mockMvc.perform(get("/music?startsWith=j"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
        verify(musicService, times(1)).findMusicStartingWith("j");
    }

    @Test
    public void 存在する楽曲を全件取得できること() throws Exception {

        List<Music> expectedMusic = List.of(
                new Music(1, "jaded", "Aero Smith"),
                new Music(2, "cry", "Aero Smith"));

        doReturn(expectedMusic).when(musicService).findMusicStartingWith(null);

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(expectedMusic);
        mockMvc.perform(get("/music?startsWith"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
        verify(musicService, times(1)).findMusicStartingWith(null);
    }

    @Test
    public void 存在する楽曲のIDを指定した場合に正常に該当する音楽が返されること() throws Exception {

        Music expectedMusic = new Music(1, "jaded", "Aero Smith");
        doReturn(expectedMusic).when(musicService).findMusic(1);

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(expectedMusic);
        mockMvc.perform(get("/music/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
        verify(musicService, times(1)).findMusic(1);
    }

    @Test
    public void 新しい楽曲を登録できること() throws Exception {

        Music insertMusic = new Music(1, "jaded", "Aero Smith");
        doReturn(insertMusic).when(musicService).insert("jaded", "Aero Smith");

        ObjectMapper objectMapper = new ObjectMapper();
        String musicJson = "{\"message\":\"music created\"}";

        mockMvc.perform(post("/music")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(insertMusic)))
                .andExpect(status().isCreated())
                .andExpect(content().json(musicJson));
        verify(musicService, times(1)).insert("jaded", "Aero Smith");
    }

    @Test
    public void 楽曲データを更新できること() throws Exception {

        Music updateMusic = new Music(1, "jaded", "Aero Smith");
        doReturn(updateMusic).when(musicService).update(1, "jaded", "Aero Smith");

        ObjectMapper objectMapper = new ObjectMapper();
        String musicJson = "{\"message\":\"music updated\"}";

        mockMvc.perform(patch("/music/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateMusic)))
                .andExpect(status().isOk())
                .andExpect(content().json(musicJson));
        verify(musicService, times(1)).update(1, "jaded", "Aero Smith");
    }

    @Test
    public void IDを指定して楽曲を削除できること() throws Exception {

        Music deleteMusic = new Music(1, "jaded", "Aero Smith");
        doReturn(deleteMusic).when(musicService).delete(1);

        ObjectMapper objectMapper = new ObjectMapper();
        String musicJson = "{\"message\":\"music delete\"}";

        mockMvc.perform(delete("/music/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteMusic)))
                .andExpect(status().isOk())
                .andExpect(content().json(musicJson));
        verify(musicService, times(1)).delete(1);
    }

    
}
