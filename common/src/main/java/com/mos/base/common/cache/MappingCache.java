package com.mos.base.common.cache;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ID映射关系缓存
 * 用于维护迁移过程中新旧ID的对应关系
 *
 * @author ly
 */
@Component
public class MappingCache {

    /**
     * 账户ID映射：旧accountId -> 新accountId
     */
    private final Map<Integer, Integer> accountMapping = new ConcurrentHashMap<>();

    /**
     * 用户ID映射：旧userId -> 新userId
     */
    private final Map<Integer, Integer> userMapping = new ConcurrentHashMap<>();

    /**
     * 骑行卡ID映射：旧rideCardId -> 新rideCardId
     */
    private final Map<Integer, Integer> rideCardMapping = new ConcurrentHashMap<>();

    /**
     * 礼品卡ID映射：旧giftCardId -> 新giftCardId
     */
    private final Map<Integer, Integer> giftCardMapping = new ConcurrentHashMap<>();

    /**
     * 用户手机号映射：用于数据校验
     * Key: "newUserId:newPhone", Value: "oldUserId:oldPhone"
     */
    private final Map<String, String> userPhoneMapping = new ConcurrentHashMap<>();

    // ==================== Account Mapping ====================

    public void putAccountMapping(Integer oldAccountId, Integer newAccountId) {
        accountMapping.put(oldAccountId, newAccountId);
    }

    public void putAllAccountMapping(Map<Integer, Integer> mapping) {
        accountMapping.putAll(mapping);
    }

    public Integer getNewAccountId(Integer oldAccountId) {
        return accountMapping.get(oldAccountId);
    }

    public boolean containsOldAccountId(Integer oldAccountId) {
        return accountMapping.containsKey(oldAccountId);
    }

    public Map<Integer, Integer> getAccountMapping() {
        return new ConcurrentHashMap<>(accountMapping);
    }

    public void clearAccountMapping() {
        accountMapping.clear();
    }

    // ==================== User Mapping ====================

    public void putUserMapping(Integer oldUserId, Integer newUserId) {
        userMapping.put(oldUserId, newUserId);
    }

    public void putAllUserMapping(Map<Integer, Integer> mapping) {
        userMapping.putAll(mapping);
    }

    public Integer getNewUserId(Integer oldUserId) {
        return userMapping.get(oldUserId);
    }

    public boolean containsOldUserId(Integer oldUserId) {
        return userMapping.containsKey(oldUserId);
    }

    public Map<Integer, Integer> getUserMapping() {
        return new ConcurrentHashMap<>(userMapping);
    }

    public void clearUserMapping() {
        userMapping.clear();
    }

    // ==================== RideCard Mapping ====================

    public void putRideCardMapping(Integer oldRideCardId, Integer newRideCardId) {
        rideCardMapping.put(oldRideCardId, newRideCardId);
    }

    public void putAllRideCardMapping(Map<Integer, Integer> mapping) {
        rideCardMapping.putAll(mapping);
    }

    public Integer getNewRideCardId(Integer oldRideCardId) {
        return rideCardMapping.get(oldRideCardId);
    }

    public boolean containsOldRideCardId(Integer oldRideCardId) {
        return rideCardMapping.containsKey(oldRideCardId);
    }

    public Map<Integer, Integer> getRideCardMapping() {
        return new ConcurrentHashMap<>(rideCardMapping);
    }

    public void clearRideCardMapping() {
        rideCardMapping.clear();
    }

    // ==================== GiftCard Mapping ====================

    public void putGiftCardMapping(Integer oldGiftCardId, Integer newGiftCardId) {
        giftCardMapping.put(oldGiftCardId, newGiftCardId);
    }

    public void putAllGiftCardMapping(Map<Integer, Integer> mapping) {
        giftCardMapping.putAll(mapping);
    }

    public Integer getNewGiftCardId(Integer oldGiftCardId) {
        return giftCardMapping.get(oldGiftCardId);
    }

    public boolean containsOldGiftCardId(Integer oldGiftCardId) {
        return giftCardMapping.containsKey(oldGiftCardId);
    }

    public Map<Integer, Integer> getGiftCardMapping() {
        return new ConcurrentHashMap<>(giftCardMapping);
    }

    public void clearGiftCardMapping() {
        giftCardMapping.clear();
    }

    // ==================== UserPhone Mapping ====================

    public void putUserPhoneMapping(String newKey, String oldValue) {
        userPhoneMapping.put(newKey, oldValue);
    }

    public void putAllUserPhoneMapping(Map<String, String> mapping) {
        userPhoneMapping.putAll(mapping);
    }

    public String getOldUserPhone(String newKey) {
        return userPhoneMapping.get(newKey);
    }

    public Map<String, String> getUserPhoneMapping() {
        return new ConcurrentHashMap<>(userPhoneMapping);
    }

    public void clearUserPhoneMapping() {
        userPhoneMapping.clear();
    }

    // ==================== Utility ====================

    public void clearAll() {
        accountMapping.clear();
        userMapping.clear();
        rideCardMapping.clear();
        giftCardMapping.clear();
        userPhoneMapping.clear();
    }
}
