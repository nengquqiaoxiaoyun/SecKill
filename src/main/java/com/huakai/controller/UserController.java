package com.huakai.controller;

import com.huakai.controller.dto.UserDto;
import com.huakai.error.BussinesssError;
import com.huakai.error.ErrorEnum;
import com.huakai.response.CommonReturnType;
import com.huakai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    private CommonReturnType getUser(@RequestParam("id") Integer id) throws BussinesssError {
        UserDto user = userService.getUserById(id);

        if (user == null) {
            throw new BussinesssError(ErrorEnum.PARAMTER_VALIDATION_ERROR, "邮箱不存在");
        }

        return CommonReturnType.create(user);
    }

}
