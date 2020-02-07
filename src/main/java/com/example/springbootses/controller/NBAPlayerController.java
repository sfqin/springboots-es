package com.example.springbootses.controller;

import com.example.springbootses.service.NBAPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/player")
public class NBAPlayerController {

    @Autowired
    private NBAPlayerService nbaPlayerService;

    @GetMapping("all")
    public Object getAll(){
        return nbaPlayerService.getAll();
    }

    @GetMapping("term")
    public Object queryTerm(String key,String value){
        System.out.println("term ==> key,value => " + key + "," + value);
       return nbaPlayerService.termQuery(key,value);
    }


    @GetMapping("match")
    public Object queryMatch(String key,String value){
        System.out.println("match ==> key,value => " + key + "," + value);
        return nbaPlayerService.textQuery(key,value);
    }

    @GetMapping("prefix")
    public Object queryPrefix(String key,String value){
        System.out.println("prefix ==> key,value => " + key + "," + value);
        return nbaPlayerService.prefixQuery(key,value);
    }
}
