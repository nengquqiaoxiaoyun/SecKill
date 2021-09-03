package com.huakai.service;

import com.huakai.error.BussinesssError;
import com.huakai.mapper.dataobject.OrderDo;

/**
 * @author: huakaimay
 * @since: 2021-09-02
 */
public interface OrderService {

    OrderDo createOrder(Integer uesrId, Integer itemId, Integer amount) throws BussinesssError;

}
