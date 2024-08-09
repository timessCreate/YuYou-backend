package com.script;

import com.model.domain.User;
import com.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author timess
 */
@SpringBootTest
class InsertUsersTest {

    @Resource
    private UserService userService;

    /**
     * 批量插入用户
     */
    @Test
    public void doInsertUsers() {
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        int j = 0;
//        List<CompletableFuture<Void>> futureList = new ArrayList<>();
//        //插入十万条数据，分成十组,每一组放置到一个线程池
//        for (int i = 0; i < 10; i++) {
//            List<User> userList = Collections.synchronizedList(new ArrayList<>());
//            while (true) {
//                j++;
//                User user = new User();
//                user.setUsername("ikun");
//                user.setUserAccount("testAccount");
//                user.setAvatarUrl("https://tse3-mm.cn.bing.net/th/id/OIP-C.8kz364SloNboLZUeKWOKUQHaHa?rs=1&pid=ImgDetMain");
//                user.setGender(0);
//                user.setUserPassword("12345678");
//                user.setPhone("666");
//                user.setEmail("888");
//                user.setUserStatus(0);
//                user.setUserRole(0);
//                user.setPlanetCode("1111111");
//                userList.add(user);
//                if(j % 10000 == 0){
//                    break;
//                }
//            }
//            //将数据分组数据加入异步执行
//            CompletableFuture<Void> future = CompletableFuture.runAsync(()->{
//                System.out.println("threadName:" + Thread.currentThread().getName());
//                userService.saveBatch(userList, 10000);
//            });
//            futureList.add(future);
//        }
//        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();
//
//        stopWatch.stop();
//        System.out.println(stopWatch.getTotalTimeMillis());
    }
}