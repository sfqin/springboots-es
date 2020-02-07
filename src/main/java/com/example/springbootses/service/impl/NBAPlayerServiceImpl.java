package com.example.springbootses.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.springbootses.dao.domain.NBAPlayer;
import com.example.springbootses.dao.mapper.NBAPlayerMapper;
import com.example.springbootses.service.NBAPlayerService;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class NBAPlayerServiceImpl implements NBAPlayerService {

    @Autowired
    private NBAPlayerMapper nbaPlayerMapper;

    @Autowired
    private RestHighLevelClient restEsClient;

    private static final String INDEX_ID = "nba";

    @Override
    public List<NBAPlayer> getAll() {
        return nbaPlayerMapper.selectAll();
    }

    @Override
    public boolean importData() {
        List<NBAPlayer> nbaPlayers = nbaPlayerMapper.selectAll();
        if(!CollectionUtils.isEmpty(nbaPlayers)){
            nbaPlayers.forEach(nbaPlayer -> addDoc("aa_index",null,nbaPlayer));
            return true;
        }
        return false;
    }

    @Override
    public boolean addIndex(String indexId) {

        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexId);
        createIndexRequest.settings(Settings.builder()
                .put("index.number_of_shards",1)
                .put("index.number_of_replicas",1)
                .build());

        JSONObject jsonObject = new JSONObject();
        JSONObject properties = new JSONObject();
        jsonObject.put("properties",properties);

        JSONObject json_id = new JSONObject();
        json_id.put("type","long");
        properties.put("id",json_id);
        JSONObject json_name = new JSONObject();
        json_name.put("type","text");
        properties.put("displayName",json_name);
        JSONObject json_country = new JSONObject();
        json_country.put("type","keyword");
        properties.put("country",json_country);
        String mappingStr = jsonObject.toJSONString();
        createIndexRequest.mapping(mappingStr, XContentType.JSON);
        try {
            CreateIndexResponse createIndexResponse = restEsClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            System.out.println(JSONObject.toJSON(createIndexResponse));
            return createIndexResponse.isAcknowledged();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteIndex(String indexId) {

        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexId);

        try {
            AcknowledgedResponse delete = restEsClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
            System.out.println(JSONObject.toJSON(delete));
            return delete.isAcknowledged();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean addDoc(String indexId,String docId, NBAPlayer nbaPlayer) {
        IndexRequest indexRequest = new IndexRequest(indexId).id(docId).source(beanToMap(nbaPlayer));
        try {
            IndexResponse index = restEsClient.index(indexRequest, RequestOptions.DEFAULT);
            System.out.println(index);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteDoc(String indexId, String docId) {
        DeleteRequest deleteRequest = new DeleteRequest(indexId,"_doc",docId);
        try {
            DeleteResponse delete = restEsClient.delete(deleteRequest, RequestOptions.DEFAULT);
            System.out.println(delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteByQuery(String index) {
        DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest(index);
        deleteByQueryRequest.setSize(1000);
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("displayName","猪八戒");
        deleteByQueryRequest.setQuery(queryBuilder);
        try {
            BulkByScrollResponse bulkByScrollResponse = restEsClient.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
            System.out.println(bulkByScrollResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<NBAPlayer> termQuery(String key, String value) {

        SearchRequest searchRequest = new SearchRequest("aa_index");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery(key,value));
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(1000);
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse search = restEsClient.search(searchRequest, RequestOptions.DEFAULT);
            System.out.println(search);

            SearchHit[] hits = search.getHits().getHits();

            List<NBAPlayer> result = new LinkedList<>();

            if(hits != null && hits.length > 0){
                for(int i=0; i<hits.length; i++){
                    NBAPlayer player = JSONObject.parseObject(hits[i].getSourceAsString(),NBAPlayer.class);
                    result.add(player);
                }
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public List<NBAPlayer> textQuery(String key, String value) {

        SearchRequest searchRequest = new SearchRequest(INDEX_ID);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery(key,value));
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(1000);
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse search = restEsClient.search(searchRequest, RequestOptions.DEFAULT);
            System.out.println(search);

            SearchHit[] hits = search.getHits().getHits();

            List<NBAPlayer> result = new LinkedList<>();

            if(hits != null && hits.length > 0){
                for(int i=0; i<hits.length; i++){
                    NBAPlayer player = JSONObject.parseObject(hits[i].getSourceAsString(),NBAPlayer.class);
                    result.add(player);
                }
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public List<NBAPlayer> prefixQuery(String key, String value) {

        SearchRequest searchRequest = new SearchRequest(INDEX_ID);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.prefixQuery(key,value));
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(1000);
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse search = restEsClient.search(searchRequest, RequestOptions.DEFAULT);
            System.out.println(search);

            SearchHit[] hits = search.getHits().getHits();
            List<NBAPlayer> result = new LinkedList<>();
            if(hits != null && hits.length > 0){
                for(int i=0; i<hits.length; i++){
                    NBAPlayer player = JSONObject.parseObject(hits[i].getSourceAsString(),NBAPlayer.class);
                    result.add(player);
                }
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = new HashMap<>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                if(beanMap.get(key) != null)
                    map.put(key + "", beanMap.get(key));
            }
        }
        return map;
    }
}
