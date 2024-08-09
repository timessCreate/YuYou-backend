package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.DeleteRequest;
import com.common.ErrorCode;
import com.dto.TeamQuery;
import com.exception.BusinessException;
import com.model.domain.Team;
import com.model.domain.User;
import com.model.domain.UserTeam;
import com.model.enums.TeamStatusEnum;
import com.model.request.TeamJoinRequest;
import com.model.request.TeamQuitRequest;
import com.model.request.TeamUpdateRequest;
import com.model.vo.TeamUserVO;
import com.model.vo.UserVO;
import com.service.TeamService;
import com.mapper.TeamMapper;
import com.service.UserService;
import com.service.UserTeamService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
* @author timess
* @description 针对表【team(队伍)】的数据库操作Service实现
* @createDate 2024-07-30 15:02:53
*/

@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService{

    @Resource
    UserTeamService userTeamService;

    @Resource
    UserService userService;
    /**
     * 用户创建队伍
     * @param team 队伍
     * @param loginUser 创建用户
     * @return 创建队伍id
     */
    @Override
    //开启事务
    @Transactional(rollbackFor = Exception.class)
    public long addTeam(Team team, User loginUser) {
//        1. 请求参数是否为 空
        if(team == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
//        2. 是否登录，未登录不允许创建
        if(loginUser == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }

        final long id = loginUser.getId();
        //3. 校验信息
        //a. 队伍人数 > 1 且  <= 20
        int maxNum = Optional.ofNullable(team.getMaxNum()).orElse(0);
        if(maxNum < 1 || maxNum > 20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍人数不满足要求");
        }
        //b. 队伍标题 <= 20
        String name = team.getName();
        if(StringUtils.isBlank(name) || name.length() > 20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍标题过长");
        }
        //c. 描述  <= 512
        String description = team.getDescription();
        if( StringUtils.isNotBlank(description) && description.length() > 512){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍描述过长");
        }
        //d. 是否公开 (int) ,默认为 0(公开)
        int status = Optional.ofNullable(team.getStatus()).orElse(0);
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumValue(status);
        if(statusEnum == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍状态错误");
        }
        //e. 如果队伍 status 是加密，则必须设置密码且 0 < 进队伍密码  <=  32
        String password = team.getPassword();
        if(TeamStatusEnum.SECRET.equals(statusEnum)){
            if(password.length() > 32){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍密码过长");
            }
            if(password.isEmpty()){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"加密状态密码不能为空");
            }
        }
        //f. 队伍过期时间  <  当前时间
        Date expireTime = team.getExpireTime();
        if(new Date().after(expireTime)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍过期时间 < 当前时间");
        }
        //g. 校验用户最多创建五个队伍
        //TODO: bug: 如果用户快速多次点击，可能创建超过五个队伍
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId",id);
        long hasTeamNum = this.count(queryWrapper);
        if(hasTeamNum >= 5){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户最多创建5个队伍");
        }
        //4. 插入队伍信息到队伍表

        //id为null,数据库id自增
        team.setId(null);
        team.setUserId(loginUser.getId());
        boolean result = this.save(team);
        Long teamId = team.getId();
        if(!result || teamId == null){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"创建队伍失败");
        }
        //5. 插入user_team表  ==》 关系表
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(team.getUserId());
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        result = userTeamService.save(userTeam);
        if(!result){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"插入关系表失败");
        }
        return teamId;
    }

    /**
     * 根据条件查询队伍
     *
     * @param teamQuery 查询队伍条件
     * @param isAdmin 是否是管理员
     * @return 符合条件的用户信息
     */
    @Override
    public List<TeamUserVO> listTeams(TeamQuery teamQuery, boolean isAdmin) {
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        //如果不为空，则进行条件查询，否则为查询全部
        //组合查询条件
        if(teamQuery != null){
            //id
            Long id = teamQuery.getId();
            if(id != null && id > 0){
                queryWrapper.eq("id",id);
            }

            List<Long> idList = teamQuery.getIdList();
            if(CollectionUtils.isNotEmpty(idList)){
                queryWrapper.in("id",idList);
            }
            //队伍名
            String name = teamQuery.getName();
            if(StringUtils.isNotBlank(name)){
               queryWrapper.like("name",name);
            }
            //关键字查询
            String searchText = teamQuery.getSearchText();
            if(StringUtils.isNotBlank(searchText)){
                queryWrapper.and(qw -> qw.like("name",searchText)
                        .or().like("description",searchText));
            }
            //队伍描述
            String description = teamQuery.getDescription();
            if(StringUtils.isNotBlank(description)){
                queryWrapper.like("description",description);
            }
            //队伍最大人数
            Integer maxNum = teamQuery.getMaxNum();
            if(maxNum!= null && maxNum > 0){
                queryWrapper.eq("maxNum",maxNum);
            }
            //根据创建人（默认为队长）查询
            Long userId = teamQuery.getUserId();
            if(userId != null && userId > 0){
                queryWrapper.eq("userId",userId);
            }
            //根据队伍状态查询
            Integer status = teamQuery.getStatus();
            TeamStatusEnum statusEnum = TeamStatusEnum.getEnumValue(status);
            if(statusEnum == null){
                statusEnum = TeamStatusEnum.PUBLIC;
            }
            if(!isAdmin && !statusEnum.equals(TeamStatusEnum.PUBLIC)){
                throw new BusinessException(ErrorCode.NO_AUTH);
            }
            queryWrapper.eq("status",statusEnum.getValue());
        }
        //不展示已过期的任务
        //expireTime is  null or expire > now()
        queryWrapper.and(qw -> qw.gt("expireTime", new Date()).or().isNull("expireTime"));
        List<Team> teamList = this.list(queryWrapper);
        if(CollectionUtils.isEmpty(teamList)){
            return new ArrayList<>();
        }

        List<TeamUserVO> teamUserVOList = new ArrayList<>();
        //关联查询用户信息
        for(Team team : teamList){
            Long userId = team.getUserId();
            if(userId == null){
                continue;
            }
            User user = userService.getById(userId);
            //用户信息脱敏
            UserVO userVO = new UserVO();
            try {
                BeanUtils.copyProperties(userVO,user);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            //队伍信息脱敏
            TeamUserVO teamUserVO = new TeamUserVO();
            try {
                BeanUtils.copyProperties(teamUserVO,team);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            teamUserVO.setCreateUser(userVO);
            teamUserVOList.add(teamUserVO);
        }
        return teamUserVOList;
    }

    /**
     *
     * @param teamUpdateRequest 队伍修改类
     * return 是否修改成功
     */
    @Override
    public boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser) {
        if(teamUpdateRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = teamUpdateRequest.getId();
        if(id == null || id <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team oldTeam = this.getById(id);
        if(oldTeam == null){
            throw new BusinessException(ErrorCode.NULL_ERROR,"队伍不存在");
        }
        //不是创建者且不是管理员
        if(!(oldTeam.getUserId().equals(loginUser.getId())) && !userService.isAdmin(loginUser)){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumValue(teamUpdateRequest.getStatus());
        if(statusEnum.equals(TeamStatusEnum.SECRET)){
            if(StringUtils.isBlank(teamUpdateRequest.getPassword())){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍私有权限,密码不能为空");
            }
        }
        Team updateTeam = new Team();
        try {
            BeanUtils.copyProperties(updateTeam, teamUpdateRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this.updateById(updateTeam);
    }

    /**
     * 加入队伍
     * @param teamJoinRequest
     * @param loginUser
     * @return
     */
    @Override
    public boolean joinTeam(TeamJoinRequest teamJoinRequest,User loginUser) {
        if(teamJoinRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //传入teamJoinRequest的队伍id属性不能为空
        Long teamId = teamJoinRequest.getTeamId();
        if(teamId == null || teamId <= 0){
            throw new BusinessException(ErrorCode.NULL_ERROR,"队伍id不能空");
        }
        //team表查询队伍是否存在
        Team team = this.getById(teamId);
        if(team == null){
            throw new BusinessException(ErrorCode.NULL_ERROR, "队伍不存在");
        }
        //判定队伍是否过期
        if(team.getExpireTime() != null && team.getExpireTime().before(new Date())){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍已过期");
        }
        //判定当前队伍权限状态
        Integer status = team.getStatus();
        TeamStatusEnum teamStatusEnum = TeamStatusEnum.getEnumValue(status);
        if(TeamStatusEnum.PRIVATE.equals(teamStatusEnum)){
            throw new BusinessException(ErrorCode.NULL_ERROR, "禁止加入私有队伍");
        }
        //私有队伍需要验证密码
        String password = teamJoinRequest.getPassword();
        if(StringUtils.isBlank(password) && TeamStatusEnum.SECRET.equals(teamStatusEnum)){
            throw new BusinessException(ErrorCode.NULL_ERROR,"加入加密队伍密码不能为空");
        }
        if(TeamStatusEnum.SECRET.equals(teamStatusEnum) && !team.getPassword().equals(password)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码错误");
        }
        //最多加入五个队伍
        //TODO: bug: 只能加入五个队伍
        long userId = loginUser.getId();
        QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("userId",userId);
        long hasJoinNum = userTeamService.count(userTeamQueryWrapper);
        if(hasJoinNum >= 5){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"最多加入五个队伍");
        }
        //不能重复加入已加入的队伍
        userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("userId",userId);
        userTeamQueryWrapper.eq("teamId",teamId);
        long hasUserJoinTeam =userTeamService.count(userTeamQueryWrapper);
        if(hasUserJoinTeam > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户已经加入该队伍");
        }
        //只能加入未满的队伍
        long teamHasJoinNum = this.countUserTeamByTeamId(teamId);
        if(teamHasJoinNum >= team.getMaxNum()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍已满");
        }
        //加入用户-队伍表
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        return userTeamService.save(userTeam);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser) {
        if(teamQuitRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long teamId = teamQuitRequest.getTeamId();
        if(teamId == null || teamId <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = this.getById(teamId);
        if(team == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍不存在");
        }
        Long userId = loginUser.getId();
        UserTeam queryUserTeam = new UserTeam();
        queryUserTeam.setTeamId(teamId);
        queryUserTeam.setUserId(userId);
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>(queryUserTeam);
        long count = userTeamService.count(queryWrapper);
        if(count == 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"未加入队伍");
        }
        
        long teamHasJoinNum = this.countUserTeamByTeamId(teamId);
        //队伍只剩一人，解散
        if(teamHasJoinNum == 1){
            //删除队伍和所有加入队伍的关系
            this.removeById(teamId);
        }else{
            //判定是队长,队伍剩余人数 >= 2
            if( userId.equals(team.getUserId())){
                //按照加入时间顺位继承 --》同一队伍
                //1. 查询已经加入队伍的所有用户和加入时间
                QueryWrapper<UserTeam>userTeamQueryWrapper = new QueryWrapper<>();
                userTeamQueryWrapper.eq("teamId",teamId);
                userTeamQueryWrapper.last("order by id asc limit 2");
                List<UserTeam> userTeamList = userTeamService.list(userTeamQueryWrapper);
                //TODO
                if(CollectionUtils.isEmpty(userTeamList) || userTeamList.size() <= 1){
                    throw  new BusinessException(ErrorCode.SYSTEM_ERROR);
                }
                UserTeam nextUserTeam = userTeamList.get(1);
                Long nextTeamLeaderId = nextUserTeam.getUserId();
                //更新当前队伍的队长, 此处是全局替换 TODO： 修改为部分字段替换
                Team updateTeam = new Team();
                updateTeam.setId(teamId);
                updateTeam.setUserId(nextTeamLeaderId);
                boolean result =  this.updateById(updateTeam);
                if(!result){
                    throw new BusinessException(ErrorCode.PARAMS_ERROR,"更新队伍队长失败");
                }
            }
        }
        //移除user_team关联关系
        return userTeamService.remove(queryWrapper);
    }

    /**
     * 队长删除队伍
     * @param id
     * @param loginUser
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTeam(Long id, User loginUser) {
        if(id == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long userId = loginUser.getId();
        if(userId == null || userId <= 0 || id <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //检验队伍是否存在
        Team team = this.getById(id);
        if(team == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍不存在");
        }
        if((long)team.getUserId() != userId){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"非队长不能删除队伍");
        }
        //删除关联信息表
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teamId", id);
        boolean remove = userTeamService.remove(queryWrapper);
        if(!remove){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"删除关联表信息错误");
        }
        //删除队伍表
        return this.removeById(id);
    }

    /**
     * 获取某队伍当前人数
     * @param teamId 队伍id
     * @return
     */
    private long countUserTeamByTeamId(long teamId){
        //只能加入未满的队伍
        QueryWrapper<UserTeam>userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("teamId",teamId);
        return userTeamService.count(userTeamQueryWrapper);
    }
}




