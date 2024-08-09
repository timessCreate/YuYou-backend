package com.common;/**
 * @author timess
 */

import lombok.Data;

import java.io.Serializable;

/**
 * @author timess
 * @className: PageRequest
 * @Version: 1.0
 * @description: 通用分页请求参数
 */

@Data
public class PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 页面大小
     */
    protected int pageSize = 10;

    /**
     * 页码
     */
    protected int pageNum = 1;

}
