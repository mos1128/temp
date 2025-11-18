package com.mos.base.common.service.impl;

import com.mos.base.common.cache.MappingCache;
import com.mos.base.common.dto.MigrationRequest;
import com.mos.base.common.dto.MigrationResponse;
import com.mos.base.common.entity.User;
import com.mos.base.common.mapper.source.SourceUserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户表迁移服务
 *
 * @author ly
 */
@Service
public class UserMigrationServiceImpl {

    @Autowired
    private SourceUserMapper sourceUserMapper;

    @Autowired
    private MappingCache mappingCache;

    @Autowired
    private TransactionService transactionService;

    /**
     * 迁移用户数据
     *
     * @param request 迁移请求
     * @return 迁移响应
     */
    public MigrationResponse migrateUser(MigrationRequest request) {
        Integer batchSize = request.getBatchSize();
        Integer totalCount = sourceUserMapper.selectTotalCount();
        int offset = 0;
        int totalMigrated = 0;
        int batchCount = 0;

        try {
            while (offset < totalCount) {
                // 1. 读取数据（无事务）
                List<User> sourceUsers = sourceUserMapper.selectByPage(offset, batchSize);
                if (sourceUsers == null || sourceUsers.isEmpty()) {
                    break;
                }

                // 2. 处理数据（无事务）
                List<User> targetUsers = new ArrayList<>();
                List<Integer> oldUserIds = new ArrayList<>();
                List<String> oldPhones = new ArrayList<>();

                for (User sourceUser : sourceUsers) {
                    User targetUser = new User();
                    BeanUtils.copyProperties(sourceUser, targetUser);

                    // 转换accountId
                    Integer oldAccountId = sourceUser.getAccountId();
                    Integer newAccountId = mappingCache.getNewAccountId(oldAccountId);
                    if (newAccountId == null) {
                        throw new RuntimeException("找不到accountId映射关系：" + oldAccountId);
                    }
                    targetUser.setAccountId(newAccountId);

                    // 清空userId，让数据库自动生成
                    targetUser.setUserId(null);

                    targetUsers.add(targetUser);
                    oldUserIds.add(sourceUser.getUserId());
                    oldPhones.add(sourceUser.getPhone());
                }

                // 3. 开启事务：写入 + 验证（在同一个事务中）
                int inserted = transactionService.insertAndValidateUser(targetUsers, oldUserIds, oldPhones);

                totalMigrated += inserted;
                batchCount++;
                offset += batchSize;
            }

            return MigrationResponse.builder()
                    .success(true)
                    .message("用户数据迁移成功")
                    .totalCount(totalCount)
                    .migratedCount(totalMigrated)
                    .batchCount(batchCount)
                    .mapping(mappingCache.getUserMapping())
                    .build();

        } catch (Exception e) {
            return MigrationResponse.builder()
                    .success(false)
                    .message("用户数据迁移失败：" + e.getMessage())
                    .totalCount(totalCount)
                    .migratedCount(totalMigrated)
                    .batchCount(batchCount)
                    .build();
        }
    }

}
