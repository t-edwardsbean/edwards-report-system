package com.baidu.mapper;

import com.baidu.model.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by edwardsbean on 2014/8/23 0023.
 */
public interface UserMapper {
    @Select("SELECT * FROM users WHERE id = #{userId}")
    User getUser(@Param("userId") String userId);
}