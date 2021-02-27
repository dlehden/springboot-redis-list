package com.redis.list.controller;


import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("/api")

public class RedisZsetController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	   @Autowired
	    public StringRedisTemplate template;

	    public static final String SCORE_RANK = "score_rank";

	    @GetMapping("/show")
	    public void show() {
	        batchAdd();

	    }
	    /*
	     * score 단위로 리스트를 만들수 있음.
	     */
	    
	    @GetMapping("/batch")
	    public void batchAdd() {
	        Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
	        long startTime = System.currentTimeMillis();

	        for(int i=0; i<10; i++) {
	            DefaultTypedTuple<String> tuple = new DefaultTypedTuple<>("test" + i,1.0 +i);
	            //double 형식으로 score 를 넣어야함.
	            tuples.add(tuple);
	        }

	        long runTime = System.currentTimeMillis() - startTime;
	        logger.info("runTime: " + runTime);
	        template.opsForZSet().add(SCORE_RANK, tuples);
	    }
	    
	    @GetMapping("/top")
	    public void top10() {
	        Set<String> range = template.opsForZSet().reverseRange(SCORE_RANK, 0, 10);
	        logger.info("획득 한 순위 목록 :" + JSON.toJSONString(range));

	        Set<ZSetOperations.TypedTuple<String>> rangeWithScores = template.opsForZSet().reverseRangeWithScores(SCORE_RANK, 0, 10);
	        logger.info("획득 한 순위 및 점수 목록 :" + JSON.toJSONString(rangeWithScores));
	    }
	    
	    @GetMapping("/add")
	    public void add() {
	        template.opsForZSet().add(SCORE_RANK, "list", 36); // key, name , score
	    }
	    
}
