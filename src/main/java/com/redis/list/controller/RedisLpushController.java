package com.redis.list.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/lpush")

public class RedisLpushController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	   @Autowired
	    public StringRedisTemplate template;
	   
	   private final RedisTemplate<String,String> redisTemplate;
	    private final ListOperations<String, String> listOperations;
	    private final String REDIS_PREFIX_KEY = "articles:";
	    private final int ARTICLE_MAX_SIZE = 5;

	    public RedisLpushController(RedisTemplate<String, String> redisTemplate) {
	        this.redisTemplate = redisTemplate;
	        this.listOperations = redisTemplate.opsForList();
	    }

	    @GetMapping("/set")
	    public void show() {
	    	logger.info("실행");
	    	for(int i=0 ; i < 6; i++) {
	    		 setScore("abc","http:www.naver.com" + i);
	    	}
	       

	    }
	    /*
	     * redist lpush 를이용한  최근 방문 기록 
	     * key 값으론 로그인 id 로 ,   0 , max_size-1 (ex) 0 ,4 ) 5개 까지 
	     *  1,2,3,4,5 set 으로 들어갈시 redist lpush 로 들어가면 5,4,3,2,1 로 왼쪽부터 등록 
	     */
	    public void setScore(String nid, String Url){
	    	logger.info(Url);
	    	   //TODO:timestamp 사용하지 않음
	    	listOperations.leftPush(REDIS_PREFIX_KEY + nid, Url);  
	        listOperations.trim(REDIS_PREFIX_KEY + nid, 0, ARTICLE_MAX_SIZE - 1);

	    }
	    public void deleteById(String nid) {
	        redisTemplate.delete(REDIS_PREFIX_KEY + nid);
	    }
	    
	    public List<String> recentlyArticlesById(String nid) {
	        return listOperations.range(REDIS_PREFIX_KEY + nid, 0, 4);
	    }
}
