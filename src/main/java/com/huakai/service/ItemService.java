package com.huakai.service;

import com.huakai.controller.dto.ItemDto;

import java.util.List;

/**
 * @author: huakaimay
 * @since: 2021-09-02
 */
public interface ItemService {

    ItemDto createItem(ItemDto itemDto);

    List<ItemDto> listItem();

    /**
     * 商品详情
     */
    ItemDto getItemDetailById(Integer id);
}
