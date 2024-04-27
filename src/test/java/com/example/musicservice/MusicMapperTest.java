package com.example.musicservice;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DBRider
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MusicMapperTest {

    @Autowired
    MusicMapper musicMapper;

    @Test
//    @Sql(scripts = {"classpath:/sqlannotation/delete-music.sql", "classpath:/sqlannotation/insert-music.sql"},
//            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
//    )
    @DataSet(value = "datasets/music.yml")
    @Transactional
    void すべての楽曲が取得できること() {
        List<Music> musicList = musicMapper.findAll();

        assertThat(musicList)
                .hasSize(6)
                .contains(
                        new Music(1, "jaded", "Aero Smith"),
                        new Music(2, "Runaway Baby", "Bruno Mars"),
                        new Music(3, "WHY", "Avril Lavigne"),
                        new Music(4, "It's My Life", "Bon Jovi"),
                        new Music(5, "You're Young", "Fun"),
                        new Music(6, "The Way I am", "Ana Johnson")
                );
    }

    @Test
    @DataSet(value = "datasets/music.yml")
    @Transactional
    void 引数に指定した文字列から始まる楽曲の情報が返されること() {
        List<Music> musicList = musicMapper.findByMusicStartingWith("j");

        assertThat(musicList)
                .hasSize(1)
                .contains(
                        new Music(1, "jaded", "Aero Smith")
                );
    }

    @Test
    @DataSet(value = "datasets/music.yml")
    @Transactional
    void 存在するIDを指定した場合に該当する楽曲が返されること() {
        Optional<Music> musicList = musicMapper.findById(1);

        assertThat(musicList)
                .isPresent()
                .hasValue(
                        new Music(1, "jaded", "Aero Smith")
                );
    }

    @Test
    @DataSet(value = "datasets/music.yml")
    @Transactional
    void 存在しない楽曲のIDを指定した場合に空のデータが返されること() {
        Optional<Music> musicList = musicMapper.findById(7);

        assertThat(musicList)
                .isEmpty();
    }

    @Test
    @ExpectedDataSet(value = "datasets/insertMusic.yml", ignoreCols = "id")
    @Transactional
    public void 新しい楽曲を登録する場合() {
        Music music = new Music("Cry", "Aero Smith");
        musicMapper.insert(music);

        Optional<Music> musicList = musicMapper.findById(music.getId());
        assertThat(musicList).isNotEmpty();
    }

    @Test
    @DataSet(value = "datasets/music.yml") // 初期データセットアップ
    @ExpectedDataSet(value = "datasets/updateMusic.yml", ignoreCols = "id")
    @Transactional
    public void 楽曲データを更新する場合() {
        // 更新するデータの準備
        Music music = new Music(1, "Angel", "Aero Smith");

        // 更新処理実行（戻り値を使わない）
        musicMapper.update(music);

        // 更新後のデータを確認
        Optional<Music> updatedMusic = musicMapper.findById(1);
        assertThat(updatedMusic).isPresent();
        assertThat(updatedMusic.get().getTitle()).isEqualTo("Angel");
        assertThat(updatedMusic.get().getArtist()).isEqualTo("Aero Smith");
    }

    @Test
    @DataSet(value = "datasets/music.yml") // 初期データセットアップ
    @ExpectedDataSet(value = "datasets/deleteMusic.yml", ignoreCols = "id")
    @Transactional
    void 楽曲データを削除する場合() {
        Music music = new Music(6, "The Way I am", "Ana Johnson");
        musicMapper.delete(music);

        Optional<Music> deletedMusic = musicMapper.findById(6);
        assertThat(deletedMusic).isEmpty();
    }
}
