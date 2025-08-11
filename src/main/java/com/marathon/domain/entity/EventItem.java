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
 * 赛事项目实体类
 *
 * @author marathon
 */
@Data
@TableName("event_item")
public class EventItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 项目ID
     */
    @TableId(value = "item_id", type = IdType.AUTO)
    private Long itemId;

    /**
     * 赛事ID
     */
    private Long eventId;

    /**
     * 项目名称
     */
    private String itemName;

    /**
     * 项目类型（1全程马拉松 2半程马拉松 3迷你马拉松 4健康跑 5其他）
     */
    private Integer itemType;

    /**
     * 项目距离（单位：公里）
     */
    private BigDecimal distance;

    /**
     * 项目限制人数
     */
    private Integer limitNumber;

    /**
     * 已报名人数
     */
    private Integer registeredNumber;

    /**
     * 项目价格
     */
    private BigDecimal price;

    /**
     * 项目描述
     */
    private String description;

    /**
     * 项目状态（0关闭 1开放）
     */
    private Integer status;

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