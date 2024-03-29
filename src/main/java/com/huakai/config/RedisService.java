package com.huakai.config;

import com.google.gson.Gson;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: huakaimay
 * @since: 2023-02-03
 */
@Service
public class RedisService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public Boolean setnx(String key, String value) {
        ValueOperations<String, String> valueOperations =
                stringRedisTemplate.opsForValue();
        return valueOperations.setIfAbsent(key, value);
    }

    public void expire(String key, Long time, TimeUnit unit) {
        stringRedisTemplate.expire(key, time, unit);
    }

    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * set redis: string类型
     *
     * @param key   key
     * @param value value
     */
    public void put(String key, String value) {
        ValueOperations<String, String> valueOperations =
                stringRedisTemplate.opsForValue();
        valueOperations.set(key, value);
    }


    /**
     * set redis: string类型包含过期时间
     *
     * @param key
     * @param value
     * @param timeout
     * @param unit
     */
    public void put(String key, String value, long timeout, TimeUnit unit) {
        ValueOperations<String, String> valueOperations =
                stringRedisTemplate.opsForValue();
        valueOperations.set(key, value, timeout, unit);
    }

    /**
     * get redis: string类型
     *
     * @param key key
     * @return
     */
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }


    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public <T> T get(String key, Class<T> clazz) {
        Object value = null;
        try {
            value = stringRedisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (value != null) {
            Gson gson = new Gson();
            return gson.fromJson(value.toString(), clazz);
        }
        return null;
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @param expireTime 过期时间，单位秒（默认过期时间为0，即不过期）
     * @return
     */
    public boolean set(String key, Object value, long expireTime) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(value);
            if (expireTime > 0) {
                stringRedisTemplate.opsForValue().set(key, json, expireTime, TimeUnit.SECONDS);
            } else {
                stringRedisTemplate.opsForValue().set(key, json);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * set redis: hash类型
     *
     * @param key      key
     * @param filedKey filedkey
     * @param value    value
     */
    public void setHash(String key, String filedKey, String value) {
        HashOperations<String, Object, Object> hashOperations =
                stringRedisTemplate.opsForHash();
        hashOperations.put(key, filedKey, value);
    }

    /**
     * 判断redis中是否存在指定的key
     *
     * @param key 要检查的key
     * @return 如果存在返回true，否则返回false
     */
    public boolean hasKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    /**
     * get redis: hash类型
     *
     * @param key      key
     * @param filedkey filedkey
     * @return
     */
    public String getHash(String key, String filedkey) {
        return (String) stringRedisTemplate.opsForHash().get(key, filedkey);
    }


    /**
     * set redis:list类型
     *
     * @param key   key
     * @param value value
     * @return
     */
    public long setList(String key, String value) {
        ListOperations<String, String> listOperations =
                stringRedisTemplate.opsForList();
        return listOperations.leftPush(key, value);
    }

    /**
     * get redis:list类型
     *
     * @param key   key
     * @param start start
     * @param end   end
     * @return
     */
    public List<String> getList(String key, long start, long end) {
        return stringRedisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 对 key 对应的值执行加操作
     *
     * @param key   key
     * @param delta 加的值
     * @return 加操作后的值
     */
    public Long increment(String key, long delta) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        return valueOperations.increment(key, delta);
    }

    /**
     * 对 key 对应的值执行减操作
     *
     * @param key   key
     * @param delta 减的值
     * @return 减操作后的值
     */
    public Long decrement(String key, long delta) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        return valueOperations.decrement(key, delta);
    }


}
