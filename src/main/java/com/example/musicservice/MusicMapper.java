package com.example.musicservice;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MusicMapper {
        @Select("SELECT * FROM music")
        List<Music> findAll();
}
