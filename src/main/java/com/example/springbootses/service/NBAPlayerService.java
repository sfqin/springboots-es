package com.example.springbootses.service;

import com.example.springbootses.dao.domain.NBAPlayer;

import java.util.List;

/**
 * 原生api 操作es
 */
public interface NBAPlayerService {

    List<NBAPlayer> getAll();


    boolean importData();

    /**
     * 创建索引
     * @param indexId
     * @return
     */
    boolean addIndex(String indexId);

    /**
     * 删除索引
     * @param indexId
     * @return
     */
    boolean deleteIndex(String indexId);

    /**
     * 新增一个文档
     * @param indexId 往哪个索引里面加文档
     * @param docId 文档id 不指定es可自动生成
     * @param nbaPlayer 文档的内容(包装成实体类)
     * @return
     */
    boolean addDoc(String indexId,String docId, NBAPlayer nbaPlayer);

    /**
     * 根据文档id删除文档
     * @param indexId
     * @param docId
     * @return
     */
    boolean deleteDoc(String indexId,String docId);

    /**
     * 根据查询条件进行删除
     * @return
     */
    boolean deleteByQuery(String index);

    /**
     * terms 词条查询 关键词需为 非text类型
     * @return
     */
    List<NBAPlayer> termQuery(String key,String value);

    /**
     * text 全文查找
     * @param key
     * @param value
     * @return
     */
    List<NBAPlayer> textQuery(String key,String value);

    /**
     * text 前缀查询
     * @param key
     * @param value
     * @return
     */
    List<NBAPlayer> prefixQuery(String key,String value);
}
