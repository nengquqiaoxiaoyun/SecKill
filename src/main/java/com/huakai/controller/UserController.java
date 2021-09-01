package com.huakai.controller;

import com.alibaba.druid.util.StringUtils;
import com.huakai.controller.dto.UserDto;
import com.huakai.error.BussinesssError;
import com.huakai.error.ErrorEnum;
import com.huakai.response.CommonReturnType;
import com.huakai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Random;

/**
 * @author: huakaimay
 * @since: 2021-08-30
 */
@RestController
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true", originPatterns = "*")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;


    /**
     * {@code @RequestBody }只能接受json格式，
     * 如果前端没有明确规定{@code Content-Type: "application/json"}则会报错
     * ajax不规定格式的话默认 Content-Type: "application/x-www-form-urlencoded"
     */
    @PostMapping(value = "/register")
    public CommonReturnType register(UserDto userDto, @RequestParam("otpCode") String otpCode) throws BussinesssError {

        String inSessionOtpCode = (String) request.getSession().getAttribute(userDto.getTelephone());
        if (!StringUtils.equals(otpCode, inSessionOtpCode)) {
             throw new BussinesssError(ErrorEnum.PARAMTER_VALIDATION_ERROR, "otp验证失败");
        }

        userService.register(userDto);

        return CommonReturnType.create(null);
    }

    @GetMapping("/get")
    public CommonReturnType getUser(@RequestParam("id") Integer id) throws BussinesssError {
        UserDto user = userService.getUserById(id);

        if (user == null) {
            throw new BussinesssError(ErrorEnum.PARAMTER_VALIDATION_ERROR, "邮箱不存在");
        }

        return CommonReturnType.create(user);
    }

    @PostMapping(value = "/getotp", consumes = "application/x-www-form-urlencoded")
    public CommonReturnType getOpt(@RequestParam("telphone") String telphone) {
        // 生成随机数
        Random random = new Random();
        int num = random.nextInt(99999);
        num += 10000;
        String optCode = String.valueOf(num);

        // 和手机号关联（分布式中通过redis来实现，每次获取都更新）这边使用HttpSession
        HttpSession session = request.getSession();
        session.setAttribute(telphone, optCode);

        // 将opt代码通过短信通道发送给用户 省略
        System.out.println("telephone: " + telphone + ", otpCode: " + optCode);

        return CommonReturnType.create(null);

    }

}
