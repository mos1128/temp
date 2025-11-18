package com.mos.base.common.service.impl;
import com.mos.base.common.exception.DataMigrationException;

import com.mos.base.common.cache.MappingCache;
import com.mos.base.common.dto.MigrationRequest;
import com.mos.base.common.dto.MigrationResponse;
import com.mos.base.common.entity.GiftCard;
import com.mos.base.common.mapper.source.SourceGiftCardMapper;
import com.mos.base.common.mapper.target.TargetGiftCardMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 礼品卡表迁移服务
 *
 * @author ly
 */
@Service
public class GiftCardMigrationServiceImpl {

    @Autowired
    private SourceGiftCardMapper sourceGiftCardMapper;

    @Autowired
    private MappingCache mappingCache;

    @Autowired
    private TransactionService transactionService;

    /**
     * 迁移礼品卡数据
     *
     * @param request 迁移请求
     * @return 迁移响应
     */
    public MigrationResponse migrateGiftCard(MigrationRequest request) {
        Integer batchSize = request.getBatchSize();
        Integer totalCount = sourceGiftCardMapper.selectTotalCount();
        int offset = 0;
        int totalMigrated = 0;
        int batchCount = 0;

        try {
            while (offset < totalCount) {
                // 1. 读取数据（无事务）
                List<GiftCard> sourceData = sourceGiftCardMapper.selectByPage(offset, batchSize);
                if (sourceData == null || sourceData.isEmpty()) {
                    break;
                }

                // 2. 处理数据（无事务）
                List<GiftCard> targetData = new ArrayList<>();
                List<Integer> oldGiftCardIds = new ArrayList<>();

                for (GiftCard source : sourceData) {
                    GiftCard target = new GiftCard();
                    BeanUtils.copyProperties(source, target);

                    // 转换accountId
                    Integer oldAccountId = source.getAccountId();
                    Integer newAccountId = mappingCache.getNewAccountId(oldAccountId);
                    if (newAccountId == null) {
                        throw new DataMigrationException("找不到accountId映射关系：" + oldAccountId);
                    }
                    target.setAccountId(newAccountId);

                    // 转换userId（如果已使用）
                    Integer oldUserId = source.getUserId();
                    if (oldUserId != null) {
                        Integer newUserId = mappingCache.getNewUserId(oldUserId);
                        if (newUserId == null) {
                            throw new DataMigrationException("找不到userId映射关系：" + oldUserId);
                        }
                        target.setUserId(newUserId);
                    }

                    // 清空giftcardId，让数据库自动生成
                    target.setGiftcardId(null);

                    targetData.add(target);
                    oldGiftCardIds.add(source.getGiftcardId());
                }

                // 3. 开启事务：写入 + 验证
                int inserted = transactionService.insertAndValidateGiftCard(targetData, oldGiftCardIds);

                totalMigrated += inserted;
                batchCount++;
                offset += batchSize;
            }

            return MigrationResponse.builder()
                    .success(true)
                    .message("礼品卡数据迁移成功")
                    .totalCount(totalCount)
                    .migratedCount(totalMigrated)
                    .batchCount(batchCount)
                    .mapping(mappingCache.getGiftCardMapping())
                    .build();

        } catch (Exception e) {
            return MigrationResponse.builder()
                    .success(false)
                    .message("礼品卡数据迁移失败：" + e.getMessage())
                    .totalCount(totalCount)
                    .migratedCount(totalMigrated)
                    .batchCount(batchCount)
                    .build();
        }
    }

}
