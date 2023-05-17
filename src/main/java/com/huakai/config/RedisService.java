package com.huakai.config;

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
     * @param key key
     * @param value value
     */
    public void put(String key, String value){
        ValueOperations<String, String> valueOperations =
                stringRedisTemplate.opsForValue();
        valueOperations.set(key, value);
    }


    /**
     * set redis: string类型包含过期时间
     * @param key
     * @param value
     * @param timeout
     * @param unit
     */
    public void put(String key, String value, long timeout, TimeUnit unit){
        ValueOperations<String, String> valueOperations =
                stringRedisTemplate.opsForValue();
        valueOperations.set(key, value, timeout, unit);
    }

    /**
     * get redis: string类型
     * @param key key
     * @return
     */
    public String get(String key){
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * set redis: hash类型
     * @param key key
     * @param filedKey filedkey
     * @param value value
     */
    public void setHash(String key, String filedKey, String value){
        HashOperations<String, Object, Object> hashOperations =
                stringRedisTemplate.opsForHash();
        hashOperations.put(key,filedKey, value);
    }
    /**
     * get redis: hash类型
     * @param key key
     * @param filedkey filedkey
     * @return
     */
    public String getHash(String key, String filedkey){
        return (String) stringRedisTemplate.opsForHash().get(key, filedkey);
    }


    /**
     * set redis:list类型
     * @param key key
     * @param value value
     * @return
     */
    public long setList(String key, String value){
        ListOperations<String, String> listOperations =
                stringRedisTemplate.opsForList();
        return listOperations.leftPush(key, value);
    }
    /**
     * get redis:list类型
     * @param key key
     * @param start start
     * @param end end
     * @return
     */
    public List<String> getList(String key, long start, long end){
        return stringRedisTemplate.opsForList().range(key, start, end);
    }


}
