package com.mos.base.common.mapper.source;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mos.base.common.entity.RideCard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 品牌A骑行卡表Mapper
 *
 * @author ly
 */
@Mapper
public interface SourceRideCardMapper extends BaseMapper<RideCard> {

    /**
     * 分页查询骑行卡数据
     *
     * @param offset 偏移量
     * @param limit 每页数量
     * @return 骑行卡列表
     */
    @Select("SELECT * FROM ridecard LIMIT #{offset}, #{limit}")
    List<RideCard> selectByPage(Integer offset, Integer limit);

    /**
     * 查询总数
     *
     * @return 总数
     */
    @Select("SELECT COUNT(*) FROM ridecard")
    Integer selectTotalCount();
}
