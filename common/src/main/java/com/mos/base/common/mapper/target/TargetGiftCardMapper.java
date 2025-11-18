package com.mos.base.common.mapper.target;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mos.base.common.entity.GiftCard;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import java.util.List;

/**
 * 品牌B礼品卡表Mapper
 *
 * @author ly
 */
@Mapper
public interface TargetGiftCardMapper extends BaseMapper<GiftCard> {

    /**
     * 批量插入礼品卡数据
     *
     * @param giftCards 礼品卡列表
     * @return 插入数量
     */
    @Insert({
            "<script>",
            "INSERT INTO giftcard (accountId, cardType, cardNO, money, couponId, couponCount, couponExpireDays, expireDate, createTime, useTime, userId, remark, memberFeeDays) VALUES ",
            "<foreach collection='list' item='item' separator=','>",
            "(#{item.accountId}, #{item.cardType}, #{item.cardNO}, #{item.money}, #{item.couponId}, #{item.couponCount}, #{item.couponExpireDays}, #{item.expireDate}, #{item.createTime}, #{item.useTime}, #{item.userId}, #{item.remark}, #{item.memberFeeDays})",
            "</foreach>",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "giftcardId", keyColumn = "giftcardId")
    int batchInsert(List<GiftCard> giftCards);
}
