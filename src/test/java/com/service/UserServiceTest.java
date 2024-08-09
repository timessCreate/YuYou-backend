package com.service;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


/**
 * @author timess
 * 用户服务测试
 */
@SpringBootTest
class UserServiceTest {
    @Resource
    private UserService userService;

//    @Test
//    public void testSearchUserByTags(){
//        List<String> list = Arrays.asList("java","python");
//        List<User> userList = userService.searchUserByTags(list);
//        Assertions.assertNotNull(userList);
//    }
    }

