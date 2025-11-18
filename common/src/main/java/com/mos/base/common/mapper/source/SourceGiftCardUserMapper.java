package com.mos.base.common.mapper.source;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mos.base.common.entity.GiftCardUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 品牌A礼品卡用户余额表Mapper
 *
 * @author ly
 */
@Mapper
public interface SourceGiftCardUserMapper extends BaseMapper<GiftCardUser> {

    /**
     * 分页查询礼品卡用户余额数据
     *
     * @param offset 偏移量
     * @param limit 每页数量
     * @return 余额列表
     */
    @Select("SELECT * FROM giftcarduser LIMIT #{offset}, #{limit}")
    List<GiftCardUser> selectByPage(Integer offset, Integer limit);

    /**
     * 查询总数
     *
     * @return 总数
     */
    @Select("SELECT COUNT(*) FROM giftcarduser")
    Integer selectTotalCount();
}
