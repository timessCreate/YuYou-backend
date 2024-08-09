package com.service;

import com.common.DeleteRequest;
import com.dto.TeamQuery;
import com.model.domain.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.model.domain.User;
import com.model.request.TeamJoinRequest;
import com.model.request.TeamQuitRequest;
import com.model.request.TeamUpdateRequest;
import com.model.vo.TeamUserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author timess
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2024-07-30 15:02:53
*/
public interface TeamService extends IService<Team> {
    /**
     * 创建队伍
     *
     * @param team
     * @param loginUser
     * @return
     */
    long addTeam(Team team, User loginUser);

    /**
     * 搜索队伍
     * @param teamQuery
     * @param isAdmin 是否为管理员
     * @return 队伍列表
     */
    List<TeamUserVO> listTeams(TeamQuery teamQuery, boolean isAdmin);

    /**
     *
     * @param teamUpdateRequest 队伍修改类
     * @return
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser);

    /**
     * 加入队伍
     * @param teamJoinRequest
     * @param loginUser
     * @return
     */
    boolean joinTeam(TeamJoinRequest teamJoinRequest,User loginUser);

    /**
     * 用户退出
     * @param teamQuitRequest
     * @param loginUser
     * @return
     */
    boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);

    /**
     * 队长删除队伍
     * @param id
     * @param loginUser
     * @return
     */
    boolean deleteTeam(Long id, User loginUser);
}
