package com.huakai.service.impl;

import com.huakai.mapper.StockLogDOMapper;
import com.huakai.mapper.dataobject.StockLogDO;
import com.huakai.service.StockLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: huakaimay
 * @since: 2023-06-12
 */
@Service
public class StockLogServiceImpl implements StockLogService {

    @Autowired
    private StockLogDOMapper stockLogDOMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createStock(StockLogDO stockLogDO) {
        return stockLogDOMapper.insert(stockLogDO) > 0;
    }
}
