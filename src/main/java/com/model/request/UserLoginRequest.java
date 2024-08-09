package com.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author timess
 * 用户注册请求
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 5482371902757746049L;

    private String userAccount;
    private String userPassword;

}
