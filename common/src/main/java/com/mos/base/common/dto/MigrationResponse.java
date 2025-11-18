package com.mos.base.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 迁移响应DTO
 *
 * @author ly
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MigrationResponse {

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 消息
     */
    private String message;

    /**
     * 总数据量
     */
    private Integer totalCount;

    /**
     * 已迁移数量
     */
    private Integer migratedCount;

    /**
     * 批次数量
     */
    private Integer batchCount;

    /**
     * 映射关系（可选）
     */
    private Map<?, ?> mapping;
}
