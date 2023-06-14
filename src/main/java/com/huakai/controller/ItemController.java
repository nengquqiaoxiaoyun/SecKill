package com.huakai.controller;

import com.google.gson.Gson;
import com.huakai.config.RedisService;
import com.huakai.controller.dto.ItemDto;
import com.huakai.controller.dto.PromoDto;
import com.huakai.error.BussinesssError;
import com.huakai.error.ErrorEnum;
import com.huakai.response.CommonReturnType;
import com.huakai.service.ItemService;
import com.huakai.service.LocalCacheService;
import com.huakai.service.PromoService;
import com.huakai.valiator.ValidationResult;
import com.huakai.valiator.ValidatorImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
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
    private LocalCacheService localCacheService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private PromoService promoService;

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

        // 先从本地缓存中查询商品信息
        ItemDto itemDto = (ItemDto) localCacheService.get("item_" + id);
        if (!ObjectUtils.isEmpty(itemDto)) {
            return CommonReturnType.create(itemDto);
        }

        // 如果本地缓存中不存在，则从 Redis 中查询
        String itemJson = redisService.get("item_" + id);
        if (!StringUtils.isEmpty(itemJson)) {
            itemDto = new Gson().fromJson(itemJson, ItemDto.class);
            localCacheService.put("item_" + id, itemDto);  // 将查询到的结果放入本地缓存
            return CommonReturnType.create(itemDto);
        }

        // 如果 Redis 中也没有，则从数据库中查询
        itemDto = itemService.getItemDetailById(id);
        if (ObjectUtils.isEmpty(itemDto)) {
            throw new BussinesssError(ErrorEnum.ITEM_NOT_EXIST);
        }

        // 获取查询到的商品的秒杀信息
        PromoDto promoDto = itemDto.getPromoDto();
        // 若秒杀信息为空，则默认设置为没有秒杀
        if (ObjectUtils.isEmpty(promoDto)) {
            PromoDto innerPromo = new PromoDto();
            innerPromo.setStatus(0);
            itemDto.setPromoDto(innerPromo);
        }

        // 将查询到的结果放入 Redis 和本地缓存中
        redisService.put("item_" + id, new Gson().toJson(itemDto), 10, TimeUnit.MINUTES);
        localCacheService.put("item_" + id, itemDto);


        // 返回查询到的商品信息
        return CommonReturnType.create(itemDto);
    }


    /**
     * 发布活动
     */
    @GetMapping("/publishPromo")
    public CommonReturnType publishPromo(@RequestParam("id") Integer id) throws BussinesssError {
        promoService.publishPromo(id);
        return CommonReturnType.create(null);
    }




}
