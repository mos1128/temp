package com.mos.base.common.controller;

import com.mos.base.common.cache.MappingCache;
import com.mos.base.common.dto.AccountMappingRequest;
import com.mos.base.common.dto.MigrationRequest;
import com.mos.base.common.dto.MigrationResponse;
import com.mos.base.common.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据迁移控制器
 *
 * @author ly
 */
@RestController
@RequestMapping("/migration")
public class MigrationController {

    @Autowired
    private MappingCache mappingCache;

    @Autowired
    private UserMigrationServiceImpl userMigrationService;

    @Autowired
    private UserToAgentMigrationServiceImpl userToAgentMigrationService;

    @Autowired
    private RideCardMigrationServiceImpl rideCardMigrationService;

    @Autowired
    private RideCardUserMigrationServiceImpl rideCardUserMigrationService;

    @Autowired
    private GiftCardMigrationServiceImpl giftCardMigrationService;

    @Autowired
    private GiftCardUserMigrationServiceImpl giftCardUserMigrationService;

    /**
     * 初始化Account映射关系
     *
     * @param request 映射请求
     * @return 响应结果
     */
    @PostMapping("/init-account-mapping")
    public Map<String, Object> initAccountMapping(@RequestBody AccountMappingRequest request) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (request.getAccountMapping() == null || request.getAccountMapping().isEmpty()) {
                result.put("success", false);
                result.put("message", "accountMapping不能为空");
                return result;
            }

            mappingCache.putAllAccountMapping(request.getAccountMapping());

            result.put("success", true);
            result.put("message", "Account映射初始化成功");
            result.put("mappingCount", request.getAccountMapping().size());
            result.put("accountMapping", mappingCache.getAccountMapping());
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Account映射初始化失败：" + e.getMessage());
            return result;
        }
    }

    /**
     * 迁移User表
     *
     * @param request 迁移请求
     * @return 迁移响应
     */
    @PostMapping("/user")
    public MigrationResponse migrateUser(@RequestBody MigrationRequest request) {
        return userMigrationService.migrateUser(request);
    }

    /**
     * 迁移UserToAgent表
     *
     * @param request 迁移请求
     * @return 迁移响应
     */
    @PostMapping("/usertoagent")
    public MigrationResponse migrateUserToAgent(@RequestBody MigrationRequest request) {
        return userToAgentMigrationService.migrateUserToAgent(request);
    }

    /**
     * 迁移RideCard表
     *
     * @param request 迁移请求
     * @return 迁移响应
     */
    @PostMapping("/ridecard")
    public MigrationResponse migrateRideCard(@RequestBody MigrationRequest request) {
        return rideCardMigrationService.migrateRideCard(request);
    }

    /**
     * 迁移RideCardUser表
     *
     * @param request 迁移请求
     * @return 迁移响应
     */
    @PostMapping("/ridecarduser")
    public MigrationResponse migrateRideCardUser(@RequestBody MigrationRequest request) {
        return rideCardUserMigrationService.migrateRideCardUser(request);
    }

    /**
     * 迁移GiftCard表
     *
     * @param request 迁移请求
     * @return 迁移响应
     */
    @PostMapping("/giftcard")
    public MigrationResponse migrateGiftCard(@RequestBody MigrationRequest request) {
        return giftCardMigrationService.migrateGiftCard(request);
    }

    /**
     * 迁移GiftCardUser表
     *
     * @param request 迁移请求
     * @return 迁移响应
     */
    @PostMapping("/giftcarduser")
    public MigrationResponse migrateGiftCardUser(@RequestBody MigrationRequest request) {
        return giftCardUserMigrationService.migrateGiftCardUser(request);
    }

    /**
     * 获取映射关系
     *
     * @param type 映射类型 (account/user/ridecard/giftcard/userphone)
     * @return 映射关系
     */
    @GetMapping("/mappings/{type}")
    public Map<String, Object> getMappings(@PathVariable String type) {
        Map<String, Object> result = new HashMap<>();
        try {
            Object mapping = switch (type.toLowerCase()) {
                case "account" -> mappingCache.getAccountMapping();
                case "user" -> mappingCache.getUserMapping();
                case "ridecard" -> mappingCache.getRideCardMapping();
                case "giftcard" -> mappingCache.getGiftCardMapping();
                case "userphone" -> mappingCache.getUserPhoneMapping();
                default -> null;
            };

            if (mapping == null) {
                result.put("success", false);
                result.put("message", "不支持的映射类型：" + type);
            } else {
                result.put("success", true);
                result.put("type", type);
                result.put("mapping", mapping);
            }
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取映射关系失败：" + e.getMessage());
            return result;
        }
    }

    /**
     * 清空所有映射关系
     *
     * @return 响应结果
     */
    @PostMapping("/clear-all-mappings")
    public Map<String, Object> clearAllMappings() {
        Map<String, Object> result = new HashMap<>();
        try {
            mappingCache.clearAll();
            result.put("success", true);
            result.put("message", "所有映射关系已清空");
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "清空映射关系失败：" + e.getMessage());
            return result;
        }
    }

    /**
     * 获取用户手机号映射（用于数据校验）
     *
     * @return 用户手机号映射
     */
    @GetMapping("/validate-user-phone")
    public Map<String, Object> validateUserPhone() {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, String> userPhoneMapping = mappingCache.getUserPhoneMapping();
            result.put("success", true);
            result.put("message", "获取用户手机号映射成功");
            result.put("totalCount", userPhoneMapping.size());
            result.put("userPhoneMapping", userPhoneMapping);
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取用户手机号映射失败：" + e.getMessage());
            return result;
        }
    }
}
