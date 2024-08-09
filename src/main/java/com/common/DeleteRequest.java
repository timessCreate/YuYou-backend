package com.common;/**
 * @author timess
 */

import lombok.Data;

import java.io.Serializable;

/**
 * @author: timess
 * @className: DeleteRequest
 * @Version: 1.0
 * @description:
 */

@Data
public class DeleteRequest implements Serializable {

    private static final long serialVersionUID = -780174924591745611L;

    /**
     * 删除对象id
     */
    private Long id;
}
