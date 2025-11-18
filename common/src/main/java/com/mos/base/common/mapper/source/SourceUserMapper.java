package com.mos.base.common.mapper.source;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mos.base.common.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 品牌A用户表Mapper
 *
 * @author ly
 */
@Mapper
public interface SourceUserMapper extends BaseMapper<User> {

    /**
     * 分页查询用户数据
     *
     * @param offset 偏移量
     * @param limit 每页数量
     * @return 用户列表
     */
    @Select("SELECT * FROM user LIMIT #{offset}, #{limit}")
    List<User> selectByPage(Integer offset, Integer limit);

    /**
     * 查询用户总数
     *
     * @return 总数
     */
    @Select("SELECT COUNT(*) FROM user")
    Integer selectTotalCount();
}
