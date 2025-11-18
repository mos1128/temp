package com.mos.base.common.service.impl;

import com.mos.base.common.cache.MappingCache;
import com.mos.base.common.dto.MigrationRequest;
import com.mos.base.common.dto.MigrationResponse;
import com.mos.base.common.entity.GiftCardUser;
import com.mos.base.common.mapper.source.SourceGiftCardUserMapper;
import com.mos.base.common.mapper.target.TargetGiftCardUserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 礼品卡用户余额表迁移服务
 *
 * @author ly
 */
@Service
public class GiftCardUserMigrationServiceImpl {

    @Autowired
    private SourceGiftCardUserMapper sourceGiftCardUserMapper;

    @Autowired
    private TargetGiftCardUserMapper targetGiftCardUserMapper;

    @Autowired
    private MappingCache mappingCache;

    /**
     * 迁移礼品卡用户余额数据
     *
     * @param request 迁移请求
     * @return 迁移响应
     */
    public MigrationResponse migrateGiftCardUser(MigrationRequest request) {
        Integer batchSize = request.getBatchSize();
        Integer totalCount = sourceGiftCardUserMapper.selectTotalCount();
        int offset = 0;
        int totalMigrated = 0;
        int batchCount = 0;

        try {
            while (offset < totalCount) {
                // 1. 读取数据（无事务）
                List<GiftCardUser> sourceData = sourceGiftCardUserMapper.selectByPage(offset, batchSize);
                if (sourceData == null || sourceData.isEmpty()) {
                    break;
                }

                // 2. 处理数据（无事务）
                List<GiftCardUser> targetData = new ArrayList<>();

                for (GiftCardUser source : sourceData) {
                    GiftCardUser target = new GiftCardUser();
                    BeanUtils.copyProperties(source, target);

                    // 转换accountId
                    Integer oldAccountId = source.getAccountId();
                    Integer newAccountId = mappingCache.getNewAccountId(oldAccountId);
                    if (newAccountId == null) {
                        throw new RuntimeException("找不到accountId映射关系：" + oldAccountId);
                    }
                    target.setAccountId(newAccountId);

                    // 转换userId
                    Integer oldUserId = source.getUserId();
                    Integer newUserId = mappingCache.getNewUserId(oldUserId);
                    if (newUserId == null) {
                        throw new RuntimeException("找不到userId映射关系：" + oldUserId);
                    }
                    target.setUserId(newUserId);

                    targetData.add(target);
                }

                // 3. 开启事务：写入 + 验证
                int inserted = insertAndValidate(targetData);

                totalMigrated += inserted;
                batchCount++;
                offset += batchSize;
            }

            return MigrationResponse.builder()
                    .success(true)
                    .message("礼品卡用户余额数据迁移成功")
                    .totalCount(totalCount)
                    .migratedCount(totalMigrated)
                    .batchCount(batchCount)
                    .build();

        } catch (Exception e) {
            return MigrationResponse.builder()
                    .success(false)
                    .message("礼品卡用户余额数据迁移失败：" + e.getMessage())
                    .totalCount(totalCount)
                    .migratedCount(totalMigrated)
                    .batchCount(batchCount)
                    .build();
        }
    }

    /**
     * 事务方法：插入数据并验证
     *
     * @param targetData 目标数据列表
     * @return 插入数量
     */
    @Transactional(transactionManager = "targetTransactionManager", rollbackFor = Exception.class)
    public int insertAndValidate(List<GiftCardUser> targetData) {
        // 写入数据
        int inserted = targetGiftCardUserMapper.batchInsert(targetData);

        // 验证数据量
        if (inserted != targetData.size()) {
            throw new RuntimeException("插入数量不匹配！期望:" + targetData.size() + ", 实际:" + inserted);
        }

        return inserted;
    }
}
