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
 * 报名实体类
 *
 * @author marathon
 */
@Data
@TableName("registration")
public class Registration implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 报名ID
     */
    @TableId(value = "registration_id", type = IdType.AUTO)
    private Long registrationId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 赛事ID
     */
    private Long eventId;

    /**
     * 项目ID
     */
    private Long itemId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 参赛者姓名
     */
    private String name;

    /**
     * 参赛者性别（0男 1女）
     */
    private Integer gender;

    /**
     * 参赛者年龄
     */
    private Integer age;

    /**
     * 参赛者身份证号
     */
    private String idCard;

    /**
     * 参赛者手机号
     */
    private String phone;

    /**
     * 参赛者邮箱
     */
    private String email;

    /**
     * 紧急联系人
     */
    private String emergencyContact;

    /**
     * 紧急联系人电话
     */
    private String emergencyPhone;

    /**
     * 参赛号码
     */
    private String raceNumber;

    /**
     * 衣服尺码（S、M、L、XL、XXL）
     */
    private String shirtSize;

    /**
     * 报名费用
     */
    private BigDecimal fee;

    /**
     * 支付状态（0未支付 1已支付 2已退款）
     */
    private Integer payStatus;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 支付方式（1微信 2支付宝 3其他）
     */
    private Integer payType;

    /**
     * 支付交易号
     */
    private String transactionId;

    /**
     * 报名状态（0待审核 1已通过 2已拒绝 3已取消）
     */
    private Integer status;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 审核人
     */
    private String auditBy;

    /**
     * 审核备注
     */
    private String auditRemark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

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