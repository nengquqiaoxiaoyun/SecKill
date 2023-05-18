package com.huakai.controller;

import com.google.gson.Gson;
import com.huakai.config.RedisService;
import com.huakai.controller.dto.ItemDto;
import com.huakai.controller.dto.PromoDto;
import com.huakai.error.BussinesssError;
import com.huakai.error.ErrorEnum;
import com.huakai.response.CommonReturnType;
import com.huakai.service.ItemService;
import com.huakai.valiator.ValidationResult;
import com.huakai.valiator.ValidatorImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: huakaimay
 * @since: 2021-09-02
 */
@RequestMapping("/item")
@RestController
@CrossOrigin(allowCredentials = "true", originPatterns = "*")
public class ItemController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ValidatorImpl validator;

    @PostMapping("/create")
    public CommonReturnType create(ItemDto itemDto) throws BussinesssError {
        // 参数校验
        ValidationResult validate = validator.validate(itemDto);
        if (validate.isHasErrors()) {
            throw new BussinesssError(ErrorEnum.PARAMTER_VALIDATION_ERROR, validate.getErrMsg());
        }

        // 插入数据库 返回插入结果
        ItemDto item = itemService.createItem(itemDto);

        return CommonReturnType.create(item);
    }


    @GetMapping("/listItem")
    public CommonReturnType listItem() {

        List<ItemDto> itemDtos =
                itemService.listItem();

        return CommonReturnType.create(itemDtos);
    }

    @GetMapping("/get")
    public CommonReturnType get(@RequestParam("id") Integer id) throws BussinesssError {

        ItemDto itemDto = redisService.get("item_" + id, ItemDto.class);

        if(ObjectUtils.isEmpty(itemDto)) {
            itemDto =itemService.getItemDetailById(id);
            if(!ObjectUtils.isEmpty(itemDto)) {
                redisService.put("item_" + id, new Gson().toJson(itemDto), 10, TimeUnit.MINUTES);
            } else {
                throw new BussinesssError(ErrorEnum.PARAMTER_VALIDATION_ERROR, "数据异常");
            }
        }


        PromoDto promoDto = itemDto.getPromoDto();
        if (ObjectUtils.isEmpty(promoDto)) {
            PromoDto innerPromo = new PromoDto();
            innerPromo.setStatus(0);
            itemDto.setPromoDto(innerPromo);
        }

        return CommonReturnType.create(itemDto);

    }

}
