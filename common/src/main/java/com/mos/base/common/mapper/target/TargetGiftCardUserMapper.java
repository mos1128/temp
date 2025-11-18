package com.mos.base.common.mapper.target;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mos.base.common.entity.GiftCardUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 品牌B礼品卡用户余额表Mapper
 *
 * @author ly
 */
@Mapper
public interface TargetGiftCardUserMapper extends BaseMapper<GiftCardUser> {

    /**
     * 批量插入礼品卡用户余额数据
     *
     * @param giftCardUsers 余额列表
     * @return 插入数量
     */
    @Insert({
            "<script>",
            "INSERT INTO giftcarduser (accountId, userId, money, updateTime) VALUES ",
            "<foreach collection='list' item='item' separator=','>",
            "(#{item.accountId}, #{item.userId}, #{item.money}, #{item.updateTime})",
            "</foreach>",
            "</script>"
    })
    int batchInsert(List<GiftCardUser> giftCardUsers);
}
