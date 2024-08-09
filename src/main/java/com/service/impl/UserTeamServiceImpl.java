package com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.model.domain.UserTeam;
import com.service.UserTeamService;
import com.mapper.UserTeamMapper;
import org.springframework.stereotype.Service;

/**
* @author timess
* @description 针对表【user_team(用户-队伍关系表)】的数据库操作Service实现
* @createDate 2024-07-30 15:05:04
*/
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
    implements UserTeamService{

}




