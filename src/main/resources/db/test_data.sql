-- 马拉松系统测试数据DML语句
-- 用户表测试数据
INSERT INTO `mi_user` (`user_id`, `username`, `password`, `nickname`, `phone`, `email`, `gender`, `avatar`, `openid`, `user_type`, `status`, `login_ip`, `login_date`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `deleted`) VALUES
(2, 'zhangsan', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '张三', '13800138001', 'zhangsan@example.com', 0, 'https://example.com/avatar1.jpg', 'openid123456', 0, 0, '192.168.1.100', '2024-01-15 10:00:00', 'admin', '2024-01-15 09:00:00', 'admin', '2024-01-15 09:00:00', '普通用户', 0),
(3, 'lisi', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '李四', '13800138002', 'lisi@example.com', 1, 'https://example.com/avatar2.jpg', 'openid234567', 0, 0, '192.168.1.101', '2024-01-16 11:00:00', 'admin', '2024-01-16 10:00:00', 'admin', '2024-01-16 10:00:00', '普通用户', 0),
(4, 'wangwu', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '王五', '13800138003', 'wangwu@example.com', 0, 'https://example.com/avatar3.jpg', 'openid345678', 0, 0, '192.168.1.102', '2024-01-17 12:00:00', 'admin', '2024-01-17 11:00:00', 'admin', '2024-01-17 11:00:00', '普通用户', 0),
(5, 'zhaoliu', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '赵六', '13800138004', 'zhaoliu@example.com', 1, 'https://example.com/avatar4.jpg', 'openid456789', 0, 0, '192.168.1.103', '2024-01-18 13:00:00', 'admin', '2024-01-18 12:00:00', 'admin', '2024-01-18 12:00:00', '普通用户', 0);

-- 赛事表测试数据
INSERT INTO `mi_event` (`event_id`, `event_name`, `cover_image`, `event_type`, `event_status`, `start_time`, `end_time`, `registration_start_time`, `registration_end_time`, `location`, `province`, `city`, `address`, `introduction`, `description`, `route_map`, `registration_guide`, `refund_policy`, `contact_phone`, `contact_email`, `organizer`, `max_participants`, `current_participants`, `is_recommended`, `is_hot`, `view_count`, `favorite_count`, `registration_count`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `deleted`) VALUES
(100, '2024北京马拉松', 'https://example.com/bj_marathon.jpg', 1, 2, '2024-04-15 07:30:00', '2024-04-15 13:30:00', '2024-02-01 09:00:00', '2024-03-31 18:00:00', '北京天安门', '北京市', '北京市', '北京市东城区天安门广场', '北京马拉松是中国最具影响力的马拉松赛事', '北京马拉松始于1981年，是中国历史最悠久的马拉松赛事，从天安门广场出发，终点设在奥林匹克公园景观大道，沿途经过长安街、复兴门、木樨地、玉渊潭、中关村等地标，充分展现了北京的古都风貌和现代气息。', 'https://example.com/bj_route_map.jpg', '1. 参赛者须年满18周岁\n2. 须提交一年内有效体检证明\n3. 须持有有效身份证件\n4. 报名成功后不可更改项目', '1. 比赛前30天取消可全额退款\n2. 比赛前15-30天取消收取50%手续费\n3. 比赛前15天内不予退款', '010-12345678', 'info@bj-marathon.com', '中国田径协会', 30000, 25000, 1, 1, 50000, 8000, 25000, 'admin', '2024-01-10 10:00:00', 'admin', '2024-01-10 10:00:00', '热门赛事', 0),
(101, '2024上海马拉松', 'https://example.com/sh_marathon.jpg', 1, 2, '2024-05-20 07:00:00', '2024-05-20 13:00:00', '2024-02-15 09:00:00', '2024-04-30 18:00:00', '上海外滩', '上海市', '上海市', '上海市黄浦区外滩', '上海马拉松是国际化大都市的体育名片', '上海马拉松始于1996年，赛道从外滩出发，途经南京路、静安寺、徐汇滨江、浦东陆家嘴等上海标志性区域，充分展现了上海的国际大都市风貌。', 'https://example.com/sh_route_map.jpg', '1. 参赛者须年满18周岁\n2. 须提交半年内有效体检证明\n3. 须持有有效身份证件\n4. 须完成疫苗接种', '1. 比赛前45天取消可全额退款\n2. 比赛前30-45天取消收取30%手续费\n3. 比赛前30天内不予退款', '021-87654321', 'info@sh-marathon.com', '上海市体育局', 35000, 28000, 1, 1, 45000, 7500, 28000, 'admin', '2024-01-12 10:00:00', 'admin', '2024-01-12 10:00:00', '金牌赛事', 0),
(102, '2024广州马拉松', 'https://example.com/gz_marathon.jpg', 1, 1, '2024-06-10 07:00:00', '2024-06-10 13:00:00', '2024-03-01 09:00:00', '2024-05-31 18:00:00', '广州塔', '广东省', '广州市', '广州市海珠区广州塔广场', '广州马拉松是花城广州的体育盛事', '广州马拉松始于2012年，赛道从广州塔出发，途经珠江新城、天河体育中心、越秀山、沙面岛等广州标志性区域，展现了花城广州的独特魅力。', 'https://example.com/gz_route_map.jpg', '1. 参赛者须年满16周岁\n2. 须提交一年内有效体检证明\n3. 须持有有效身份证件\n4. 须签署免责声明', '1. 比赛前30天取消可全额退款\n2. 比赛前15-30天取消收取50%手续费\n3. 比赛前15天内不予退款', '020-12345678', 'info@gz-marathon.com', '广州市体育局', 25000, 18000, 0, 1, 30000, 5000, 18000, 'admin', '2024-01-15 10:00:00', 'admin', '2024-01-15 10:00:00', '新兴赛事', 0),
(103, '2024深圳马拉松', 'https://example.com/sz_marathon.jpg', 1, 3, '2024-03-25 07:30:00', '2024-03-25 13:30:00', '2024-01-15 09:00:00', '2024-02-28 18:00:00', '深圳市民中心', '广东省', '深圳市', '深圳市福田区市民中心广场', '深圳马拉松是创新之都的体育盛宴', '深圳马拉松始于2013年，赛道从市民中心出发，途经平安金融中心、深圳湾体育中心、华侨城、大梅沙等深圳标志性区域，展现了深圳的创新活力。', 'https://example.com/sz_route_map.jpg', '1. 参赛者须年满18周岁\n2. 须提交半年内有效体检证明\n3. 须持有有效身份证件\n4. 须完成在线培训', '1. 比赛前45天取消可全额退款\n2. 比赛前30-45天取消收取30%手续费\n3. 比赛前30天内不予退款', '0755-12345678', 'info@sz-marathon.com', '深圳市文体局', 20000, 20000, 1, 0, 25000, 4000, 20000, 'admin', '2024-01-08 10:00:00', 'admin', '2024-01-08 10:00:00', '科技赛事', 0);


