package com.mos.base.common.mapper.target;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mos.base.common.entity.UserToAgent;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 品牌B用户区域关联表Mapper
 *
 * @author ly
 */
@Mapper
public interface TargetUserToAgentMapper extends BaseMapper<UserToAgent> {

    /**
     * 批量插入用户区域关联数据
     *
     * @param userToAgents 关联列表
     * @return 插入数量
     */
    @Insert({
            "<script>",
            "INSERT INTO usertoagent (userId, accountId, updateDt, firstId) VALUES ",
            "<foreach collection='list' item='item' separator=','>",
            "(#{item.userId}, #{item.accountId}, #{item.updateDt}, #{item.firstId})",
            "</foreach>",
            "</script>"
    })
    int batchInsert(List<UserToAgent> userToAgents);
}
