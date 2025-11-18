package com.mos.base.common.mapper.source;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mos.base.common.entity.RideCardUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 品牌A骑行卡用户关联表Mapper
 *
 * @author ly
 */
@Mapper
public interface SourceRideCardUserMapper extends BaseMapper<RideCardUser> {

    /**
     * 分页查询骑行卡用户关联数据
     *
     * @param offset 偏移量
     * @param limit 每页数量
     * @return 关联列表
     */
    @Select("SELECT * FROM ridecarduser LIMIT #{offset}, #{limit}")
    List<RideCardUser> selectByPage(Integer offset, Integer limit);

    /**
     * 查询总数
     *
     * @return 总数
     */
    @Select("SELECT COUNT(*) FROM ridecarduser")
    Integer selectTotalCount();
}
