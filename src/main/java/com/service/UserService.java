package com.service;

import com.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author timess
* @description 针对表【user】的数据库操作Service
* @createDate 2024-07-03 14:51:03
*/
public interface UserService extends IService<User> {

    /**
     * 用户注释
     * @param userAccount 用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return  新用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword,String planetCode);


    /**
     * 用户登录
     * @param userAccount 用户账户
     * @param userPassword  用户密码
     * @return  返回脱敏后的信息
     */

    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户注销
     * 登录逻辑：登录时记录了用户对应的登录态，将其存到了服务器上（springboot 框架使用tomcat记录）
     *
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);

    /**
     *
     * @param originalUser 原始用户类
     * @return   返回安全处理后的user类
     */
    User getSafetyUser(User originalUser);

    /**
     * 根据标签搜索用户
     * @param tagNameList
     * @return
     */
    List<User> searchUserByTags(List<String> tagNameList);

    /**
     * 更新用户信息
     *
     * @param user      用户
     * @param loginUser
     * @return 返回值，如果为-1 ，则更新失败
     */
    int updateUser(User user, User loginUser);

    /**
     * 获取当前登录用户信息
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    boolean isAdmin(HttpServletRequest request);

    boolean isAdmin(User loginUser);

    /**
     * 获取推荐用户
     * @param num
     * @param loginUser
     * @return
     */
    List<User> matchUsers(long num, User loginUser);
}
