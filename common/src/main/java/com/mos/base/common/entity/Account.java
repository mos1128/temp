package com.mos.base.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 管理账户实体
 *
 * @author ly
 */
@Data
@TableName("account")
public class Account {

    @TableId(value = "accountId", type = IdType.AUTO)
    private Integer accountId;

    @TableField("type")
    private Integer type;

    @TableField("parentId")
    private Integer parentId;

    @TableField("name")
    private String name;

    @TableField("password")
    private String password;

    @TableField("phone")
    private String phone;

    @TableField("email")
    private String email;

    @TableField("remark")
    private String remark;

    @TableField("joinTime")
    private LocalDateTime joinTime;

    @TableField("updateTime")
    private LocalDateTime updateTime;

    @TableField("country")
    private Integer country;

    @TableField("delFlag")
    private Integer delFlag;
}
