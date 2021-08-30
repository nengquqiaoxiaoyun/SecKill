package com.huakai.controller;

import com.huakai.controller.dto.UserDto;
import com.huakai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: huakaimay
 * @since: 2021-08-30
 */
@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;

    @GetMapping("/get")
    private UserDto getUser(@RequestParam("id")Integer id) {
        return userService.getUserById(id);
    }

}
