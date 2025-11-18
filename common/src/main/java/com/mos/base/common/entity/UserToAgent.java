package com.mos.base.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户和运营区域关联表实体
 *
 * @author ly
 */
@Data
@TableName("usertoagent")
public class UserToAgent {

    @TableId(value = "recordId", type = IdType.AUTO)
    private Integer recordId;

    @TableField("userId")
    private Integer userId;

    @TableField("accountId")
    private Integer accountId;

    @TableField("updateDt")
    private LocalDateTime updateDt;

    @TableField("firstId")
    private Integer firstId;
}
