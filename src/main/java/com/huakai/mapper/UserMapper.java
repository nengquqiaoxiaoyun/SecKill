package com.huakai.mapper;

import com.huakai.dto.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author: huakaimay
 * @since: 2021-08-30
 */
public interface UserMapper {

    @Select("select * from User")
    List<User> getUser();
}
