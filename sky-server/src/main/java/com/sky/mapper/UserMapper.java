package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Date：2024/9/10  16:01
 * Description：TODO
 *
 * @author Leon
 * @version 1.0
 */
@Mapper
public interface UserMapper {

    /**
     * 根据openid查询用户
     * @param openId
     * @return
     */
    @Select("select * from user where openid = #{openid}")
    User getByOpenId(String openId);

    void insert(User user);
}
