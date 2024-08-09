package com.model.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author timess
 * 用户注册请求
 */
@Data
public class TeamAddRequest implements Serializable {

    private static final long serialVersionUID = 5482371902757746049L;


    /**
     * 队伍名称
     */
    private String name;

    /**
     * 队伍简介
     */
    private String description;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 状态 0 - 正常  1: 私有， 2： 加密
     */
    private Integer status;

    /**
     * 密码
     */
    private String password;

}
