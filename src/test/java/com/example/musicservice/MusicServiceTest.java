package com.example.musicservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MusicServiceTest {

    @InjectMocks
    MusicService musicService;

    @Mock
    MusicMapper musicMapper;

    @Test
    public void 存在する楽曲を全件取得する() throws MusicNotFoundException {

        List<Music> musicList = List.of(
                new Music(1, "jaded", "breaststroke"),
                new Music(2, "Runaway Baby", "Bruno Mars"));

        doReturn(musicList).when(musicMapper).findAll();
        List<Music> actual = musicService.findMusicStartingWith(null);
        assertThat(actual).isEqualTo(musicList);
        verify(musicMapper, times(1)).findAll();
    }

    @Test
    public void URLにクエリ文字列を指定した場合に指定したクエリ文字列の文字列から始まるタイトルの楽曲が返されること() throws MusicNotFoundException {

        List<Music> expectedMusic = List.of(new Music(1, "jaded", "Aero Smith"));

        doReturn(expectedMusic).when(musicMapper).findByMusicStartingWith("j");
        List<Music> actual = musicService.findMusicStartingWith("j");
        assertThat(actual).isEqualTo(expectedMusic);
        verify(musicMapper, times(1)).findByMusicStartingWith("j");
    }

    @Test
    public void 存在する楽曲のIDを指定した場合に正常に該当する音楽が返されること() throws MusicNotFoundException {
        doReturn(Optional.of(new Music(1, "jaded", "Aero Smith"))).when(musicMapper).findById(1);
        Music actual = musicService.findMusic(1);
        assertThat(actual).isEqualTo(new Music(1, "jaded", "Aero Smith"));
        verify(musicMapper, times(1)).findById(1);
    }

    //    @Test
//    public void 存在しない楽曲のIDを指定した場合にエラーが返されること() throws MusicNotFoundException {
//        doReturn(Optional.empty()).when(musicMapper).findById(99);
//        assertThrows(MusicNotFoundException.class, () -> {
//            musicService.findMusic(99);
//        });
//        verify(musicMapper, times(1)).findById(99);
//    }
    @Test
    public void 存在しない楽曲のIDを指定した場合にエラーが返されること() {
        doReturn(Optional.empty()).when(musicMapper).findById(99);
        assertThatThrownBy(() -> {
            musicService.findMusic(99);
        }).isInstanceOf(MusicNotFoundException.class);
        verify(musicMapper, times(1)).findById(99);
    }

    @Test
    public void 新しい楽曲を登録する場合() {

//        when(musicMapper.diplicatedMusic("Bohemian Rhapsody", "Queen")).thenReturn(Optional.empty());
        /**
         * ↑↓でもどちらでもテストはとおる。4/20
         */
        doReturn(Optional.empty()).when(musicMapper).diplicatedMusic("Bohemian Rhapsody", "Queen");
//        doNothing().when(musicMapper).diplicatedMusic("Bohemian Rhapsody", "Queen");
        // メソッドの実行
        Music actual = musicService.insert("Bohemian Rhapsody", "Queen");

        // 検証
        assertNotNull(actual);
        assertThat(actual).isEqualTo(new Music("Bohemian Rhapsody", "Queen"));
        /**
         * ↑↓どちらでもとおる。
         */
//        assertEquals("Bohemian Rhapsody", actual.getTitle());
//        assertEquals("Queen", actual.getArtist());
        verify(musicMapper, times(1)).insert(any(Music.class));
    }

    @Test
    public void 新しい楽曲を登録する時にタイトルの重複がある場合は例外処理が返されること() throws MusicDuplicatedException {

        doReturn(Optional.of(new Music("Bohemian Rhapsody", "Queen"))).when(musicMapper).diplicatedMusic("Bohemian Rhapsody", "Queen");

        // 検証
        assertThatThrownBy(() -> {
            musicService.insert("Bohemian Rhapsody", "Queen");
        }).isInstanceOf(MusicDuplicatedException.class);
        verify(musicMapper, times(1)).diplicatedMusic("Bohemian Rhapsody", "Queen");
    }

    @Test
    public void 楽曲データを更新する場合() {
        // 事前条件
        Music music = new Music(1, "Bohemian Rhapsody", "Queen");
        doReturn(Optional.of(music)).when(musicMapper).findById(1);
        doReturn(Optional.empty()).when(musicMapper).diplicatedMusic("Bohemian Rhapsody", "Queen");
        // メソッドの実行
        Music actual = musicService.update(1, "Bohemian Rhapsody", "Queen");
        // 検証
        assertNotNull(actual);
        assertThat(actual).isEqualTo(new Music(1, "Bohemian Rhapsody", "Queen"));
        verify(musicMapper).update(music);
    }

    @Test
    public void 楽曲を更新する時に指定したIDが見つからない場合() {

        doReturn(Optional.empty()).when(musicMapper).findById(99);

        assertThatThrownBy(() -> {
            musicService.update(99, "Bohemian Rhapsody", "Queen");
        }).isInstanceOf(MusicNotFoundException.class);
        verify(musicMapper, times(1)).findById(99);
    }

    @Test
    public void 楽曲を更新する時にタイトルが重複している場合() {
        // 更新しようとしている楽曲
        Music updatingMusic = new Music(1, "Bohemian Rhapsody", "Queen");
        doReturn(Optional.of(updatingMusic)).when(musicMapper).findById(1);

        // 重複していると判断される別の楽曲
        Music duplicatedMusic = new Music(2, "Bohemian Rhapsody", "Queen");// 重複楽曲は異なるIDを持つ
        doReturn(Optional.of(duplicatedMusic)).when(musicMapper).diplicatedMusic("Bohemian Rhapsody", "Queen");

        assertThatThrownBy(() -> {
            musicService.update(1, "Bohemian Rhapsody", "Queen");
        }).isInstanceOf(MusicDuplicatedException.class);
        verify(musicMapper, times(1)).findById(1);
        verify(musicMapper, times(1)).diplicatedMusic("Bohemian Rhapsody", "Queen");
    }

    @Test
    public void IDを指定して楽曲を削除する場合() {
        Music music = new Music(1, "Bohemian Rhapsody", "Queen");
        doReturn(Optional.of(music)).when(musicMapper).findById(1);

        Music actual = musicService.delete(1);

        assertThat(actual).isEqualTo(new Music(1, "Bohemian Rhapsody", "Queen"));
        verify(musicMapper, times(1)).findById(1);
    }

}
