package com.example.springbootses.service;

import com.example.springbootses.dao.domain.NBAPlayer;

import java.util.List;

/**
 * 使用 esTemplate 操作（由于版本冲突，实现作废）
 */
public interface NBAPlayerServiceV2 {


    boolean createIndex(String index);

    boolean deleteIndex(String index);

    boolean addDoc(String index, String docId, NBAPlayer nbaPlayer);

    boolean deleteDoc(String index,String docId);

    boolean deleteDocByQuery(String index);

    List<NBAPlayer> queryTerm(String index, String key, String value);

    List<NBAPlayer> queryMatch(String index,String key,String value);

    List<NBAPlayer> queryPrefix(String index,String key,String value);
}
