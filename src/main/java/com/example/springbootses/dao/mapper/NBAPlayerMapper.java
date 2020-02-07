package com.example.springbootses.dao.mapper;

import com.example.springbootses.dao.domain.NBAPlayer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NBAPlayerMapper {

    @Select("select * from nba_player")
    List<NBAPlayer> selectAll();

}
