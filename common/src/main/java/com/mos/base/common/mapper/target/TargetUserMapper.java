package com.mos.base.common.mapper.target;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mos.base.common.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import java.util.List;

/**
 * 品牌B用户表Mapper
 *
 * @author ly
 */
@Mapper
public interface TargetUserMapper extends BaseMapper<User> {

    /**
     * 批量插入用户数据
     *
     * @param users 用户列表
     * @return 插入数量
     */
    @Insert({
            "<script>",
            "INSERT INTO user (accountId, country, phone, sex, birthday, email, depositState, depositMoney, money, joinTime, updateTime, remark, imageUrl, idNO, name, nameAuth, studentAuth, studentIdNo, depositDate, password, rideMoney, ridingScore) VALUES ",
            "<foreach collection='list' item='item' separator=','>",
            "(#{item.accountId}, #{item.country}, #{item.phone}, #{item.sex}, #{item.birthday}, #{item.email}, #{item.depositState}, #{item.depositMoney}, #{item.money}, #{item.joinTime}, #{item.updateTime}, #{item.remark}, #{item.imageUrl}, #{item.idNO}, #{item.name}, #{item.nameAuth}, #{item.studentAuth}, #{item.studentIdNo}, #{item.depositDate}, #{item.password}, #{item.rideMoney}, #{item.ridingScore})",
            "</foreach>",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "userId")
    int batchInsert(List<User> users);
}
