package com.model.request;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: timess
 * @className: TeamUpdateRequest
 * @Version: 1.0
 * @description:
 */
@Data
public class TeamUpdateRequest implements Serializable {

    private static final long serialVersionUID = 6613129811008708139L;
    /**
     * 待更新队伍id
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
     * 过期时间
     */
    private Date expireTime;

    /**
     * 状态 0 - 正常  1: 私有， 2： 加密
     */
    private Integer status;

    /**
     * 密码
     */
    private String password;
}
