-- auto-generated definition
create table user_team
(
    id           bigint auto_increment comment 'id' primary key,
    userId       bigint  comment '用户id',
    teamId       bigint  comment '队伍id',
    createTime   datetime  comment '加入时间',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    isDelete     tinyint  default 0                 not null comment '是否删除'
)
    comment '用户-队伍关系表';