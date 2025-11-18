package com.mos.base.common.service.impl;

import com.mos.base.common.cache.MappingCache;
import com.mos.base.common.dto.MigrationRequest;
import com.mos.base.common.dto.MigrationResponse;
import com.mos.base.common.entity.RideCard;
import com.mos.base.common.mapper.source.SourceRideCardMapper;
import com.mos.base.common.mapper.target.TargetRideCardMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 骑行卡表迁移服务
 *
 * @author ly
 */
@Service
public class RideCardMigrationServiceImpl {

    @Autowired
    private SourceRideCardMapper sourceRideCardMapper;

    @Autowired
    private TargetRideCardMapper targetRideCardMapper;

    @Autowired
    private MappingCache mappingCache;

    /**
     * 迁移骑行卡数据
     *
     * @param request 迁移请求
     * @return 迁移响应
     */
    public MigrationResponse migrateRideCard(MigrationRequest request) {
        Integer batchSize = request.getBatchSize();
        Integer totalCount = sourceRideCardMapper.selectTotalCount();
        int offset = 0;
        int totalMigrated = 0;
        int batchCount = 0;

        try {
            while (offset < totalCount) {
                // 1. 读取数据（无事务）
                List<RideCard> sourceData = sourceRideCardMapper.selectByPage(offset, batchSize);
                if (sourceData == null || sourceData.isEmpty()) {
                    break;
                }

                // 2. 处理数据（无事务）
                List<RideCard> targetData = new ArrayList<>();
                List<Integer> oldRideCardIds = new ArrayList<>();

                for (RideCard source : sourceData) {
                    RideCard target = new RideCard();
                    BeanUtils.copyProperties(source, target);

                    // 转换accountId
                    Integer oldAccountId = source.getAccountId();
                    Integer newAccountId = mappingCache.getNewAccountId(oldAccountId);
                    if (newAccountId == null) {
                        throw new RuntimeException("找不到accountId映射关系：" + oldAccountId);
                    }
                    target.setAccountId(newAccountId);

                    // 清空rideCardId，让数据库自动生成
                    target.setRideCardId(null);

                    targetData.add(target);
                    oldRideCardIds.add(source.getRideCardId());
                }

                // 3. 开启事务：写入 + 验证
                int inserted = insertAndValidate(targetData, oldRideCardIds);

                totalMigrated += inserted;
                batchCount++;
                offset += batchSize;
            }

            return MigrationResponse.builder()
                    .success(true)
                    .message("骑行卡数据迁移成功")
                    .totalCount(totalCount)
                    .migratedCount(totalMigrated)
                    .batchCount(batchCount)
                    .mapping(mappingCache.getRideCardMapping())
                    .build();

        } catch (Exception e) {
            return MigrationResponse.builder()
                    .success(false)
                    .message("骑行卡数据迁移失败：" + e.getMessage())
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
     * @param oldRideCardIds 旧骑行卡ID列表
     * @return 插入数量
     */
    @Transactional(transactionManager = "targetTransactionManager", rollbackFor = Exception.class)
    public int insertAndValidate(List<RideCard> targetData, List<Integer> oldRideCardIds) {
        // 写入数据（MyBatis会自动填充rideCardId）
        int inserted = targetRideCardMapper.batchInsert(targetData);

        // 验证数据量
        if (inserted != targetData.size()) {
            throw new RuntimeException("插入数量不匹配！期望:" + targetData.size() + ", 实际:" + inserted);
        }

        // 构建映射关系
        Map<Integer, Integer> finalMapping = new HashMap<>();
        for (int i = 0; i < targetData.size(); i++) {
            Integer oldRideCardId = oldRideCardIds.get(i);
            Integer newRideCardId = targetData.get(i).getRideCardId();
            finalMapping.put(oldRideCardId, newRideCardId);
        }

        // 验证成功后，更新全局映射缓存
        mappingCache.putAllRideCardMapping(finalMapping);

        return inserted;
    }
}
