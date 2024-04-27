package com.example.musicservice;

import java.util.Objects;

/**
 * ミュージックテーブル用エンティティ
 */
public class Music {

    private Integer id;
    private String title;
    private String artist;

    public Music(Integer id, String title, String artist) {
        this.id = id;
        this.title = title;
        this.artist = artist;
    }

    /**
     * コンストラクタは2つ作成できる
     * データベースに登録する前にインスタンス化する時に使う
     * 　IDは自動採番されるので null でよい。
     *
     * @param title
     * @param artist
     */
    public Music(String title, String artist) {
        this.id = null;
        this.title = title;
        this.artist = artist;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    /**
     * 更新処理のためのsetter
     *
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Music music = (Music) o;
        return Objects.equals(id, music.id) &&
                Objects.equals(title, music.title) &&
                Objects.equals(artist, music.artist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, artist);
    }
}
