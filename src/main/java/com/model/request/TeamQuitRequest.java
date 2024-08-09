package com.model.request;/**
 * @author timess
 */

import lombok.Data;

import java.io.Serializable;

/**
 * @author: timess
 * @className: TeamQuitRequest
 * @Version: 1.0
 * @description:
 */
@Data
public class TeamQuitRequest implements Serializable {
    private static final long serialVersionUID = 5482371902757746049L;
    /**
     * 队伍id
     */
    private Long teamId;
}
