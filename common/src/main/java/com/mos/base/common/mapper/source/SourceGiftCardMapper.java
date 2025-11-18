package com.mos.base.common.mapper.source;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mos.base.common.entity.GiftCard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 品牌A礼品卡表Mapper
 *
 * @author ly
 */
@Mapper
public interface SourceGiftCardMapper extends BaseMapper<GiftCard> {

    /**
     * 分页查询礼品卡数据
     *
     * @param offset 偏移量
     * @param limit 每页数量
     * @return 礼品卡列表
     */
    @Select("SELECT * FROM giftcard LIMIT #{offset}, #{limit}")
    List<GiftCard> selectByPage(Integer offset, Integer limit);

    /**
     * 查询总数
     *
     * @return 总数
     */
    @Select("SELECT COUNT(*) FROM giftcard")
    Integer selectTotalCount();
}
