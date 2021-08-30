package com.huakai.controller;

import com.huakai.dto.User;
import com.huakai.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: huakaimay
 * @since: 2021-08-30
 */
@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/user")
    public String getUser() {
        List<User> user = userMapper.getUser();
        return "name: " + user.get(0).getName() + ", age: " + user.get(0).getAge();
    }

}
