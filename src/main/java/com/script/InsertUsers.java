package com.script;
import java.util.Date;/**
 * @author timess
 */

import com.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @className: InsertUsers
 * @Version: 1.0
 * @description:
 */

@Component
public class InsertUsers {
    @Resource
    private UserService userService;

    /**
     * 批量插入用户
     */
////    @Scheduled(fixedDelay = 5000)
////    public void doInsertUsers(){
////        final int INSERT_NUM = 10000000; //一千万条数据
////        for (int i = 0; i < INSERT_NUM; i++) {
////            User user = new User();
////            user.setUsername("");
////            user.setUserAccount("");
////            user.setAvatarUrl("");
////            user.setGender(0);
////            user.setUserPassword("");
////            user.setPhone("");
////            user.setEmail("");
////            user.setUserStatus(0);
////            user.setUserRole(0);
////            user.setPlanetCode("");
////
////
////        }
//        //userService.saveBatch();
//    }

}
