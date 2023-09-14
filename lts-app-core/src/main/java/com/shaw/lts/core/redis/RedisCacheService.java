package com.shaw.lts.core.redis;

import com.shaw.lts.core.utils.IdGeneratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * RedisCacheService
 * redis缓存服务
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2022/7/20 17:36
 **/
public interface RedisCacheService {

    Logger logger = LoggerFactory.getLogger(RedisCacheService.class);

    Long RELEASE_SUCCESS = 1L;

    /**
     * 分布式锁
     *
     * @param key     key
     * @param value   value
     * @param seconds 过期时间
     * @return {@link Boolean}
     */
    boolean lock(String key, String value, int seconds);

    /**
     * 分布式释放锁
     *
     * @param key   key
     * @param value value
     * @return {@link Boolean}
     */
    boolean unlock(String key, String value);

    /**
     * 缓存中添加值
     *
     * @param key   key
     * @param value value
     */
    void set(String key, String value);

    /**
     * 缓存中添加值有过期时间
     *
     * @param key     key
     * @param value   value
     * @param seconds 过期时间
     */
    void set(String key, String value, int seconds);

    /**
     * 根据key获取value
     *
     * @param key key
     * @return value
     */
    String get(String key);

    /**
     * 删除key
     *
     * @param key key
     */
    void del(String key);

    /**
     * 获取map类型
     *
     * @param key key
     * @return Map<String, String>
     */
    Map<String, String> getMap(String key);

    /**
     * 缓存map类型
     *
     * @param key       key
     * @param stringMap stringMap
     */
    void setMap(String key, Map<String, String> stringMap);

    /**
     * 缓存map类型
     * @param key key
     * @param stringMap stringMap
     * @param seconds 过期时间
     */
    void setMap(String key, Map<String, String> stringMap, int seconds);

    /**
     * 获取map中的值
     *
     * @param key   key
     * @param field 字段
     * @return String
     */
    String getMapValue(String key, String field);

    /**
     *
     * @param key key
     * @param field 字段
     * @param value 值
     */
    void setMapValue(String key, String field, String value);

    /**
     * 检测可以是否存在
     *
     * @param key key
     * @return 是否存在
     */
    boolean hasKey(String key);

    /**
     * 数值累加
     *
     * @param key   键
     * @param value 累加值
     * @return 累加后的值
     */
    Long increment(String key, long value);

    /**
     * 累加
     *
     * @param key 累加key
     * @return 累加后的值
     */
    Long increment(String key);

    /**
     * 根据key获取剩余生存时间
     * @param key key
     * @return value
     */
    Long ttl(String key);

    /**
     * 添加 list 结构数据
     * @param key key
     * @param strings List<String>
     */
    void setList(String key, List<String> strings);

    /**
     * 获取 list 结构数据
     * @param key key
     */
    List<String> getList(String key);

    /**
     * 设置 key 过期时间
     * @param key key
     */
    void expire(String key, long times);

    default <T> T executeWithLock(String key, int seconds, Supplier<T> supplier) {
        String value = IdGeneratorUtils.uuid();
        boolean lock = this.lock(key, value, seconds);
        if (!lock) {
            throw new RuntimeException("加锁失败");
        }
        logger.info("key:{}加锁成功", key);
        try {
            return supplier.get();
        } finally {
            boolean flag = this.unlock(key, value);
            if (flag) {
                logger.info("key:{},锁释放成功", key);
            } else {
                logger.info("key:{},锁释放失败", key);
            }
        }
    }
}
