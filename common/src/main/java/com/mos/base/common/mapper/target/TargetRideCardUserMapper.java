package com.mos.base.common.mapper.target;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mos.base.common.entity.RideCardUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 品牌B骑行卡用户关联表Mapper
 *
 * @author ly
 */
@Mapper
public interface TargetRideCardUserMapper extends BaseMapper<RideCardUser> {

    /**
     * 批量插入骑行卡用户关联数据
     *
     * @param rideCardUsers 关联列表
     * @return 插入数量
     */
    @Insert({
            "<script>",
            "INSERT INTO ridecarduser (userId, accountId, rideCardId, buyTime, startDate, endDate, useCount, isValid, currentNum, updateTime, orderNO, payState, remark) VALUES ",
            "<foreach collection='list' item='item' separator=','>",
            "(#{item.userId}, #{item.accountId}, #{item.rideCardId}, #{item.buyTime}, #{item.startDate}, #{item.endDate}, #{item.useCount}, #{item.isValid}, #{item.currentNum}, #{item.updateTime}, #{item.orderNO}, #{item.payState}, #{item.remark})",
            "</foreach>",
            "</script>"
    })
    int batchInsert(List<RideCardUser> rideCardUsers);
}
