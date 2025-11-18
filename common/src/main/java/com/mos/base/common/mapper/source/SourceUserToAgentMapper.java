package com.mos.base.common.mapper.source;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mos.base.common.entity.UserToAgent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 品牌A用户区域关联表Mapper
 *
 * @author ly
 */
@Mapper
public interface SourceUserToAgentMapper extends BaseMapper<UserToAgent> {

    /**
     * 分页查询用户区域关联数据
     *
     * @param offset 偏移量
     * @param limit 每页数量
     * @return 关联列表
     */
    @Select("SELECT * FROM usertoagent LIMIT #{offset}, #{limit}")
    List<UserToAgent> selectByPage(Integer offset, Integer limit);

    /**
     * 查询总数
     *
     * @return 总数
     */
    @Select("SELECT COUNT(*) FROM usertoagent")
    Integer selectTotalCount();
}
