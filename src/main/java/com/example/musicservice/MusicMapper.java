package com.example.musicservice;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Optional;

/**
 * ミュージックテーブル用のマッパー
 */
@Mapper
public interface MusicMapper {
    @Select("SELECT * FROM music")
    List<Music> findAll();

//    @Select("SELECT * FROM music WHERE title LIKE CONCAT(#{prefix}, '%') AND title LIKE CONCAT('%',#{suffix})")
//    List<Music> findMusic(String prefix, String suffix);

    @Select("SELECT * FROM music WHERE title LIKE CONCAT(#{prefix}, '%')")
    List<Music> findByMusicStartingWith(String prefix);

    @Select("SELECT * FROM music WHERE id = #{id}")
    Optional<Music> findById(Integer id);

    /**
     * 登録処理
     *
     * @param music
     */
    @Insert("INSERT INTO music (title, artist) VALUES (#{title}, #{artist})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Music music);

    /**
     * 重複確認用
     *
     * @param title
     * @param artist
     * @return
     */
    @Select("SELECT * FROM music WHERE title = #{title} AND artist = #{artist}")
    Optional<Music> diplicatedMusic(String title, String artist);

    /**
     * 更新処理
     *
     * @return
     */
    @Update("UPDATE music SET title = #{title}, artist = #{artist} WHERE id = #{id}")
    void update(Music music);

    /**
     * 削除処理
     */
    @Delete("DELETE FROM music WHERE id = #{id}")
    void delete(Music music);
}
