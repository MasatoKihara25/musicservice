package integrationtest;

import com.example.musicservice.Music;
import com.example.musicservice.MusicserviceApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest(classes = MusicserviceApplication.class)
@AutoConfigureMockMvc
@DBRider
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MusicRestApiIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DataSet(value = "datasets/music.yml")
    @Transactional
    void すべての楽曲が取得できること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/music"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        [
                            {
                                "id": 1,
                                "title": "jaded",
                                "artist": "Aero Smith"
                            },
                            {
                                "id": 2,
                                "title": "Runaway Baby",
                                "artist": "Bruno Mars"
                            },
                            {
                                "id": 3,
                                "title": "WHY",
                                "artist": "Avril Lavigne"
                            },
                            {
                                "id": 4,
                                "title": "It's My Life",
                                "artist": "Bon Jovi"
                            },
                            {
                                "id": 5,
                                "title": "You're Young",
                                "artist": "Fun"
                            },
                            {
                                "id": 6,
                                "title": "The Way I am",
                                "artist": "Ana Johnson"
                            }
                        ]"""));

    }

    @Test
    @DataSet(value = "datasets/music.yml")
    @Transactional
    void 引数に指定した文字列を始まる楽曲の情報が返されること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/music?startsWith=j"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        [
                            {
                                "id": 1,
                                "title": "jaded",
                                "artist": "Aero Smith"
                            }
                        ]
                        """));
    }

    @Test
    @DataSet(value = "datasets/music.yml")
    @Transactional
    void 引数に指定した文字を空にした場合に全ての楽曲の情報が返されること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/music?startsWith="))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        [
                            {
                                "id": 1,
                                "title": "jaded",
                                "artist": "Aero Smith"
                            },
                            {
                                "id": 2,
                                "title": "Runaway Baby",
                                "artist": "Bruno Mars"
                            },
                            {
                                "id": 3,
                                "title": "WHY",
                                "artist": "Avril Lavigne"
                            },
                            {
                                "id": 4,
                                "title": "It's My Life",
                                "artist": "Bon Jovi"
                            },
                            {
                                "id": 5,
                                "title": "You're Young",
                                "artist": "Fun"
                            },
                            {
                                "id": 6,
                                "title": "The Way I am",
                                "artist": "Ana Johnson"
                            }
                        ]
                        """));
    }

    @Test
    @DataSet(value = "datasets/music.yml")
    @Transactional
    void 引数に指定した文字列から始まる楽曲がない場合に空文字が返されること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/music?startsWith=k"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        []
                        """));
    }

    @Test
    @DataSet(value = "datasets/music.yml")
    @Transactional
    void 存在する楽曲のIDを指定した場合に正常に該当する音楽が返されること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/music/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                             "id": 1,
                             "title": "jaded",
                             "artist": "Aero Smith"
                        }
                        """));
    }

    @Test
    @DataSet(value = "datasets/music.yml")
    @Transactional
    void 存在しない楽曲のIDを指定した場合にエラーが返されること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/music/7"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Music with id 7 not found"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", matchesPattern(
//                        "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{9}\\+\\d{2}:\\d{2}\\[[A-Za-z/]+\\]$"
//                )))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Not Found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/music/7"));
    }

    @Test
    @DataSet(value = "datasets/music.yml")
    @ExpectedDataSet(value = "datasets/insertMusic.yml", ignoreCols = "id")
    @Transactional
    void 新しい楽曲を登録する場合() throws Exception {
        var music = new Music("Cry", "Aero Smith");
        var objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post("/music")
                        .content(objectMapper.writeValueAsString(music))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                             "message": "music created"
                        }
                        """));
    }

    @Test
    @DataSet(value = "datasets/music.yml")
    @Transactional
    void 新しい楽曲を登録する時にタイトルの重複がある場合は例外処理が返されること() throws Exception {
        var music = new Music("jaded", "Aero Smith");
        var objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post("/music")
                        .content(objectMapper.writeValueAsString(music))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("409"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("There is duplicated data!"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", matchesPattern(
//                        "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{9}\\+\\d{2}:\\d{2}\\[[A-Za-z/]+\\]$"
//                )))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/music"));
    }

    @Test
    @DataSet(value = "datasets/music.yml")
    @ExpectedDataSet(value = "datasets/updateMusic.yml", ignoreCols = "id")
    @Transactional
    void 既存の楽曲を更新する場合() throws Exception {
        var music = new Music("Angel", "Aero Smith");
        var objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.patch("/music/1")
                        .content(objectMapper.writeValueAsString(music))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                             "message": "music updated"
                         }
                        """));
    }

    @Test
    @DataSet(value = "datasets/music.yml")
    @Transactional
    void 既存の楽曲を更新する時に指定したIDが存在しない場合() throws Exception {
        var music = new Music("Angel", "Aero Smith");
        var objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.patch("/music/7")
                        .content(objectMapper.writeValueAsString(music))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("music not found"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", matchesPattern(
//                        "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{9}\\+\\d{2}:\\d{2}\\[[A-Za-z/]+\\]$"
//                )))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Not Found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/music/7"));
    }

    @Test
    @DataSet(value = "datasets/music.yml")
    @Transactional
    void 既存の楽曲を更新する時にタイトルの重複がある場合() throws Exception {
        var music = new Music("jaded", "Aero Smith");
        var objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.patch("/music/2")
                        .content(objectMapper.writeValueAsString(music))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("409"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("There is duplicated data!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/music/2"));
    }

    @Test
    @DataSet(value = "datasets/music.yml")
    @ExpectedDataSet(value = "datasets/deleteMusic.yml", ignoreCols = "id")
    @Transactional
    void 楽曲データを削除する場合() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/music/6"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                             "message": "music delete"
                         }
                        """));
    }

    @Test
    @DataSet(value = "datasets/music.yml")
    @Transactional
    void 既存のルアーデータを削除する時に指定したIDが存在しない場合に例外が返されること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/music/7"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("music not found"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", matchesPattern(
//                        "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{9}\\+\\d{2}:\\d{2}\\[[A-Za-z/]+\\]$"
//                )))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Not Found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/music/7"));
    }
}
