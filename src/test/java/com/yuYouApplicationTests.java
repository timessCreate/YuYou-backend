package com;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.mapper.UserMapper;
import com.model.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class yuYouApplicationTests {

    @Resource
    private UserMapper userMapper;

    @Test
    void testDigest(){
        String newPassword = DigestUtils.md5DigestAsHex(("abcd"+ "password").getBytes());
        System.out.println(newPassword);
    }
    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        //Assert.isTrue(5 == userList.size(), "");
        userList.forEach(System.out::println);
    }

}