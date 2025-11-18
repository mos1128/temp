package com.mos.base.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 一线用户实体
 *
 * @author ly
 */
@Data
@TableName("user")
public class User {

    @TableId(value = "userId", type = IdType.AUTO)
    private Integer userId;

    @TableField("accountId")
    private Integer accountId;

    @TableField("country")
    private String country;

    @TableField("phone")
    private String phone;

    @TableField("sex")
    private String sex;

    @TableField("birthday")
    private LocalDate birthday;

    @TableField("email")
    private String email;

    @TableField("depositState")
    private Integer depositState;

    @TableField("depositMoney")
    private Integer depositMoney;

    @TableField("money")
    private Integer money;

    @TableField("joinTime")
    private LocalDateTime joinTime;

    @TableField("updateTime")
    private LocalDateTime updateTime;

    @TableField("remark")
    private String remark;

    @TableField("imageUrl")
    private String imageUrl;

    @TableField("idNO")
    private String idNO;

    @TableField("name")
    private String name;

    @TableField("nameAuth")
    private Boolean nameAuth;

    @TableField("studentAuth")
    private Boolean studentAuth;

    @TableField("studentIdNo")
    private String studentIdNo;

    @TableField("depositDate")
    private LocalDate depositDate;

    @TableField("password")
    private String password;

    @TableField("rideMoney")
    private Integer rideMoney;

    @TableField("ridingScore")
    private Integer ridingScore;
}
