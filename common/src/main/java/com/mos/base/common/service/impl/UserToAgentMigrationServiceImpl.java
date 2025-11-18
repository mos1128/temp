package com.mos.base.common.service.impl;
import com.mos.base.common.exception.DataMigrationException;

import com.mos.base.common.cache.MappingCache;
import com.mos.base.common.dto.MigrationRequest;
import com.mos.base.common.dto.MigrationResponse;
import com.mos.base.common.entity.UserToAgent;
import com.mos.base.common.mapper.source.SourceUserToAgentMapper;
import com.mos.base.common.mapper.target.TargetUserToAgentMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户区域关联表迁移服务
 *
 * @author ly
 */
@Service
public class UserToAgentMigrationServiceImpl {

    @Autowired
    private SourceUserToAgentMapper sourceUserToAgentMapper;

    @Autowired
    private MappingCache mappingCache;

    @Autowired
    private TransactionService transactionService;

    /**
     * 迁移用户区域关联数据
     *
     * @param request 迁移请求
     * @return 迁移响应
     */
    public MigrationResponse migrateUserToAgent(MigrationRequest request) {
        Integer batchSize = request.getBatchSize();
        Integer totalCount = sourceUserToAgentMapper.selectTotalCount();
        int offset = 0;
        int totalMigrated = 0;
        int batchCount = 0;

        try {
            while (offset < totalCount) {
                // 1. 读取数据（无事务）
                List<UserToAgent> sourceData = sourceUserToAgentMapper.selectByPage(offset, batchSize);
                if (sourceData == null || sourceData.isEmpty()) {
                    break;
                }

                // 2. 处理数据（无事务）
                List<UserToAgent> targetData = new ArrayList<>();

                for (UserToAgent source : sourceData) {
                    UserToAgent target = new UserToAgent();
                    BeanUtils.copyProperties(source, target);

                    // 转换userId
                    Integer oldUserId = source.getUserId();
                    Integer newUserId = mappingCache.getNewUserId(oldUserId);
                    if (newUserId == null) {
                        throw new DataMigrationException("找不到userId映射关系：" + oldUserId);
                    }
                    target.setUserId(newUserId);

                    // 转换accountId
                    Integer oldAccountId = source.getAccountId();
                    Integer newAccountId = mappingCache.getNewAccountId(oldAccountId);
                    if (newAccountId == null) {
                        throw new DataMigrationException("找不到accountId映射关系：" + oldAccountId);
                    }
                    target.setAccountId(newAccountId);

                    // 转换firstId
                    Integer oldFirstId = source.getFirstId();
                    if (oldFirstId != null) {
                        Integer newFirstId = mappingCache.getNewAccountId(oldFirstId);
                        if (newFirstId == null) {
                            throw new DataMigrationException("找不到firstId映射关系：" + oldFirstId);
                        }
                        target.setFirstId(newFirstId);
                    }

                    // 清空recordId，让数据库自动生成
                    target.setRecordId(null);

                    targetData.add(target);
                }

                // 3. 开启事务：写入 + 验证
                int inserted = transactionService.insertAndValidateUserToAgent(targetData);

                totalMigrated += inserted;
                batchCount++;
                offset += batchSize;
            }

            return MigrationResponse.builder()
                    .success(true)
                    .message("用户区域关联数据迁移成功")
                    .totalCount(totalCount)
                    .migratedCount(totalMigrated)
                    .batchCount(batchCount)
                    .build();

        } catch (Exception e) {
            return MigrationResponse.builder()
                    .success(false)
                    .message("用户区域关联数据迁移失败：" + e.getMessage())
                    .totalCount(totalCount)
                    .migratedCount(totalMigrated)
                    .batchCount(batchCount)
                    .build();
        }
    }

}
