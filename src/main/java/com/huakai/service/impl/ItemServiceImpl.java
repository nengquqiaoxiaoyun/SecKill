package com.huakai.service.impl;

import com.huakai.controller.dto.ItemDto;
import com.huakai.mapper.ItemDOMapper;
import com.huakai.mapper.ItemDtoMapper;
import com.huakai.mapper.ItemStockDOMapper;
import com.huakai.mapper.dataobject.ItemDO;
import com.huakai.mapper.dataobject.ItemStockDO;
import com.huakai.service.ItemService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: huakaimay
 * @since: 2021-09-02
 */
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemDOMapper itemDOMapper;

    @Autowired
    private ItemStockDOMapper itemStockDOMapper;

    @Autowired
    private ItemDtoMapper itemDtoMapper;

    @Override
    @Transactional
    public ItemDto createItem(ItemDto itemDto) {

        if (itemDto == null)
            return null;

        // insert into item
        ItemDO itemDO = convertFromItemDto(itemDto);
        itemDOMapper.insertSelective(itemDO);

        // insert into item_stock
        Integer itemId = itemDO.getId();
        ItemStockDO itemStockDO = new ItemStockDO();
        itemStockDO.setItemId(itemId);
        itemStockDO.setStock(itemDto.getStock());
        itemStockDOMapper.insertSelective(itemStockDO);
        return getItemDetailById(itemId);
    }

    private ItemDO convertFromItemDto(ItemDto itemDto) {

        if (itemDto == null)
            return null;

        ItemDO itemDO = new ItemDO();
        BeanUtils.copyProperties(itemDto, itemDO);
        return itemDO;
    }


    @Override
    public List<ItemDto> listItem() {
        return itemDtoMapper.listItemDto();
    }

    @Override
    public ItemDto getItemDetailById(Integer id) {

        ItemDO itemDO = itemDOMapper.selectByPrimaryKey(id);

        if (itemDO == null)
            return null;

        ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());

        ItemDto itemDto = convertFromItemDo(itemDO);
        itemDto.setStock(itemStockDO.getStock());
        return itemDto;
    }


    private ItemDto convertFromItemDo(ItemDO itemDO) {

        if (itemDO == null)
            return null;

        ItemDto itemDto = new ItemDto();
        BeanUtils.copyProperties(itemDO, itemDto);
        return itemDto;
    }
}
