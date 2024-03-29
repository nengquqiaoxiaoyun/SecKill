package com.huakai.service;

import com.huakai.controller.dto.PromoDto;
import com.huakai.error.BussinesssError;

/**
 * @author: huakaimay
 * @since: 2021-09-03
 */
public interface PromoService {

    /**
     * 根据itemId获取秒杀活动信息
     */
    PromoDto getPromoByItemId(Integer itemId);

    /**
     * 生成秒杀令牌
     */
    String generateScToken(Integer promoId, Integer itemId, Integer userId) throws BussinesssError;

    /**
     * 活动发布
     * @param id
     */
    void publishPromo(Integer id) throws BussinesssError;

}
