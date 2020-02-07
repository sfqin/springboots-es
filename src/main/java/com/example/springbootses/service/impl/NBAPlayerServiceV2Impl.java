package com.example.springbootses.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.springbootses.dao.domain.NBAPlayer;
import com.example.springbootses.service.NBAPlayerServiceV2;
import org.elasticsearch.common.settings.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class NBAPlayerServiceV2Impl implements NBAPlayerServiceV2 {

//    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public boolean createIndex(String index) {

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
//        json_country.put("analyzer","smartcn");
        properties.put("country",json_country);
        String mappingStr = jsonObject.toJSONString();

        Settings.Builder mapping = Settings.builder().put("mapping", mappingStr);

        JSONObject settings = new JSONObject();
        JSONObject analysis = new JSONObject();
        settings.put("analysis",analysis);
        JSONObject analyzerjson = new JSONObject();
        analysis.put("analyzer",analyzerjson);
        JSONObject my_analyzer = new JSONObject();
        analyzerjson.put("my_analyzer",my_analyzer);
        my_analyzer.put("type","smartcn");
        Settings.Builder setting = Settings.builder().put("settings", settings.toJSONString());

        elasticsearchTemplate.createIndex(index, setting);
        return elasticsearchTemplate.putMapping(index,"_doc",jsonObject);
    }

    @Override
    public boolean deleteIndex(String index) {
        return elasticsearchTemplate.deleteIndex(index);
    }

    @Override
    public boolean addDoc(String index, String docId, NBAPlayer nbaPlayer) {
        IndexQuery indexQuery = new IndexQuery();
        indexQuery.setIndexName(index);
        indexQuery.setType("_doc");
        indexQuery.setSource(JSONObject.toJSON(nbaPlayer).toString());
        indexQuery.setId(docId);
        List<IndexQuery> queryList = new ArrayList<>();
        queryList.add(indexQuery);

        elasticsearchTemplate.bulkIndex(queryList);
        queryList.clear();

        return true;
    }

    @Override
    public boolean deleteDoc(String index, String docId) {
        String doc = elasticsearchTemplate.delete(index, "_doc", docId);
        System.out.println(doc);
        return doc != null;
    }

    @Override
    public boolean deleteDocByQuery(String index) {
        return false;
    }

    @Override
    public List<NBAPlayer> queryTerm(String index, String key, String value) {
        return null;
    }

    @Override
    public List<NBAPlayer> queryMatch(String index, String key, String value) {
        return null;
    }

    @Override
    public List<NBAPlayer> queryPrefix(String index, String key, String value) {
        return null;
    }
}
