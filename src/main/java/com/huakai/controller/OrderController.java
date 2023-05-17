package com.huakai.controller;

import com.google.gson.Gson;
import com.huakai.config.RedisService;
import com.huakai.error.BussinesssError;
import com.huakai.error.ErrorEnum;
import com.huakai.mapper.dataobject.OrderDo;
import com.huakai.mapper.dataobject.UserDO;
import com.huakai.response.CommonReturnType;
import com.huakai.service.OrderService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: huakaimay
 * @since: 2021-09-02
 */
@RestController
@RequestMapping("/order")
@CrossOrigin(allowCredentials = "true", originPatterns = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RedisService redisService;

    @PostMapping("/createorder")
    public CommonReturnType createOrder(@RequestParam("itemId")Integer itemId,
                                        @RequestParam(value = "promoId", required = false)Integer promoId,
                                        @RequestParam("amount")Integer amount,
                                        @RequestParam("token") String token) throws BussinesssError {


        if(StringUtils.isEmpty(token))
            throw new BussinesssError(ErrorEnum.USER_NOT_LOGIN);

        String userDOStr = redisService.get(token);
        // Boolean isLogin = (Boolean)request.getSession().getAttribute("isLogin");
        if(ObjectUtils.isEmpty(userDOStr))
             throw new BussinesssError(ErrorEnum.USER_NOT_LOGIN);

        // UserDO userDO = (UserDO)request.getSession().getAttribute("loginUser");

        UserDO userDO = new Gson().fromJson(userDOStr, UserDO.class);
        OrderDo orderDo = orderService.createOrder(userDO.getId(), itemId, promoId, amount);
        return CommonReturnType.create(orderDo);
    }
}
