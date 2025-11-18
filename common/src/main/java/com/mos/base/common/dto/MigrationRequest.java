package com.mos.base.common.dto;

import lombok.Data;

/**
 * 迁移请求DTO
 *
 * @author ly
 */
@Data
public class MigrationRequest {

    /**
     * 批次大小
     */
    private Integer batchSize = 1000;
}
