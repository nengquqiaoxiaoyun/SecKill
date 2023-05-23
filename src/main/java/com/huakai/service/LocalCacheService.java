package com.huakai.service;

/**
 * 本地缓存put get
 * @author: huakaimay
 * @since: 2023-05-23
 */
public interface LocalCacheService {

    void put(String key, Object value);

    Object get(String key);

}
