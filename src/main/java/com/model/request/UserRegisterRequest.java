package com.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author timess
 * 用户注册请求
 */
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = 4556712987897541241L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;
    /**
     * 星球编号
     */
    private String planetCode;

}
