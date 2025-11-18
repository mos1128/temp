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
    private MappingCache mappingCache;

    @Autowired
    private TransactionService transactionService;

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
                int inserted = transactionService.insertAndValidateRideCard(targetData, oldRideCardIds);

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

}
