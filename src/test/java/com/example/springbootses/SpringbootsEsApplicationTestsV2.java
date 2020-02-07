package com.example.springbootses;

import com.example.springbootses.dao.domain.NBAPlayer;
import com.example.springbootses.service.NBAPlayerService;
import com.example.springbootses.service.NBAPlayerServiceV2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootsEsApplicationTestsV2 {

    @Autowired
    private NBAPlayerServiceV2 nbaPlayerService;

    @Test
    public void test_add_doc() {

        NBAPlayer nbaPlayer = new NBAPlayer();
        nbaPlayer.setId(3);
        nbaPlayer.setDisplayName("孙悟空");


        System.out.println("新增文档结果 => " + nbaPlayerService.addDoc("aaa-index",3+"", nbaPlayer));
    }


    @Test
    public void test_create_index(){
        System.out.println("创建索引结果 => " + nbaPlayerService.createIndex("ccc-index"));
    }

    @Test
    public void delete_index(){
        System.out.println("删除索引结果 => " + nbaPlayerService.deleteIndex("aaa-index"));
    }

    @Test
    public void delete_doc(){
        nbaPlayerService.deleteDoc("aaa-index","FHCKHnABTIUWSpM_6zGp");
    }

    @Test
    public void delete_doc2(){
        nbaPlayerService.deleteIndex("test_my_index");
    }

}
