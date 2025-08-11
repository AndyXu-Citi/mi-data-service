package com.marathon.service;

import com.marathon.common.api.R;
import com.marathon.domain.entity.Registration;

/**
 * 报名服务接口
 *
 * @author marathon
 */
public interface RegistrationService {

    /**
     * 创建报名
     *
     * @param registration 报名信息
     * @return 创建结果
     */
    R<Boolean> createRegistration(Registration registration);

    /**
     * 获取报名详情
     *
     * @param registrationId 报名ID
     * @return 报名详情
     */
    R<Registration> getRegistrationDetail(Long registrationId);

    /**
     * 更新报名信息
     *
     * @param registration 报名信息
     * @return 更新结果
     */
    R<Boolean> updateRegistration(Registration registration);

    /**
     * 取消报名
     *
     * @param registrationId 报名ID
     * @param userId        用户ID
     * @return 取消结果
     */
    R<Boolean> cancelRegistration(Long registrationId, Long userId);

    /**
     * 获取用户的报名列表
     *
     * @param userId   用户ID
     * @param status   报名状态
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 报名列表
     */
    R<?> getUserRegistrations(Long userId, Integer status, Integer pageNum, Integer pageSize);

    /**
     * 获取赛事的报名列表
     *
     * @param eventId  赛事ID
     * @param itemId   项目ID
     * @param status   报名状态
     * @param keyword  关键词（姓名、手机号）
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 报名列表
     */
    R<?> getEventRegistrations(Long eventId, Long itemId, Integer status, String keyword, Integer pageNum, Integer pageSize);

    /**
     * 审核报名
     *
     * @param registrationId 报名ID
     * @param status        审核状态（1通过 2拒绝）
     * @param remark        审核备注
     * @param auditBy       审核人
     * @return 审核结果
     */
    R<Boolean> auditRegistration(Long registrationId, Integer status, String remark, String auditBy);

    /**
     * 支付报名费用
     *
     * @param registrationId 报名ID
     * @param payType       支付方式
     * @param transactionId 交易号
     * @return 支付结果
     */
    R<Boolean> payRegistration(Long registrationId, Integer payType, String transactionId);

    /**
     * 退款
     *
     * @param registrationId 报名ID
     * @param reason        退款原因
     * @return 退款结果
     */
    R<Boolean> refundRegistration(Long registrationId, String reason);

    /**
     * 获取赛事报名统计
     *
     * @param eventId 赛事ID
     * @return 报名统计
     */
    R<?> getRegistrationStats(Long eventId);

    /**
     * 分配参赛号码
     *
     * @param registrationId 报名ID
     * @param raceNumber    参赛号码
     * @return 分配结果
     */
    R<Boolean> assignRaceNumber(Long registrationId, String raceNumber);
}