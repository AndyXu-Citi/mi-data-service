-- 创建数据库
CREATE DATABASE IF NOT EXISTS `marathon` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE `marathon`;

-- 用户表
CREATE TABLE IF NOT EXISTS `mi_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `gender` tinyint(1) DEFAULT '0' COMMENT '性别（0男 1女 2未知）',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `openid` varchar(100) DEFAULT NULL COMMENT '微信openid',
  `user_type` tinyint(1) DEFAULT '0' COMMENT '用户类型（0普通用户 1管理员）',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标志（0存在 1删除）',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `idx_username` (`username`),
  UNIQUE KEY `idx_phone` (`phone`),
  UNIQUE KEY `idx_email` (`email`),
  KEY `idx_openid` (`openid`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 赛事表
CREATE TABLE IF NOT EXISTS `mi_event` (
  `event_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '赛事ID',
  `event_name` varchar(100) NOT NULL COMMENT '赛事名称',
  `cover_image` varchar(255) DEFAULT NULL COMMENT '封面图片',
  `event_type` tinyint(1) DEFAULT '1' COMMENT '赛事类型（1马拉松 2半程马拉松 3健康跑 4越野跑 5其他）',
  `event_status` tinyint(1) DEFAULT '1' COMMENT '状态（1未开始 2报名中 3报名结束 4比赛中 5已结束）',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `registration_start_time` datetime DEFAULT NULL COMMENT '报名开始时间',
  `registration_end_time` datetime DEFAULT NULL COMMENT '报名结束时间',
  `location` varchar(100) DEFAULT NULL COMMENT '地点',
  `province` varchar(50) DEFAULT NULL COMMENT '省份',
  `city` varchar(50) DEFAULT NULL COMMENT '城市',
  `address` varchar(255) DEFAULT NULL COMMENT '详细地址',
  `introduction` varchar(500) DEFAULT NULL COMMENT '简介',
  `description` text COMMENT '详情',
  `route_map` varchar(255) DEFAULT NULL COMMENT '路线图',
  `registration_guide` text COMMENT '报名须知',
  `refund_policy` text COMMENT '退改政策',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `contact_email` varchar(100) DEFAULT NULL COMMENT '联系邮箱',
  `organizer` varchar(100) DEFAULT NULL COMMENT '组织者',
  `max_participants` int(11) DEFAULT '0' COMMENT '最大参与人数',
  `current_participants` int(11) DEFAULT '0' COMMENT '当前参与人数',
  `is_recommended` tinyint(1) DEFAULT '0' COMMENT '是否推荐（0否 1是）',
  `is_hot` tinyint(1) DEFAULT '0' COMMENT '是否热门（0否 1是）',
  `view_count` int(11) DEFAULT '0' COMMENT '浏览量',
  `favorite_count` int(11) DEFAULT '0' COMMENT '收藏量',
  `registration_count` int(11) DEFAULT '0' COMMENT '报名量',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标志（0存在 1删除）',
  PRIMARY KEY (`event_id`),
  KEY `idx_status` (`event_status`),
  KEY `idx_start_time` (`start_time`),
  KEY `idx_reg_time` (`registration_start_time`,`registration_end_time`),
  KEY `idx_location` (`province`,`city`),
  KEY `idx_event_type` (`event_type`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='赛事表';

-- 收藏表
CREATE TABLE IF NOT EXISTS `mi_favorite` (
  `favorite_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `event_id` bigint(20) NOT NULL COMMENT '赛事ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标志（0存在 1删除）',
  PRIMARY KEY (`favorite_id`),
  UNIQUE KEY `idx_user_event` (`user_id`,`event_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_event_id` (`event_id`),
  CONSTRAINT `fk_favorite_user` FOREIGN KEY (`user_id`) REFERENCES `mi_user` (`user_id`),
  CONSTRAINT `fk_favorite_event` FOREIGN KEY (`event_id`) REFERENCES `mi_event` (`event_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';
