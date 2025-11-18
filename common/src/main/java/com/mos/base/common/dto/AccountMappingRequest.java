package com.mos.base.common.dto;

import lombok.Data;

import java.util.Map;

/**
 * Account映射请求DTO
 *
 * @author ly
 */
@Data
public class AccountMappingRequest {

    /**
     * 旧accountId -> 新accountId的映射
     */
    private Map<Integer, Integer> accountMapping;
}
