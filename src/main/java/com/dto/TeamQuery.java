package com.dto;

import com.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 队伍请求包装类
 *
 * @author timess
 * @className: TeamQuery
 * @Version: 1.0
 * @description: 仅需要部分team类的部分字段
 */


@EqualsAndHashCode(callSuper = true)
@Data
public class TeamQuery extends PageRequest {
    /**
     * id
     */
    private Long id;

    /**
     * id列表
     */
    private List<Long> idList;
    /**
     * 队伍名称
     */
    private String name;

    /**
     * 关键字查询队伍
     * 同时匹配队伍名称和搜索描述
     */
    private String searchText;

    /**
     * 队伍简介
     */
    private String description;

    /**
     * TODO :多添加的属性
     * 队伍头像
     */
    private String avatarUrl;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 状态 0 - 正常  1: 私有， 2： 加密
     */
    private Integer status;


}
