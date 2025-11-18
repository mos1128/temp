package com.mos.base.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 礼品卡用户余额实体
 *
 * @author ly
 */
@Data
@TableName("giftcarduser")
public class GiftCardUser {

    @TableField("accountId")
    private Integer accountId;

    @TableField("userId")
    private Integer userId;

    @TableField("money")
    private Integer money;

    @TableField("updateTime")
    private LocalDateTime updateTime;
}
