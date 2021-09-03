package com.huakai.controller;

import com.huakai.error.BussinesssError;
import com.huakai.error.ErrorEnum;
import com.huakai.mapper.dataobject.OrderDo;
import com.huakai.mapper.dataobject.UserDO;
import com.huakai.response.CommonReturnType;
import com.huakai.service.OrderService;
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

    @PostMapping("/createorder")
    public CommonReturnType createOrder(@RequestParam("itemId")Integer itemId, @RequestParam("amount")Integer amount) throws BussinesssError {


        Boolean isLogin = (Boolean)request.getSession().getAttribute("isLogin");
        if(isLogin == null || !isLogin)
             throw new BussinesssError(ErrorEnum.USER_NOT_LOGIN);

        UserDO userDO = (UserDO)request.getSession().getAttribute("loginUser");

        OrderDo orderDo = orderService.createOrder(userDO.getId(), itemId, amount);
        return CommonReturnType.create(orderDo);
    }
}
