package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.BaseResponse;
import com.common.ErrorCode;
import com.common.ResultUtils;
import com.dto.TeamQuery;
import com.exception.BusinessException;
import com.model.domain.User;
import com.model.domain.UserTeam;
import com.model.request.UserLoginRequest;
import com.model.request.UserRegisterRequest;
import com.model.vo.TeamUserVO;
import com.model.vo.UserVO;
import com.service.TeamService;
import com.service.UserService;
import com.service.UserTeamService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.constant.UserConstant.ADMIN_ROLE;
import static com.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author timess
 * 用户接口
 */
@RestController
@RequestMapping("/user")
@CrossOrigin({"http://localhost:3000"})
@Slf4j
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private TeamService teamService;
    @Resource
    private UserTeamService userTeamService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        // 校验
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();

        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            return null;
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }


    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    //TODO 退出登录
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @PostMapping("/update")
    public BaseResponse<Integer> updateUser(@RequestBody User user, HttpServletRequest request) {
        //1.校验参数是否为空
        if (user == null) {
            //抛出请求参数错误
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //TODO : 如何user对象除了id外，其他属性都为null,则抛出异常且返回更新失败
        //否则前端传输过来，### SQL: UPDATE user    WHERE id=?  AND isDelete=0
        //拼接出来的查询语句有问题，或者修改前端即可

        //2.获取当前登录用户信息
        User loginUser = userService.getLoginUser(request);

        Integer result = userService.updateUser(user, loginUser);

        return ResultUtils.success(result);
    }

    /**
     * 获取当前用户
     * @param request
     * @return
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        long userId = currentUser.getId();
        // TODO 校验用户是否合法【比如是否已经被封号等】
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }


    // TODO: 待测试
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {

        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "缺少管理员权限");
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    //TODO 逻辑删除
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {

        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(id);

        return ResultUtils.success(b);
    }


    @GetMapping("/search/tags")
    public BaseResponse<List<User>> searchUsersByTags(@RequestParam(required = false) List<String> tagNameList) {
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<User> userList = userService.searchUserByTags(tagNameList);
        return ResultUtils.success(userList);
    }

    /**
     * 分页查询
     * @param pageSize 每一页数据的大小
     * @param pageNum 第几页
     * @param request
     * @return 查询页数据
     */
    //TODO: 推荐功能未实现
    @GetMapping("/recommend")
    public BaseResponse<Page<User>> recommendUsers(long pageSize, long pageNum, HttpServletRequest request) {
        //获取当前用户
        User loginUser = userService.getLoginUser(request);
        //如果有缓存，直接读取
        //根据每个用户的id存储对应的缓存数据
        String redisKey = String.format("timess:user:recommend:%s",loginUser.getId());
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        Page<User> userPage =(Page<User>) valueOperations.get(redisKey);
        if(userPage != null){
            return ResultUtils.success(userPage);
        }
        //无缓存，查数据库
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        userPage = userService.page(new Page<>(pageNum, pageSize), queryWrapper);
        //写缓存
        try {
            valueOperations.set(redisKey,userPage,5, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("redis set key error",e);
        }
        return ResultUtils.success(userPage);
    }

    @GetMapping("/match")
    public BaseResponse<List<User>> matchUsers(long num, HttpServletRequest request) {
        if(num <= 0 || num > 20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success( userService.matchUsers(num,loginUser));
    }


}
