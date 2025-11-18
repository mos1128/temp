package com.mos.base.common.exception;

/**
 * 数据迁移异常
 * 用于数据迁移过程中的业务异常
 *
 * @author ly
 */
public class DataMigrationException extends RuntimeException {

    public DataMigrationException(String message) {
        super(message);
    }

    public DataMigrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