-- 收藏表测试数据
INSERT INTO `mi_favorite` (`favorite_id`, `user_id`, `event_id`, `create_time`, `update_time`, `deleted`) VALUES
(100, 2, 100, '2024-03-10 10:00:00', '2024-03-10 10:00:00', 0),
(101, 2, 101, '2024-03-11 11:00:00', '2024-03-11 11:00:00', 0),
(102, 2, 102, '2024-03-12 12:00:00', '2024-03-12 12:00:00', 0),
(103, 3, 100, '2024-03-13 13:00:00', '2024-03-13 13:00:00', 0),
(104, 3, 103, '2024-03-14 14:00:00', '2024-03-14 14:00:00', 0),
(105, 4, 101, '2024-03-15 15:00:00', '2024-03-15 15:00:00', 0),
(106, 4, 102, '2024-03-16 16:00:00', '2024-03-16 16:00:00', 0),
(107, 5, 100, '2024-03-17 17:00:00', '2024-03-17 17:00:00', 0),
(108, 5, 101, '2024-03-18 18:00:00', '2024-03-18 18:00:00', 0),
(109, 5, 102, '2024-03-19 19:00:00', '2024-03-19 19:00:00', 0);

-- 更新赛事表的收藏统计
UPDATE `mi_event` SET 
`favorite_count` = (SELECT COUNT(*) FROM `mi_favorite` WHERE event_id = 100)
WHERE event_id = 100;

UPDATE `mi_event` SET 
`favorite_count` = (SELECT COUNT(*) FROM `mi_favorite` WHERE event_id = 101)
WHERE event_id = 101;

UPDATE `mi_event` SET 
`favorite_count` = (SELECT COUNT(*) FROM `mi_favorite` WHERE event_id = 102)
WHERE event_id = 102;

UPDATE `mi_event` SET 
`favorite_count` = (SELECT COUNT(*) FROM `mi_favorite` WHERE event_id = 103)
WHERE event_id = 103;