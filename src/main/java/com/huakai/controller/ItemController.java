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

    /**
     * Get方法的映射控制器，用于获取指定id的数据。
     *
     * @param id 待查询的数据id
     * @return 返回CommonReturnType类型的结果，包含查询得到的数据
     * @throws BussinesssError 在查询过程中出现业务异常
     */
    @GetMapping("/get")
    public CommonReturnType get(@RequestParam("id") Integer id) throws BussinesssError {
        // 从redis数据库查询id对应的数据信息
        ItemDto itemDto = redisService.get("item_" + id, ItemDto.class);
        // 若查询到的数据为空，则从数据库中查询，然后存入redis
        if(ObjectUtils.isEmpty(itemDto)) {
            itemDto =itemService.getItemDetailById(id);
            if(!ObjectUtils.isEmpty(itemDto)) {
                redisService.put("item_" + id, new Gson().toJson(itemDto), 10, TimeUnit.MINUTES);
            } else {
                throw new BussinesssError(ErrorEnum.PARAMTER_VALIDATION_ERROR, "数据异常");
            }
        }
        // 获取查询到的商品的优惠信息
        PromoDto promoDto = itemDto.getPromoDto();
        // 若秒杀信息为空，则默认设置为没有秒杀
        if (ObjectUtils.isEmpty(promoDto)) {
            PromoDto innerPromo = new PromoDto();
            innerPromo.setStatus(0);
            itemDto.setPromoDto(innerPromo);
        }
        // 返回查询到的商品信息
        return CommonReturnType.create(itemDto);
    }

}
