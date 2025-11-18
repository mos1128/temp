package com.mos.base.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户骑行会员实体
 *
 * @author ly
 */
@Data
@TableName("ridecarduser")
public class RideCardUser {

    @TableField("userId")
    private Integer userId;

    @TableField("accountId")
    private Integer accountId;

    @TableField("rideCardId")
    private Integer rideCardId;

    @TableField("buyTime")
    private LocalDateTime buyTime;

    @TableField("startDate")
    private LocalDate startDate;

    @TableField("endDate")
    private LocalDate endDate;

    @TableField("useCount")
    private Integer useCount;

    @TableField("isValid")
    private Boolean isValid;

    @TableField("currentNum")
    private Integer currentNum;

    @TableField("updateTime")
    private LocalDateTime updateTime;

    @TableField("orderNO")
    private String orderNO;

    @TableField("payState")
    private Integer payState;

    @TableField("remark")
    private String remark;
}
