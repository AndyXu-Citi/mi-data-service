package com.marathon.controller;

import com.marathon.common.api.R;
import com.marathon.domain.entity.Registration;
import com.marathon.service.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 报名控制器
 *
 * @author marathon
 */
@Tag(name = "报名管理", description = "报名相关接口")
@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    /**
     * 创建报名
     */
    @Operation(summary = "创建报名", description = "用户报名赛事")
    @PostMapping
    public R<String> createRegistration(@RequestBody Registration registration) {
        return registrationService.createRegistration(registration);
    }

    /**
     * 获取报名详情
     */
    @Operation(summary = "获取报名详情", description = "根据报名ID获取报名详情")
    @GetMapping("/{registrationId}")
    public R<Registration> getRegistrationDetail(@Parameter(description = "报名ID") @PathVariable Long registrationId) {
        return registrationService.getRegistrationDetail(registrationId);
    }

    /**
     * 更新报名信息
     */
    @Operation(summary = "更新报名信息", description = "更新报名信息")
    @PutMapping
    public R<Boolean> updateRegistration(@RequestBody Registration registration) {
        return registrationService.updateRegistration(registration);
    }

    /**
     * 取消报名
     */
    @Operation(summary = "取消报名", description = "取消报名")
    @DeleteMapping("/{registrationId}")
    public R<Boolean> cancelRegistration(@Parameter(description = "报名ID") @PathVariable Long registrationId) {
        return registrationService.cancelRegistration(registrationId);
    }

    /**
     * 获取用户报名列表
     */
    @Operation(summary = "获取用户报名列表", description = "获取用户的报名列表")
    @GetMapping("/user/{userId}")
    public R<?> getUserRegistrations(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return registrationService.getUserRegistrations(userId, pageNum, pageSize);
    }

    /**
     * 获取赛事报名列表
     */
    @Operation(summary = "获取赛事报名列表", description = "获取赛事的报名列表")
    @GetMapping("/event/{eventId}")
    public R<?> getEventRegistrations(
            @Parameter(description = "赛事ID") @PathVariable Long eventId,
            @RequestParam(required = false) Long itemId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return registrationService.getEventRegistrations(eventId, itemId, status, pageNum, pageSize);
    }

    /**
     * 审核报名
     */
    @Operation(summary = "审核报名", description = "审核报名")
    @PutMapping("/audit")
    public R<Boolean> auditRegistration(
            @RequestParam Long registrationId,
            @RequestParam Integer status,
            @RequestParam(required = false) String remark) {
        return registrationService.auditRegistration(registrationId, status, remark);
    }

    /**
     * 支付报名费用
     */
    @Operation(summary = "支付报名费用", description = "支付报名费用")
    @PostMapping("/pay")
    public R<Boolean> payRegistration(
            @RequestParam Long registrationId,
            @RequestParam String paymentMethod,
            @RequestParam(required = false) String transactionId) {
        return registrationService.payRegistration(registrationId, paymentMethod, transactionId);
    }

    /**
     * 退款
     */
    @Operation(summary = "退款", description = "报名退款")
    @PostMapping("/refund")
    public R<Boolean> refundRegistration(
            @RequestParam Long registrationId,
            @RequestParam(required = false) String reason) {
        return registrationService.refundRegistration(registrationId, reason);
    }

    /**
     * 获取赛事报名统计
     */
    @Operation(summary = "获取赛事报名统计", description = "获取赛事报名统计数据")
    @GetMapping("/stats/{eventId}")
    public R<?> getRegistrationStats(@Parameter(description = "赛事ID") @PathVariable Long eventId) {
        return registrationService.getRegistrationStats(eventId);
    }

    /**
     * 分配参赛号码
     */
    @Operation(summary = "分配参赛号码", description = "为报名分配参赛号码")
    @PutMapping("/assign-number")
    public R<Boolean> assignRaceNumber(
            @RequestParam Long registrationId,
            @RequestParam String raceNumber) {
        return registrationService.assignRaceNumber(registrationId, raceNumber);
    }
}