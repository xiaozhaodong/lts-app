package com.shaw.lts.core.redis.impl;

import com.google.gson.Gson;
import com.shaw.lts.core.redis.RedisCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * RedisStandServiceImpl
 * redis单机版
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2022/7/20 17:41
 **/
public class RedisStandServiceImpl implements RedisCacheService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JedisPool jedisPool;

    public RedisStandServiceImpl(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public boolean lock(String key, String value, int seconds) {
        boolean result = false;
        try (Jedis jedis = jedisPool.getResource()) {
            SetParams setParams = new SetParams();
            setParams.ex(seconds);
            setParams.nx();
            String res = jedis.set(key, value, setParams);
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
        try (Jedis jedis = jedisPool.getResource()) {
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else " +
                    "return 0 end";
            Object object = jedis.eval(script, Collections.singletonList(key), Collections.singletonList(value));
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
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, value);
        } catch (Exception e) {
            logger.error("redis.set()发生异常" + e.getMessage());
        }
    }

    @Override
    public void set(String key, String value, int seconds) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.setex(key, seconds, value);
        } catch (Exception e) {
            logger.error("redis.set()发生异常" + e.getMessage());
        }
    }

    @Override
    public String get(String key) {
        String result = null;
        try (Jedis jedis = jedisPool.getResource()) {
            result = jedis.get(key);
        } catch (Exception e) {
            logger.error("redis.get()发生异常" + e.getMessage());
        }
        return result;
    }

    @Override
    public void del(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(key);
        } catch (Exception e) {
            logger.error("redis.del()发生异常{}", e.getMessage());
        }
    }

    @Override
    public Map<String, String> getMap(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hgetAll(key);
        } catch (Exception e) {
            logger.error("redis.del()发生异常{}", e.getMessage());
        }
        return null;
    }

    @Override
    public void setMap(String key, Map<String, String> stringMap) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hmset(key, stringMap);
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
        try (Jedis jedis = jedisPool.getResource()) {
            String mapValuesJson = new Gson().toJson(stringMap);
            jedis.eval(script, 1, key, String.valueOf(seconds), mapValuesJson);
        } catch (Exception e) {
            logger.error("redis.setMap()发生异常{}", e.getMessage());
        }
    }

    @Override
    public String getMapValue(String key, String field) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hget(key, field);
        } catch (Exception e) {
            logger.error("redis.getMapValue()发生异常{}", e.getMessage());
        }
        return null;
    }

    @Override
    public void setMapValue(String key, String field, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(key, field, value);
        } catch (Exception e) {
            logger.error("redis.setMapValue()发生异常{}", e.getMessage());
        }
    }

    @Override
    public boolean hasKey(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.exists(key);
        } catch (Exception e) {
            logger.error("redis.hasKey()发生异常{}", e.getMessage());
        }

        return false;
    }

    @Override
    public Long increment(String key, long value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.incrBy(key, value);
        } catch (Exception e) {
            logger.error("redis.increment(key,value)发生异常{}", e.getMessage());
        }

        return null;
    }

    @Override
    public Long increment(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.incr(key);
        } catch (Exception e) {
            logger.error("redis.increment(key)发生异常{}", e.getMessage());
        }

        return null;
    }

    @Override
    public Long ttl(String key) {
        Long result = null;
        try (Jedis jedis = jedisPool.getResource()) {
            result = jedis.ttl(key);
        } catch (Exception e) {
            logger.error("redis.ttl()发生异常" + e.getMessage());
        }
        return result;
    }

    @Override
    public void setList(String key, List<String> strings) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.rpush(key, strings.toArray(new String[0]));
        } catch (Exception e) {
            logger.error("redis.rpush()发生异常" + e.getMessage());
        }
    }

    @Override
    public List<String> getList(String key) {
        List<String> strings = new ArrayList<>();
        try (Jedis jedis = jedisPool.getResource()) {
            strings = jedis.lrange(key, 0 , -1);
        } catch (Exception e) {
            logger.error("redis.lrange()发生异常" + e.getMessage());
        }
        return strings;
    }

    @Override
    public void expire(String key, long times) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.expire(key, times);
        } catch (Exception e) {
            logger.error("redis.expire()发生异常" + e.getMessage());
        }
    }
}
