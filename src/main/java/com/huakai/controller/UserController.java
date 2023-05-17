package com.huakai.controller;

import com.alibaba.druid.util.StringUtils;
import com.google.gson.Gson;
import com.huakai.config.RedisService;
import com.huakai.controller.dto.UserDto;
import com.huakai.error.BussinesssError;
import com.huakai.error.ErrorEnum;
import com.huakai.mapper.dataobject.UserDO;
import com.huakai.response.CommonReturnType;
import com.huakai.service.UserService;
import com.huakai.valiator.ValidationResult;
import com.huakai.valiator.ValidatorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

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

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private RedisService redisService;

    @PostMapping(value = "/login")
    public CommonReturnType login(@RequestParam("telephone") String telephone,
                                  @RequestParam("password") String password) throws BussinesssError {


        // 参数校验
        if (org.apache.commons.lang3.StringUtils.isEmpty(telephone)
                || org.apache.commons.lang3.StringUtils.isEmpty(password))
            throw new BussinesssError(ErrorEnum.PARAMTER_VALIDATION_ERROR);

        // 根据手机号查询用于是否存在，不存在返回提示
        UserDO userDO = userService.getUserByTelephone(telephone);
        if (userDO == null) {
            throw new BussinesssError(ErrorEnum.LOGIN_FAIL);
        }

        // 用户存在，验证密码
        if (!userService.userExist(userDO, password)) {
            throw new BussinesssError(ErrorEnum.LOGIN_FAIL);
        }

        /*
        这边修改成：若用户登陆成功酱对应的登陆信息和token一起存入redis
        记录登录状态为true
        将用户信息存入session
         */
        // request.getSession().setAttribute("isLogin", true);
        // request.getSession().setAttribute("loginUser", userDO);


        // 生成登陆凭证token
        String uuidToken = UUID.randomUUID().toString().replace("-", "");
        // 建立token和用户登陆状态之间的联系
        redisService.put(uuidToken, new Gson().toJson(userDO), 1, TimeUnit.HOURS);


        return CommonReturnType.create(uuidToken);
    }


    /**
     * {@code @RequestBody }只能接受json格式，
     * 如果前端没有明确规定{@code Content-Type: "application/json"}则会报错
     * ajax不规定格式的话默认 Content-Type: "application/x-www-form-urlencoded"
     */
    @PostMapping(value = "/register")
    public CommonReturnType register(UserDto userDto, @RequestParam("otpCode") String otpCode) throws BussinesssError {

        ValidationResult validate = validator.validate(userDto);
        if(validate.isHasErrors())
            throw new BussinesssError(ErrorEnum.PARAMTER_VALIDATION_ERROR, validate.getErrMsg());

        String inSessionOtpCode = (String) request.getSession().getAttribute(userDto.getTelephone());

        System.out.println("用户输入otp: " + otpCode + "系统otp: " + inSessionOtpCode);
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
    public CommonReturnType getOpt(@RequestParam("telphone")  String telphone) throws BussinesssError {

        // 手机号校验
        String reg = "(?:(?:\\+|00)86)?1(?:(?:3[\\d])|(?:4[5-79])|(?:5[0-35-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\\d])|(?:9[189]))\\d{8}";
        boolean matches = Pattern.matches(reg, telphone);

        if(!matches) {
            throw new BussinesssError(ErrorEnum.PARAMTER_VALIDATION_ERROR, "手机号码格式错误");
        }

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
