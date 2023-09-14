package com.shaw.lts.core.redis.impl;

import com.google.gson.Gson;
import com.shaw.lts.core.redis.RedisCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.params.SetParams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * RedisClusterServiceImpl
 * redis集群
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2022/7/20 17:43
 **/
public class RedisClusterServiceImpl implements RedisCacheService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JedisCluster jedisCluster;

    public RedisClusterServiceImpl(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    @Override
    public boolean lock(String key, String value, int seconds) {
        boolean result = false;
        try {
            SetParams setParams = new SetParams();
            setParams.ex(seconds);
            setParams.nx();
            String res = jedisCluster.set(key, value, setParams);
            if (res != null) {
                result = true;
            }
        } catch (Exception e) {
            logger.error("redis.Lock()发生异常{}", e.getMessage());
        }
        return result;
    }

    @Override
    public boolean unlock(String key, String value) {
        boolean result = false;
        try {
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else " +
                "return 0 end";
            Object object = jedisCluster.eval(script, Collections.singletonList(key), Collections.singletonList(value));
            if (RELEASE_SUCCESS.equals(object)) {
                result = true;
            }
        } catch (Exception e) {
            logger.error("redis.unLock()发生异常" + e.getMessage());
        }
        return result;
    }

    @Override
    public void set(String key, String value) {
        try {
            jedisCluster.set(key, value);
        } catch (Exception e) {
            logger.error("redis.set()发生异常" + e.getMessage());
        }
    }

    @Override
    public void set(String key, String value, int seconds) {
        try {
            jedisCluster.setex(key, seconds, value);
        } catch (Exception e) {
            logger.error("redis.set()发生异常" + e.getMessage());
        }
    }

    @Override
    public String get(String key) {
        String result = null;
        try {
            result = jedisCluster.get(key);
        } catch (Exception e) {
            logger.error("redis.get()发生异常" + e.getMessage());
        }
        return result;
    }

    @Override
    public void del(String key) {
        try {
            jedisCluster.del(key);
        } catch (Exception e) {
            logger.error("redis.del()发生异常{}", e.getMessage());
        }
    }

    @Override
    public Map<String, String> getMap(String key) {
        try {
            return jedisCluster.hgetAll(key);
        } catch (Exception e) {
            logger.error("redis.getMap()发生异常{}", e.getMessage());
        }
        return null;
    }

    @Override
    public void setMap(String key, Map<String, String> stringMap) {
        try {
            jedisCluster.hmset(key, stringMap);
        } catch (Exception e) {
            logger.error("redis.setMap()发生异常{}", e.getMessage());
        }
    }

    @Override
    public void setMap(String key, Map<String, String> stringMap, int seconds) {
        String script = "local mapKey = KEYS[1]\n"
            + "local expirationSeconds = tonumber(ARGV[1])\n"
            + "local mapValues = cjson.decode(ARGV[2])\n"
            + "for k,v in pairs(mapValues) do\n"
            + "    redis.call('HSET', mapKey, k, v)\n"
            + "end\n"
            + "redis.call('EXPIRE', mapKey, expirationSeconds)\n"
            + "return 1";
        try {
            String mapValuesJson = new Gson().toJson(stringMap);
            jedisCluster.eval(script, 1, key, String.valueOf(seconds), mapValuesJson);
        } catch (Exception e) {
            logger.error("redis.setMap()发生异常{}", e.getMessage());
        }
    }

    @Override
    public String getMapValue(String key, String field) {
        try {
            return jedisCluster.hget(key, field);
        } catch (Exception e) {
            logger.error("redis.getMapValue()发生异常{}", e.getMessage());
        }
        return null;
    }

    @Override
    public void setMapValue(String key, String field, String value) {
        try {
             jedisCluster.hset(key, field, value);
        } catch (Exception e) {
            logger.error("redis.setMapValue()发生异常{}", e.getMessage());
        }
    }

    @Override
    public boolean hasKey(String key) {
        try {
            return jedisCluster.exists(key);
        } catch (Exception e) {
            logger.error("redis.hasKey()发生异常{}", e.getMessage());
        }

        return false;
    }

    @Override
    public Long increment(String key, long value) {
        try {
            return jedisCluster.incrBy(key, value);
        } catch (Exception e) {
            logger.error("redis.increment(key,value)发生异常{}", e.getMessage());
        }

        return null;
    }

    @Override
    public Long increment(String key) {
        try {
            return jedisCluster.incr(key);
        } catch (Exception e) {
            logger.error("redis.increment(key)发生异常{}", e.getMessage());
        }

        return null;
    }

    @Override
    public Long ttl(String key) {
        Long result = null;
        try {
            result = jedisCluster.ttl(key);
        } catch (Exception e) {
            logger.error("redis.ttl()发生异常" + e.getMessage());
        }
        return result;
    }

    @Override
    public void setList(String key, List<String> strings) {
        try {
            jedisCluster.rpush(key, strings.toArray(new String[0]));
        } catch (Exception e) {
            logger.error("redis.rpush()发生异常" + e.getMessage());
        }
    }

    @Override
    public List<String> getList(String key) {
        List<String> strings = new ArrayList<>();
        try {
            strings = jedisCluster.lrange(key, 0 , -1);
        } catch (Exception e) {
            logger.error("redis.lrange()发生异常" + e.getMessage());
        }
        return strings;
    }

    @Override
    public void expire(String key, long times) {
        try {
            jedisCluster.expire(key, times);
        } catch (Exception e) {
            logger.error("redis.expire()发生异常" + e.getMessage());
        }
    }
}
