package com.huakai.service;

import com.huakai.controller.dto.UserDto;
import org.springframework.stereotype.Service;

/**
 * @author: huakaimay
 * @since: 2021-08-30
 */
@Service
public interface UserService {


    UserDto getUserById(Integer id);


    void register(UserDto userDto);

}
