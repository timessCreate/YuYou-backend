package com.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author timess
 */
@SpringBootTest
@Slf4j
class RedissonConfigTest {
    @Resource
    private RedissonClient redissonClient;
     @Test
    void test(){
         // Redisson使用实例
         //list 和 Redisson的list对比
         List<String> list = new ArrayList<>();
         list.add("test1");
         System.out.println(list.get(0));
         list.remove(0);

         //数据存储在redis
         RList<String> rList = redissonClient.getList("key");
         rList.add("test1");
         String s = rList.get(0);
         System.out.println(s);
         rList.remove(0);
        ////////////////////////////////////////////////////////////////////////
         //map
         Map<String, String> map = new HashMap<>();
         map.put("timess","test1");
         System.out.println(map.get("timess"));

         RMap<String,String> rMap = redissonClient.getMap("mapTest");
         rMap.put("timess","Rtest");
         System.out.println(rMap.get("timess"));






    }
}