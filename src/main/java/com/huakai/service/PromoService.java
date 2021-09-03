package com.huakai.service;

import com.huakai.controller.dto.PromoDto;

/**
 * @author: huakaimay
 * @since: 2021-09-03
 */
public interface PromoService {

    /**
     * 根据itemId获取秒杀活动信息
     */
    PromoDto getPromoByItemId(Integer itemId);

}
