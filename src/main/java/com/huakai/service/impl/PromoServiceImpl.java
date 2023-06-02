package com.huakai.service.impl;

import com.huakai.config.RedisService;
import com.huakai.controller.dto.ItemDto;
import com.huakai.controller.dto.PromoDto;
import com.huakai.error.BussinesssError;
import com.huakai.error.ErrorEnum;
import com.huakai.mapper.PromoDoMapper;
import com.huakai.mapper.dataobject.PromoDo;
import com.huakai.service.ItemService;
import com.huakai.service.PromoService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author: huakaimay
 * @since: 2021-09-03
 */
@Service
public class PromoServiceImpl implements PromoService {

    @Autowired
    private PromoDoMapper promoDoMapper;

    @Autowired
    private ItemService itemService;

    @Autowired
    private RedisService redisService;

    @Override
    public PromoDto getPromoByItemId(Integer itemId) {
        if (itemId == null)
            return null;

        PromoDto promoDto = new PromoDto();

        PromoDo promoDo = promoDoMapper.selectByItemId(itemId);
        if (promoDo == null)
            return null;

        BeanUtils.copyProperties(promoDo, promoDto);
        LocalDateTime doStartDate = LocalDateTime.ofInstant(promoDo.getStartDate().toInstant(), ZoneId.systemDefault());
        LocalDateTime doEndDate = LocalDateTime.ofInstant(promoDo.getEndDate().toInstant(), ZoneId.systemDefault());


        promoDto.setStartDate(doStartDate);

        promoDto.setEndDate(doEndDate);

        // 校验活动状态
        // 早于活动开始时间 1: 未开始
        if (LocalDateTime.now().isBefore(promoDto.getStartDate())) {
            promoDto.setStatus(1);
            // 活动已经结束 3: 结束
        } else if (LocalDateTime.now().isAfter(promoDto.getEndDate())) {
            promoDto.setStatus(3);
        } else {
            // 2: 活动正在进行中
            promoDto.setStatus(2);
        }

        return promoDto;
    }

    @Override
    public void publishPromo(Integer id) throws BussinesssError {
        PromoDo promoDo = promoDoMapper.selectByPrimaryKey(id);

        if(ObjectUtils.isEmpty(promoDo))
            throw new BussinesssError(ErrorEnum.PROMO_NOT_EXIST);

        if(ObjectUtils.isEmpty(promoDo.getItemId()) || promoDo.getItemId() == 0) {
            throw new BussinesssError(ErrorEnum.PROMO_NO_ITEM);
        }

        ItemDto item = itemService.getItemDetailById(promoDo.getItemId());
        String key = "promo_item_stock_" + item.getId();
        redisService.put(key, String.valueOf(item.getStock()));

    }

}
