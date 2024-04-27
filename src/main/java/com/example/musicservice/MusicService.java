package com.example.musicservice;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 　ミュージックサービスに関する業務処理を担うサービスクラス
 */
@Service
public class MusicService {
    /**
     * コンストラクタ
     */
    private final MusicMapper musicMapper;

    /**
     * フィールド
     */
    public MusicService(MusicMapper musicMapper) {
        this.musicMapper = musicMapper;
    }

    /**
     * メソッド（一つの業務処理あたり一つのメソッド）
     */

//    public List<Music> findAll() {
//
//        return musicMapper.findAll();
//    }
//
//    public List<Music> findMusicStartingWith(String prefix) {
//        return musicMapper.findByMusicStartingWith(prefix);
//    }
    public List<Music> findMusicStartingWith(String prefix) {
        if (prefix != null) {
            return musicMapper.findByMusicStartingWith(prefix);
        } else {
            return musicMapper.findAll();
        }
    }

    public Music findMusic(Integer id) {
        Optional<Music> music = musicMapper.findById(id);
        if (music.isPresent()) {
            return music.get();
        } else {
            throw new MusicNotFoundException("Music with id " + id + " not found");
        }
    }

//    public Music insert(String title, String artist) {
//        Music music = new Music(title, artist);
//        musicMapper.insert(music);
//        return music;
//    }

    public Music insert(String title, String artist) {
        Music music = new Music(title, artist);
        if (!musicMapper.diplicatedMusic(title, artist).isEmpty()) {
            throw new MusicDuplicatedException("There is duplicated data!");
        }
        musicMapper.insert(music);
        return music;
    }

    /**
     * 更新処理
     * シンプルに music をreturnする。
     */
//    public Music update(Integer id, String title, String artist) {
//        Music music = new Music(id, title, artist);
//        musicMapper.update(music);
//        return music;
//    }

    /**
     * 更新処理
     * id がなければ、ステータスコード404を返す。重複があれば409を返す。
     */
    public Music update(Integer id, String title, String artist) {
        Optional<Music> optionalMusic = musicMapper.findById(id);
        Music music = optionalMusic.orElseThrow(() -> new MusicNotFoundException("music not found"));


        Optional<Music> existingMusicId = musicMapper.diplicatedMusic(title, artist);
//        if (!existingMusicId.isEmpty() && !existingMusicId.stream().anyMatch(l -> l.getId().equals(id))) {
//            throw new MusicDuplicatedException("There is duplicated data!");
//        }
        /**
         * 上記if文はこのような書き方でもOK 4/18
         */
//        if (existingMusicId.filter(l -> !l.getId().equals(id)).isPresent()) {
//            throw new MusicDuplicatedException("There is duplicated data!");
//        }
        /**
         * それかif文はこのような書き方もある。否定形は可読性が下がる。こっちの方が良い。
         */
        if (existingMusicId.isPresent() && existingMusicId.stream().noneMatch(l -> l.getId().equals(id))) {
            throw new MusicDuplicatedException("There is duplicated data!");
        }
        /**
         * 下記のコードでは不備がある2024/4/18
         */
//        if (music.getTitle().equals(title) && music.getArtist().equals(artist)) {
//            return music;
//        } else if (!musicMapper.diplicatedMusic(title, artist).isEmpty()) {
//            throw new MusicDuplicatedException("There is duplicated data!");
//        }
//        if (!musicMapper.diplicatedMusic(title, artist).isEmpty()) {
//            throw new MusicDuplicatedException("There is duplicated data!");
//        }

        music.setTitle(title);
        music.setArtist(artist);

        musicMapper.update(music);

        return music;
    }

    /**
     * 削除処理
     */
    public Music delete(Integer id) {
        Optional<Music> optionalMusic = musicMapper.findById(id);
        Music music = optionalMusic.orElseThrow(() -> new MusicNotFoundException("music not found"));
        musicMapper.delete(music);
        return music;
    }
}
