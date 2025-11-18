package com.mos.base.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 骑行会员实体
 *
 * @author ly
 */
@Data
@TableName("ridecard")
public class RideCard {

    @TableId(value = "rideCardId", type = IdType.AUTO)
    private Integer rideCardId;

    @TableField("accountId")
    private Integer accountId;

    @TableField("name")
    private String name;

    @TableField("type")
    private Integer type;

    @TableField("createTime")
    private LocalDateTime createTime;

    @TableField("money")
    private BigDecimal money;

    @TableField("timeCount")
    private Integer timeCount;

    @TableField("maxRide")
    private Integer maxRide;

    @TableField("userType")
    private Integer userType;

    @TableField("enable")
    private Boolean enable;

    @TableField("description")
    private String description;

    @TableField("remark")
    private String remark;

    @TableField("ridecardType")
    private Integer ridecardType;

    @TableField("discountDes")
    private String discountDes;

    @TableField("price")
    private Integer price;

    @TableField("feeId")
    private Integer feeId;

    @TableField("vaildDays")
    private Integer vaildDays;

    @TableField("vaildTime")
    private String vaildTime;

    @TableField("validWeek")
    private String validWeek;

    @TableField("sort")
    private Integer sort;

    @TableField("updateTime")
    private LocalDateTime updateTime;

    @TableField("shelfStartTime")
    private LocalDateTime shelfStartTime;

    @TableField("shelfEndTime")
    private LocalDateTime shelfEndTime;
}
