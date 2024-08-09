-- auto-generated definition
create table team
(
    id           bigint auto_increment comment 'id' primary key,
    name  varchar(256)                       null comment '队伍名称',
    description   varchar(1024)              null comment '队伍简介',
    avatarUrl    varchar(1024)               null comment '队伍头像',
    maxNum   int      default 1          not null comment '最大人数',
    expireTime    datetime                   null comment '过期时间',
    userId     bigint  comment '用户id',
    status   int      default 0          not null comment '状态 0 - 正常  1: 私有， 2： 加密',
    password varchar(512)                    null comment '密码',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    isDelete     tinyint  default 0                 not null comment '是否删除'
)
    comment '队伍';