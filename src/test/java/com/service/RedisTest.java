package com.service;
import java.util.Date;/**
 * @author timess
 */

import com.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

/**
 * @className: RedisTest
 * @Version: 1.0
 * @description:
 */

@SpringBootTest
public class RedisTest {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void test(){
        ValueOperations valueOperations = redisTemplate.opsForValue();// 操作Redis字符串的对象

        //增
        valueOperations.set("timessString","test1");
        valueOperations.set("timessInt", 1);
        valueOperations.set("timessDouble", 3.0);
        User user = new User();
        user.setId(56L);
        user.setUsername("测试");
        user.setUserAccount("RedisTest");
        user.setProfile("");
        user.setAvatarUrl("");
        user.setGender(0);
        user.setUserPassword("12345678");
        user.setUserStatus(0);
        user.setIsDelete(0);
        user.setUserRole(0);
        user.setPlanetCode("2667");
        valueOperations.set("timessUser", user);

        //查操作
        String str = (String) valueOperations.get("timessString");
        Assertions.assertTrue("test1".equals(str));
        int i = (int)valueOperations.get("timessInt");
        Assertions.assertTrue(i == 1);
        double d = (double) valueOperations.get("timessDouble");
        Assertions.assertTrue(3.0 == d);
        User userTest = (User) valueOperations.get("timessUser");
        Assertions.assertTrue(user.equals(userTest));
        System.out.println(userTest);

        //删除操作
        redisTemplate.delete("timessInt");



    }
}
