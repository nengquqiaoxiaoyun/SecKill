package com.huakai.service.impl;

import com.huakai.controller.dto.PromoDto;
import com.huakai.mapper.PromoDoMapper;
import com.huakai.mapper.dataobject.PromoDo;
import com.huakai.service.PromoService;
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

    public static void main(String[] args) {


        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String format = now.format(df);
        System.out.println(format);
        LocalDateTime parse = LocalDateTime.parse(format, df);
        System.out.println(parse);

    }
}
