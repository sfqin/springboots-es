package com.example.springbootses;

import com.example.springbootses.dao.domain.NBAPlayer;
import com.example.springbootses.service.NBAPlayerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootsEsApplicationTests {

    @Autowired
    private NBAPlayerService nbaPlayerService;

    @Test
    public void test_add_doc() {

        NBAPlayer nbaPlayer = new NBAPlayer();
        nbaPlayer.setId(1);
        nbaPlayer.setDisplayName("唐僧");

        boolean my_test = nbaPlayerService.addDoc("test_my_index",1+"", nbaPlayer);
    }

    @Test
    public void test_import(){
        boolean b = nbaPlayerService.importData();
        System.out.println("同步结果 => " + b);
    }

    @Test
    public void test_create_index(){
        System.out.println("创建索引结果 => " + nbaPlayerService.addIndex("new-index"));
    }

    @Test
    public void delete_index(){
        System.out.println("删除索引结果 => " + nbaPlayerService.deleteIndex("bbb-index"));
    }

    @Test
    public void delete_doc(){
        nbaPlayerService.deleteDoc("nba","sh16GnAB_KqgmKZmwWeb");
    }

    @Test
    public void delete_doc2(){
        nbaPlayerService.deleteByQuery("test_my_index");
    }

}
