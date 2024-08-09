package com.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: timess
 * @className: TeamUserVO
 * @Version: 1.0
 * @description: 队伍和用户信息封装类，返回给前端的信息
 */

@Data
public class TeamUserVO implements Serializable {

    private static final long serialVersionUID = -3209734229111892861L;

    /**
     * id
     */
    private Long id;

    /**
     * 队伍名称
     */
    private String name;

    /**
     * 队伍简介
     */
    private String description;

    /**
     * 队伍头像
     */
    private String avatarUrl;

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
     * 创建时间
     */
    private Date createTime;

    /**
     *更新时间
     */
    private Date updateTime;

    /**
     * 创建人用户信息
     */
    private UserVO createUser;

    /**
     * 是否已经加入本队伍
     */
    boolean hasJoin = false;
}
