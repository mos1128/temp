package com.mos.base.common.service.impl;

import com.mos.base.common.cache.MappingCache;
import com.mos.base.common.entity.*;
import com.mos.base.common.exception.DataMigrationException;
import com.mos.base.common.mapper.target.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 事务管理服务
 * 专门处理需要事务的写入和验证操作
 *
 * @author ly
 */
@Service
public class TransactionService {

    @Autowired
    private MappingCache mappingCache;

    @Autowired
    private TargetUserMapper targetUserMapper;

    @Autowired
    private TargetUserToAgentMapper targetUserToAgentMapper;

    @Autowired
    private TargetRideCardMapper targetRideCardMapper;

    @Autowired
    private TargetRideCardUserMapper targetRideCardUserMapper;

    @Autowired
    private TargetGiftCardMapper targetGiftCardMapper;

    @Autowired
    private TargetGiftCardUserMapper targetGiftCardUserMapper;

    /**
     * 事务方法：插入用户数据并验证
     *
     * @param targetUsers 目标用户列表
     * @param oldUserIds 旧用户ID列表
     * @param oldPhones 旧手机号列表
     * @return 插入数量
     */
    @Transactional(transactionManager = "targetTransactionManager", rollbackFor = Exception.class)
    public int insertAndValidateUser(List<User> targetUsers, List<Integer> oldUserIds, List<String> oldPhones) {
        // 写入数据（MyBatis会自动填充userId到targetUsers中）
        int inserted = targetUserMapper.batchInsert(targetUsers);

        // 验证数据量
        if (inserted != targetUsers.size()) {
            throw new RuntimeException("插入数量不匹配！期望:" + targetUsers.size() + ", 实际:" + inserted);
        }

        // 构建映射关系（此时targetUsers中的userId已经被MyBatis填充）
        Map<Integer, Integer> finalUserMapping = new HashMap<>();
        Map<String, String> finalUserPhoneMapping = new HashMap<>();

        for (int i = 0; i < targetUsers.size(); i++) {
            Integer oldUserId = oldUserIds.get(i);
            Integer newUserId = targetUsers.get(i).getUserId();
            String oldPhone = oldPhones.get(i);
            String newPhone = targetUsers.get(i).getPhone();

            // 用户ID映射
            finalUserMapping.put(oldUserId, newUserId);

            // 手机号映射
            String newKey = newUserId + ":" + newPhone;
            String oldValue = oldUserId + ":" + oldPhone;
            finalUserPhoneMapping.put(newKey, oldValue);
        }

        // 验证成功后，更新全局映射缓存
        mappingCache.putAllUserMapping(finalUserMapping);
        mappingCache.putAllUserPhoneMapping(finalUserPhoneMapping);

        return inserted;
    }

    /**
     * 事务方法：插入UserToAgent数据并验证
     *
     * @param targetData 目标数据列表
     * @return 插入数量
     */
    @Transactional(transactionManager = "targetTransactionManager", rollbackFor = Exception.class)
    public int insertAndValidateUserToAgent(List<UserToAgent> targetData) {
        int inserted = targetUserToAgentMapper.batchInsert(targetData);

        if (inserted != targetData.size()) {
            throw new DataMigrationException("插入数量不匹配！期望:" + targetData.size() + ", 实际:" + inserted);
        }

        return inserted;
    }

    /**
     * 事务方法：插入RideCard数据并验证
     *
     * @param targetData 目标数据列表
     * @param oldRideCardIds 旧骑行卡ID列表
     * @return 插入数量
     */
    @Transactional(transactionManager = "targetTransactionManager", rollbackFor = Exception.class)
    public int insertAndValidateRideCard(List<RideCard> targetData, List<Integer> oldRideCardIds) {
        int inserted = targetRideCardMapper.batchInsert(targetData);

        if (inserted != targetData.size()) {
            throw new DataMigrationException("插入数量不匹配！期望:" + targetData.size() + ", 实际:" + inserted);
        }

        // 构建映射关系
        Map<Integer, Integer> finalMapping = new HashMap<>();
        for (int i = 0; i < targetData.size(); i++) {
            Integer oldRideCardId = oldRideCardIds.get(i);
            Integer newRideCardId = targetData.get(i).getRideCardId();
            finalMapping.put(oldRideCardId, newRideCardId);
        }

        mappingCache.putAllRideCardMapping(finalMapping);

        return inserted;
    }

    /**
     * 事务方法：插入RideCardUser数据并验证
     *
     * @param targetData 目标数据列表
     * @return 插入数量
     */
    @Transactional(transactionManager = "targetTransactionManager", rollbackFor = Exception.class)
    public int insertAndValidateRideCardUser(List<RideCardUser> targetData) {
        int inserted = targetRideCardUserMapper.batchInsert(targetData);

        if (inserted != targetData.size()) {
            throw new DataMigrationException("插入数量不匹配！期望:" + targetData.size() + ", 实际:" + inserted);
        }

        return inserted;
    }

    /**
     * 事务方法：插入GiftCard数据并验证
     *
     * @param targetData 目标数据列表
     * @param oldGiftCardIds 旧礼品卡ID列表
     * @return 插入数量
     */
    @Transactional(transactionManager = "targetTransactionManager", rollbackFor = Exception.class)
    public int insertAndValidateGiftCard(List<GiftCard> targetData, List<Integer> oldGiftCardIds) {
        int inserted = targetGiftCardMapper.batchInsert(targetData);

        if (inserted != targetData.size()) {
            throw new DataMigrationException("插入数量不匹配！期望:" + targetData.size() + ", 实际:" + inserted);
        }

        // 构建映射关系
        Map<Integer, Integer> finalMapping = new HashMap<>();
        for (int i = 0; i < targetData.size(); i++) {
            Integer oldGiftCardId = oldGiftCardIds.get(i);
            Integer newGiftCardId = targetData.get(i).getGiftcardId();
            finalMapping.put(oldGiftCardId, newGiftCardId);
        }

        mappingCache.putAllGiftCardMapping(finalMapping);

        return inserted;
    }

    /**
     * 事务方法：插入GiftCardUser数据并验证
     *
     * @param targetData 目标数据列表
     * @return 插入数量
     */
    @Transactional(transactionManager = "targetTransactionManager", rollbackFor = Exception.class)
    public int insertAndValidateGiftCardUser(List<GiftCardUser> targetData) {
        int inserted = targetGiftCardUserMapper.batchInsert(targetData);

        if (inserted != targetData.size()) {
            throw new DataMigrationException("插入数量不匹配！期望:" + targetData.size() + ", 实际:" + inserted);
        }

        return inserted;
    }
}
