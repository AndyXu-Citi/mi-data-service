package com.marathon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.marathon.common.api.R;
import com.marathon.domain.entity.Event;
import com.marathon.domain.entity.EventItem;
import com.marathon.domain.entity.Registration;
import com.marathon.mapper.EventItemMapper;
import com.marathon.mapper.EventMapper;
import com.marathon.mapper.RegistrationMapper;
import com.marathon.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报名服务实现类
 *
 * @author marathon
 */
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl extends ServiceImpl<RegistrationMapper, Registration> implements RegistrationService {

    private final EventMapper eventMapper;
    private final EventItemMapper eventItemMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> createRegistration(Registration registration) {
        // 参数校验
        if (registration == null || registration.getUserId() == null || registration.getEventId() == null || registration.getItemId() == null) {
            return R.fail("参数不能为空");
        }

        // 检查赛事是否存在
        Event event = eventMapper.selectById(registration.getEventId());
        if (event == null) {
            return R.fail("赛事不存在");
        }

        // 检查赛事状态是否允许报名
        if (event.getEventStatus() != 2) {
            return R.fail("赛事不在报名阶段");
        }

        // 检查赛事项目是否存在
        EventItem eventItem = eventItemMapper.selectById(registration.getItemId());
        if (eventItem == null) {
            return R.fail("赛事项目不存在");
        }

        // 检查项目是否已满
        if (eventItem.getRegisteredNumber() >= eventItem.getLimitNumber()) {
            return R.fail("该项目报名人数已满");
        }

        // 检查用户是否已报名该项目
        LambdaQueryWrapper<Registration> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Registration::getUserId, registration.getUserId())
                .eq(Registration::getEventId, registration.getEventId())
                .eq(Registration::getItemId, registration.getItemId())
                .ne(Registration::getStatus, 3); // 非取消状态
        if (count(queryWrapper) > 0) {
            return R.fail("您已报名该项目");
        }

        // 设置默认值
        registration.setOrderNo(generateOrderNo());
        registration.setStatus(0); // 待审核
        registration.setPayStatus(0); // 未支付
        registration.setCreateTime(LocalDateTime.now());
        registration.setFee(eventItem.getPrice());

        // 保存报名信息
        boolean result = save(registration);
        if (result) {
            // 更新项目报名人数
            eventItem.setRegisteredNumber(eventItem.getRegisteredNumber() + 1);
            eventItemMapper.updateById(eventItem);

            // 更新赛事报名人数
            event.setCurrentParticipants(event.getCurrentParticipants() + 1);
            event.setRegistrationCount(event.getRegistrationCount() + 1);
            eventMapper.updateById(event);
        }

        return result ? R.ok(true, "报名成功") : R.fail("报名失败");
    }

    @Override
    public R<Registration> getRegistrationDetail(Long registrationId) {
        if (registrationId == null) {
            return R.fail("报名ID不能为空");
        }
        Registration registration = getById(registrationId);
        if (registration == null) {
            return R.fail("报名信息不存在");
        }
        return R.ok(registration);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> updateRegistration(Registration registration) {
        if (registration == null || registration.getRegistrationId() == null) {
            return R.fail("参数不能为空");
        }

        // 获取原报名信息
        Registration original = getById(registration.getRegistrationId());
        if (original == null) {
            return R.fail("报名信息不存在");
        }

        // 只允许更新部分字段
        registration.setEventId(null);
        registration.setItemId(null);
        registration.setUserId(null);
        registration.setOrderNo(null);
        registration.setStatus(null);
        registration.setPayStatus(null);
        registration.setUpdateTime(LocalDateTime.now());

        boolean result = updateById(registration);
        return result ? R.ok(true, "更新成功") : R.fail("更新失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> cancelRegistration(Long registrationId, Long userId) {
        if (registrationId == null || userId == null) {
            return R.fail("参数不能为空");
        }

        // 获取报名信息
        Registration registration = getById(registrationId);
        if (registration == null) {
            return R.fail("报名信息不存在");
        }

        // 验证用户身份
        if (!registration.getUserId().equals(userId)) {
            return R.fail("无权操作");
        }

        // 只有待审核和已通过的报名可以取消
        if (registration.getStatus() != 0 && registration.getStatus() != 1) {
            return R.fail("当前状态不允许取消");
        }

        // 更新报名状态
        registration.setStatus(3); // 已取消
        registration.setUpdateTime(LocalDateTime.now());
        boolean result = updateById(registration);

        if (result) {
            // 更新项目报名人数
            EventItem eventItem = eventItemMapper.selectById(registration.getItemId());
            if (eventItem != null && eventItem.getRegisteredNumber() > 0) {
                eventItem.setRegisteredNumber(eventItem.getRegisteredNumber() - 1);
                eventItemMapper.updateById(eventItem);
            }

            // 更新赛事报名人数
            Event event = eventMapper.selectById(registration.getEventId());
            if (event != null && event.getCurrentParticipants() > 0) {
                event.setCurrentParticipants(event.getCurrentParticipants() - 1);
                eventMapper.updateById(event);
            }
        }

        return result ? R.ok(true, "取消成功") : R.fail("取消失败");
    }

    @Override
    public R<?> getUserRegistrations(Long userId, Integer status, Integer pageNum, Integer pageSize) {
        if (userId == null) {
            return R.fail("用户ID不能为空");
        }

        LambdaQueryWrapper<Registration> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Registration::getUserId, userId)
                .eq(status != null, Registration::getStatus, status)
                .orderByDesc(Registration::getCreateTime);

        Page<Registration> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        return R.ok(page);
    }

    @Override
    public R<?> getEventRegistrations(Long eventId, Long itemId, Integer status, String keyword, Integer pageNum, Integer pageSize) {
        if (eventId == null) {
            return R.fail("赛事ID不能为空");
        }

        LambdaQueryWrapper<Registration> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Registration::getEventId, eventId)
                .eq(itemId != null, Registration::getItemId, itemId)
                .eq(status != null, Registration::getStatus, status)
                .and(StringUtils.hasText(keyword), q -> q
                        .like(Registration::getName, keyword)
                        .or()
                        .like(Registration::getPhone, keyword))
                .orderByDesc(Registration::getCreateTime);

        Page<Registration> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        return R.ok(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> auditRegistration(Long registrationId, Integer status, String remark, String auditBy) {
        if (registrationId == null || status == null) {
            return R.fail("参数不能为空");
        }

        // 获取报名信息
        Registration registration = getById(registrationId);
        if (registration == null) {
            return R.fail("报名信息不存在");
        }

        // 只有待审核的报名可以审核
        if (registration.getStatus() != 0) {
            return R.fail("当前状态不允许审核");
        }

        // 更新报名状态
        registration.setStatus(status);
        registration.setAuditRemark(remark);
        registration.setAuditBy(auditBy);
        registration.setAuditTime(LocalDateTime.now());
        registration.setUpdateTime(LocalDateTime.now());

        boolean result = updateById(registration);

        // 如果拒绝，更新报名人数
        if (result && status == 2) {
            // 更新项目报名人数
            EventItem eventItem = eventItemMapper.selectById(registration.getItemId());
            if (eventItem != null && eventItem.getRegisteredNumber() > 0) {
                eventItem.setRegisteredNumber(eventItem.getRegisteredNumber() - 1);
                eventItemMapper.updateById(eventItem);
            }

            // 更新赛事报名人数
            Event event = eventMapper.selectById(registration.getEventId());
            if (event != null && event.getCurrentParticipants() > 0) {
                event.setCurrentParticipants(event.getCurrentParticipants() - 1);
                eventMapper.updateById(event);
            }
        }

        return result ? R.ok(true, "审核成功") : R.fail("审核失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> payRegistration(Long registrationId, Integer payType, String transactionId) {
        if (registrationId == null || payType == null) {
            return R.fail("参数不能为空");
        }

        // 获取报名信息
        Registration registration = getById(registrationId);
        if (registration == null) {
            return R.fail("报名信息不存在");
        }

        // 只有未支付的报名可以支付
        if (registration.getPayStatus() != 0) {
            return R.fail("当前状态不允许支付");
        }

        // 更新支付信息
        registration.setPayStatus(1); // 已支付
        registration.setPayType(payType);
        registration.setTransactionId(transactionId);
        registration.setPayTime(LocalDateTime.now());
        registration.setUpdateTime(LocalDateTime.now());

        boolean result = updateById(registration);
        return result ? R.ok(true, "支付成功") : R.fail("支付失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> refundRegistration(Long registrationId, String reason) {
        if (registrationId == null) {
            return R.fail("报名ID不能为空");
        }

        // 获取报名信息
        Registration registration = getById(registrationId);
        if (registration == null) {
            return R.fail("报名信息不存在");
        }

        // 只有已支付的报名可以退款
        if (registration.getPayStatus() != 1) {
            return R.fail("当前状态不允许退款");
        }

        // 更新支付状态
        registration.setPayStatus(2); // 已退款
        registration.setRemark(reason);
        registration.setUpdateTime(LocalDateTime.now());

        boolean result = updateById(registration);
        return result ? R.ok(true, "退款成功") : R.fail("退款失败");
    }

    @Override
    public R<?> getRegistrationStats(Long eventId) {
        if (eventId == null) {
            return R.fail("赛事ID不能为空");
        }

        // 获取赛事项目列表
        LambdaQueryWrapper<EventItem> itemQueryWrapper = new LambdaQueryWrapper<>();
        itemQueryWrapper.eq(EventItem::getEventId, eventId);
        List<EventItem> eventItems = eventItemMapper.selectList(itemQueryWrapper);

        // 统计各项目报名人数
        Map<String, Object> stats = new HashMap<>();
        stats.put("items", eventItems);

        // 统计各状态报名数量
        Map<String, Long> statusStats = new HashMap<>();
        LambdaQueryWrapper<Registration> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Registration::getEventId, eventId);
        List<Registration> registrations = list(queryWrapper);

        long totalCount = registrations.size();
        long pendingCount = registrations.stream().filter(r -> r.getStatus() == 0).count();
        long approvedCount = registrations.stream().filter(r -> r.getStatus() == 1).count();
        long rejectedCount = registrations.stream().filter(r -> r.getStatus() == 2).count();
        long canceledCount = registrations.stream().filter(r -> r.getStatus() == 3).count();

        statusStats.put("total", totalCount);
        statusStats.put("pending", pendingCount);
        statusStats.put("approved", approvedCount);
        statusStats.put("rejected", rejectedCount);
        statusStats.put("canceled", canceledCount);

        stats.put("status", statusStats);

        // 统计支付状态
        Map<String, Long> payStats = new HashMap<>();
        long unpaidCount = registrations.stream().filter(r -> r.getPayStatus() == 0).count();
        long paidCount = registrations.stream().filter(r -> r.getPayStatus() == 1).count();
        long refundedCount = registrations.stream().filter(r -> r.getPayStatus() == 2).count();

        payStats.put("unpaid", unpaidCount);
        payStats.put("paid", paidCount);
        payStats.put("refunded", refundedCount);

        stats.put("payment", payStats);

        return R.ok(stats);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> assignRaceNumber(Long registrationId, String raceNumber) {
        if (registrationId == null || !StringUtils.hasText(raceNumber)) {
            return R.fail("参数不能为空");
        }

        // 获取报名信息
        Registration registration = getById(registrationId);
        if (registration == null) {
            return R.fail("报名信息不存在");
        }

        // 只有已通过的报名可以分配号码
        if (registration.getStatus() != 1) {
            return R.fail("当前状态不允许分配号码");
        }

        // 检查号码是否已被使用
        LambdaQueryWrapper<Registration> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Registration::getEventId, registration.getEventId())
                .eq(Registration::getRaceNumber, raceNumber)
                .ne(Registration::getRegistrationId, registrationId);
        if (count(queryWrapper) > 0) {
            return R.fail("号码已被使用");
        }

        // 更新号码
        registration.setRaceNumber(raceNumber);
        registration.setUpdateTime(LocalDateTime.now());

        boolean result = updateById(registration);
        return result ? R.ok(true, "分配成功") : R.fail("分配失败");
    }

    /**
     * 生成订单号
     *
     * @return 订单号
     */
    private String generateOrderNo() {
        return "R" + System.currentTimeMillis() + String.format("%04d", (int) (Math.random() * 10000));
    }
}