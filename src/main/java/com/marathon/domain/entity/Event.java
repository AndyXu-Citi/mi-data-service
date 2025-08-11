package com.marathon.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 赛事实体类
 *
 * @author marathon
 */
@Data
@TableName("event")
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 赛事ID
     */
    @TableId(value = "event_id", type = IdType.AUTO)
    private Long eventId;

    /**
     * 赛事名称
     */
    private String eventName;

    /**
     * 赛事封面图
     */
    private String coverImage;

    /**
     * 赛事类型（1马拉松 2半程马拉松 3健康跑 4越野跑 5其他）
     */
    private Integer eventType;

    /**
     * 赛事状态（1未开始 2报名中 3报名结束 4比赛中 5已结束）
     */
    private Integer eventStatus;

    /**
     * 赛事开始时间
     */
    private LocalDateTime startTime;

    /**
     * 赛事结束时间
     */
    private LocalDateTime endTime;

    /**
     * 报名开始时间
     */
    private LocalDateTime registrationStartTime;

    /**
     * 报名结束时间
     */
    private LocalDateTime registrationEndTime;

    /**
     * 赛事地点
     */
    private String location;

    /**
     * 赛事省份
     */
    private String province;

    /**
     * 赛事城市
     */
    private String city;

    /**
     * 赛事详细地址
     */
    private String address;

    /**
     * 赛事简介
     */
    private String introduction;

    /**
     * 赛事详情
     */
    private String description;

    /**
     * 赛事路线图
     */
    private String routeMap;

    /**
     * 报名须知
     */
    private String registrationGuide;

    /**
     * 退改政策
     */
    private String refundPolicy;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 联系邮箱
     */
    private String contactEmail;

    /**
     * 组织者
     */
    private String organizer;

    /**
     * 最大参与人数
     */
    private Integer maxParticipants;

    /**
     * 当前参与人数
     */
    private Integer currentParticipants;

    /**
     * 是否推荐（0否 1是）
     */
    private Integer isRecommended;

    /**
     * 是否热门（0否 1是）
     */
    private Integer isHot;

    /**
     * 浏览量
     */
    private Integer viewCount;

    /**
     * 收藏量
     */
    private Integer favoriteCount;

    /**
     * 报名量
     */
    private Integer registrationCount;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标志（0存在 1删除）
     */
    @TableLogic
    private Integer deleted;
}