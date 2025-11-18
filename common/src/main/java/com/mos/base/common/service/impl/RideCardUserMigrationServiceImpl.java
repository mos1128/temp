package com.mos.base.common.service.impl;

import com.mos.base.common.cache.MappingCache;
import com.mos.base.common.dto.MigrationRequest;
import com.mos.base.common.dto.MigrationResponse;
import com.mos.base.common.entity.RideCardUser;
import com.mos.base.common.mapper.source.SourceRideCardUserMapper;
import com.mos.base.common.mapper.target.TargetRideCardUserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 骑行卡用户关联表迁移服务
 *
 * @author ly
 */
@Service
public class RideCardUserMigrationServiceImpl {

    @Autowired
    private SourceRideCardUserMapper sourceRideCardUserMapper;

    @Autowired
    private MappingCache mappingCache;

    @Autowired
    private TransactionService transactionService;

    /**
     * 迁移骑行卡用户关联数据
     *
     * @param request 迁移请求
     * @return 迁移响应
     */
    public MigrationResponse migrateRideCardUser(MigrationRequest request) {
        Integer batchSize = request.getBatchSize();
        Integer totalCount = sourceRideCardUserMapper.selectTotalCount();
        int offset = 0;
        int totalMigrated = 0;
        int batchCount = 0;

        try {
            while (offset < totalCount) {
                // 1. 读取数据（无事务）
                List<RideCardUser> sourceData = sourceRideCardUserMapper.selectByPage(offset, batchSize);
                if (sourceData == null || sourceData.isEmpty()) {
                    break;
                }

                // 2. 处理数据（无事务）
                List<RideCardUser> targetData = new ArrayList<>();

                for (RideCardUser source : sourceData) {
                    RideCardUser target = new RideCardUser();
                    BeanUtils.copyProperties(source, target);

                    // 转换userId
                    Integer oldUserId = source.getUserId();
                    Integer newUserId = mappingCache.getNewUserId(oldUserId);
                    if (newUserId == null) {
                        throw new RuntimeException("找不到userId映射关系：" + oldUserId);
                    }
                    target.setUserId(newUserId);

                    // 转换accountId
                    Integer oldAccountId = source.getAccountId();
                    Integer newAccountId = mappingCache.getNewAccountId(oldAccountId);
                    if (newAccountId == null) {
                        throw new RuntimeException("找不到accountId映射关系：" + oldAccountId);
                    }
                    target.setAccountId(newAccountId);

                    // 转换rideCardId
                    Integer oldRideCardId = source.getRideCardId();
                    Integer newRideCardId = mappingCache.getNewRideCardId(oldRideCardId);
                    if (newRideCardId == null) {
                        throw new RuntimeException("找不到rideCardId映射关系：" + oldRideCardId);
                    }
                    target.setRideCardId(newRideCardId);

                    targetData.add(target);
                }

                // 3. 开启事务：写入 + 验证
                int inserted = transactionService.insertAndValidateRideCardUser(targetData);

                totalMigrated += inserted;
                batchCount++;
                offset += batchSize;
            }

            return MigrationResponse.builder()
                    .success(true)
                    .message("骑行卡用户关联数据迁移成功")
                    .totalCount(totalCount)
                    .migratedCount(totalMigrated)
                    .batchCount(batchCount)
                    .build();

        } catch (Exception e) {
            return MigrationResponse.builder()
                    .success(false)
                    .message("骑行卡用户关联数据迁移失败：" + e.getMessage())
                    .totalCount(totalCount)
                    .migratedCount(totalMigrated)
                    .batchCount(batchCount)
                    .build();
        }
    }

}
