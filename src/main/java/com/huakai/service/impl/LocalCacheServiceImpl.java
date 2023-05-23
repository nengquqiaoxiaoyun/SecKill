package com.huakai.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.huakai.service.LocalCacheService;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 本地缓存实现类
 * @author: huakaimay
 * @since: 2023-05-23
 */
@Service
public class LocalCacheServiceImpl implements LocalCacheService {

    private final Cache<String, Object> CACHE = CacheBuilder.newBuilder()
            .initialCapacity(10)
            .maximumSize(100)
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

    @Override
    public void put(String key, Object value) {
        CACHE.put(key, value);
    }

    @Override
    public Object get(String key) {
        return CACHE.getIfPresent(key);
    }
}
