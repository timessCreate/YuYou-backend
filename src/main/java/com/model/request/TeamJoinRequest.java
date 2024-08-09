package com.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author timess
 * 用户加入请求
 */
@Data
public class TeamJoinRequest implements Serializable {

    private static final long serialVersionUID = 54823719027576049L;

    /**
     * 队伍id
     */
    private Long teamId;
    /**
     * 密码
     */
    private String password;

}
