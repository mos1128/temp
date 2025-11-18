package com.mos.base.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 礼品卡实体
 *
 * @author ly
 */
@Data
@TableName("giftcard")
public class GiftCard {

    @TableId(value = "giftcardId", type = IdType.AUTO)
    private Integer giftcardId;

    @TableField("accountId")
    private Integer accountId;

    @TableField("cardType")
    private Integer cardType;

    @TableField("cardNO")
    private String cardNO;

    @TableField("money")
    private Integer money;

    @TableField("couponId")
    private Integer couponId;

    @TableField("couponCount")
    private Integer couponCount;

    @TableField("couponExpireDays")
    private Integer couponExpireDays;

    @TableField("expireDate")
    private LocalDateTime expireDate;

    @TableField("createTime")
    private LocalDateTime createTime;

    @TableField("useTime")
    private LocalDateTime useTime;

    @TableField("userId")
    private Integer userId;

    @TableField("remark")
    private String remark;

    @TableField("memberFeeDays")
    private Integer memberFeeDays;
}
