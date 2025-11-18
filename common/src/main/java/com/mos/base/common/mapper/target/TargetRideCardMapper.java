package com.mos.base.common.mapper.target;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mos.base.common.entity.RideCard;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import java.util.List;

/**
 * 品牌B骑行卡表Mapper
 *
 * @author ly
 */
@Mapper
public interface TargetRideCardMapper extends BaseMapper<RideCard> {

    /**
     * 批量插入骑行卡数据
     *
     * @param rideCards 骑行卡列表
     * @return 插入数量
     */
    @Insert({
            "<script>",
            "INSERT INTO ridecard (accountId, name, type, createTime, money, timeCount, maxRide, userType, enable, description, remark, ridecardType, discountDes, price, feeId, vaildDays, vaildTime, validWeek, sort, updateTime, shelfStartTime, shelfEndTime) VALUES ",
            "<foreach collection='list' item='item' separator=','>",
            "(#{item.accountId}, #{item.name}, #{item.type}, #{item.createTime}, #{item.money}, #{item.timeCount}, #{item.maxRide}, #{item.userType}, #{item.enable}, #{item.description}, #{item.remark}, #{item.ridecardType}, #{item.discountDes}, #{item.price}, #{item.feeId}, #{item.vaildDays}, #{item.vaildTime}, #{item.validWeek}, #{item.sort}, #{item.updateTime}, #{item.shelfStartTime}, #{item.shelfEndTime})",
            "</foreach>",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "rideCardId", keyColumn = "rideCardId")
    int batchInsert(List<RideCard> rideCards);
}
